package servicios;

import estructuras.ColaPrioridad;
import estructuras.ElementoPrioridad;
import estructuras.Lista;
import estructuras.Nodo;
import modelos.Paciente;
import modelos.Tiquete;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Modulo 1.1: registro de pacientes, cola de prioridades y persistencia.
 */
public class ServicioRegistro {

    private static final String ARCHIVO = "tiquetes.json";
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Lista<Tiquete> tiquetes;
    private final ColaPrioridad<Tiquete> colaEspera;
    private int siguienteIdTiquete;
    private int siguienteIdPaciente;

    public ServicioRegistro() {
        tiquetes = new Lista<>();
        colaEspera = new ColaPrioridad<>();
        siguienteIdTiquete = 1;
        siguienteIdPaciente = 1;
        cargar();
    }

    public Tiquete registrarPaciente(Scanner sc) {
        System.out.println(" registro de paciente (modulo 1.1) ");
        String nombre = leerTexto(sc, "nombre completo: ");
        String identificacion = leerTexto(sc, "identificacion: ");
        int edad = leerEntero(sc, "edad: ", 0);
        String tipoSeguro = leerTexto(sc, "tipo de seguro: ");

        System.out.println("prioridad: 1. CRITICA  2. ALTA  3. MEDIA  4. BAJA");
        int opcionPrioridad = leerEnteroRango(sc, "seleccione: ", 1, 4);
        String prioridad = nombrePrioridad(opcionPrioridad);

        System.out.println("tipo de atencion: 1. EMERGENCIA  2. PREFERENCIAL  3. GENERAL");
        int opcionTipo = leerEnteroRango(sc, "seleccione: ", 1, 3);
        String tipoAtencion = switch (opcionTipo) {
            case 1 -> "EMERGENCIA";
            case 2 -> "PREFERENCIAL";
            default -> "GENERAL";
        };

        Paciente paciente = new Paciente(siguienteIdPaciente++, nombre, identificacion, edad, tipoSeguro);
        Tiquete tiquete = crearTiquete(paciente, prioridad, tipoAtencion);
        System.out.println("paciente registrado. " + tiquete + "\n");
        return tiquete;
    }

    public Tiquete crearTiquete(Paciente paciente, String prioridad, String tipoAtencion) {
        Tiquete tiquete = new Tiquete(
                siguienteIdTiquete++,
                paciente,
                ahora(),
                "-1",
                prioridad,
                tipoAtencion,
                "EN ESPERA"
        );
        tiquetes.agregar(tiquete);
        colaEspera.encolar(tiquete, valorPrioridad(prioridad));
        guardar();
        return tiquete;
    }

    public Tiquete verSiguienteEnEspera() {
        return colaEspera.frente();
    }

    public Tiquete asignarSiguienteA(String codigoUnidad) {
        Tiquete tiquete = colaEspera.desencolar();
        if (tiquete != null) {
            String codigo = codigoUnidad == null ? "SIN UNIDAD" : codigoUnidad.toUpperCase();
            tiquete.setAtencion("EN FILA" + codigo);
            tiquete.setFechaAtencion("-1");
            guardar();
        }
        return tiquete;
    }

    public void marcarEnAtencion(int idTiquete) {
        Tiquete tiquete = buscarTiquete(idTiquete);
        if (tiquete != null) {
            tiquete.setAtencion("EN_ATENCION");
            tiquete.setFechaAtencion(ahora());
            guardar();
        }
    }

    public Tiquete siguienteParaAtencion() {
        Tiquete tiquete = colaEspera.desencolar();
        if (tiquete != null) {
            tiquete.setAtencion("EN ATENCION");
            tiquete.setFechaAtencion(ahora());
            guardar();
        }
        return tiquete;
    }

    public Tiquete reingresarPaciente(String identificacion) {
        Tiquete anterior = buscarUltimoPorIdentificacion(identificacion);
        if (anterior == null) {
            return null;
        }

        Paciente original = anterior.getPaciente();
        Paciente paciente = new Paciente(
                original.getId(),
                original.getNombre(),
                original.getIdentificacion(),
                original.getEdad(),
                original.getTipoSeguro()
        );
        String prioridad = "CRITICA".equals(anterior.getPrioridad()) ? "CRITICA" : "ALTA";
        return crearTiquete(paciente, prioridad, "REINGRESO");
    }

    public void marcarEstado(int idTiquete, String estado) {
        Tiquete tiquete = buscarTiquete(idTiquete);
        if (tiquete != null) {
            tiquete.setAtencion(estado);
            guardar();
        }
    }

