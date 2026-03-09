# Cómputo Concurrente 2026-2 | Práctica 2: Exclusión Mutua 

Este repositorio contiene las soluciones para la **Práctica 2** de la materia Cómputo Concurrente. El objetivo es resolver problemas relacionados con condiciones de carrera y exclusión mutua mediante el uso de locks, semáforos y pools de hilos.

## Integrantes

* Flores Linares Oscar Daniel 320208591
* Márquez Corona Danna Lizette 320279991

## 📋 Prerrequisitos

* [cite_start]**Java 21 LTS** 

## 📂 Estructura de Archivos en `src/`

* **`ColaConcurrente.java` (Ejercicios 1 a 3)**: En este código implementamos una cola concurrente utilizando un `ExecutorService`. El objetivo de esta pool es crear un grupo de hilos en espera de tareas por ejecutar. Hicimos uso de referencias atómicas (`AtomicReference`) y ciclos `while(true)` con operaciones `compareAndSet` sobre los apuntadores `head` y `tail`. En el método `main`, generamos 4 hilos fijos que ejecutan tareas asíncronas de (`enq`) y (`deq`).
* **`ColaConcurrenteCandado.java` (Ejercicio 4)**:  Esta es otra versión cola concurrente. Para evitar colisiones de nombres, la clase `Nodo` la pusimos como estática interna (`private static class Nodo`). Implementamos un único candado global `ReentrantLock`. Los métodos `enq()` y `deq()` inician su ejecución adquiriendo el candado mediante `lock.lock()` y garantizan su liberación dentro de un bloque `finally` con `lock.unlock()`.  Las dos operaciones ahora son una sola sección crítica, aseguramos la exclusión mutua. Así, solo un hilo puede modificar los apuntadores a la vez.
* **`Scheduler.java` (Ejercicio 6)**: Se configura un `ExecutorService` con un 6 hilos, representando a las 6 personas que envían tareas. Para cumplir con las reglas de acceso, se inicializan :
    1. Un `Semaphore(3)`, que funciona como un candado permitiendo que un máximo de 3 hilos accedan a la sección crítica al mismo tiempo.
    2. Un `ReentrantLock` (`lock02`), diseñado específicamente para garantizar la exclusión mutua entre los hilos 0 y 2.
    El programa itera 26 veces para enviar tareas, inyectando el semáforo y el candado en el constructor de cada instancia de `Tarea`.
* **`Tarea.java` (Ejercicio 6)**: Esta clase en su método `run()`, primero calcula a qué "persona" pertenece usando `id % 6` y asigna el tiempo de ejecución que corresponde (500ns, 2000ns o 3000ns).
    1. Todos los hilos deben invocar `smphre.acquire()` para intentar entrar al servidor (limitado a 3).
    2. Si el hilo es el 0 o el 2, debe invocar también `lock02.lock()` para asegurar que el otro no esté dentro.
    3. Después de simular el tiempo de ejecución con `Thread.sleep()`, el hilo entra a un bloque `finally` donde libera los recursos al revés: primero suelta el candado `lock02` (si lo adquirió) y después invoca `smphre.release()` para permitir que otro hilo de la fila pueda ingresar al servidor.

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
