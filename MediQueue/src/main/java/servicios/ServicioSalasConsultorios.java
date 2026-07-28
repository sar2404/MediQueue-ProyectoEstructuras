package servicios;

import estructuras.Cola;
import estructuras.Lista;
import estructuras.Nodo;
import modelos.Consultorio;
import modelos.Sala;
import modelos.Sede;
import modelos.Tiquete;
import modelos.UnidadAtencion;
import persistencia.Json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Modulo 1.3: administra salas y consultorios mediante una Lista de Colas.
 * La asignacion automatica selecciona la fila compatible mas corta.
 */
public class ServicioSalasConsultorios {

    private static final String ARCHIVO = "salas.json";

    private Lista<UnidadAtencion> unidades;
    private Lista<Cola<Tiquete>> filas;
    private final ServicioRegistro registro;

    public ServicioSalasConsultorios(Sede sede, ServicioRegistro registro) {
        this.registro = registro;
        unidades = new Lista<>();
        filas = new Lista<>();

        if (new File(ARCHIVO).exists()) {
            cargar();
        }
        sincronizarConSede(sede);
        guardar();
    }

    /** Agrega unidades nuevas de la configuracion sin borrar las filas existentes. */
    public void sincronizarConSede(Sede sede) {
        Nodo<Sala> salaActual = sede.getSalas().getCabeza();
        while (salaActual != null) {
            String codigo = "S-" + salaActual.getDato().getNumero();
            if (buscarIndice(codigo) == -1) {
                agregarUnidad(UnidadAtencion.desdeSala(salaActual.getDato()));
            }
            salaActual = salaActual.getSiguiente();
        }

        Nodo<Consultorio> consultorioActual = sede.getConsultorios().getCabeza();
        while (consultorioActual != null) {
            String codigo = "C-" + consultorioActual.getDato().getNumero();
            if (buscarIndice(codigo) == -1) {
                agregarUnidad(UnidadAtencion.desdeConsultorio(consultorioActual.getDato()));
            }
            consultorioActual = consultorioActual.getSiguiente();
        }
        guardar();
    }

    public Tiquete asignarSiguientePaciente() {
        Tiquete candidato = registro.verSiguienteEnEspera();
        if (candidato == null) {
            System.out.println("no hay pacientes pendientes en la cola de prioridad.\n");
            return null;
        }

        int indice = buscarFilaMasCorta(candidato);
        if (indice == -1) {
            System.out.println("no existe una sala o consultorio con espacio disponible.\n");
            return null;
        }

        UnidadAtencion unidad = unidades.obtener(indice);
        Tiquete asignado = registro.asignarSiguienteA(unidad.getCodigo());
        if (asignado == null) {
            return null;
        }

        filas.obtener(indice).encolar(asignado);
        guardar();
        System.out.println("paciente asignado automaticamente a " + unidad.getCodigo()
                + " por tener la fila compatible mas corta.");
        System.out.println(asignado + "\n");
        return asignado;
    }

    /**
     * Saca al primer paciente de la fila de una unidad y lo deja listo para que
     * el Modulo 1.2 cree la consulta correspondiente.
     */
    public Tiquete llamarSiguientePaciente(Scanner sc) {
        mostrarResumenUnidades();
        String codigo = leerTexto(sc, "codigo de sala o consultorio: ").toUpperCase();
        int indice = buscarIndice(codigo);
        if (indice == -1) {
            System.out.println("la unidad indicada no existe.\n");
            return null;
        }

        Cola<Tiquete> fila = filas.obtener(indice);
        if (fila.estaVacia()) {
            System.out.println("la fila de " + codigo + " esta vacia.\n");
            return null;
        }

        try {
            Tiquete tiquete = fila.desencolar();
            registro.marcarEnAtencion(tiquete.getId());
            guardar();
            System.out.println("siguiente paciente llamado desde " + codigo + ":");
            System.out.println(tiquete + "\n");
            return tiquete;
        } catch (Exception e) {
            System.out.println("no fue posible obtener el siguiente paciente.\n");
            return null;
        }
    }

