package estructuras;


//Creé la clase Arbol ABB
public class ArbolABB<T extends Comparable<T>> {

    private NodoArbol<T> raiz;

    public void inserta(T dato) {
        if (raiz == null) {
            raiz = new NodoArbol<>(dato);
        } else {
            insertaRecursivo(raiz, dato);
        }

    }

    private void insertaRecursivo(NodoArbol<T> nodo, T dato) {

        if (dato.compareTo(nodo.getDato()) <= 0) {
            if (nodo.getHijoIzq() == null) {
                nodo.setHijoIzq(new NodoArbol<>(dato));
            } else {
                insertaRecursivo(nodo.getHijoIzq(), dato);
            }

        } else {

            if (nodo.getHijoDer() == null) {
                nodo.setHijoDer(new NodoArbol<>(dato));
            } else {

                insertaRecursivo(nodo.getHijoDer(), dato);
            }
        }
    }

    public void inorden() {

        inordenRecursivo(raiz);
        System.out.println();
    }

    private void inordenRecursivo(NodoArbol<T> nodo) {

        if (nodo != null) {
            inordenRecursivo(nodo.getHijoIzq());
            System.out.println(nodo.getDato());
            inordenRecursivo(nodo.getHijoDer());
        }
    }

    public T buscar(T dato) {
        return buscarRecursivo(raiz, dato);

    }

    private T buscarRecursivo(NodoArbol<T> nodo, T dato) {

        if (nodo == null) {
            return null;
        }

        int comparacion = dato.compareTo(nodo.getDato());
        if (comparacion == 0) {
            return nodo.getDato();
        }
        if (comparacion < 0) {
            return buscarRecursivo(nodo.getHijoIzq(), dato);
        }
        return buscarRecursivo(nodo.getHijoDer(), dato);

    }

    public int cuentaNodos() {
        
        return cuentaNodosRecursivo(raiz);

    }

    private int cuentaNodosRecursivo(NodoArbol<T> nodo) {

        if (nodo == null) {
            return 0;
        }
        return 1
                + cuentaNodosRecursivo(nodo.getHijoIzq())
                + cuentaNodosRecursivo(nodo.getHijoDer());
    }

}
