package servicios;

import estructuras.Nodo;
import modelos.Consultorio;
import modelos.Sala;
import modelos.Sede;
import modelos.Usuario;
import persistencia.Json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// modulo 1.0: configuracion de la sede, persistencia en config.json y login
public class ServicioConfiguracion {

    private static final String ARCHIVO = "config.json";
    private Sede sede;

    public boolean existeConfiguracion() {

        return new File(ARCHIVO).exists();
    }

    public Sede getSede() {

        return sede;
    }

    // asistente de primera ejecucion
    public void configurar(Scanner sc) {

        System.out.println("*** configuracion de la sede (modulo 1.0) ***");

        System.out.print("nombre de la sede: ");
        String nombre = sc.nextLine().trim();

        sede = new Sede(nombre);

        int salasMedicas = leerEntero(sc, "cantidad de salas medicas (min 2): ", 2);
        int emergencias = leerEntero(sc, "cantidad de salas de emergencia (min 1): ", 1);

        int consultorios = leerEntero(sc, "cantidad de consultorios (min 2): ", 2);

        generarSalas(salasMedicas, emergencias);
        generarConsultorios(consultorios);
        sembrarUsuarios();

        guardar();
        System.out.println("configuracion guardada en " + ARCHIVO + "\n");
    }

    // una sala preferencial entre las medicas yuna critica entre las de emergencia
    private void generarSalas(int salasMedicas, int emergencias) {

        int numero = 1;
        for (int i = 0; i < salasMedicas; i++) {
            String tipo = (i == 0) ? "PREFERENCIAL" : "NORMAL";
            sede.getSalas().agregar(new Sala(numero, tipo, 10));
            numero++;
        }
        for (int i = 0; i < emergencias; i++) {
            String tipo = (i == 0) ? "CRITICA" : "EMERGENCIA";
            sede.getSalas().agregar(new Sala(numero, tipo, 10));
            numero++;
        }
    }

    // un consultorio preferencial, resto normales
    private void generarConsultorios(int cantidad) {

        for (int i = 0; i < cantidad; i++) {

            String tipo = (i == 0) ? "PREFERENCIAL" : "NORMAL";
            sede.getConsultorios().agregar(new Consultorio(i + 1, tipo, "Valoracion"));
        }
    }

    private void sembrarUsuarios() {

        sede.getUsuarios().agregar(new Usuario("santiago", "123"));
        sede.getUsuarios().agregar(new Usuario("admin", "123"));
    }

    // escribe el config.json a mano con stringbuilder
    public void guardar() {

        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("\"nombre\": \"").append(sede.getNombre()).append("\",\n");

        sb.append("\"salas\": [\n");
        Nodo<Sala> ns = sede.getSalas().getCabeza();
        while (ns != null) {

            sb.append(ns.getDato().aTextoJson());
            if (ns.getSiguiente() != null) {
                sb.append(",");
            }
            sb.append("\n");
            ns = ns.getSiguiente();
            
        }
        sb.append("],\n");

        sb.append("\"consultorios\": [\n");
        Nodo<Consultorio> nc = sede.getConsultorios().getCabeza();
        while (nc != null) {
            sb.append(nc.getDato().aTextoJson());
            if (nc.getSiguiente() != null) {
                sb.append(",");
            }
            sb.append("\n");
            nc = nc.getSiguiente();
        }
        sb.append("],\n");

        sb.append("\"usuarios\": [\n");
        Nodo<Usuario> nu = sede.getUsuarios().getCabeza();
        while (nu != null) {
            sb.append(nu.getDato().aTextoJson());
            if (nu.getSiguiente() != null) {
                sb.append(",");
            }
            sb.append("\n");
            nu = nu.getSiguiente();
        }
        sb.append("]\n");

        sb.append("}\n");

        try {
            FileWriter fw = new FileWriter(ARCHIVO);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            System.out.println("error al guardar: " + e.getMessage());
        }
    }

    // lee el config.json linea por linea
    public void cargar() {

        try {
            BufferedReader br = new BufferedReader(new FileReader(ARCHIVO));
            sede = new Sede();
            String seccion = "";
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.startsWith("\"nombre\"")) {
                    sede.setNombre(Json.valorDe(linea, "nombre"));
                } else if (linea.startsWith("\"salas\"")) {
                    seccion = "salas";
                } else if (linea.startsWith("\"consultorios\"")) {
                    seccion = "consultorios";
                } else if (linea.startsWith("\"usuarios\"")) {
                    seccion = "usuarios";
                } else if (linea.startsWith("{")) {
                    if (seccion.equals("salas")) {
                        sede.getSalas().agregar(Sala.desdeTexto(linea));
                    } else if (seccion.equals("consultorios")) {
                        sede.getConsultorios().agregar(Consultorio.desdeTexto(linea));
                    } else if (seccion.equals("usuarios")) {
                        sede.getUsuarios().agregar(Usuario.desdeTexto(linea));
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("error al cargar: " + e.getMessage());
        }
    }

    public Usuario autenticar(String usuario, String password) {

        Nodo<Usuario> actual = sede.getUsuarios().getCabeza();
        while (actual != null) {
            Usuario u = actual.getDato();
            if (u.getUsuario().equals(usuario) && u.getPassword().equals(password)) {
                return u;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    private int leerEntero(Scanner sc, String mensaje, int minimo) {

        while (true) {
            System.out.print(mensaje);
            try {
                int valor = Integer.parseInt(sc.nextLine().trim());
                if (valor < minimo) {
                    System.out.println("debe ser al menos " + minimo + ".");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("ingrese un numero  valido.");
            }
        }
    }

    //Agreué el método Mostrar usuarios. 
    
    public void mostrarUsuarios() {

        Nodo<Usuario> actual = sede.getUsuarios().getCabeza();
        while (actual != null) {
            System.out.println(actual.getDato());
            actual = actual.getSiguiente();
        }

    }

}
