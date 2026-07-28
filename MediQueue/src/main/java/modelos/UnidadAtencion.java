package modelos;

import persistencia.Json;

/**
 * Informacion operativa de una sala o consultorio.
 * La fila no se guarda dentro del modelo: ServicioSalasConsultorios mantiene
 * una Lista de Colas paralela a la lista de unidades.
 */
public class UnidadAtencion {

    private String codigo;
    private String categoria;
    private int numero;
    private String tipo;
    private int capacidadFila;
    private String medico;
    private String enfermera;
    private String especialidad;

    public UnidadAtencion(String codigo, String categoria, int numero, String tipo,
                          int capacidadFila, String medico, String enfermera,
                          String especialidad) {
        this.codigo = codigo;
        this.categoria = categoria;
        this.numero = numero;
        this.tipo = tipo;
        this.capacidadFila = capacidadFila;
        this.medico = medico;
        this.enfermera = enfermera;
        this.especialidad = especialidad;
    }

    public UnidadAtencion() {
    }

    public static UnidadAtencion desdeSala(Sala sala) {
        String especialidad = "CRITICA".equalsIgnoreCase(sala.getTipo())
                || "EMERGENCIA".equalsIgnoreCase(sala.getTipo())
                ? "Emergencias" : "Medicina general";
        return new UnidadAtencion(
                "S-" + sala.getNumero(),
                "SALA",
                sala.getNumero(),
                sala.getTipo(),
                sala.getCapacidad(),
                "SIN ASIGNAR",
                "SIN ASIGNAR",
                especialidad
        );
    }

    public static UnidadAtencion desdeConsultorio(Consultorio consultorio) {
        return new UnidadAtencion(
                "C-" + consultorio.getNumero(),
                "CONSULTORIO",
                consultorio.getNumero(),
                consultorio.getTipo(),
                10,
                "SIN ASIGNAR",
                "SIN ASIGNAR",
                consultorio.getEspecialidad()
        );
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCapacidadFila() {
        return capacidadFila;
    }

    public void setCapacidadFila(int capacidadFila) {
        this.capacidadFila = capacidadFila;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getEnfermera() {
        return enfermera;
    }

    public void setEnfermera(String enfermera) {
        this.enfermera = enfermera;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public boolean esSala() {
        return "SALA".equalsIgnoreCase(categoria);
    }

    public boolean esConsultorio() {
        return "CONSULTORIO".equalsIgnoreCase(categoria);
    }

    public boolean esPreferencial() {
        return "PREFERENCIAL".equalsIgnoreCase(tipo);
    }

    @Override
    public String toString() {
        return codigo + categoria + " " + tipo
                + "  especialidad: " + especialidad
                + "  medico: " + medico
                + "  enfermera: " + enfermera;
    }

    public String aTextoJson() {
        return "{ \"codigo\": \"" + escapar(codigo) + "\""
                + ", \"categoria\": \"" + escapar(categoria) + "\""
                + ", \"numero\": " + numero
                + ", \"tipo\": \"" + escapar(tipo) + "\""
                + ", \"capacidadFila\": " + capacidadFila
                + ", \"medico\": \"" + escapar(medico) + "\""
                + ", \"enfermera\": \"" + escapar(enfermera) + "\""
                + ", \"especialidad\": \"" + escapar(especialidad) + "\" }";
    }

    public static UnidadAtencion desdeTexto(String linea) {
        return new UnidadAtencion(
                Json.valorDe(linea, "codigo"),
                Json.valorDe(linea, "categoria"),
                Integer.parseInt(Json.valorDe(linea, "numero")),
                Json.valorDe(linea, "tipo"),
                Integer.parseInt(Json.valorDe(linea, "capacidadFila")),
                Json.valorDe(linea, "medico"),
                Json.valorDe(linea, "enfermera"),
                Json.valorDe(linea, "especialidad")
        );
    }

    private String escapar(String texto) {
        return texto == null ? "" : texto.replace("\"", "'");
    }
}
