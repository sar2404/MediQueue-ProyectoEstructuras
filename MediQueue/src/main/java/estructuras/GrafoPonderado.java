package estructuras;

public class GrafoPonderado<T extends Comparable<T>> {

    private Lista<Vertice<T, Arista<T>>> vertices;

    public GrafoPonderado() {
        vertices = new Lista<>();
    }

    public void agregarVertice(T dato) {
        if (buscarVertice(dato) == null) {
            vertices.agregar(new Vertice<>(dato));
        }
    }

    public void agregarArista(T origen, T destino, int distancia, int tiempo, int costo) {
        Vertice<T, Arista<T>> vOrigen = buscarVertice(origen);
        Vertice<T, Arista<T>> vDestino = buscarVertice(destino);

        if (vOrigen != null && vDestino != null) {
            Arista<T> nueva = new Arista<>(destino, distancia, tiempo, costo);

            if (!existeArista(vOrigen, destino)) {
                vOrigen.getAdyacentes().agregar(nueva);
            }
        }
    }

    private Vertice<T, Arista<T>> buscarVertice(T dato) {
        Nodo<Vertice<T, Arista<T>>> actual = vertices.getCabeza();

        while (actual != null) {
            if (actual.getDato().getValor().equals(dato)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    private boolean existeArista(Vertice<T, Arista<T>> vertice, T destino) {
        Nodo<Arista<T>> actual = vertice.getAdyacentes().getCabeza();

        while (actual != null) {
            if (actual.getDato().getDestino().equals(destino)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public void imprimir() {
        Nodo<Vertice<T, Arista<T>>> actual = vertices.getCabeza();

        while (actual != null) {
            System.out.println(actual.getDato());
            actual = actual.getSiguiente();
        }
    }

    public Lista<Vertice<T, Arista<T>>> getVertices() {
        return vertices;
    }

    public Vertice<T, Arista<T>> obtenerVertice(T dato) {
        return buscarVertice(dato);
    }
    public void dijkstra(T origen, T destino) {
    Lista<DatoDijkstra<T>> datos = new Lista<>();

    Nodo<Vertice<T, Arista<T>>> actualVertice = vertices.getCabeza();
    while (actualVertice != null) {
        datos.agregar(new DatoDijkstra<>(actualVertice.getDato().getValor()));
        actualVertice = actualVertice.getSiguiente();
    }

    DatoDijkstra<T> datoOrigen = buscarDato(datos, origen);
    if (datoOrigen == null) {
        System.out.println("el origen no existe.");
        return;
    }

    DatoDijkstra<T> datoDestino = buscarDato(datos, destino);
    if (datoDestino == null) {
        System.out.println("el destino no existe.");
        return;
    }

    datoOrigen.setDistancia(0);

    while (true) {
        DatoDijkstra<T> menor = buscarMenorNoVisitado(datos);

        if (menor == null || menor.getDistancia() == Integer.MAX_VALUE) {
            break;
        }

        menor.setVisitado(true);

        if (menor.getValor().equals(destino)) {
            break;
        }

        Vertice<T, Arista<T>> verticeActual = buscarVertice(menor.getValor());

        Nodo<Arista<T>> aristaActual = verticeActual.getAdyacentes().getCabeza();

        while (aristaActual != null) {
            Arista<T> arista = aristaActual.getDato();
            DatoDijkstra<T> vecino = buscarDato(datos, arista.getDestino());

            if (vecino != null && !vecino.isVisitado()) {
                int nuevaDistancia = menor.getDistancia() + arista.getDistancia();

                if (nuevaDistancia < vecino.getDistancia()) {
                    vecino.setDistancia(nuevaDistancia);
                    vecino.setAnterior(menor.getValor());
                }
            }

            aristaActual = aristaActual.getSiguiente();
        }
    }

    if (datoDestino.getDistancia() == Integer.MAX_VALUE) {
        System.out.println("no existe una ruta entre las sedes indicadas.");
        return;
    }

    System.out.println("distancia minima: " + datoDestino.getDistancia() + " km");
    System.out.print("ruta: ");
    imprimirRuta(datos, destino);
    System.out.println();
}

private DatoDijkstra<T> buscarDato(Lista<DatoDijkstra<T>> datos, T valor) {
    Nodo<DatoDijkstra<T>> actual = datos.getCabeza();

    while (actual != null) {
        if (actual.getDato().getValor().equals(valor)) {
            return actual.getDato();
        }
        actual = actual.getSiguiente();
    }

    return null;
}

private DatoDijkstra<T> buscarMenorNoVisitado(Lista<DatoDijkstra<T>> datos) {
    Nodo<DatoDijkstra<T>> actual = datos.getCabeza();
    DatoDijkstra<T> menor = null;

    while (actual != null) {
        DatoDijkstra<T> dato = actual.getDato();

        if (!dato.isVisitado()) {
            if (menor == null || dato.getDistancia() < menor.getDistancia()) {
                menor = dato;
            }
        }

        actual = actual.getSiguiente();
    }

    return menor;
}

private void imprimirRuta(Lista<DatoDijkstra<T>> datos, T destino) {
    DatoDijkstra<T> dato = buscarDato(datos, destino);

    if (dato == null) {
        return;
    }

    if (dato.getAnterior() != null) {
        imprimirRuta(datos, dato.getAnterior());
        System.out.print(" -> ");
    }

    System.out.print(dato.getValor());
}

}
