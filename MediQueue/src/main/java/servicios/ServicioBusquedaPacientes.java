package servicios;

import estructuras.ArbolABB;
import modelos.Paciente;

public class ServicioBusquedaPacientes {

    private ArbolABB<Paciente> arbol;

    public ServicioBusquedaPacientes() {

        arbol = new ArbolABB<>();

    }

    public void agregarPaciente(Paciente paciente) {

        arbol.inserta(paciente);

    }

    public Paciente buscarPaciente(Paciente paciente) {

        return arbol.buscar(paciente);

    }

    public void mostrarPacientes() {

        arbol.inorden();

    }

}