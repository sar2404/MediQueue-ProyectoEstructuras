package servicios;

import estructuras.ArbolABB;
import estructuras.Cola;
import estructuras.Lista;
import estructuras.Nodo;
import modelos.Consulta;
import modelos.EntradaHistorial;
import modelos.Paciente;
import modelos.Tiquete;
import modelos.UnidadAtencion;

/**
 * Modulo 1.4: Historial y Busquedas.
 *
 * Mantiene un INDICE de pacientes en un Arbol Binario de Busqueda (ArbolABB)
 * ordenado por la identificacion del paciente. Cada nodo del arbol guarda un
 * IndicePaciente, es decir: el paciente + la lista de tiquetes de ese paciente.
 *
 * El indice se construye DESDE MEMORIA, a partir de los tiquetes que el Modulo
 * 1.1 ya cargo de tiquetes.json, asi que siempre refleja los pacientes reales.
 *
 * Que resuelve el arbol y que no:
 *   - Busquedas 1 y 2 (por identificacion) y el listado ordenado SI usan el
 *     arbol
 *   - Busquedas 3, 4 y 5 (medico, fecha, lugar) recorren listas y colas, porque
 *     su clave no es la identificacion. Ahi el arbol se usa como ficha: dado un
 *     tiquete se saca la identificacion y con ella se pide al arbol los datos
 *     completos del paciente.
 */
public class ServicioBusquedaPacientes {

    // Servicios de los otros modulos de donde salen los datos a buscar.
    private final ServicioRegistro registro;
    private final ServicioAtencion atencion;
    private final ServicioSalasConsultorios salas;

    // Indice de pacientes: ordenado por identificacion.
    private ArbolABB<IndicePaciente> arbol;

    // Cuantos tiquetes habia la ultima vez que se construyo el indice.
    // Sirve para no reconstruir el arbol si nada cambio.
    private int tiquetesIndexados;

    public ServicioBusquedaPacientes(ServicioRegistro registro, ServicioAtencion atencion, ServicioSalasConsultorios salas) {
        this.registro = registro;
        this.atencion = atencion;
        this.salas = salas;
        this.arbol = new ArbolABB<>();
        this.tiquetesIndexados = -1;
        indexar();
    }
    // Construccion del indice (arbol ABB)

    /**
     * Construye el arbol recorriendo la lista de tiquetes.
     * Por cada tiquete se busca su paciente en el arbol:
     *   - si no esta, se crea el nodo (IndicePaciente) y se inserta;
     *   - si ya esta, se le agrega el tiquete a la lista que trae el nodo.
     * Asi un paciente con 3 visitas ocupa 1 solo nodo con 3 tiquetes adentro.
     *
     * Solo se reconstruye cuando cambio la cantidad de tiquetes; si nada cambio
     * se reutiliza el arbol que ya estaba armado.
     */
    public void indexar() {

        Lista<Tiquete> tiquetes = registro.getTiquetes();
        if (tiquetes.getTamano() == tiquetesIndexados) {
            return;
        }

        arbol = new ArbolABB<>();

        Nodo<Tiquete> actual = tiquetes.getCabeza();
        while (actual != null) {
            Tiquete tiquete = actual.getDato();
            Paciente paciente = tiquete.getPaciente();

            if (paciente != null) {
                IndicePaciente nodo = buscarEnArbol(paciente.getIdentificacion());
                if (nodo == null) {
                    nodo = new IndicePaciente(paciente);
                    arbol.inserta(nodo);
                }
                nodo.agregarTiquete(tiquete);
            }

            actual = actual.getSiguiente();
        }

        tiquetesIndexados = tiquetes.getTamano();
    }

    /**
     * Unica puerta de entrada al arbol: arma una llave con la identificacion y
     * baja por el arbol comparando (izquierda si es menor, derecha si es mayor).
     */
    private IndicePaciente buscarEnArbol(String identificacion) {

        if (identificacion == null || identificacion.trim().isEmpty()) {
            return null;
        }
        return arbol.buscar(IndicePaciente.clave(identificacion));
    }

    /** Devuelve solo el paciente (sin sus tiquetes) buscandolo en el arbol. */
    public Paciente buscarPorIdentificacion(String identificacion) {

        indexar();
        IndicePaciente nodo = buscarEnArbol(identificacion);
        return nodo == null ? null : nodo.getPaciente();
    }
    // Busqueda 1: tiquetes de un paciente  (usa el arbol)
//Salida: datos del paciente y todos sus tiquetes de atencion. no recorre la lista de tiquetes: el nodo del arbol ya los trae adentro.
     
