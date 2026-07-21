package cr.ac.ufidelitas.leccion04.mediqueue;

import estructuras.Nodo;
import modelos.Consultorio;
import modelos.Sala;
import modelos.Usuario;
import servicios.ServicioConfiguracion;
import estructuras.Lista;
import modelos.Paciente;
import servicios.ServicioBusquedaPacientes;

import java.util.Scanner;

// punto de entrada: configuracion (modulo 1.0), login y menu principal
public class MediQueue {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ServicioConfiguracion config = new ServicioConfiguracion();

        if (config.existeConfiguracion()) {
            config.cargar();
        } else {
            config.configurar(sc);
        }

        if (!login(sc, config)) {
            System.out.println("demasiados intentos. saliendo.");
            return;
        }

        menuPrincipal(sc, config);

        // Prueba del Arbol ABB para almacenar pacientes por identificacion
        ServicioBusquedaPacientes servicioPacientes = new ServicioBusquedaPacientes();

        Paciente p1 = new Paciente(1, "Fabian", "30555001", 25, "Efectivo");
        Paciente p2 = new Paciente(2, "Santiago", "10222002", 19, "Seguro");
        Paciente p3 = new Paciente(3, "Jurgens", "50111003", 22, "Seguro");

        servicioPacientes.agregarPaciente(p1);
        servicioPacientes.agregarPaciente(p2);
        servicioPacientes.agregarPaciente(p3);

        System.out.println("Pacientes almacenados en el Arbol ABB:");
        servicioPacientes.mostrarPacientes();

// Prueba de busqueda utilizando el Arbol ABB - Fabián
        Paciente pacienteBuscar = new Paciente();
        pacienteBuscar.setIdentificacion("30555001");

        Paciente resultado = servicioPacientes.buscarPaciente(pacienteBuscar);

        System.out.println("Resultado de busqueda:");
        System.out.println(resultado);

// Prueba del metodo buscar agregado a Lista enlazada - Fabián
        Lista<Paciente> listaPacientes = new Lista<>();

        listaPacientes.agregar(p1);
        listaPacientes.agregar(p2);
        listaPacientes.agregar(p3);

        Paciente encontrado = listaPacientes.buscar(p2);

        System.out.println("Paciente encontrado en Lista:");
        System.out.println(encontrado);

// Prueba del metodo remove agregado a Lista enlazada - Fabián
        boolean eliminado = listaPacientes.remove(p2);

        System.out.println("Paciente eliminado:");
        System.out.println(eliminado);

// Prueba del metodo toString agregado a Lista - Fabián
        System.out.println("Contenido de la Lista:");
        System.out.println(listaPacientes);

// Prueba del metodo contar agregado a Lista - Fabián
        System.out.println("Cantidad de pacientes registrados:");
        System.out.println(listaPacientes.contar());

// Prueba del metodo toString agregado al Nodo - Fabián
        Nodo<Paciente> nodo = new Nodo<>(p1);

        System.out.println("Informacion del Nodo:");
        System.out.println(nodo);

    }

    private static boolean login(Scanner sc, ServicioConfiguracion config) {

        System.out.println("=== login mediqueue ===");
        for (int intentos = 0; intentos < 3; intentos++) {
            System.out.print("usuario: ");
            String usuario = sc.nextLine().trim();
            System.out.print("contrasena: ");
            String password = sc.nextLine().trim();
            Usuario u = config.autenticar(usuario, password);
            if (u != null) {
                System.out.println("bienvenido,  " + u.getUsuario() + "\n");
                return true;
            }
            System.out.println("credenciales invalidas.");
        }
        return false;
    }

    private static void menuPrincipal(Scanner sc, ServicioConfiguracion config) {

        int opcion = -1;
        while (opcion != 0) {
            System.out.println("===== mediqueue - " + config.getSede().getNombre() + " =====");
            System.out.println("1. ver configuracion de la sede");
            System.out.println("2. reconfigurar sede");
            System.out.println("0. salir");
            System.out.print("opcion: ");
            try {
                opcion = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                opcion = -1;
            }
            switch (opcion) {
                case 1 ->
                    mostrarConfiguracion(config);
                case 2 ->
                    config.configurar(sc);
                case 0 ->
                    System.out.println("hasta luego.");
                default ->
                    System.out.println("opcion  invalida.");
            }
        }
    }

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
        System.out.println();
    }
}
