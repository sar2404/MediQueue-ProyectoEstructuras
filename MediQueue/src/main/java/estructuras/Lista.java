package estructuras;

// lista enlazada simple generica (base de las demas estructuras)
public class Lista<T> {

    private Nodo<T> cabeza;
    private int tamano;

    public Lista() {

        this.cabeza = null;
        this.tamano = 0;
    }

    public void agregar(T dato) {

        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        tamano++;
    }

    public T obtener(int indice) {

        if (indice < 0 || indice >= tamano) {
            return null;
        }
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    public int getTamano() {

        return tamano;
    }

    public boolean estaVacia() {

        return cabeza == null;
    }

    public Nodo<T> getCabeza() {

        return cabeza;
    }
}