    public void buscarTiquetes(String identificacion) {

        indexar();

        IndicePaciente nodo = buscarEnArbol(identificacion);

        if (nodo == null) {

            System.out.println("no existe un paciente con identificacion " + identificacion + ".");
            espacioFinal();
            return;

        }

        Paciente paciente = nodo.getPaciente();

        System.out.println("*** tiquetes de " + paciente.getNombre() + " (" + nodo.getIdentificacion() + ") ***");

        System.out.println("  edad: " + paciente.getEdad() + " | seguro: " + paciente.getTipoSeguro() + " | visitas: " + nodo.getTiquetes().getTamano());

        Nodo<Tiquete> actual = nodo.getTiquetes().getCabeza();
        if (actual == null) {
            System.out.println("  (sin tiquetes)");
        }
        while (actual != null) {
            Tiquete t = actual.getDato();
            System.out.println("  " + t + " | ingreso: " + t.getFechaIngreso());
            actual = actual.getSiguiente();
        }
        espacioFinal();
    }

    /**
     * Salida: entradas del historial clinico del paciente, mas reciente primero.
     * El arbol valida que el paciente exista y entrega su identificacion oficial;
     * con esa identificacion el Modulo 1.2 devuelve su pila de historial.
     */
    public void buscarHistorial(String identificacion) {

        indexar();
        IndicePaciente nodo = buscarEnArbol(identificacion);
        if (nodo == null) {
            System.out.println("no existe un paciente con identificacion " + identificacion + ".");
            espacioFinal();
            return;
        }

        System.out.println("*** historial de atenciones de " + nodo.getPaciente().getNombre()
                + " (" + nodo.getIdentificacion() + ") ***");

        Lista<EntradaHistorial> historial = atencion.obtenerHistorial(nodo.getIdentificacion());
        if (historial.estaVacia()) {
            System.out.println("  (sin atenciones registradas)");
        } else {
            Nodo<EntradaHistorial> actual = historial.getCabeza();
            while (actual != null) {
                System.out.println("  " + actual.getDato());
                actual = actual.getSiguiente();
            }
        }
        espacioFinal();
    }

    // Busqueda 3: por medico
    //Salida: unidades (salas/consultorios) a cargo del medico con su fila deespera, y las consultas que ese medico ha atendido.
    public void buscarPorMedico(String medico) {

        indexar();

        System.out.println("*** busqueda por medico: " + medico + " ***");
        boolean hay = false;

        // Parte 1: unidades a cargo del medico (Modulo 1.3) con su fila.
        Nodo<UnidadAtencion> nu = salas.getUnidades().getCabeza();

        while (nu != null) {
            UnidadAtencion u = nu.getDato();
            if (u.getMedico() != null && u.getMedico().equalsIgnoreCase(medico)) {
                System.out.println("  unidad " + u.getCodigo() + " (" + u.getEspecialidad() + "):");
                mostrarFila(u.getCodigo());
                hay = true;
            }
            nu = nu.getSiguiente();
        }

        // Parte 2: consultas atendidas por el medico (Modulo 1.2).
        Nodo<Consulta> nc = atencion.getConsultas().getCabeza();
        while (nc != null) {

            Consulta c = nc.getDato();

            if (c.getMedico() != null && c.getMedico().equalsIgnoreCase(medico)) {

                System.out.println("  consulta #" + c.getId() + " | " + fichaDelTiquete(c.getIdTiquete()) + " | lugar " + c.getLugarAtencion() + " | estado " + c.getEstado());
                hay = true;
            }

            nc = nc.getSiguiente();
        }

        if (!hay) {
            System.out.println("  (sin resultados para ese medico)");
        }
        espacioFinal();
    }

    // Busqueda 4: por fecha (formato aaaa-mm-dd)
    //Salida: tiquetes ingresados y consultas iniciadas en esa fecha.

    public void buscarPorFecha(String fecha) {

        indexar();
        System.out.println("*** busqueda por fecha: " + fecha + " ***");
        boolean hay = false;

        System.out.println("  tiquetes ingresados:");
        boolean hayTiquete = false;
        Nodo<Tiquete> nt = registro.getTiquetes().getCabeza();

        while (nt != null) {

            Tiquete t = nt.getDato();

            if (t.getFechaIngreso() != null && t.getFechaIngreso().startsWith(fecha)) {

                System.out.println("    " + t + " | ingreso: " + t.getFechaIngreso());
                hayTiquete = true;
                hay = true;

            }
            nt = nt.getSiguiente();
        } if (!hayTiquete) {

            System.out.println("    (ninguno)");

        }

        System.out.println("  consultas iniciadas:");
        boolean hayConsulta = false;
        Nodo<Consulta> nc = atencion.getConsultas().getCabeza();

        while (nc != null) {

            Consulta c = nc.getDato();

            if (c.getFechaInicio() != null && c.getFechaInicio().startsWith(fecha)) {

                System.out.println("    consulta #" + c.getId() + " | " + fichaDelTiquete(c.getIdTiquete()) + " | medico " + c.getMedico() + " | lugar " + c.getLugarAtencion());
                hayConsulta = true;
                hay = true;

            }
            nc = nc.getSiguiente();
        }
        if (!hayConsulta) {

            System.out.println("    (ninguna)");
        }

        if (!hay) {

            System.out.println("  (sin resultados para esa fecha)");
        }
        espacioFinal();
    }

 
    // Busqueda 5: por salon o consultorio (codigo S-x / C-y)
    // Salida: pacientes en espera en esa unidad y consultas atendidas ahi

