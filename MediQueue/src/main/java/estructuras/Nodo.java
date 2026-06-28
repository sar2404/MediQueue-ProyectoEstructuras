package estructuras;

import modelos.Tiquete;

public class Nodo{

    private Tiquete  dato;
    private Nodo siguiente;

    public Nodo() {
        this.siguiente = null;
        this.dato = null;
    }
    public Nodo(Tiquete newDato) {
        this.dato = newDato;
    }

    public Tiquete getDato() {
        return dato;
    }

    public void setDato(Tiquete dato) {
        this.dato = dato;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
    
    @Override
    public String toString() {
        return "Nodo:{" + "dato:" + dato.toString() +"}";
    }
}