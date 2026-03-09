package practica02.src;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
 
public class Scheduler {

    // Semáforo inicializado en 3: máximo 3 hilos pueden acceder al servidor al mismo tiempo
    static Semaphore smphre = new Semaphore(3);

    // Candado compartido: garantiza que los hilos 0 y 2 no accedan al mismo tiempo
    static Lock lock02 = new ReentrantLock();

    public static void main(String[] args) {
        ExecutorService executorTarea = Executors.newFixedThreadPool(6);

        // Se lanzan 26 tareas, cada una recibe referencias al semáforo y al candado
        for (int i = 0; i < 26; i++) {
            executorTarea.execute(new Tarea(i, smphre, lock02));
        }

        executorTarea.shutdown();
    }
}