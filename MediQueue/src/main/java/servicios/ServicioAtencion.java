package servicios;

import estructuras.Lista;
import estructuras.Nodo;
import estructuras.Pila;
import modelos.Consulta;
import modelos.EntradaHistorial;
import modelos.Tiquete;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Modulo 1.2: Atencion de Pacientes.
 *
 * Responsabilidades:
 *   1. Iniciar atencion a partir de un Tiquete desencola o seleccionado.
 *   2. Finalizar atencion (completa campos clinicos y cierra la consulta).
 *   3. Cancelar atencion (el paciente se retira sin ser atendido).
 *   4. Reingreso de paciente (requiere al menos una consulta FINALIZADA previa).
 *
 * Historial clinico:
 *   Cada paciente tiene su propia Pila<EntradaHistorial> identificada por su
 *   numero de identificacion.  Las pilas viven en una Lista<NodoPila> donde
 *   cada elemento guarda la identificacion y la pila correspondiente.
 *   El historial se serializa en historial.json y las consultas en atendidos.json.
 *
 * Persistencia:
 *   - atendidos.json : consultas abiertas o cerradas (una linea JSON por consulta)
 *   - historial.json : entradas del historial (una linea JSON por entrada)
 */
public class ServicioAtencion {

    private static final String ARCHIVO_ATENDIDOS = "atendidos.json";
    private static final String ARCHIVO_HISTORIAL  = "historial.json";
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // lista de consultas activas o cerradas (persiste entre llamadas)
    private Lista<Consulta> consultas;

    // lista de pilas de historial, una pila por identificacion de paciente
    // cada nodo guarda: identificacion (String) + pila (Pila<EntradaHistorial>)
    private Lista<NodoPilaHistorial> historialPorPaciente;

    private int contadorConsultas;

    public ServicioAtencion() {
        consultas = new Lista<>();
        historialPorPaciente = new Lista<>();
        contadorConsultas = 1;
        cargar();
    }

    /**
     * Inicia la atencion de un tiquete.
     * Si la cola de ese tiquete estaba vacia al encolarlo (detectado en
     * ServicioTiquetes), este metodo se invoca de inmediato.
     * Pide medico y lugar de atencion; crea la Consulta en estado EN_ATENCION.
     *
     * @param tiquete tiquete a atender
     * @param sc      Scanner activo
     * @return la Consulta creada
     */
    public Consulta iniciarAtencion(Tiquete tiquete, Scanner sc) {

        System.out.println("\n*** iniciar atencion ***");
        System.out.println("atendiendo: " + tiquete);

        System.out.print("medico asignado: ");
        String medico = sc.nextLine().trim();

        System.out.print("lugar (ej. S-1 para sala 1, C-2 para consultorio 2): ");
        String lugar = sc.nextLine().trim().toUpperCase();

        String ahora = LocalDateTime.now().format(FMT);
        tiquete.setFechaAtencion(ahora);

        Consulta consulta = new Consulta(contadorConsultas, tiquete.getId(), medico, lugar);
        consulta.setFechaInicio(ahora);
        contadorConsultas++;

        consultas.agregar(consulta);
        guardarAtendidos();

        System.out.println("consulta #" + consulta.getId() + " iniciada. estado: EN_ATENCION");
        return consulta;
    }