    public void configurarPersonal(Scanner sc) {
        mostrarResumenUnidades();
        String codigo = leerTexto(sc, "codigo de sala o consultorio: ").toUpperCase();
        int indice = buscarIndice(codigo);
        if (indice == -1) {
            System.out.println("la unidad indicada no existe.\n");
            return;
        }

        UnidadAtencion unidad = unidades.obtener(indice);
        System.out.println(" personal y especialidad de " + codigo);
        unidad.setMedico(leerTexto(sc, "medico responsable: "));
        unidad.setEnfermera(leerTexto(sc, "enfermera responsable: "));
        unidad.setEspecialidad(leerTexto(sc, "especialidad: "));
        guardar();
        System.out.println("informacion actualizada correctamente.\n");
    }

    public void mostrarEstado() {
        System.out.println("salas y consultorios (modulo 1.3) ");
        for (int i = 0; i < unidades.getTamano(); i++) {
            UnidadAtencion unidad = unidades.obtener(i);
            Cola<Tiquete> fila = filas.obtener(i);
            System.out.println(unidad);
            System.out.println("  fila: " + fila.getTamano() + "/" + unidad.getCapacidadFila());

            Nodo<Tiquete> actual = fila.getFrente();
            int posicion = 1;
            while (actual != null) {
                System.out.println("    " + posicion + ". " + actual.getDato());
                actual = actual.getSiguiente();
                posicion++;
            }
        }
        System.out.println();
    }

    public void mostrarResumenUnidades() {
        System.out.println("unidades disponibles ");
        for (int i = 0; i < unidades.getTamano(); i++) {
            UnidadAtencion unidad = unidades.obtener(i);
            Cola<Tiquete> fila = filas.obtener(i);
            System.out.println(unidad.getCodigo() + " - " + unidad.getCategoria()
                    + " " + unidad.getTipo() + " | " + unidad.getEspecialidad()
                    + " fila: " + fila.getTamano() + unidad.getCapacidadFila());
        }
        System.out.println();
    }

    public UnidadAtencion buscarUnidad(String codigo) {
        int indice = buscarIndice(codigo);
        return indice == -1 ? null : unidades.obtener(indice);
    }

    public int cantidadEnFila(String codigo) {
        int indice = buscarIndice(codigo);
        return indice == -1 ? -1 : filas.obtener(indice).getTamano();
    }

    /** Unidades registradas. Usado por el Modulo 1.4 (busquedas por medico/lugar). */
    public Lista<UnidadAtencion> getUnidades() {
        return unidades;
    }

    /** Fila de espera de una unidad por su codigo, o null si la unidad no existe. */
    public Cola<Tiquete> filaDe(String codigo) {
        int indice = buscarIndice(codigo);
        return indice == -1 ? null : filas.obtener(indice);
    }

    private void agregarUnidad(UnidadAtencion unidad) {
        unidades.agregar(unidad);
        filas.agregar(new Cola<>());
    }

    private int buscarFilaMasCorta(Tiquete tiquete) {
        int indice = buscarMasCorta(tiquete, 1);
        if (indice == -1) {
            indice = buscarMasCorta(tiquete, 2);
        }
        if (indice == -1) {
            indice = buscarMasCorta(tiquete, 3);
        }
        return indice;
    }

    /**
     * Nivel 1: compatibilidad especifica.
     * Nivel 2: categoria general (sala o consultorio).
     * Nivel 3: cualquier unidad con espacio.
     */
    private int buscarMasCorta(Tiquete tiquete, int nivel) {
        int mejorIndice = -1;
        int menorTamano = Integer.MAX_VALUE;

        for (int i = 0; i < unidades.getTamano(); i++) {
            UnidadAtencion unidad = unidades.obtener(i);
            Cola<Tiquete> fila = filas.obtener(i);
            if (fila.getTamano() >= unidad.getCapacidadFila()) {
                continue;
            }
            if (!esCompatible(unidad, tiquete, nivel)) {
                continue;
            }
            if (fila.getTamano() < menorTamano) {
                menorTamano = fila.getTamano();
                mejorIndice = i;
            }
        }
        return mejorIndice;
    }

