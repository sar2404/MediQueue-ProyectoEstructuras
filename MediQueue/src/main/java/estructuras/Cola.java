package estructuras;

// cola fifo generica
public class Cola<T> {

    private Nodo<T> frente;
    private Nodo<T> fin;

    public Cola() {

        this.frente = null;
        this.fin = null;
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

    public Nodo<T> getFrente() {

        return frente;
    }
}
