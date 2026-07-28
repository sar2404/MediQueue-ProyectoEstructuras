package estructuras;

/**
 * Cola de prioridad generica construida con nodos propios.
 * Un numero de prioridad menor se atiende primero. Cuando dos elementos tienen
 * la misma prioridad, se conserva el orden de llegada (FIFO).
 */
public class ColaPrioridad<T> {

    private Nodo<ElementoPrioridad<T>> frente;
    private int tamano;

    public ColaPrioridad() {
        frente = null;
        tamano = 0;
    }

    public void encolar(T dato, int prioridad) {
        ElementoPrioridad<T> elemento = new ElementoPrioridad<>(dato, prioridad);
        Nodo<ElementoPrioridad<T>> nuevo = new Nodo<>(elemento);

        if (frente == null || prioridad < frente.getDato().getPrioridad()) {
            nuevo.setSiguiente(frente);
            frente = nuevo;
        } else {
            Nodo<ElementoPrioridad<T>> actual = frente;
            while (actual.getSiguiente() != null
                    && actual.getSiguiente().getDato().getPrioridad() <= prioridad) {
                actual = actual.getSiguiente();
            }
            nuevo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevo);
        }
        tamano++;
    }

    public T desencolar() {
        if (frente == null) {
            return null;
        }
        T dato = frente.getDato().getDato();
        frente = frente.getSiguiente();
        tamano--;
        return dato;
    }

    public T frente() {
        return frente == null ? null : frente.getDato().getDato();
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamano() {
        return tamano;
    }

    public Nodo<ElementoPrioridad<T>> getFrente() {
        return frente;
    }
}