    /**
     * Finaliza una consulta: completa todos los campos clinicos y cierra
     * con estado FINALIZADA. Agrega una entrada al historial del paciente.
     *
     * @param idConsulta id de la consulta a finalizar
     * @param tiquete    tiquete vinculado (para obtener identificacion del paciente)
     * @param sc         Scanner activo
     */
    public void finalizarAtencion(int idConsulta, Tiquete tiquete, Scanner sc) {

        Consulta c = buscarConsulta(idConsulta);
        if (c == null) {
            System.out.println("consulta #" + idConsulta + " no encontrada.");
            return;
        }
        if (!c.getEstado().equals("EN_ATENCION") && !c.getEstado().equals("REINGRESO")) {
            System.out.println("la consulta ya fue cerrada (estado: " + c.getEstado() + ").");
            return;
        }

        System.out.println("\n*** finalizar atencion - consulta #" + idConsulta + " ***");

        System.out.print("diagnostico: ");
        c.setDiagnostico(sc.nextLine().trim());

        System.out.print("medicamentos recetados: ");
        c.setMedicamentos(sc.nextLine().trim());

        System.out.print("observaciones: ");
        c.setObservaciones(sc.nextLine().trim());

        System.out.print("procedimientos realizados: ");
        c.setProcedimientos(sc.nextLine().trim());

        System.out.print("costo total: ");
        try {
            c.setCosto(Double.parseDouble(sc.nextLine().trim()));
        } catch (NumberFormatException e) {
            c.setCosto(0.0);
        }

        String ahora = LocalDateTime.now().format(FMT);
        c.setEstado("FINALIZADA");
        c.setFechaFin(ahora);

        guardarAtendidos();

        // registrar en historial
        String resumen = "medico: " + c.getMedico()
                + " | lugar: " + c.getLugarAtencion()
                + " | diag: " + c.getDiagnostico();
        registrarEnHistorial(new EntradaHistorial(
                c.getId(),
                tiquete.getPaciente().getIdentificacion(),
                "FINALIZADA", resumen, ahora));

        System.out.println("consulta #" + idConsulta + " finalizada con exito.");
    }

    /**
     * Cancela una consulta: el paciente se retira sin recibir atencion.
     * Registra en historial con resultado CANCELADA.
     *
     * @param idConsulta id de la consulta a cancelar
     * @param tiquete    tiquete vinculado
     */
    public void cancelarAtencion(int idConsulta, Tiquete tiquete) {

        Consulta c = buscarConsulta(idConsulta);
        if (c == null) {
            System.out.println("consulta #" + idConsulta + " no encontrada.");
            return;
        }
        if (!c.getEstado().equals("EN_ATENCION")) {
            System.out.println("no se puede cancelar (estado actual: " + c.getEstado() + ").");
            return;
        }

        String ahora = LocalDateTime.now().format(FMT);
        c.setEstado("CANCELADA");
        c.setFechaFin(ahora);
        guardarAtendidos();

        String resumen = "paciente se retiro | lugar: " + c.getLugarAtencion()
                + " | medico: " + c.getMedico();
        registrarEnHistorial(new EntradaHistorial(
                c.getId(),
                tiquete.getPaciente().getIdentificacion(),
                "CANCELADA", resumen, ahora));

        System.out.println("consulta #" + idConsulta + " cancelada.");
    }

    /**
     * Reingreso de un paciente: solo se permite si tiene al menos una
     * consulta FINALIZADA registrada.
     * Crea una nueva consulta para el tiquete con estado REINGRESO y puede
     * cambiar el lugar de atencion.
     *
     * @param tiquete tiquete del reingreso
     * @param sc      Scanner activo
     * @return la Consulta creada, o null si no cumple la condicion
     */
    public Consulta reingresar(Tiquete tiquete, Scanner sc) {

        String identPaciente = tiquete.getPaciente().getIdentificacion();

        // verificar que tenga al menos una consulta finalizada
        if (!tieneConsultaFinalizada(identPaciente)) {
            System.out.println("el paciente " + tiquete.getPaciente().getNombre()
                    + " no tiene consultas finalizadas previas. "
                    + "no puede reingresar.");
            return null;
        }

        System.out.println("\n*** reingreso de paciente ***");
        System.out.println("paciente: " + tiquete.getPaciente().getNombre());

        System.out.print("medico asignado: ");
        String medico = sc.nextLine().trim();

        System.out.print("nuevo lugar de atencion (puede cambiar): ");
        String lugar = sc.nextLine().trim().toUpperCase();

        String ahora = LocalDateTime.now().format(FMT);
        tiquete.setFechaAtencion(ahora);

        Consulta consulta = new Consulta(contadorConsultas, tiquete.getId(), medico, lugar);
        consulta.setFechaInicio(ahora);
        consulta.setEstado("REINGRESO");
        contadorConsultas++;

        consultas.agregar(consulta);
        guardarAtendidos();

        // registrar en historial
        String resumen = "reingreso | medico: " + medico + " | lugar: " + lugar;
        registrarEnHistorial(new EntradaHistorial(
                consulta.getId(), identPaciente, "REINGRESO", resumen, ahora));

        System.out.println("reingreso registrado. consulta #" + consulta.getId());
        return consulta;
    }

