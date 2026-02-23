package src;

public class DeterminanteConcurrenteDosHilos extends Thread{
    static int determinante;
    static int n_prueba = 3;
    static int matriz_prueba[][] = { { 1, 2, 2 }, { 1, 0, -2 }, { 3, -1, 1 }};
    
	int[][] matriz;
    int parcial;
    boolean esPositivo;
    
	// Constructor que recibe la referencia a la matriz y el rol del hilo (positivo o negativo)
	public DeterminanteConcurrenteDosHilos(int[][] matriz, boolean esPositivo) {
        this.matriz = matriz;
        this.esPositivo = esPositivo;
    }
    
    public static int determinanteMatriz3x3(int matriz[][], int n) {
        int result = 0;
        
        // Creamos solo 2 hilos
		// thrPos calculará las 3 diagonales positivas 
        DeterminanteConcurrenteDosHilos thrPos = new DeterminanteConcurrenteDosHilos(matriz, true);
		// thrPos calculará las 3 diagonales negativas 
        DeterminanteConcurrenteDosHilos thrNeg = new DeterminanteConcurrenteDosHilos(matriz, false);

        // Se inicia la concurrencia
        thrPos.start();
        thrNeg.start();

        try {
            // Esperamos a que los 2 terminen para obtener los resultados 
            thrPos.join();
            thrNeg.join();
        } catch (InterruptedException e) {
            System.err.println("Error en la sincronización: " + e.getMessage()); 
        }

        // El resultado final es la resta de la parte positiva menos la negativa
        result = thrPos.parcial - thrNeg.parcial;
        return result;
    }
    
    @Override
    public void run() {
        // Cada hilo ahora procesa una región de memoria compartida (Heap)
        if (esPositivo) {
            // Suma de diagonales principales
            this.parcial = (matriz[0][0] * matriz[1][1] * matriz[2][2]) +
                           (matriz[1][0] * matriz[2][1] * matriz[0][2]) +
                           (matriz[2][0] * matriz[0][1] * matriz[1][2]);
        } else {
            // Suma de diagonales secundarias
            this.parcial = (matriz[2][0] * matriz[1][1] * matriz[0][2]) +
                           (matriz[1][0] * matriz[0][1] * matriz[2][2]) +
                           (matriz[0][0] * matriz[2][1] * matriz[1][2]);
        }
    }

    public static void main(String[] args) {
		// Medimos el tiempo total de ejecución incluyendo la creación y sincronización de hilos
        long startTime = System.nanoTime();
        determinante = determinanteMatriz3x3(matriz_prueba, n_prueba);
        long endTime = System.nanoTime();
        
        System.out.println("Program took " + (endTime - startTime) + "ns");
        System.out.println("Result: " + determinante);
    }
}