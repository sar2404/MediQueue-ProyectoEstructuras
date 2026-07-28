package cr.ac.ufidelitas.leccion04.mediqueue;

import estructuras.Nodo;
import modelos.Consulta;
import modelos.Consultorio;
import modelos.Sala;
import modelos.Tiquete;
import modelos.Usuario;
import servicios.ServicioAtencion;
import servicios.ServicioBusquedaPacientes;
import servicios.ServicioConfiguracion;
import servicios.ServicioRegistro;
import servicios.ServicioSalasConsultorios;

import java.util.Scanner;

/**
 * Punto de entrada de MediQueue.
 *
 * Coordina todos los modulos del proyecto mediante un menu anidado:
 *   1.0 Configuracion de la sede y login    -> ServicioConfiguracion
 *   1.1 Registro de pacientes (colas)        -> ServicioRegistro
 *   1.2 Atencion de pacientes (colas y pilas) -> ServicioAtencion
 *   1.3 Salas y consultorios (lista de colas) -> ServicioSalasConsultorios
 *   1.4 Historial y busquedas (arbol ABB)     -> ServicioBusquedaPacientes
 *
 * El menu principal ofrece un modulo por opcion; cada modulo tiene su submenu.
 */
public class MediQueue {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Modulo 1.0: configuracion o carga de la sede
        ServicioConfiguracion config = new ServicioConfiguracion();
        if (config.existeConfiguracion()) {
            config.cargar();
        } else {
            config.configurar(sc);
        }

        // Login bloqueante (3 intentos)
        if (!login(sc, config)) {
            System.out.println("demasiados intentos. saliendo.");
            return;
        }

        // Servicios que dependen de la sede ya configurada. Cada uno carga su
        // propia persistencia (tiquetes.json, salas.json, atendidos/historial.json).

        ServicioRegistro registro = new ServicioRegistro();
        ServicioSalasConsultorios salas = new ServicioSalasConsultorios(config.getSede(), registro);
        ServicioAtencion atencion = new ServicioAtencion();

        // El Modulo 1.4 se apoya en los tres anteriores: de ahi saca los tiquetes,
        // las consultas y las filas sobre los que hace sus busquedas.

        ServicioBusquedaPacientes busquedas = new ServicioBusquedaPacientes(registro, atencion, salas);

