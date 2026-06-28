package modelos;

/**
 *
 * @author XPC
 */
public class Paciente {

    private int id;
    private String nombre;
    private String identificacion;
    private int edad;
    private String tipoSeguro;

    public Paciente(int id, String nombre, String identificacion, int edad, String tipoSeguro) {
        this.id = id;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.edad = edad;
        this.tipoSeguro = tipoSeguro;
    }

    public Paciente() {
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

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getTipoSeguro() {
        return tipoSeguro;
    }

    public void setTipoSeguro(String tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    @Override
    public String toString() {
        return "Paciente{" + "id=" + id + ", nombre='" + nombre + '\'' + ", identificacion='" + identificacion + '\'' + ", edad=" + edad + ", tipoSeguro='" + tipoSeguro + '\'' + '}';
    }

}
