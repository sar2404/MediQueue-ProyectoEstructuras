package modelos;

import persistencia.Json;

// sala. tipo: NORMAL, PREFERENCIAL, EMERGENCIA o CRITICA
public class Sala {

    private int numero;
    private String tipo;
    private int capacidad;

    public Sala(int numero, String tipo, int capacidad) {

        this.numero = numero;
        this.tipo = tipo;
        this.capacidad = capacidad;
    }

    public Sala() {

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

    public int getCapacidad() {

        return capacidad;
    }

    public void setCapacidad(int capacidad) {

        this.capacidad = capacidad;
    }

    @Override
    public String toString() {

        return "Sala " + numero + " (" + tipo + ", cap. " + capacidad + ")";
    }

    // arma la sala como una linea json a mano
    public String aTextoJson() {

        return "{ \"numero\": " + numero + ", \"tipo\": \"" + tipo + "\", \"capacidad\": " + capacidad + " }";
    }

    // reconstruye una sala leyendo la linea
    public static Sala desdeTexto(String linea) {

        int numero = Integer.parseInt(Json.valorDe(linea, "numero"));
        String tipo = Json.valorDe(linea, "tipo");

        int capacidad = Integer.parseInt(Json.valorDe(linea, "capacidad"));
        return new Sala(numero, tipo, capacidad);
    }
}
