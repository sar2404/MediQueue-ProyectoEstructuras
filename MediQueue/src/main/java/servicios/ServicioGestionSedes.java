package servicios;

import estructuras.Arista;
import estructuras.GrafoPonderado;
import estructuras.Nodo;
import estructuras.Vertice;
import modelos.CentroSalud;
import persistencia.Json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ServicioGestionSedes {

    private static final String ARCHIVO = "grafo.json";
    private GrafoPonderado<CentroSalud> grafo;
    private int siguienteId;

    public ServicioGestionSedes() {
        grafo = new GrafoPonderado<>();
        siguienteId = 1;
        cargar();
    }

    public void agregarSede(Scanner sc) {
        System.out.println("*** agregar sede ***");

        System.out.print("nombre de la sede: ");
        String nombre = sc.nextLine().trim();

        System.out.println("tipo: 1. HOSPITAL  2. CLINICA  3. EBAIS");
        int opcion = leerEnteroRango(sc, "seleccione: ", 1, 3);

        String tipo = switch (opcion) {
            case 1 -> "HOSPITAL";
            case 2 -> "CLINICA";
            default -> "EBAIS";
        };

        CentroSalud sede = new CentroSalud(siguienteId++, nombre, tipo);
        grafo.agregarVertice(sede);
        guardar();

        System.out.println("sede agregada: " + sede);
    }

    public void agregarConexion(Scanner sc) {
        mostrarSedes();

        int idOrigen = leerEntero(sc, "id sede origen: ");
        int idDestino = leerEntero(sc, "id sede destino: ");

        CentroSalud origen = buscarPorId(idOrigen);
        CentroSalud destino = buscarPorId(idDestino);

        if (origen == null || destino == null) {
            System.out.println("la sede origen o destino no existe.");
            return;
        }

        int distancia = leerEntero(sc, "distancia en km: ");
        int tiempo = leerEntero(sc, "tiempo en minutos: ");
        int costo = leerEntero(sc, "costo del traslado: ");

        grafo.agregarArista(origen, destino, distancia, tiempo, costo);
        guardar();

        System.out.println("conexion agregada: " + origen.getNombre()
                + " -> " + destino.getNombre());
    }

    public void imprimirGrafo() {
        System.out.println("*** grafo de sedes ***");
        grafo.imprimir();
        System.out.println();
    }

    public void calcularRuta(Scanner sc) {
        mostrarSedes();

        int idOrigen = leerEntero(sc, "id sede origen: ");
        int idDestino = leerEntero(sc, "id sede destino: ");

        CentroSalud origen = buscarPorId(idOrigen);
        CentroSalud destino = buscarPorId(idDestino);

        if (origen == null || destino == null) {
            System.out.println("la sede origen o destino no existe.");
            return;
        }

        System.out.println("*** ruta mas corta ***");
        grafo.dijkstra(origen, destino);
    }

    public void mostrarSedes() {
        System.out.println("*** sedes registradas ***");

        Nodo<Vertice<CentroSalud, Arista<CentroSalud>>> actual
                = grafo.getVertices().getCabeza();

        if (actual == null) {
            System.out.println("no hay sedes registradas.");
            return;
        }

        while (actual != null) {
            System.out.println(actual.getDato().getValor());
            actual = actual.getSiguiente();
        }
    }

    private CentroSalud buscarPorId(int id) {
        Nodo<Vertice<CentroSalud, Arista<CentroSalud>>> actual
                = grafo.getVertices().getCabeza();

        while (actual != null) {
            CentroSalud sede = actual.getDato().getValor();

            if (sede.getId() == id) {
                return sede;
            }

            actual = actual.getSiguiente();
        }

        return null;
    }

    public void guardar() {
        try (FileWriter fw = new FileWriter(ARCHIVO)) {

            fw.write("{\n\"sedes\": [\n");

            Nodo<Vertice<CentroSalud, Arista<CentroSalud>>> actual
                    = grafo.getVertices().getCabeza();

            boolean primera = true;

            while (actual != null) {
                CentroSalud sede = actual.getDato().getValor();

                if (!primera) {
                    fw.write(",\n");
                }

                fw.write("{ \"id\": " + sede.getId()
                        + ", \"nombre\": \"" + escapar(sede.getNombre()) + "\""
                        + ", \"tipo\": \"" + escapar(sede.getTipo()) + "\" }");

                primera = false;
                actual = actual.getSiguiente();
            }

            fw.write("\n],\n\"aristas\": [\n");

            primera = true;
            actual = grafo.getVertices().getCabeza();

            while (actual != null) {
                CentroSalud origen = actual.getDato().getValor();
                Nodo<Arista<CentroSalud>> aristaActual
                        = actual.getDato().getAdyacentes().getCabeza();

                while (aristaActual != null) {
                    Arista<CentroSalud> arista = aristaActual.getDato();

                    if (!primera) {
                        fw.write(",\n");
                    }

                    fw.write("{ \"origen\": " + origen.getId()
                            + ", \"destino\": " + arista.getDestino().getId()
                            + ", \"distancia\": " + arista.getDistancia()
                            + ", \"tiempo\": " + arista.getTiempo()
                            + ", \"costo\": " + arista.getCosto() + " }");

                    primera = false;
                    aristaActual = aristaActual.getSiguiente();
                }

                actual = actual.getSiguiente();
            }

            fw.write("\n]\n}\n");

        } catch (IOException e) {
            System.out.println("error al guardar grafo: " + e.getMessage());
        }
    }

    private void cargar() {
        File archivo = new File(ARCHIVO);

        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            String seccion = "";

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.startsWith("\"sedes\"")) {
                    seccion = "sedes";

                } else if (linea.startsWith("\"aristas\"")) {
                    seccion = "aristas";

                } else if (linea.startsWith("{") && "sedes".equals(seccion)
                        && linea.contains("\"nombre\"")) {

                    int id = Integer.parseInt(Json.valorDe(linea, "id"));
                    String nombre = Json.valorDe(linea, "nombre");
                    String tipo = Json.valorDe(linea, "tipo");

                    CentroSalud sede = new CentroSalud(id, nombre, tipo);
                    grafo.agregarVertice(sede);

                    if (id >= siguienteId) {
                        siguienteId = id + 1;
                    }

                } else if (linea.startsWith("{") && "aristas".equals(seccion)
                        && linea.contains("\"origen\"")) {

                    int idOrigen = Integer.parseInt(Json.valorDe(linea, "origen"));
                    int idDestino = Integer.parseInt(Json.valorDe(linea, "destino"));
                    int distancia = Integer.parseInt(Json.valorDe(linea, "distancia"));
                    int tiempo = Integer.parseInt(Json.valorDe(linea, "tiempo"));
                    int costo = Integer.parseInt(Json.valorDe(linea, "costo"));

                    CentroSalud origen = buscarPorId(idOrigen);
                    CentroSalud destino = buscarPorId(idDestino);

                    if (origen != null && destino != null) {
                        grafo.agregarArista(origen, destino, distancia, tiempo, costo);
                    }
                }
            }

        } catch (IOException | RuntimeException e) {
            System.out.println("error al cargar grafo: " + e.getMessage());
        }
    }

    private int leerEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);

            try {
                return Integer.parseInt(sc.nextLine().trim());

            } catch (NumberFormatException e) {
                System.out.println("ingrese un numero valido.");
            }
        }
    }

    private int leerEnteroRango(Scanner sc, String mensaje, int minimo, int maximo) {
        while (true) {
            int valor = leerEntero(sc, mensaje);

            if (valor >= minimo && valor <= maximo) {
                return valor;
            }

            System.out.println("seleccione una opcion entre "
                    + minimo + " y " + maximo + ".");
        }
    }

    private String escapar(String texto) {
        return texto == null ? "" : texto.replace("\"", "'");
    }
}
