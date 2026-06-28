package estructuras;

// pila lifo generica
public class Pila<T> {

    private Nodo<T> cima;

    public Pila() {

        this.cima = null;
    }

    public void apilar(T dato) {

        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(cima);
        cima = nuevo;
    }

    public T desapilar() {

        if (cima == null) {
            return null;
        }
        T dato = cima.getDato();
        cima = cima.getSiguiente();
        return dato;
    }

    public boolean esVacia() {

        return cima == null;
    }

    public Nodo<T> getCima() {

        return cima;
    }
}
