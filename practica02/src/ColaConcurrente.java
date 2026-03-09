package practica02.src;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Nodo {
    String item;
    AtomicReference<Nodo> next;

    public Nodo(String item) {
        this.item = item;
        this.next = new AtomicReference<>(null);
    }
}

public class ColaConcurrente {
    private final AtomicReference<Nodo> head;
    private final AtomicReference<Nodo> tail;

    public ColaConcurrente() {
        // En algoritmos lock-free, se suele usar un nodo centinela (dummy)
        Nodo dummy = new Nodo("sentinel");
        this.head = new AtomicReference<>(dummy);
        this.tail = new AtomicReference<>(dummy);
    }

    public boolean enq(String x) {
        Nodo newNode = new Nodo(x);
        while (true) {
            Nodo last = tail.get();
            Nodo next = last.next.get();
            if (last == tail.get()) { // ¿Es el estado consistente?
                if (next == null) {
                    // Intento de insertar el nodo al final
                    if (last.next.compareAndSet(null, newNode)) {
                        // Intento de mover tail al nuevo nodo
                        tail.compareAndSet(last, newNode);
                        return true;
                    }
                } else {
                    // La cola está en un estado intermedio, ayudamos a mover tail
                    tail.compareAndSet(last, next);
                }
            }
        }
    }

    public String deq() {
        while (true) {
            Nodo first = head.get();
            Nodo last = tail.get();
            Nodo next = first.next.get();
            if (first == head.get()) { // ¿Es el estado consistente?
                if (first == last) {
                    if (next == null) {
                        return "empty"; // Cola vacía
                    }
                    // Tail se quedó atrás, intentamos avanzar
                    tail.compareAndSet(last, next);
                } else {
                    String value = next.item;
                    if (head.compareAndSet(first, next)) {
                        return value;
                    }
                }
            }
        }
    }

    public void print() {
        Nodo curr = head.get().next.get();
        System.out.print("Cola actual: ");
        while (curr != null) {
            System.out.print(curr.item + " -> ");
            curr = curr.next.get();
        }
        System.out.println("null");
    }

    public static void main(String[] args) {
        ColaConcurrente queue = new ColaConcurrente();
        // Pool de hilos para probar la concurrencia
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