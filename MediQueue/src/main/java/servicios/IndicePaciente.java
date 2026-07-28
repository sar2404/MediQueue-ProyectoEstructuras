package servicios;

import estructuras.Lista;
import modelos.Paciente;
import modelos.Tiquete;

/**
 * Dato que se guarda dentro de cada nodo del arbol ABB del Modulo 1.4.
 *
 * La clave de ordenamiento es la identificacion del paciente y, pegada a esa
 * clave, el nodo guarda tambien la lista de tiquetes de ese paciente. Gracias a
 * eso una sola busqueda en el arbol devuelve al paciente Y todas sus visitas,
 * sin tener que recorrer la lista completa de tiquetes.
 *
 * Es el mismo patron de NodoPilaHistorial (clave + estructura asociada), pero
 * aplicado al arbol en lugar de a la lista.
 */
public class IndicePaciente implements Comparable<IndicePaciente> {

    private Paciente paciente;
    private Lista<Tiquete> tiquetes;

    public IndicePaciente(Paciente paciente) {
        this.paciente = paciente;
        this.tiquetes = new Lista<>();
    }

    /**
     * Crea un objeto que solo sirve como "llave" para buscar en el arbol.
     * Nunca se inserta: solo se usa para comparar contra los nodos existentes.
     */
    public static IndicePaciente clave(String identificacion) {
        Paciente p = new Paciente();
        p.setIdentificacion(identificacion);
        return new IndicePaciente(p);
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Lista<Tiquete> getTiquetes() {
        return tiquetes;
    }

    public String getIdentificacion() {
        return paciente == null ? null : paciente.getIdentificacion();
    }

    public void agregarTiquete(Tiquete tiquete) {
        tiquetes.agregar(tiquete);
    }

    /**
     * Regla de orden del arbol: se compara la identificacion como texto.
     * Se ignoran espacios y mayusculas para que "502340118 " y "502340118"
     * caigan en el mismo nodo.
     */
    @Override
    public int compareTo(IndicePaciente otro) {
        String propia = limpiar(getIdentificacion());
        String ajena = limpiar(otro.getIdentificacion());
        return propia.compareToIgnoreCase(ajena);
    }

    private String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
    }

    @Override
    public String toString() {
        return getIdentificacion()
                + " | " + paciente.getNombre()
                + " | edad: " + paciente.getEdad()
                + " | seguro: " + paciente.getTipoSeguro()
                + " | tiquetes: " + tiquetes.getTamano();
    }
}
