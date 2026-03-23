# Cómputo Concurrente 2026-1 | Práctica 1

Este repositorio contiene las soluciones para la **Práctica 1** de la materia Cómputo Concurrente. 

## Integrantes

* Flores Linares Oscar Daniel 320208591
* Márquez Corona Danna Lizette 320279991

## 📋 Prerrequisitos

* **Java 21 LTS** 

## 📂 Estructura de Archivos en `src/`

* **`DeterminanteConcurrenteSecuencial.java`**: En este código todo el trabajo lo realiza el hilo principal (main) de principio a fin.
El método determinanteMatriz3x3 divide el cálculo del determinante en dos grupos: las diagonales positivas y las negativas. Calcula las 6 multiplicaciones por separado y los guarda en variables locales (p1, p2, p3, s1, s2, s3), que se situan en la stack del hilo main.
por último regresa la suma p1 + p2 + p3 - s1 - s2 - s3. En el main medimos el tiempo con System.nanoTime() antes y después de llamar al método. Este programa no tiene overhead de gestión de concurrencia, lo que hace que sea el más rápido porque no paraleliza nada.

* **`DeterminanteConcurrenteRuntable.java`**: Este código representa una tarea ejecutable pero no es un hilo por sí mismo. Aquí hay una diferencia importante con extender thread:

El objeto Runnable encapsula la tarea que se tiene que hacer y guarda el resultado parcial.

El objeto Thread es el hilo real del sistema operativo que ejecuta esa tarea.

Creamos 6 pares Runnable+Thread, para cada Runnable en el constructor tiene los 3 valores que debe multiplicar (num1, num2, num3), y cuando su hilo ejecuta run(), hace partial = num1 * num2 * num3, una sola multiplicación.
Los 6 hilos empiezan con start() y corren concurrentemente. Luego el hilo principal llama join() sobre cada uno. Si no se hiciera, el hilo principal podría leer los partial antes de que los hilos los hayan escrito. Cuando todos terminaron se suman y restan los 6 resultados parciales.

* **`DeterminanteConcurrenteDosHilos.java`**: Este código extiende la clase Thread por lo que cada instancia es un hilo. 
Aquí en lugar de asignar una multiplicación por hilo como en la versión de 6, cada hilo se encarga de la mitad completa del cálculo:

thrPos recibe esPositivo = true y en run() calculamos la suma de las 3 diagonales positivas, haciendo 3 multiplicaciones y 2 sumas.
thrNeg recibe esPositivo = false y calculamos la suma de las 3 diagonales negativas de la misma manera.

Los 2 hilos entran a la misma referencia de la matriz que está en el heap y al solo leerlas no hay condiciones de carrera ni se necesita más sincronización. Cada hilo guarda su resultado en su propio parcial.
El hilo principal espera a ambos con join() y después hace la resta final thrPos.parcial - thrNeg.parcial para obtener el determinante.

## 🚀 Ejecución

### 1. Compilación
```bash
javac src/*.java
```
```bash
# Versión Secuencial (Sin hilos)
java src.DeterminanteConcurrenteSecuencial

# Versión Multihilo (Runnable - 6 hilos)
java src.DeterminanteConcurrenteRuntable

# Versión Optimizada (Thread - 2 hilos)
java src.DeterminanteConcurrenteDosHilos
```
