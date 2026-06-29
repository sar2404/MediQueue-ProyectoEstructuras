package modelos;

import persistencia.Json;

public class EntradaHistorial {

    private int idConsulta;
    private String identificacionPaciente;
    private String resultado;   // EN_ATENCION | FINALIZADA | CANCELADA | REINGRESO
    private String resumen;     // texto libre con datos clave de la consulta
    private String fecha;

    public EntradaHistorial(int idConsulta, String identificacionPaciente,
                            String resultado, String resumen, String fecha) {
        this.idConsulta = idConsulta;
        this.identificacionPaciente = identificacionPaciente;
        this.resultado = resultado;
        this.resumen = resumen;
        this.fecha = fecha;
    }

    public EntradaHistorial() {
    }

    // --- getters y setters ---

    public int getIdConsulta() { return idConsulta; }
    public void setIdConsulta(int idConsulta) { this.idConsulta = idConsulta; }

    public String getIdentificacionPaciente() { return identificacionPaciente; }
    public void setIdentificacionPaciente(String id) { this.identificacionPaciente = id; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    @Override
    public String toString() {
        return "[" + fecha + "] Consulta #" + idConsulta
                + " | " + resultado + " | " + resumen;
    }

    public String aTextoJson() {
        return "{ \"idConsulta\": " + idConsulta
                + ", \"identificacionPaciente\": \"" + identificacionPaciente + "\""
                + ", \"resultado\": \"" + resultado + "\""
                + ", \"resumen\": \"" + escapar(resumen) + "\""
                + ", \"fecha\": \"" + fecha + "\""
                + " }";
    }

    public static EntradaHistorial desdeTexto(String linea) {
        EntradaHistorial e = new EntradaHistorial();
        e.idConsulta              = Integer.parseInt(Json.valorDe(linea, "idConsulta"));
        e.identificacionPaciente  = Json.valorDe(linea, "identificacionPaciente");
        e.resultado               = Json.valorDe(linea, "resultado");
        e.resumen                 = Json.valorDe(linea, "resumen");
        e.fecha                   = Json.valorDe(linea, "fecha");
        return e;
    }

    private String escapar(String s) {
        if (s == null) return "";
        return s.replace("\"", "'");
    }
}