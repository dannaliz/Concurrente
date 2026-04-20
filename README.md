# Cómputo Concurrente 2026-2 | Práctica 5

## Integrantes

* Flores Linares Oscar Daniel 320208591
* Márquez Corona Danna Lizette 320279991

## 📋 Prerrequisitos

* **Java 21 LTS** ## 📂 Estructura de Archivos en `src/`

* **`ShavitTreeCounter.java`**: Este código implementa un contador que se basa en un árbol binario. Para evitar cuellos de botella masivos, el método `increment()` distribuye el tráfico de los hilos concurrentes con nodos internos hasta llegar a incrementar atómicamente una de las hojas. 

La modificación principal sucede en el método `fetch()`, que implementa la técnica de **Double-Collect**. En lugar de usar candados (`locks`) que detendrían a los escritores, el lector realiza un recorrido en profundidad (DFS) para tomar dos "fotos" consecutivas del estado de las hojas. Si las dos fotos son iguales, se garantiza que ninguna escritura interfirió durante la recolección.

## 🚀 Ejecución

### 1. Compilación
Asegúrate de estar posicionado en el directorio raíz del proyecto (`practica5`) y ejecuta:
```bash
javac src/*.java
```

### 2. Ejecución de la Prueba
Para ejecutar el método main incluido en el contador del árbol, que lanza 8 hilos realizando 1000 incrementos cada uno y verifica la consistencia del resultado final coloca lo siguiente en el directorio raíz del proyecto (`practica5`)

```Bash
java src.ShavitTreeCounter
```