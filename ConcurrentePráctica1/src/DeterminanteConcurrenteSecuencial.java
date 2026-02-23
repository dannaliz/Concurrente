package src;
public class DeterminanteConcurrenteSecuencial {
    static int determinante;
    static int n_prueba = 3;
    static int matriz_prueba[][] = { { 1, 2, 2 }, { 1, 0, -2 }, { 3, -1, 1 }};

    public static int determinanteMatriz3x3(int matriz[][]) {
        // Calculo de las diagonales principales
        int p1 = matriz[0][0] * matriz[1][1] * matriz[2][2];
        int p2 = matriz[1][0] * matriz[2][1] * matriz[0][2];
        int p3 = matriz[2][0] * matriz[0][1] * matriz[1][2];

        // Calculo de las diagonales secundarias
        int s1 = matriz[2][0] * matriz[1][1] * matriz[0][2];
        int s2 = matriz[1][0] * matriz[0][1] * matriz[2][2];
        int s3 = matriz[0][0] * matriz[2][1] * matriz[1][2];

        // Determinante = suma positivas - suma negativas 
        return p1 + p2 + p3 - s1 - s2 - s3;
    }

    public static void main(String[] args) {
        // Medimos el tiempo de la versión secuencial para comparar con las versiones con hilos
        long startTime = System.nanoTime();
        
        determinante = determinanteMatriz3x3(matriz_prueba);
        
        long endTime = System.nanoTime();
        
        System.out.println("Program took " + (endTime - startTime) + " ns, result: " + determinante);
    }
}