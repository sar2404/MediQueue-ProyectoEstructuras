package estructuras;

// cola FIFO generica construida con nodos propios
public class Cola<T> {

    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamano;

    public Cola() {
        this.frente = null;
        this.fin = null;
        this.tamano = 0;
    }

    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (fin != null) {
            fin.setSiguiente(nuevo);
        }
        fin = nuevo;
        if (frente == null) {
            frente = nuevo;
        }
        tamano++;
    }

    public T desencolar() throws Exception {
        if (frente == null) {
            throw new Exception("la cola esta vacia");
        }
        T dato = frente.getDato();
        frente = frente.getSiguiente();
        if (frente == null) {
            fin = null;
        }
        tamano--;
        return dato;
    }

    public T frente() throws Exception {
        if (frente == null) {
            throw new Exception("la cola esta vacia");
        }
        return frente.getDato();
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamano() {
        return tamano;
    }

    public Nodo<T> getFrente() {
        return frente;
    }
}
