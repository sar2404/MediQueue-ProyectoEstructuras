package modelos;

import persistencia.Json;

public class Consulta {

    private int id;
    private int idTiquete;
    private String medico;
    private String diagnostico;
    private String medicamentos;
    private String observaciones;
    private String procedimientos;
    private String lugarAtencion;   // "S-1" para sala 1, "C-2" para consultorio 2
    private double costo;
    private String estado;          // EN_ATENCION | FINALIZADA | CANCELADA | REINGRESO
    private String fechaInicio;
    private String fechaFin;        

    public Consulta(int id, int idTiquete, String medico, String lugarAtencion) {
        this.id = id;
        this.idTiquete = idTiquete;
        this.medico = medico;
        this.lugarAtencion = lugarAtencion;
        this.diagnostico = "";
        this.medicamentos = "";
        this.observaciones = "";
        this.procedimientos = "";
        this.costo = 0.0;
        this.estado = "EN_ATENCION";
        this.fechaFin = "-1";
    }

    public Consulta() {
    }

    // --- getters y setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdTiquete() { return idTiquete; }
    public void setIdTiquete(int idTiquete) { this.idTiquete = idTiquete; }

    public String getMedico() { return medico; }
    public void setMedico(String medico) { this.medico = medico; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getMedicamentos() { return medicamentos; }
    public void setMedicamentos(String medicamentos) { this.medicamentos = medicamentos; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getProcedimientos() { return procedimientos; }
    public void setProcedimientos(String procedimientos) { this.procedimientos = procedimientos; }

    public String getLugarAtencion() { return lugarAtencion; }
    public void setLugarAtencion(String lugarAtencion) { this.lugarAtencion = lugarAtencion; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    @Override
    public String toString() {
        return "Consulta{id=" + id + ", tiquete=" + idTiquete
                + ", medico='" + medico + "', lugar='" + lugarAtencion
                + "', estado='" + estado + "'}";
    }

    public String aTextoJson() {
        return "{ \"id\": " + id
                + ", \"idTiquete\": " + idTiquete
                + ", \"medico\": \"" + escapar(medico) + "\""
                + ", \"diagnostico\": \"" + escapar(diagnostico) + "\""
                + ", \"medicamentos\": \"" + escapar(medicamentos) + "\""
                + ", \"observaciones\": \"" + escapar(observaciones) + "\""
                + ", \"procedimientos\": \"" + escapar(procedimientos) + "\""
                + ", \"lugarAtencion\": \"" + lugarAtencion + "\""
                + ", \"costo\": " + costo
                + ", \"estado\": \"" + estado + "\""
                + ", \"fechaInicio\": \"" + escapar(fechaInicio) + "\""
                + ", \"fechaFin\": \"" + escapar(fechaFin) + "\""
                + " }";
    }

    public static Consulta desdeTexto(String linea) {
        Consulta c = new Consulta();
        c.id            = Integer.parseInt(Json.valorDe(linea, "id"));
        c.idTiquete     = Integer.parseInt(Json.valorDe(linea, "idTiquete"));
        c.medico        = Json.valorDe(linea, "medico");
        c.diagnostico   = Json.valorDe(linea, "diagnostico");
        c.medicamentos  = Json.valorDe(linea, "medicamentos");
        c.observaciones = Json.valorDe(linea, "observaciones");
        c.procedimientos= Json.valorDe(linea, "procedimientos");
        c.lugarAtencion = Json.valorDe(linea, "lugarAtencion");
        c.costo         = Double.parseDouble(Json.valorDe(linea, "costo"));
        c.estado        = Json.valorDe(linea, "estado");
        c.fechaInicio   = Json.valorDe(linea, "fechaInicio");
        c.fechaFin      = Json.valorDe(linea, "fechaFin");
        return c;
    }

    private String escapar(String s) {
        if (s == null) return "";
        return s.replace("\"", "'");
    }
}