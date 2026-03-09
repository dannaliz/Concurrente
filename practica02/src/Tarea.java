package practica02.src;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class Tarea implements Runnable {

    int tiempoTarea;
    int task;
    final Semaphore smphre;  // Controla que máximo 3 hilos accedan al servidor a la vez
    final Lock lock02;       // Controla que los hilos 0 y 2 no accedan al mismo tiempo

    public Tarea(int i, Semaphore smphre, Lock lock02) {
        this.task = i;
        this.smphre = smphre;
        this.lock02 = lock02;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        long id = currentThread.getId();

        // Determinamos el "tipo" de hilo según su id
        int value = (int) (id % 6);
        System.out.println("Running Thread " + value + " task: " + this.task);

        // Asignamos el tiempo de tarea según el tipo de hilo
        switch (value) {
            case 0, 2 -> this.tiempoTarea = 500;
            case 1    -> this.tiempoTarea = 2000;
            default   -> this.tiempoTarea = 3000;
        }

        try {
            // Paso 1: Adquirir el semáforo — esperar si ya hay 3 hilos en el servidor
            smphre.acquire();

            // Paso 2: Si soy hilo 0 o 2, adquirir el candado para exclusión mutua entre ellos
            if (value == 0 || value == 2) {
                lock02.lock();
            }

            // Paso 3: Ejecutar la tarea (simulada con sleep)
            System.out.println("Thread " + value + " accediendo al servidor | task: " + this.task);
            Thread.sleep(this.tiempoTarea);
            System.out.println("Thread " + value + " terminó | time: " + this.tiempoTarea + "ns");

        } catch (InterruptedException e) {
            System.out.println(e);
        } finally {
            // Paso 4: Si soy hilo 0 o 2, liberar el candado
            if (value == 0 || value == 2) {
                lock02.unlock();
            }

            // Paso 5: Liberar el semáforo para que otro hilo pueda entrar
            smphre.release();
        }
    }
}
