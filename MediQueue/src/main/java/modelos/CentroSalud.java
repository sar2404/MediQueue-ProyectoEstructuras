package modelos;

public class CentroSalud implements Comparable<CentroSalud> {

    private int id;
    private String nombre;
    private String tipo;

    public CentroSalud(int id, String nombre, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public CentroSalud() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int compareTo(CentroSalud otro) {
        return Integer.compare(this.id, otro.id);
    }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ", id: " + id + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CentroSalud) {
            return this.id == ((CentroSalud) obj).id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
