package src;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class Contador {
    public int valor = 0;
    public void inc() { valor++; }
}

public class PruebaContador {
    public static void main(String[] args) throws InterruptedException {
        Contador contador = new Contador();
        Peterson4Threads candado = new Peterson4Threads();
        AtomicInteger[] conteoPorHilo = new AtomicInteger[4];
        
        for (int i = 0; i < 4; i++) conteoPorHilo[i] = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 400; i++) {
            executor.execute(() -> {
                // Identificamos el hilo (0 a 3) basándonos en su ID único
                int id = (int) (Thread.currentThread().getId() % 4);
                
                candado.lock(id);
                try {
                    contador.inc();
                    conteoPorHilo[id].incrementAndGet();
                } finally {
                    candado.unlock(id);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("--- RESULTADOS ---");
        System.out.println("Valor final del contador: " + contador.valor);
        for (int i = 0; i < 4; i++) {
            System.out.println("Hilo " + i + " incrementó: " + conteoPorHilo[i].get() + " veces.");
        }
    }
}