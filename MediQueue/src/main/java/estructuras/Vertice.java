package estructuras;

public class Vertice<T extends Comparable<T>, A extends Comparable<A>>
        implements Comparable<Vertice<T, A>> {

    private T valor;
    private Lista<A> adyacentes;

    public Vertice(T valor) {
        this.valor = valor;
        this.adyacentes = new Lista<>();
    }

    public T getValor() {
        return valor;
    }

    public Lista<A> getAdyacentes() {
        return adyacentes;
    }

    @Override
    public int compareTo(Vertice<T, A> otro) {
        return this.valor.compareTo(otro.valor);
    }

    @Override
    public String toString() {
        return valor + " => " + adyacentes;
    }
}
