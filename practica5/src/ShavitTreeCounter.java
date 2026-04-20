package src;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ShavitTreeCounter {

    // Nodo: puede ser balanceador (interno) o contador (hoja)
    static class Node {
        boolean isLeaf;
        Node left, right;
        AtomicInteger balancer = new AtomicInteger(0);
        AtomicLong count      = new AtomicLong(0);     // contador real (solo hojas)

        // Constructor hoja
        Node() {
            this.isLeaf = true;
        }

        // Constructor nodo interno
        Node(Node left, Node right) {
            this.isLeaf = false;
            this.left   = left;
            this.right  = right;
        }
    }

    // Raíz del árbol
    private final Node root;

    // Constructor: recibe la profundidad del árbol.
    // Con depth=d se crean 2^d hojas.
    public ShavitTreeCounter(int depth) {
        this.root = buildTree(depth);
    }

    // Construye el árbol recursivamente
    private Node buildTree(int depth) {
        if (depth == 0) return new Node();
        return new Node(buildTree(depth - 1), buildTree(depth - 1));
    }

    // increment(): distribuye el tráfico por el árbol usando los balanceadores (primas de difracción) hasta llegar
    // a una hoja y la incrementa atómicamente.
    public void increment() {
        Node current = root;
        while (!current.isLeaf) {
            int route = current.balancer.getAndIncrement();
            current = (route % 2 == 0) ? current.left : current.right;
        }
        current.count.incrementAndGet();
    }

    //   - Obstruction-free: si corre solo, siempre termina.
    //   - Linealizable: el valor retornado corresponde a un
    //     estado real del árbol (punto de linealización =
    //     final de la primera recolección exitosa).
    public long fetch() {
        while (true) {
            // 1. Primera "foto" de todas las hojas
            List<Long> foto1 = new ArrayList<>();
            obtenerValores(root, foto1);

            // 2. Segunda "foto" inmediatamente después
            List<Long> foto2 = new ArrayList<>();
            obtenerValores(root, foto2);

            // 3. Si ambas fotos son iguales, ningún escritor
            //    modificó el árbol entre las dos lecturas.
            //    El punto de linealización es el final de foto1.
            if (foto1.equals(foto2)) {
                long total = 0;
                for (long v : foto2) total += v;
                return total;
            }
            // Si son distintas, un escritor intervino. Reintentamos.
        }
    }

    // Recorre el árbol en orden y añade el valor de cada hoja a la lista
    private void obtenerValores(Node n, List<Long> lista) {
        if (n.isLeaf) {
            lista.add(n.count.get());
        } else {
            obtenerValores(n.left,  lista);
            obtenerValores(n.right, lista);
        }
    }

    // main: prueba con varios hilos escritores y un
    //       hilo lector al final.
    public static void main(String[] args) throws InterruptedException {
        final int DEPTH       = 4;   // 2^4 = 16 hojas
        final int NUM_THREADS = 8;
        final int INCREMENTS  = 1000; // cada hilo hace 1000 incrementos

        ShavitTreeCounter counter = new ShavitTreeCounter(DEPTH);

        // Creamos los hilos escritores
        Thread[] writers = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            writers[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS; j++) {
                    counter.increment();
                }
            });
        }

        // Arrancamos todos los hilos
        for (Thread t : writers) t.start();

        // Esperamos a que terminen
        for (Thread t : writers) t.join();

        // Leemos el total con Double-Collect
        long resultado = counter.fetch();
        long esperado  = (long) NUM_THREADS * INCREMENTS;

        System.out.println("Resultado obtenido : " + resultado);
        System.out.println("Resultado esperado : " + esperado);
        System.out.println(resultado == esperado ? "CORRECTO" : "ERROR");
    }
}