    /**
     * Muestra los ultimos n registros del historial de un paciente.
     * Como la pila tiene lo mas reciente en la cima, se desapila hasta n veces
     * y se vuelve a apilar para no perder el historial.
     *
     * @param identificacion cedula o id del paciente
     * @param n              cantidad de registros a mostrar
     */
    public void mostrarUltimosN(String identificacion, int n) {

        Pila<EntradaHistorial> pila = buscarPilaHistorial(identificacion);
        if (pila == null || pila.esVacia()) {
            System.out.println("no hay historial para la identificacion " + identificacion + ".");
            return;
        }

        System.out.println("\n*** historial clinico (ultimos " + n + ") | paciente: "
                + identificacion + " ***");

        // desapilar hasta n en pila temporal, luego restaurar
        Pila<EntradaHistorial> temp = new Pila<>();
        int mostrados = 0;

        while (!pila.esVacia() && mostrados < n) {
            EntradaHistorial e = pila.desapilar();
            System.out.println("  " + (mostrados + 1) + ". " + e);
            temp.apilar(e);
            mostrados++;
        }

        // restaurar pila original (orden inverso -> re-invertir)
        Pila<EntradaHistorial> auxRestore = new Pila<>();
        while (!temp.esVacia()) auxRestore.apilar(temp.desapilar());
        while (!auxRestore.esVacia()) pila.apilar(auxRestore.desapilar());

        System.out.println();
    }

