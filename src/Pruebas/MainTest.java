package Pruebas;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;


public class MainTest {
    public static void main(String[] args) throws FileNotFoundException {


        float[] alphas = {1.0f, 2.0f};
        float[] betas = {2.5f, 5.0f};
        float[] evaporaciones = {0.25f, 0.5f};

        int numAnts = 100;           // Número de hormigas que participan en cada iteración.
        int numIterations = 10000;   // Número de iteraciones para ejecutar el algoritmo.
        float Q = 100.0f;  // Constante para la cantidad de feromona depositada.
        String direccionArchivo = "C:\\Users\\Doia\\Desktop\\MatrizReal.txt"; //Matrices a partir de un archivo
        //String direccionArchivo2 = "C:\\Users\\Doia\\Desktop\\MatrizS.txt";  //prueba con una matriz pequeña

        DatosExtraidos datos = Lector.leerDesdeArchivo(direccionArchivo);

        int[][] matriz    = datos.matrizEnteros;
        double[] arreglo1 = datos.arregloDoubles1;
        double[] arreglo2 = datos.arregloDoubles2;

        PrintWriter writer = new PrintWriter("resultadosACO.csv");
        writer.println("iteracion,alpha,beta,evaporacion,costo,sensores_activos,Sl,St");



        for(float alpha : alphas) {
            for(float beta : betas) {
                for(float evap : evaporaciones) {
                    for (int i = 0; i < 5; i++) {
                        System.out.println("========== EJECUCION #" + (i+1) + "========");
                        System.out.println("Probando combinacion : " + "alpha: " + alpha + ", beta: " + beta + ", evap: " + evap);
                        ACHTest redPetri = new ACHTest(matriz,arreglo1,arreglo2,numAnts,numIterations,alpha,beta,evap,Q);
                        redPetri.imprimirEstadisticas();
                        redPetri.imprimirConfiguracionesMinimas();
                        Configuracion best = redPetri.getConfiguracionBest();
                        int activos = ACHTest.contarActivos(best.Sl) + ACHTest.contarActivos(best.St);
                        writer.println(i + "," + alpha + "," + beta + "," + evap + "," + redPetri.getBestCost() + "," + activos + "," +
                                Arrays.toString(best.Sl) + "," + Arrays.toString(best.St));
                    }
            }
        }
            writer.close();
    }

    }
}



