# Cómputo Concurrente 2026-2 | Práctica 4

Este repositorio contiene las soluciones para la **Práctica 4** de la materia Cómputo Concurrente. 

## Integrantes

* Flores Linares Oscar Daniel 320208591
* Márquez Corona Danna Lizette 320279991

## 📋 Prerrequisitos

* **Java 21 LTS** 

## 📂 Estructura de Archivos en `src/`

* **`RunSpin.java`**: Este archivo fue modificado para resolver el Ejercicio 2 de la práctica. En lugar de incrementar un contador simple, la función `task(lock)` fue cambiada para que los hilos trabajen sobre una matriz compartida de tamaño 10x10. Se implementaron dos funciones separadas: una para asignar valores (donde se simula el trabajo sobre las casillas) y otra para imprimir la matriz la en consola. Las 2 funciones son llamadas dentro de la sección crítica protegida por el candado (`lock.lock()` y `lock.unlock()`) para garantizar la exclusión mutua. Para evaluar los distintos algoritmos en las métricas, se debe comentar y descomentar la instancia del candado correspondiente en el método `main`.

* **Algoritmos de Sincronización** (`TASLock.java`, `TTASLock.java`, `Backoff.java`, `BackoffLock.java`, `ALock.java`, `MCSLock.java`, `CLHLock.java`): Todos estos archivos que contienen las implementaciones de los Spinlocks y la clase Backoff se tomaron del repositorio de la profesora en: https://github.com/gilde-valeria/FC_CConcurrente.

## 🚀 Ejecución

### 1. Compilación
Asegúrate de estar en el directorio raíz de la práctica (un nivel arriba de `src/`) y compila todos los archivos del paquete:
```bash
javac src/*.java
```

### 2. Ejecución de la Prueba
Para ejecutar el programa y observar el trabajo de los hilos sobre la matriz compartida:

```bash
java src.RunSpin
```