    private boolean esCompatible(UnidadAtencion unidad, Tiquete tiquete, int nivel) {
        if (nivel == 3) {
            return true;
        }

        String tipoAtencion = tiquete.getTipoAtencion() == null
                ? "GENERAL" : tiquete.getTipoAtencion().toUpperCase();
        String prioridad = tiquete.getPrioridad() == null
                ? "BAJA" : tiquete.getPrioridad().toUpperCase();

        boolean requiereSala = "EMERGENCIA".equals(tipoAtencion)
                || "REINGRESO".equals(tipoAtencion);

        if (nivel == 2) {
            return requiereSala ? unidad.esSala() : unidad.esConsultorio();
        }

        if ("PREFERENCIAL".equals(tipoAtencion)) {
            return unidad.esPreferencial();
        }
        if (requiereSala && "CRITICA".equals(prioridad)) {
            return unidad.esSala() && "CRITICA".equalsIgnoreCase(unidad.getTipo());
        }
        if (requiereSala) {
            return unidad.esSala()
                    && ("EMERGENCIA".equalsIgnoreCase(unidad.getTipo())
                    || "CRITICA".equalsIgnoreCase(unidad.getTipo()));
        }
        return unidad.esConsultorio() && !unidad.esPreferencial();
    }

    private int buscarIndice(String codigo) {
        if (codigo == null) {
            return -1;
        }
        for (int i = 0; i < unidades.getTamano(); i++) {
            if (codigo.equalsIgnoreCase(unidades.obtener(i).getCodigo())) {
                return i;
            }
        }
        return -1;
    }

    public void guardar() {
        try (FileWriter fw = new FileWriter(ARCHIVO)) {
            fw.write("{\n\"unidades\": [\n");
            for (int i = 0; i < unidades.getTamano(); i++) {
                fw.write(unidades.obtener(i).aTextoJson());
                if (i < unidades.getTamano() - 1) {
                    fw.write(",");
                }
                fw.write("\n");
            }
            fw.write("],\n\"filas\": [\n");

            boolean primera = true;
            for (int i = 0; i < unidades.getTamano(); i++) {
                String codigo = unidades.obtener(i).getCodigo();
                Nodo<Tiquete> actual = filas.obtener(i).getFrente();
                while (actual != null) {
                    if (!primera) {
                        fw.write(",\n");
                    }
                    fw.write("{ \"codigo\": \"" + codigo
                            + "\", \"tiqueteId\": " + actual.getDato().getId() + " }");
                    primera = false;
                    actual = actual.getSiguiente();
                }
            }
            if (!primera) {
                fw.write("\n");
            }
            fw.write("]\n}\n");
        } catch (IOException e) {
            System.out.println("error al guardar salas y filas: " + e.getMessage());
        }
    }

    private void cargar() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String seccion = "";
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.startsWith("\"unidades\"")) {
                    seccion = "unidades";
                } else if (linea.startsWith("\"filas\"")) {
                    seccion = "filas";
                } else if (linea.startsWith("{") && "unidades".equals(seccion)
                        && linea.contains("\"capacidadFila\"")) {
                    agregarUnidad(UnidadAtencion.desdeTexto(linea));
                } else if (linea.startsWith("{") && "filas".equals(seccion)
                        && linea.contains("\"tiqueteId\"")) {
                    String codigo = Json.valorDe(linea, "codigo");
                    int idTiquete = Integer.parseInt(Json.valorDe(linea, "tiqueteId"));
                    int indice = buscarIndice(codigo);
                    Tiquete tiquete = registro.buscarTiquete(idTiquete);
                    if (indice != -1 && tiquete != null) {
                        filas.obtener(indice).encolar(tiquete);
                    }
                }
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("error al cargar salas y filas: " + e.getMessage());
            unidades = new Lista<>();
            filas = new Lista<>();
        }
    }

    private String leerTexto(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("el valor no puede estar vacio.");
        }
    }
}
