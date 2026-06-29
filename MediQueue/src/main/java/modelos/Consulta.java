package modelos;

/**
 * Representa una atencion medica registrada para un paciente.
 * Vinculada a un Tiquete mediante idTiquete.
 */
public class Consulta {

    private int id;
    private int idTiquete;
    private String medico;
    private String diagnostico;
    private String medicamentos;
    private String lugarAtencion;
    private double costo;
    private String estado; // EN_ATENCION, FINALIZADA, CANCELADA, REINGRESO

    public Consulta(int id, int idTiquete, String medico, String lugarAtencion) {
        this.id = id;
        this.idTiquete = idTiquete;
        this.medico = medico;
        this.lugarAtencion = lugarAtencion;
        this.diagnostico = "";
        this.medicamentos = "";
        this.costo = 0.0;
        this.estado = "EN_ATENCION";
    }

    public Consulta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTiquete() {
        return idTiquete;
    }

    public void setIdTiquete(int idTiquete) {
        this.idTiquete = idTiquete;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }

    public String getLugarAtencion() {
        return lugarAtencion;
    }

    public void setLugarAtencion(String lugarAtencion) {
        this.lugarAtencion = lugarAtencion;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Consulta{id=" + id + ", tiquete=" + idTiquete + ", medico='" + medico + "', lugar='" + lugarAtencion + "', estado='" + estado + "'}";
    }
}