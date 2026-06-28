package estructuras;

import modelos.Tiquete;

public class Cola {

    private Nodo frente;
    private Nodo fin;

    public Cola() {
        this.frente = null;
        this.fin = null;
    }

    public Cola(Nodo frente, Nodo fin) {
        this.frente = frente;
        this.fin = fin;
    }

    // Operación para insertar un elemento (Enqueue)
    public void encolar(Tiquete dato) {
        Nodo nuevo = new Nodo(dato); // Usa el constructor genérico
        if (fin != null) {
            fin.setSiguiente(nuevo);
        }
        fin = nuevo;
        if (frente == null) {
            frente = nuevo;
        }
    }

    // Operación para eliminar un elemento (Dequeue)
    public Tiquete desencolar() throws Exception {
        if (frente == null) {
            throw new Exception("La cola está vacía");
        }
        Tiquete dato = frente.getDato();
        frente = frente.getSiguiente();
        if (frente == null) {
            fin = null;
        }
        return dato;
    }

    // Operación para ver el elemento al frente
    public Tiquete frente() throws Exception {
        if (frente == null) {
            throw new Exception("La cola está vacía");
        }
        return frente.getDato();
    }

    public boolean estaVacia() {
        return frente == null;
    }

    @Override
    public String toString() {
        if (estaVacia()) {
            return "Cola vacía";
        }

        StringBuilder sb = new StringBuilder();
        Nodo actual = frente;
        while (actual != null) {
            sb.append(actual.getDato());
            actual = actual.getSiguiente();
            if (actual != null) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }
    
    public boolean encuentra(Tiquete newDato){
        Nodo actual = this.frente;
        while (actual != null) {//mientras no llegue al final
            //if(actual.equals(new Nodo<T>(newDato))){//comparacion de nodos
            if(actual.getDato().equals(newDato)){//Comparamos directamente el dato
             return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }
}
