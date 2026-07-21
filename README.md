# MediQueue - Proyecto Estructuras de Datos (SC-304)

Sistema de gestion hospitalaria (pacientes, atencion y emergencias) para multiples
sedes. Proyecto Java Maven con estructuras de datos dinamicas Nodo-Dato hechas a mano.

## Como correr

- Requiere Java (17+) y Maven.
- Compilar:  `mvn compile`
- Ejecutar la clase principal: `cr.ac.ufidelitas.leccion04.mediqueue.MediQueue`
- En la primera ejecucion se crea `config.json` con los datos de la sede.
  Si se borra ese archivo, el sistema vuelve a pedir la configuracion.

## Credenciales (login)

| Usuario   | Contrasena |
|-----------|------------|
| santiago  | 123        |
| admin     | 123        |

Los usuarios se guardan en `config.json`, no quedan quemados en el codigo.

## Estado actual

- Modulo 1.0 (Configuracion): sede, salas, consultorios y usuarios en `config.json`.
- Login y menu principal.
- Estructuras genericas propias: Nodo, Lista, Cola, Pila.
- Modulo 1.2 (Modelos): Se crearon los modelos necesarios para poder crear ServicioAtencion
- Modulo 1.2 (Servicios) completado

Pendiente: Modulos 1.1 a 1.5 (registro, atencion, salas/consultorios, arboles, grafos).
