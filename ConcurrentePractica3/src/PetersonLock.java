package src;

/**
 * Implementación del algoritmo de Peterson para 2 hilos.
 * Garantiza Exclusión Mutua, Progreso y Espera Acotada.
 */
public class PetersonLock {
    // volatile asegura visibilidad 
    // y evita el reordenamiento de instrucciones por el compilador.
    private volatile boolean[] flag = new boolean[2];
    private volatile int victim;

    public void lock(int id) {
        int i = id; // ID del hilo actual (0 o 1)
        int j = 1 - i; // ID del hilo rival
        flag[i] = true;
        victim = i;
        // Espera activa (Busy-wait): Se bloquea si el rival tiene interés
        while (flag[j] && victim == i) {
            // Espera activa
        }
    }

    public void unlock(int id) {
        // Al bajar la bandera, permitimos que el rival salga del ciclo while.
        flag[id] = false;
    }
}