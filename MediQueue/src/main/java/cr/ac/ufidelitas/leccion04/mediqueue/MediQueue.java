package cr.ac.ufidelitas.leccion04.mediqueue;

import estructuras.Cola;
import estructuras.PilaPacientes;
import modelos.Paciente;
import modelos.Tiquete;

public class MediQueue {

    public static void main(String[] args) {

        //Creacion de pacientes
        Paciente paciente1 = new Paciente(1, "Fabian Sandoval", "101110111", 24, "CCSS");
        Paciente paciente2 = new Paciente(2, "Santiago Rodriguez", "202220222", 25, "INS");
        Paciente paciente3 = new Paciente(3, "Ignacio Calero", "303330333", 22, "CCSS");
        Paciente paciente4 = new Paciente(4, "Giovanni Jurguens", "404440444", 26, "Privado");

        //Creacion de tiquetes
        Tiquete t1 = new Tiquete(1001, paciente1, "2026-06-23 08:00", "-1", "REGULAR", "C", "N");
        Tiquete t2 = new Tiquete(1002, paciente2, "2026-06-23 08:10", "-1", "URGENTE", "E", "P");
        Tiquete t3 = new Tiquete(1003, paciente3, "2026-06-23 08:20", "-1", "CONTROL", "C", "N");
        Tiquete t4 = new Tiquete(1004, paciente4, "2026-06-23 08:20", "-1", "CRITICO", "E", "P");

        //Cola de pacientes
        Cola colaPacientes = new Cola();
        colaPacientes.encolar(t1);
        colaPacientes.encolar(t2);
        colaPacientes.encolar(t3);
        colaPacientes.encolar(t4);
        System.out.println("Cola de Pacientes: " + colaPacientes);

        //Pila
        PilaPacientes historial = new PilaPacientes();
        historial.push(t1);
        historial.push(t2);
        historial.push(t3);
        historial.push(t4);
        System.out.println("Historial de Pacientes: ");
        historial.mostrar();
    }
}
