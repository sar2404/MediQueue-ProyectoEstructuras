package estructuras;

/**
 * Arista del grafo ponderado dirigido.
 * Basada en la estructura utilizada en Leccion 12.
 */
public class Arista<T extends Comparable<T>> implements Comparable<Arista<T>> {

    private T destino;
    private int distancia;
    private int tiempo;
    private int costo;

    public Arista(T destino, int distancia, int tiempo, int costo) {
        this.destino = destino;
        this.distancia = distancia;
        this.tiempo = tiempo;
        this.costo = costo;
    }

    public T getDestino() {
        return destino;
    }

    public int getDistancia() {
        return distancia;
    }

    public int getTiempo() {
        return tiempo;
    }

    public int getCosto() {
        return costo;
    }

    @Override
    public int compareTo(Arista<T> otra) {
        return this.destino.compareTo(otra.destino);
    }

    @Override
    public String toString() {
        return destino
                + " (distancia: " + distancia + " km"
                + ", tiempo: " + tiempo + " min"
                + ", costo: " + costo + ")";
    }
}