package src;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RunSpin {
    
    // 1. Matriz compartida de 10x10
    static final int N = 10;
    static int[][] matriz = new int[N][N];

    // 2. Función separada para la asignación
    // Le pasamos un valor (ej. el ID del hilo) para ver quién la modificó
    public static void asignarMatriz(int valorHilo) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                // Simulamos un trabajo un poco más "pesado"
                matriz[i][j] = valorHilo + (i * j); 
            }
        }
    }

    // 3. Función separada para la impresión
    public static void imprimirMatriz(int valorHilo) {
        System.out.println("--- Matriz impresa por Hilo " + valorHilo + " ---");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println("-----------------------------------");
    }
    
    // 4. Modificación del task(lock)
    private static void task(Lock lock, int idHilo) {
        try {
            lock.lock(); // Inicia Sección Crítica
            
            // Asignamos e imprimimos dentro de la protección del candado
            asignarMatriz(idHilo);
            imprimirMatriz(idHilo);
            
        } finally {
            lock.unlock(); // Termina Sección Crítica
        }
    }

    public static void main(String[] args) {
        List<Future<?>> futures = new ArrayList<>();
        
        // Se configura esto para las columnas de la tabla (4 hilos o max-1 hilos)
        int numberThreads = 4; 
        ExecutorService executor = Executors.newFixedThreadPool(numberThreads);

        // Descomenta el candado que vayas a probar para tu tabla
        // Lock lock = new TASLock();
        // Lock lock = new TTASLock();
        // Lock lock = new BackoffLock();
        // Lock lock = new MCSLock();
        // Lock lock = new ALock(numberThreads);
        // Lock lock = new CLHLock();
        Lock lock = new ReentrantLock();
        
        long startTime = System.nanoTime(); // Start time
        
        // Se coloca el número de tareas
        for(int i = 0; i < 100; i++) {
            final int idTarea = i; 
            futures.add(executor.submit(() -> task(lock, idTarea))); 
        }
        executor.shutdown();
        
        for (int i = 0; i < futures.size(); i++) {
            while(!futures.get(i).isDone()){}; // Comprobar que todas terminen
        }
        
        long endTime = System.nanoTime(); // Finish time
        
        System.out.println("Tiempo total (Matriz): " + (endTime - startTime)*0.000001 + " ms");
    }
}