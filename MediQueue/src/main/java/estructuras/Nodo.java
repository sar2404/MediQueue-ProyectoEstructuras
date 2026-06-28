package estructuras;

// nodo generico (filosofia nodo-dato)
public class Nodo<T> {

    private T dato;
    private Nodo<T> siguiente;

    public Nodo() {

    }

    public Nodo(T dato) {

        this.dato = dato;
    }

    public T getDato() {

        return dato;
    }

    public void setDato(T dato) {

        this.dato = dato;
    }

    public Nodo<T> getSiguiente() {

        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {

        this.siguiente = siguiente;
    }
}
