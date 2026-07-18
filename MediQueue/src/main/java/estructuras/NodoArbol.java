package estructuras;

//Creé la respectiva clase NodoArbol siguiendo la estrucutra vista en clase. 

public class NodoArbol<T> {

    private T dato;

    private NodoArbol<T> hijoIzq;
    private NodoArbol<T> hijoDer;

    public NodoArbol(T dato) {
        this.dato = dato;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoArbol<T> getHijoIzq() {
        return hijoIzq;
    }

    public void setHijoIzq(NodoArbol<T> hijoIzq) {
        this.hijoIzq = hijoIzq;
    }

    public NodoArbol<T> getHijoDer() {
        return hijoDer;
    }

    public void setHijoDer(NodoArbol<T> hijoDer) {
        this.hijoDer = hijoDer;
    }

}
