package practica02.src;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ColaConcurrenteCandado {
    
    private static class Nodo {
        String item;
        Nodo next;

        public Nodo(String item) {
            this.item = item;
            this.next = null;
        }
    }

    private Nodo head;
    private Nodo tail;
    // Utilizamos un solo candado para que enq y deq compartan la misma sección crítica 
    private final Lock lock = new ReentrantLock();

    public ColaConcurrenteCandado() {
        Nodo dummy = new Nodo("sentinel");
        this.head = dummy;
        this.tail = dummy;
    }

    public boolean enq(String x) {
        lock.lock(); // Inicia la sección crítica 
        try {
            Nodo newNode = new Nodo(x);
            tail.next = newNode;
            tail = newNode;
            return true;
        } finally {
            lock.unlock(); // Termina la sección crítica 
        }
    }

    public String deq() {
        lock.lock(); // Inicia la sección crítica 
        try {
            if (head.next == null) {
                return "empty"; // Cola vacía
            }
            Nodo first = head.next;
            head.next = first.next;
            
            // Si sacamos el último elemento, el tail debe apuntar de nuevo al dummy (head)
            if (first == tail) {
                tail = head;
            }
            return first.item;
        } finally {
            lock.unlock(); // Termina la sección crítica 
        }
    }

    public void print() {
        lock.lock();
        try {
            Nodo curr = head.next;
            System.out.print("Cola actual: ");
            while (curr != null) {
                System.out.print(curr.item + " -> ");
                curr = curr.next;
            }
            System.out.println("null");
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ColaConcurrenteCandado queue = new ColaConcurrenteCandado();
        // Pool de hilos [cite: 173]
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Tarea de inserción
        for (int i = 0; i < 5; i++) {
            final int val = i;
            executor.execute(() -> {
                queue.enq("Nodo-" + val);
                System.out.println(Thread.currentThread().getName() + " enq: Nodo-" + val);
            });
        }

        // Tarea de extracción
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " deq: " + queue.deq());
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        queue.print();
    }
}