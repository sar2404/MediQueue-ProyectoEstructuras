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

    public boolean estaVacio() {

        return raiz == null;
    }

    // Cantidad de niveles del arbol. Sirve para explicar cuantas comparaciones
    // como maximo hace una busqueda.
    public int altura() {

        return alturaRecursiva(raiz);
    }

    private int alturaRecursiva(NodoArbol<T> nodo) {

        if (nodo == null) {
            return 0;
        }
        int izq = alturaRecursiva(nodo.getHijoIzq());
        int der = alturaRecursiva(nodo.getHijoDer());
        return 1 + (izq > der ? izq : der);
    }

    // Mismo recorrido de inorden(), pero en vez de imprimir devuelve los datos
    // ordenados en una Lista para que quien llame decida el formato de salida.
    public Lista<T> recorrerInorden() {

        Lista<T> resultado = new Lista<>();
        recorrerInordenRecursivo(raiz, resultado);
        return resultado;
    }

    private void recorrerInordenRecursivo(NodoArbol<T> nodo, Lista<T> resultado) {

        if (nodo != null) {
            recorrerInordenRecursivo(nodo.getHijoIzq(), resultado);
            resultado.agregar(nodo.getDato());
            recorrerInordenRecursivo(nodo.getHijoDer(), resultado);
        }
    }

    // Dibuja el arbol con sangria por nivel (recorrido preorden). Es solo para
    // revisar visualmente la forma que quedo el indice.
    public void imprimirEstructura() {

        imprimirEstructuraRecursivo(raiz, 0, "raiz");
    }

    private void imprimirEstructuraRecursivo(NodoArbol<T> nodo, int nivel, String lado) {

        if (nodo == null) {
            return;
        }

        String sangria = "";
        for (int i = 0; i < nivel; i++) {
            sangria = sangria + "    ";
        }
        System.out.println(sangria + "(" + lado + ") " + nodo.getDato());

        imprimirEstructuraRecursivo(nodo.getHijoIzq(), nivel + 1, "izq");
        imprimirEstructuraRecursivo(nodo.getHijoDer(), nivel + 1, "der");
    }

}