    /**
     * Exporta el historial completo de un paciente a un archivo de texto.
     * Nombre del archivo: historial_<identificacion>.txt
     *
     * @param identificacion cedula o id del paciente
     */
    public void exportarHistorial(String identificacion) {

        Pila<EntradaHistorial> pila = buscarPilaHistorial(identificacion);
        if (pila == null || pila.esVacia()) {
            System.out.println("no hay historial para exportar.");
            return;
        }

        String nombreArchivo = "historial_" + identificacion + ".txt";

        // volcar la pila a una lista temporal para recorrerla sin destruirla
        Lista<EntradaHistorial> temp = new Lista<>();
        Pila<EntradaHistorial> aux = new Pila<>();

        while (!pila.esVacia()) {
            EntradaHistorial e = pila.desapilar();
            temp.agregar(e);
            aux.apilar(e);
        }
        // restaurar la pila original
        while (!aux.esVacia()) pila.apilar(aux.desapilar());

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo));
            pw.println("*** historial clinico completo ***");
            pw.println("paciente identificacion: " + identificacion);
            pw.println("generado: " + LocalDateTime.now().format(FMT));
            pw.println("---");

            Nodo<EntradaHistorial> actual = temp.getCabeza();
            int num = 1;
            while (actual != null) {
                pw.println(num + ". " + actual.getDato());

                // buscar datos completos en la lista de consultas
                Consulta c = buscarConsulta(actual.getDato().getIdConsulta());
                if (c != null) {
                    pw.println("   medico: "        + c.getMedico());
                    pw.println("   lugar: "         + c.getLugarAtencion());
                    pw.println("   diagnostico: "   + c.getDiagnostico());
                    pw.println("   medicamentos: "  + c.getMedicamentos());
                    pw.println("   observaciones: " + c.getObservaciones());
                    pw.println("   procedimientos: "+ c.getProcedimientos());
                    pw.println("   costo: "         + c.getCosto());
                }
                pw.println();
                actual = actual.getSiguiente();
                num++;
            }
            pw.close();
            System.out.println("historial exportado a " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("error al exportar historial: " + e.getMessage());
        }
    }

    /**
     * Muestra todas las consultas registradas (activas y cerradas).
     */
    public void mostrarTodasConsultas() {
        System.out.println("\n*** consultas registradas ***");
        Nodo<Consulta> actual = consultas.getCabeza();
        if (actual == null) {
            System.out.println("  no hay consultas registradas.");
        }
        while (actual != null) {
            System.out.println("  " + actual.getDato());
            actual = actual.getSiguiente();
        }
        System.out.println();
    }

    /**
     * Guarda todas las consultas en atendidos.json (una linea JSON por consulta).
     */
    public void guardarAtendidos() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n\"consultas\": [\n");
        Nodo<Consulta> actual = consultas.getCabeza();
        boolean primero = true;
        while (actual != null) {
            if (!primero) sb.append(",\n");
            sb.append(actual.getDato().aTextoJson());
            primero = false;
            actual = actual.getSiguiente();
        }
        sb.append("\n]\n}\n");
        escribirArchivo(ARCHIVO_ATENDIDOS, sb.toString());
    }

    /**
     * Guarda todas las entradas de historial en historial.json.
     */
    public void guardarHistorial() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n\"historial\": [\n");
        boolean primero = true;

        Nodo<NodoPilaHistorial> nNodo = historialPorPaciente.getCabeza();
        while (nNodo != null) {
            // recorrer la pila sin destruirla usando pila auxiliar
            Pila<EntradaHistorial> pila = nNodo.getDato().pila;
            Pila<EntradaHistorial> aux = new Pila<>();
            Lista<EntradaHistorial> items = new Lista<>();

            // al pasar todo a aux, la cima de aux queda siendo la entrada mas antigua
            while (!pila.esVacia()) {
                aux.apilar(pila.desapilar());
            }
            // se escribe del mas antiguo al mas reciente, que es el mismo orden en
            // que cargar() vuelve a apilar; asi la cima sigue siendo lo mas reciente
            while (!aux.esVacia()) {
                EntradaHistorial e = aux.desapilar();
                items.agregar(e);
                pila.apilar(e);   // restaura la pila original
            }

            Nodo<EntradaHistorial> ae = items.getCabeza();
            while (ae != null) {
                if (!primero) sb.append(",\n");
                sb.append(ae.getDato().aTextoJson());
                primero = false;
                ae = ae.getSiguiente();
            }
            nNodo = nNodo.getSiguiente();
        }
        sb.append("\n]\n}\n");
        escribirArchivo(ARCHIVO_HISTORIAL, sb.toString());
    }

    /**
     * Carga atendidos.json e historial.json al iniciar el sistema.
     */
    public void cargar() {
        // cargar consultas
        if (new File(ARCHIVO_ATENDIDOS).exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_ATENDIDOS));
                String linea;
                int maxId = 0;
                while ((linea = br.readLine()) != null) {
                    linea = linea.trim();
                    if (linea.startsWith("{") && linea.contains("\"id\"")) {
                        Consulta c = Consulta.desdeTexto(linea);
                        consultas.agregar(c);
                        if (c.getId() > maxId) maxId = c.getId();
                    }
                }
                br.close();
                contadorConsultas = maxId + 1;
            } catch (IOException e) {
                System.out.println("error al cargar atendidos: " + e.getMessage());
            }
        }

        // cargar historial
        if (new File(ARCHIVO_HISTORIAL).exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_HISTORIAL));
                String linea;
                while ((linea = br.readLine()) != null) {
                    linea = linea.trim();
                    if (linea.startsWith("{") && linea.contains("\"idConsulta\"")) {
                        EntradaHistorial e = EntradaHistorial.desdeTexto(linea);
                        apilarEnHistorial(e);
                    }
                }
                br.close();
            } catch (IOException e) {
                System.out.println("error al cargar historial: " + e.getMessage());
            }
        }
    }

    /** Busca una consulta por id recorriendo la lista. */
    public Consulta buscarConsulta(int idConsulta) {
        Nodo<Consulta> actual = consultas.getCabeza();
        while (actual != null) {
            if (actual.getDato().getId() == idConsulta) return actual.getDato();
            actual = actual.getSiguiente();
        }
        return null;
    }

    /** Lista de consultas registradas. Usado por el Modulo 1.4 (busquedas). */
    public Lista<Consulta> getConsultas() {
        return consultas;
    }

    /**
     * Devuelve las entradas del historial de un paciente, mas reciente primero,
     * sin destruir la pila original. Usado por el Modulo 1.4 (busqueda de historial).
     */
    public Lista<EntradaHistorial> obtenerHistorial(String identificacion) {
        Lista<EntradaHistorial> resultado = new Lista<>();
        Pila<EntradaHistorial> pila = buscarPilaHistorial(identificacion);
        if (pila == null) {
            return resultado;
        }
        Pila<EntradaHistorial> aux = new Pila<>();
        while (!pila.esVacia()) {
            EntradaHistorial e = pila.desapilar();
            resultado.agregar(e);     // mas reciente primero
            aux.apilar(e);
        }
        while (!aux.esVacia()) {       // restaurar la pila original
            pila.apilar(aux.desapilar());
        }
        return resultado;
    }

    /** Registra una entrada en la pila del paciente y persiste el historial. */
    private void registrarEnHistorial(EntradaHistorial entrada) {
        apilarEnHistorial(entrada);
        guardarHistorial();
    }

    /** Apila la entrada en la pila del paciente (busca o crea la pila). */
    private void apilarEnHistorial(EntradaHistorial entrada) {
        Pila<EntradaHistorial> pila = buscarPilaHistorial(entrada.getIdentificacionPaciente());
        if (pila == null) {
            NodoPilaHistorial nuevo = new NodoPilaHistorial(
                    entrada.getIdentificacionPaciente(), new Pila<>());
            historialPorPaciente.agregar(nuevo);
            pila = nuevo.pila;
        }
        pila.apilar(entrada);
    }

    /** Busca la pila de historial de un paciente por identificacion. */
    private Pila<EntradaHistorial> buscarPilaHistorial(String identificacion) {
        Nodo<NodoPilaHistorial> actual = historialPorPaciente.getCabeza();
        while (actual != null) {
            if (actual.getDato().identificacion.equals(identificacion)) {
                return actual.getDato().pila;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    /**
     * Verifica si el paciente tiene al menos una consulta FINALIZADA
     * buscando en su pila de historial.
     */
    private boolean tieneConsultaFinalizada(String identificacion) {
        Pila<EntradaHistorial> pila = buscarPilaHistorial(identificacion);
        if (pila == null) return false;

        Pila<EntradaHistorial> aux = new Pila<>();
        boolean encontrado = false;

        while (!pila.esVacia()) {
            EntradaHistorial e = pila.desapilar();
            if (e.getResultado().equals("FINALIZADA")) encontrado = true;
            aux.apilar(e);
        }
        // restaurar la pila
        while (!aux.esVacia()) pila.apilar(aux.desapilar());
        return encontrado;
    }

    private void escribirArchivo(String nombre, String contenido) {
        try {
            FileWriter fw = new FileWriter(nombre);
            fw.write(contenido);
            fw.close();
        } catch (IOException e) {
            System.out.println("error al guardar " + nombre + ": " + e.getMessage());
        }
    }

}
