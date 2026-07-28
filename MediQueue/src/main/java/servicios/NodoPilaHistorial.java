package servicios;

import estructuras.Pila;
import modelos.EntradaHistorial;

/**
 * Nodo que asocia la identificacion de un paciente con su pila de historial.
 * Permite implementar la lista de pilas sin usar colecciones de Java.
 */
public class NodoPilaHistorial {

    public String identificacion;
    public Pila<EntradaHistorial> pila;

    public NodoPilaHistorial(String identificacion, Pila<EntradaHistorial> pila) {
        this.identificacion = identificacion;
        this.pila = pila;
    }
}