        menuPrincipal(sc, config, registro, salas, atencion, busquedas);
    }

    private static boolean login(Scanner sc, ServicioConfiguracion config) {

        System.out.println("*** login mediqueue ***");
        for (int intentos = 0; intentos < 3; intentos++) {
            System.out.print("usuario: ");
            String usuario = sc.nextLine().trim();
            System.out.print("contrasena: ");
            String password = sc.nextLine().trim();
            Usuario u = config.autenticar(usuario, password);
            if (u != null) {
                System.out.println("bienvenido, " + u.getUsuario() + "\n");
                return true;
            }
            System.out.println("credenciales invalidas.");
        }
        return false;
    }


    // Menu principal: un modulo por opcion
    /** Vamos a crear subfunciones de menu para poder modificarlas cómodamente a lo largo del proyecto, 
     * de esta forma es más sencillo el proceso de debug y crecimiento del programa */

    private static void menuPrincipal(Scanner sc, ServicioConfiguracion config,
        ServicioRegistro registro,
        ServicioSalasConsultorios salas,
        ServicioAtencion atencion,
        ServicioBusquedaPacientes busquedas) {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("\n*** mediqueue - " + config.getSede().getNombre() + " ***");
            System.out.println("1. Configuracion de la sede (1.0)");
            System.out.println("2. Registro de pacientes (1.1)");
            System.out.println("3. Salas y consultorios (1.3)");
            System.out.println("4. Atencion de pacientes (1.2)");
            System.out.println("5. Historial y busquedas (1.4)");
            System.out.println("0. salir");
            opcion = leerOpcion(sc);

            switch (opcion) {

                case 1 -> menuConfiguracion(sc, config, salas);
                case 2 -> menuRegistro(sc, registro);
                case 3 -> menuSalas(sc, salas);
                case 4 -> menuAtencion(sc, registro, atencion);
                case 5 -> menuBusquedas(sc, busquedas);
                case 0 -> System.out.println("hasta luego.");
                default -> System.out.println("opcion invalida.");

            }
        }
    }


    // Submenu 1.0 - Configuracion 

    private static void menuConfiguracion(Scanner sc, ServicioConfiguracion config,
        ServicioSalasConsultorios salas) {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("\n-- configuracion (1.0) --");
            System.out.println("1. ver configuracion de la sede");
            System.out.println("2. reconfigurar sede");
            System.out.println("0. volver");
            opcion = leerOpcion(sc);

            switch (opcion) {

                case 1 -> mostrarConfiguracion(config);

                case 2 -> {
                    config.configurar(sc);
                    salas.sincronizarConSede(config.getSede());
    
                }
                case 0 -> { }
                default -> System.out.println("opcion invalida.");
            }
        }
    }
    
    // Submenu 1.1 - Registro

    private static void menuRegistro(Scanner sc, ServicioRegistro registro) {
        int opcion = -1;
        while (opcion != 0) {

            System.out.println("\n-- registro de pacientes (1.1) --");
            System.out.println("1. registrar paciente y generar tiquete");
            System.out.println("2. ver cola de prioridad");
            System.out.println("3. ver todos los tiquetes");
            System.out.println("0. volver");
            opcion = leerOpcion(sc);

            switch (opcion) {
                case 1 -> registro.registrarPaciente(sc);
                case 2 -> registro.mostrarCola();
                case 3 -> registro.mostrarTodos();
                case 0 -> { }
                default -> System.out.println("opcion invalida.");
            }
        }
    }


    // Submenu 1.3 - Salas y consultorios

    private static void menuSalas(Scanner sc, ServicioSalasConsultorios salas) {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("\n-- salas y consultorios (1.3) --");
            System.out.println("1. asignar siguiente paciente a la fila mas corta");
            System.out.println("2. configurar medico, enfermera y especialidad");
            System.out.println("3. ver filas de salas y consultorios");
            System.out.println("4. llamar siguiente paciente de una unidad");
            System.out.println("0. volver");
            opcion = leerOpcion(sc);

            switch (opcion) {
                case 1 -> salas.asignarSiguientePaciente();
                case 2 -> salas.configurarPersonal(sc);
                case 3 -> salas.mostrarEstado();
                case 4 -> salas.llamarSiguientePaciente(sc);
                case 0 -> { }
                default -> System.out.println("opcion invalida.");

            }
        }
    }


    // Submenu 1.2 - Atencion

    private static void menuAtencion(Scanner sc, ServicioRegistro registro, ServicioAtencion atencion) {

        int opcion = -1;
        while (opcion != 0) {

            System.out.println("\n-- atencion de pacientes (1.2) --");
            System.out.println("1. iniciar atencion de un tiquete");
            System.out.println("2. finalizar atencion");
            System.out.println("3. cancelar atencion");
            System.out.println("4. reingreso de paciente");
            System.out.println("5. ver consultas registradas");
            System.out.println("6. ver historial (ultimos n) de un paciente");
            System.out.println("7. exportar historial completo de un paciente");
            System.out.println("0. volver");
            opcion = leerOpcion(sc);

            switch (opcion) {
                case 1 -> iniciarAtencion(sc, registro, atencion);
                case 2 -> finalizarAtencion(sc, registro, atencion);
                case 3 -> cancelarAtencion(sc, registro, atencion);
                case 4 -> reingresarPaciente(sc, registro, atencion);
                case 5 -> atencion.mostrarTodasConsultas();
                case 6 -> mostrarHistorial(sc, atencion);
                case 7 -> exportarHistorial(sc, atencion);
                case 0 -> { }
                default -> System.out.println("opcion invalida.");

            }
        }
    }

    // Submenu 1.4 - Historial y busquedas (arbol ABB)

    private static void menuBusquedas(Scanner sc, ServicioBusquedaPacientes busquedas) {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("\n-- historial y busquedas / arbol ABB (1.4) --");
            System.out.println("1. buscar tiquetes de un paciente (identificacion)");
            System.out.println("2. buscar historial de atenciones (identificacion)");
            System.out.println("3. buscar por medico");
            System.out.println("4. buscar por fecha (aaaa-mm-dd)");
            System.out.println("5. buscar por salon/consultorio");
            System.out.println("6. listar pacientes ordenados (inorden ABB)");
            System.out.println("7. ver la forma del arbol ABB");
            System.out.println("0. volver");
            opcion = leerOpcion(sc);

            switch (opcion) {

                case 1 -> {

                    System.out.print("identificacion: ");
                    busquedas.buscarTiquetes(sc.nextLine().trim());
                }
                case 2 -> {
                    System.out.print("identificacion: ");
                    busquedas.buscarHistorial(sc.nextLine().trim());
                }
                case 3 -> {
                    System.out.print("medico: ");
                    busquedas.buscarPorMedico(sc.nextLine().trim());
                }
                case 4 -> {

                    System.out.print("fecha (aaaa-mm-dd): ");
                    busquedas.buscarPorFecha(sc.nextLine().trim());
                }
                case 5 -> {
                    System.out.print("codigo (S-1, C-2, ...): ");
                    busquedas.buscarPorLugar(sc.nextLine().trim().toUpperCase());
                }

                case 6 -> busquedas.listarPacientes();

                case 7 -> busquedas.mostrarEstructuraArbol();

                case 0 -> { }

                default -> System.out.println("opcion invalida.");
            }
        }
    }

    // Modulo 1.0: mostrar configuracion


    private static void mostrarConfiguracion(ServicioConfiguracion config) {

        System.out.println("sede: " + config.getSede().getNombre());
        System.out.println("-- salas --");
        Nodo<Sala> ns = config.getSede().getSalas().getCabeza();
        while (ns != null) {
            System.out.println("  " + ns.getDato());
            ns = ns.getSiguiente();
        }
        System.out.println("-- consultorios --");
        Nodo<Consultorio> nc = config.getSede().getConsultorios().getCabeza();
        while (nc != null) {
            System.out.println("  " + nc.getDato());
            nc = nc.getSiguiente();
        }
        System.out.println("-- usuarios --");
        config.mostrarUsuarios();
    }

    // Modulo 1.2: se opera por id de tiquete / de consulta


    private static void iniciarAtencion(Scanner sc, ServicioRegistro registro, ServicioAtencion atencion) {

        registro.mostrarTodos();
        int idTiquete = leerEntero(sc, "id del tiquete a atender: ");
        Tiquete tiquete = registro.buscarTiquete(idTiquete);

        if (tiquete == null) {
            System.out.println("no existe un tiquete con ese id.");
            return;
        }

        atencion.iniciarAtencion(tiquete, sc);
    }

    private static void finalizarAtencion(Scanner sc, ServicioRegistro registro, ServicioAtencion atencion) {

        atencion.mostrarTodasConsultas();
        int idConsulta = leerEntero(sc, "id de la consulta a finalizar: ");
        Consulta consulta = atencion.buscarConsulta(idConsulta);

        if (consulta == null) {
            System.out.println("no existe una consulta con ese id.");
            return;
        }

        Tiquete tiquete = registro.buscarTiquete(consulta.getIdTiquete());
        if (tiquete == null) {
            System.out.println("no se encontro el tiquete vinculado a la consulta.");
            return;
        }

        atencion.finalizarAtencion(idConsulta, tiquete, sc);
    }

    private static void cancelarAtencion(Scanner sc, ServicioRegistro registro, ServicioAtencion atencion) {

        atencion.mostrarTodasConsultas();

        int idConsulta = leerEntero(sc, "id de la consulta a cancelar: ");
        Consulta consulta = atencion.buscarConsulta(idConsulta);

        if (consulta == null) {
            System.out.println("no existe una consulta con ese id.");
            return;
        }

        Tiquete tiquete = registro.buscarTiquete(consulta.getIdTiquete());

        if (tiquete == null) {
            System.out.println("no se encontro el tiquete vinculado a la consulta.");
            return;
        }
        atencion.cancelarAtencion(idConsulta, tiquete);
    }

    private static void reingresarPaciente(Scanner sc, ServicioRegistro registro, ServicioAtencion atencion) {

        System.out.print("identificacion del paciente a reingresar: ");
        String identificacion = sc.nextLine().trim();
        Tiquete tiquete = registro.buscarUltimoPorIdentificacion(identificacion);

        if (tiquete == null) {
            System.out.println("no hay tiquetes previos para esa identificacion.");
            return;
        }

        atencion.reingresar(tiquete, sc);
    }

    private static void mostrarHistorial(Scanner sc, ServicioAtencion atencion) {

        System.out.print("identificacion del paciente: ");
        String identificacion = sc.nextLine().trim();
        int n = leerEntero(sc, "cuantos registros mostrar: ");
        atencion.mostrarUltimosN(identificacion, n);
    }

    private static void exportarHistorial(Scanner sc, ServicioAtencion atencion) {

        System.out.print("identificacion del paciente: ");
        String identificacion = sc.nextLine().trim();
        atencion.exportarHistorial(identificacion);
    }

    // utilidades de lectura

    private static int leerOpcion(Scanner sc) {

        System.out.print("opcion: ");
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }

    }

    private static int leerEntero(Scanner sc, String mensaje) {
        
        while (true) {
            
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("ingrese un numero valido.");
            }
        }
    }
}
