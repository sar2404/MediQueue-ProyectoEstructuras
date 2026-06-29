package modelos;

/**
 * Entrada del historial clinico de un paciente.
 * Se apila en una Pila<EntradaHistorial> por cada paciente.
 */
public class EntradaHistorial {

    private int idConsulta;
    private String identificacionPaciente;
    private String resultado; // FINALIZADA, CANCELADA, REINGRESO
    private String fecha;

    public EntradaHistorial(int idConsulta, String identificacionPaciente, String resultado, String fecha) {
        this.idConsulta = idConsulta;
        this.identificacionPaciente = identificacionPaciente;
        this.resultado = resultado;
        this.fecha = fecha;
    }

    public EntradaHistorial() {
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getIdentificacionPaciente() {
        return identificacionPaciente;
    }

    public void setIdentificacionPaciente(String identificacionPaciente) {
        this.identificacionPaciente = identificacionPaciente;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "EntradaHistorial{idConsulta=" + idConsulta + ", paciente='" + identificacionPaciente + "', resultado='" + resultado + "', fecha='" + fecha + "'}";
    }
}