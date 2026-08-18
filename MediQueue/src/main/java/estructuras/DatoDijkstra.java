package estructuras;

public class DatoDijkstra<T extends Comparable<T>> {

    private T valor;
    private int distancia;
    private T anterior;
    private boolean visitado;

    public DatoDijkstra(T valor) {
        this.valor = valor;
        this.distancia = Integer.MAX_VALUE;
        this.anterior = null;
        this.visitado = false;
    }

    public T getValor() {
        return valor;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public T getAnterior() {
        return anterior;
    }

    public void setAnterior(T anterior) {
        this.anterior = anterior;
    }

    public boolean isVisitado() {
        return visitado;
    }

    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }
}
