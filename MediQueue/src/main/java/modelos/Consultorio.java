package modelos;

import persistencia.Json;

// consultorio. tipo: NORMAL o PREFERENCIAL
public class Consultorio {

    private int numero;
    private String tipo;
    private String especialidad;

    public Consultorio(int numero, String tipo, String especialidad) {

        this.numero = numero;
        this.tipo = tipo;
        this.especialidad = especialidad;
    }

    public Consultorio() {

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

    public String getEspecialidad() {

        return especialidad;
    }

    public void setEspecialidad(String especialidad) {

        this.especialidad = especialidad;
    }

    @Override
    public String toString() {

        return "Consultorio " + numero + " (" + tipo + ", " + especialidad + ")";
    }

    // arma el consultorio como una linea json a mano
    public String aTextoJson() {

        return "{ \"numero\": " + numero + ", \"tipo\": \"" + tipo + "\", \"especialidad\": \"" + especialidad + "\" }";
    }

    // reconstruye un consultorio leyendo la linea
    public static Consultorio desdeTexto(String linea) {

        int numero = Integer.parseInt(Json.valorDe(linea, "numero"));
        String tipo = Json.valorDe(linea, "tipo");
        String especialidad = Json.valorDe(linea, "especialidad");
        return new Consultorio(numero, tipo, especialidad);
    }
}
