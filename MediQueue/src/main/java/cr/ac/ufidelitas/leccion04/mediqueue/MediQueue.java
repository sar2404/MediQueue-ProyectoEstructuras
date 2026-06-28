package cr.ac.ufidelitas.leccion04.mediqueue;

import estructuras.Nodo;
import modelos.Consultorio;
import modelos.Sala;
import modelos.Usuario;
import servicios.ServicioConfiguracion;

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
                case 1 -> mostrarConfiguracion(config);
                case 2 -> config.configurar(sc);
                case 0 -> System.out.println("hasta luego.");
                default -> System.out.println("opcion  invalida.");
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
