package estructuras;

/**
 * Elemento de una ColaPrioridad: envuelve un dato con su numero de prioridad.
 * Un numero de prioridad menor indica mayor urgencia.
 */
public class ElementoPrioridad<E> {

    private final E dato;
    private final int prioridad;

    public ElementoPrioridad(E dato, int prioridad) {
        this.dato = dato;
        this.prioridad = prioridad;
    }

    public E getDato() {
        return dato;
    }

    public int getPrioridad() {
        return prioridad;
    }
}
