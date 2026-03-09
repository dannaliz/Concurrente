# Cómputo Concurrente 2026-2 | [cite_start]Práctica 2: Exclusión Mutua [cite: 1, 2, 3]

Este repositorio contiene las soluciones para la **Práctica 2** de la materia Cómputo Concurrente. El objetivo es resolver problemas relacionados con condiciones de carrera y exclusión mutua mediante el uso de locks, semáforos y pools de hilos.

## Integrantes

* Flores Linares Oscar Daniel 320208591
* Márquez Corona Danna Lizette 320279991

## 📋 Prerrequisitos

* [cite_start]**Java 21 LTS** 

## 📂 Estructura de Archivos en `src/`

* **`ColaConcurrente.java` (Ejercicios 1 a 3)**: Esta clase implementa una cola utilizando operaciones atómicas pero de una manera que la hace susceptible a condiciones de carrera si no se gestiona correctamente. Utiliza una pool de hilos (`ExecutorService`) para encolar y desencolar elementos de forma concurrente, lo que permite observar el comportamiento de `race conditions` y `data races` en una ejecución no sincronizada.
* **`ColaConcurrenteCandado.java` (Ejercicio 4)**:  Esta es la versión corregida de la cola concurrente. Se modificó el código para encapsular la clase `Nodo` como estática interna. En lugar de operaciones atómicas complejas, utiliza un solo candado `ReentrantLock`. Los métodos `enq()` y `deq()` inician adquiriendo el candado (`lock.lock()`) y lo liberan en un bloque `finally` (`lock.unlock()`). Esto asegura que toda la operación sea una sola sección crítica, garantizando la consistencia de los datos y eliminando las carreras por los datos.
* **`Scheduler.java` (Ejercicio 6)**: Define el entorno principal del problema de acceso al servidor. [cite_start]Configura un `ExecutorService` con un *pool* de 6 hilos [cite: 151] y envía 26 tareas para su ejecución. [cite_start]Crea instancias estáticas compartidas de un `Semaphore(3)` [cite: 154] y un `ReentrantLock`, pasándolas como parámetros en el constructor de cada instancia de `Tarea`.
* **`Tarea.java` (Ejercicio 6)**: Implementa la interfaz `Runnable` que es ejecutada por el *pool* de hilos. [cite_start]Asigna tiempos de ejecución simulados (500ns, 2000ns, 3000ns) dependiendo del identificador del hilo (del 0 al 5)[cite: 152]. [cite_start]La ejecución sigue un orden estricto de adquisición: primero solicita el permiso del semáforo (máximo 3 hilos a la vez) [cite: 154][cite_start], y luego, si el hilo es el 0 o el 2, solicita un candado para evitar que entren al mismo tiempo[cite: 155]. Los recursos se liberan en el bloque `finally` en orden inverso para evitar inanición o bloqueos.

## 🚀 Ejecución

### 1. Compilación
```bash
javac practica02/src/*.java
```

### 2. Ejecutar Programas
```bash
# Versión de la Cola con posibles inconsistencias (Ej 1-3)
java practica02.src.ColaConcurrente

# Versión Segura de la Cola con Candados (Ej 4)
java practica02.src.ColaConcurrenteCandado

# Sistema de Acceso a Servidor (Ej 6)
java practica02.src.Scheduler
```