package modelos;

import persistencia.Json;

/**
 *
 * @author XPC
 */
public class Usuario {

    private String usuario;
    private String password;

    public Usuario(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }

    public Usuario() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Usuario{" + "usuario='" + usuario + '\'' + '}';
    }

    // arma el usuario como una linea json a mano
    public String aTextoJson() {

        return "{ \"usuario\": \"" + usuario + "\", \"password\": \"" + password + "\" }";
    }

    // reconstruye un usuario leyendo la linea
    public static Usuario desdeTexto(String linea) {

        return new Usuario(Json.valorDe(linea, "usuario"), Json.valorDe(linea, "password"));
    }

}
