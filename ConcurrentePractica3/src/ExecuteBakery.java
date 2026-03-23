package src;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Algoritmo de Lamport (Bakery) para N hilos.
 * Implementa Justicia (FCFS) mediante un sistema de tickets. 
 */
class BakeryLock {
    private static final int N = 4;
    // Instanciación directa de los arreglos
    private volatile boolean[] flag = new boolean[N];
    private volatile int[] label = new int[N];

    public void lock(int i) {
        flag[i] = true; // Indica interés en tomar un turno
        label[i] = max() + 1; // Toma el "siguiente ticket" disponible

        for (int k = 0; k < N; k++) {
            // Se espera si el hilo 'k' tiene interés Y tiene prioridad.
            // La prioridad se define por el par (label, id).
            while (k != i && flag[k] && 
                  (label[k] < label[i] || (label[k] == label[i] && k < i))) {
                // Espera activa hasta ser el turno del hilo actual
            }
        }
    }

    public void unlock(int i) {
        flag[i] = false; // Baja la bandera para ceder el turno al siguiente
    }

    private int max() {
        int max = 0;
        for (int i = 0; i < N; i++) {
            if (label[i] > max) max = label[i];
        }
        return max;
    }
}

class ContadorBakery {
    // No usamos atomic, solo volatile 
    volatile int value = 0;
    public void increment() { value++; }
    public int get() { return value; }
}

public class ExecuteBakery {
    // Generador de IDs del 0 al 3 exclusivos para los hilos del pool
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private static final ThreadLocal<Integer> threadId = 
            ThreadLocal.withInitial(idCounter::getAndIncrement);

    public static void main(String[] args) throws InterruptedException {
        BakeryLock lock = new BakeryLock();
        ContadorBakery contador = new ContadorBakery();
        Map<Integer, Integer> conteo = new ConcurrentHashMap<>();
        
        // Pool de 4 hilos representando el hardware disponible.
        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 400; i++) {
            executor.execute(() -> {
                int id = threadId.get(); 
                lock.lock(id); // Entrada a Sección Crítica
                try {
                    contador.increment();
                    conteo.merge(id, 1, Integer::sum); // Registro de justicia
                } finally {
                    lock.unlock(id); // Salida de Sección Crítica
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        System.out.println("=== Resultado Final ===");
        System.out.println("Valor del contador: " + contador.get());
        
        System.out.println("\n=== Tareas por hilo ===");
        for (int i = 0; i < 4; i++) {
            System.out.println("Hilo " + i + ": " + conteo.getOrDefault(i, 0) + " tareas");
        }
    }
}