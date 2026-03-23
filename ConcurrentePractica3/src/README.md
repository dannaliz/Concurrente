# Cómputo Concurrente 2026-1 | Práctica 3: Exclusión Mutua y JMM 

El objetivo es el diseño de algoritmos de exclusión mutua clásicos para 4 hilos, analizando el impacto del **Modelo de Memoria de Java (JMM)** en aspectos como el **Reordenamiento** y la **Visibilidad**.

## Integrantes

* Flores Linares Oscar Daniel 320208591
* Márquez Corona Danna Lizette 320279991

## 📋 Prerrequisitos

* **Java 21 LTS** 

## 📂 Estructura de Archivos en `src/`

* **`PetersonLock.java`**: Implementación base del algoritmo de Peterson para 2 hilos. Para evitar los problemas de reordenamiento del compilador y asegurar la visibilidad de los cambios entre hilos, utilizamos campos **`volatile`** en los arreglos de banderas y la variable (`victim`). 
* **`Peterson4Threads.java` (Ejercicio 1)**: Extensión del candado de Peterson para soportar 4 hilos mediante una estructura de árbol de torneo (Double Peterson). Implementamos la exclusión mutua en dos niveles: una fase clasificatoria para los hilos (0-1 y 2-3) y una fase final para los ganadores. Incluímos una gestión de IDs específica para evitar colisiones entre los hilos que compiten en cada nodo del árbol.
* **`PruebaContador.java` (Ejercicio 1)**: Programa de prueba que utiliza un `ExecutorService` para lanzar 400 tareas concurrentes. Cada tarea incrementa un objeto `Contador` protegido por el candado `Peterson4Threads`. El incremento lo hacemos de forma manual sin utilizar la paquetería `Atomic`, confiando en la exclusión mutua del candado.
* **`BakeryLock.java` (Ejercicio 2)**: Implementación del algoritmo Bakery para 4 hilos. Utilizamos arreglos `flag` y `label` de tamaño 4 marcados como **`volatile`** para garantizar que los tickets de turno se vean de forma consistente en la memoria principal. Se resuelve el acceso mediante una fase de "Doorway" (toma de ticket) y una fase de espera basada en los pares (etiqueta, ID).
* **`ContadorBakery.java`**: Se implementa de forma sencilla, evitando métodos como `getAndIncrement()` para forzar que la seguridad del programa dependa de la implementación del candado.
* **`ExecuteBakery.java` (Ejercicio 2)**: Programa que coordina 400 tareas mediante un pool de 4 hilos. Mete justicia que registra cuántas tareas realiza cada hilo mediante un mapa concurrente, permitiendo verificar la distribución y el cumplimiento de la propiedad FCFS.

## 🚀 Ejecución

### 1. Compilación
Asegúrate de estar en la carpeta raíz del proyecto y compila todos los archivos del paquete:
```bash
javac src/*.java
```

### 2. Ejecutar Programas
```bash
# Ejercicio 1: Peterson para 4 hilos y prueba de consistencia
java src.PruebaContador

# Ejercicio 2: Algoritmo Bakery para 4 hilos y análisis de justicia
java src.ExecuteBakery
```