    public void buscarPorLugar(String codigo) {

        indexar();
        System.out.println("*** busqueda por salon/consultorio: " + codigo + " ***");

        UnidadAtencion u = salas.buscarUnidad(codigo);

        if (u == null) {

            System.out.println("  la unidad " + codigo + " no existe.");
            espacioFinal();
            return;
        }

        System.out.println("  en espera en " + codigo + " (" + u.getCategoria() + " " + u.getTipo() + ", medico " + u.getMedico() + "):");
        mostrarFila(codigo);

        System.out.println("  consultas en " + codigo + ":");
        boolean hayConsulta = false;
        Nodo<Consulta> nc = atencion.getConsultas().getCabeza();

        while (nc != null) {

            Consulta c = nc.getDato();


            if (codigo.equalsIgnoreCase(c.getLugarAtencion())) {
                System.out.println("    consulta #" + c.getId() + " | " + fichaDelTiquete(c.getIdTiquete()) + " | medico " + c.getMedico() + " | estado " + c.getEstado());
                hayConsulta = true;
            }
            nc = nc.getSiguiente();
        }
        if (!hayConsulta) {
            System.out.println("    (sin consultas)");
        }
        espacioFinal();
    }

    // Vistas del arbol
    //Recorrido inorden: izquierda -> nodo -> derecha. devuelve ordenado
     
    public void listarPacientes() {

        indexar();

        if (arbol.estaVacio()) {

            System.out.println("el indice esta vacio: todavia no hay pacientes registrados.");
            espacioFinal();
            return;

        }

        System.out.println("*** pacientes ordenados por identificacion (inorden del ABB) ***");
        Lista<IndicePaciente> ordenados = arbol.recorrerInorden();
        Nodo<IndicePaciente> actual = ordenados.getCabeza();
        int posicion = 1;

        while (actual != null) {

            System.out.println("  " + posicion + ". " + actual.getDato());
            actual = actual.getSiguiente();
            posicion++;

        }
        System.out.println("  pacientes distintos: " + arbol.cuentaNodos() + " | altura del arbol: " + arbol.altura());
        espacioFinal();
    }

    // Dibuja la forma del arbol para revisar como quedo el indice.
    public void mostrarEstructuraArbol() {

        indexar();

        if (arbol.estaVacio()) {

            System.out.println("el indice esta vacio: todavia no hay pacientes registrados.");
            espacioFinal();
            return;

        }

        System.out.println("*** forma del arbol ABB (preorden con sangria) ***");
        arbol.imprimirEstructura();
        System.out.println("  nodos: " + arbol.cuentaNodos() + " | altura: " + arbol.altura() + " | comparaciones maximas por busqueda: " + arbol.altura());
        espacioFinal();
    }

    // Me cuesta leer en consola agrego funcion para crear espacios al final o modificar algun borde para que sea más sencillo ;D
    private void espacioFinal() {

        System.out.println();
        System.out.println();
    }

    // Imprime la fila (Cola) de una unidad sin desencolar a nadie.
    private void mostrarFila(String codigo) {

        Cola<Tiquete> fila = salas.filaDe(codigo);
        Nodo<Tiquete> actual = fila == null ? null : fila.getFrente();
        if (actual == null) {
            System.out.println("    (fila vacia)");
            return;
        }

        int posicion = 1;
        while (actual != null) {
            System.out.println("    " + posicion + ". " + actual.getDato());
            actual = actual.getSiguiente();
            posicion++;
        }
    }

    /**
     * Una Consulta solo guarda el id del tiquete, no el nombre del paciente.
     * Se busca el tiquete en el registro, se saca su identificacion y con ella
     * se consulta el arbol para traer la ficha oficial del paciente.
     */
    private String fichaDelTiquete(int idTiquete) {

        Tiquete tiquete = registro.buscarTiquete(idTiquete);

        if (tiquete == null || tiquete.getPaciente() == null) {

            return "tiquete " + idTiquete + " (paciente desconocido)";
        }

        IndicePaciente nodo = buscarEnArbol(tiquete.getPaciente().getIdentificacion());

        if (nodo == null) {
            return tiquete.getPaciente().getNombre();
        }
        return nodo.getPaciente().getNombre() + " (" + nodo.getIdentificacion() + ")";
    }
}
