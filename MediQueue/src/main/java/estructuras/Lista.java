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

    //Agregué un método nuevo para recorres listas:
    public T buscar(T dato) {
        Nodo<T> actual = cabeza;

        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }

        return null;
    }

    //Agreué un método nuevo para poder eliminar pacientes, usuarios o salas.
    public boolean remove(T dato) {

        if (cabeza == null) {
            return false;
        }

        if (cabeza.getDato().equals(dato)) {
            cabeza = cabeza.getSiguiente();
            tamano--;
            return true;
        }

        Nodo<T> actual = cabeza;

        while (actual.getSiguiente() != null
                && !actual.getSiguiente().getDato().equals(dato)) {

            actual = actual.getSiguiente();
        }

        if (actual.getSiguiente() != null) {
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
            tamano--;
            return true;
        }

        return false;
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

    //Agregué un métodfo ToString:
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        Nodo<T> actual = cabeza;

        while (actual != null) {

            sb.append(actual.getDato());

            if (actual.getSiguiente() != null) {
                sb.append("\n");
            }

            actual = actual.getSiguiente();
        }

        return sb.toString();
    }

    public int contar() {
        return tamano;
    }
}
