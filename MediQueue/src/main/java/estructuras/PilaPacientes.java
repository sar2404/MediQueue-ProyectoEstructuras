package estructuras;

import modelos.Paciente;
import modelos.Tiquete;

public class PilaPacientes{

  
    private Nodo cima;//Arriba de la pila//Inicio//top

    public PilaPacientes() {
        this.cima=null;//Pila vacia
    }
    
    public Nodo getCima() {
        return cima;
    }

    public void setCima(Nodo cima) {
        this.cima = cima;
    }
    
    public void push(Tiquete newDato) {
        Nodo nuevoNodo = new Nodo(newDato);
        nuevoNodo.setSiguiente(cima);
        this.cima = nuevoNodo;
    }
    
    public boolean esVacia() {
        if (cima == null) {
            return true;
        } else {
            return false;
        }
    }
    
    public Tiquete pop() {
        if (esVacia()) {
            return null;
        } else {
            Tiquete auxDato = cima.getDato();
            cima = cima.getSiguiente();
            return auxDato;
        }
    }
    
    public void mostrar() {
        if (esVacia()) {
            System.out.println("Esta vacia!!");
        } else {
            Nodo actual = this.cima;
            while (actual != null) {
                //System.out.println(actual.getDato().getInfo());
                System.out.println(actual.toString());
                actual = actual.getSiguiente();
            }
        }
    }
    
    
}