    public Tiquete buscarTiquete(int id) {
        Nodo<Tiquete> actual = tiquetes.getCabeza();
        while (actual != null) {
            if (actual.getDato().getId() == id) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public Tiquete buscarUltimoPorIdentificacion(String identificacion) {
        Tiquete encontrado = null;
        Nodo<Tiquete> actual = tiquetes.getCabeza();
        while (actual != null) {
            Tiquete tiquete = actual.getDato();
            if (tiquete.getPaciente().getIdentificacion().equalsIgnoreCase(identificacion)) {
                encontrado = tiquete;
            }
            actual = actual.getSiguiente();
        }
        return encontrado;
    }

    public void mostrarCola() {
        System.out.println(" cola de pacientes ");
        if (colaEspera.estaVacia()) {
            System.out.println("no hay pacientes en espera.\n");
            return;
        }

        int posicion = 1;
        Nodo<ElementoPrioridad<Tiquete>> actual = colaEspera.getFrente();
        while (actual != null) {
            System.out.println(posicion + ". " + actual.getDato().getDato());
            actual = actual.getSiguiente();
            posicion++;
        }
        System.out.println("total en espera: " + colaEspera.getTamano() + "\n");
    }

    public void mostrarTodos() {
        System.out.println(" tiquetes registrados ");
        Nodo<Tiquete> actual = tiquetes.getCabeza();
        if (actual == null) {
            System.out.println("no existen tiquetes.\n");
            return;
        }
        while (actual != null) {
            System.out.println(actual.getDato());
            actual = actual.getSiguiente();
        }
        System.out.println();
    }

    public Lista<Tiquete> getTiquetes() {
        return tiquetes;
    }

    public void guardar() {
        try (FileWriter fw = new FileWriter(ARCHIVO)) {
            fw.write("{\n\"tiquetes\": [\n");
            Nodo<Tiquete> actual = tiquetes.getCabeza();
            while (actual != null) {
                fw.write(actual.getDato().aTextoJson());
                if (actual.getSiguiente() != null) {
                    fw.write(",");
                }
                fw.write("\n");
                actual = actual.getSiguiente();
            }
            fw.write("]\n}\n");
        } catch (IOException e) {
            System.out.println("error al guardar tiquetes: " + e.getMessage());
        }
    }

    private void cargar() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.startsWith("{" ) || !linea.contains("\"pacienteId\"")) {
                    continue;
                }
                Tiquete tiquete = Tiquete.desdeTexto(linea);
                tiquetes.agregar(tiquete);
                if ("EN ESPERA".equals(tiquete.getAtencion())) {
                    colaEspera.encolar(tiquete, valorPrioridad(tiquete.getPrioridad()));
                }
                if (tiquete.getId() >= siguienteIdTiquete) {
                    siguienteIdTiquete = tiquete.getId() + 1;
                }
                if (tiquete.getPaciente().getId() >= siguienteIdPaciente) {
                    siguienteIdPaciente = tiquete.getPaciente().getId() + 1;
                }
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("error al cargar tiquetes: " + e.getMessage());
        }
    }

    public static int valorPrioridad(String prioridad) {
        if (prioridad == null) {
            return 4;
        }
        return switch (prioridad.toUpperCase()) {
            case "CRITICA" -> 1;
            case "ALTA" -> 2;
            case "MEDIA" -> 3;
            default -> 4;
        };
    }

    private String nombrePrioridad(int opcion) {
        return switch (opcion) {
            case 1 -> "CRITICA";
            case 2 -> "ALTA";
            case 3 -> "MEDIA";
            default -> "BAJA";
        };
    }

    private String ahora() {
        return LocalDateTime.now().format(FORMATO);
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

    private int leerEntero(Scanner sc, String mensaje, int minimo) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = Integer.parseInt(sc.nextLine().trim());
                if (valor >= minimo) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // Se muestra el mismo mensaje para ambos errores de validacion.
            }
            System.out.println("ingrese un numero valido mayor o igual a " + minimo + ".");
        }
    }

    private int leerEnteroRango(Scanner sc, String mensaje, int minimo, int maximo) {
        while (true) {
            int valor = leerEntero(sc, mensaje, minimo);
            if (valor <= maximo) {
                return valor;
            }
            System.out.println("seleccione una opcion entre " + minimo + " y " + maximo + ".");
        }
    }
}
