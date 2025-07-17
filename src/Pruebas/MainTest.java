package Pruebas;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class MainTest {
    public static void main(String[] args) throws FileNotFoundException {
            // Matriz de distancias (16x16)
        long[][] C1 = {
                {-1, -1, 0, 0, 1},
                {1, 0, 0, -1, 0},
                {0, 1, -1, 0, 0},
                {0, 1, 0, 1, -1},
                {1, 0, 1, 0, -1}};

        double[] Cl11 = {1, 1, 1, 1, 1};
        double[] Ct11 = {1, 1, 1, 1, 1};
        double[] Ct1m = {1000, 1000, 1000, 1000, 1000};
        int [][] C2=
                {       {-1, 0, 0, 1},
                        {1, -1, 0, 0},
                        {1, 0, -1, 0},
                        {0, 1, 0, -1},
                        {0, 0, 1, -1}};

        double[] Cl21 = {1, 1, 1, 1,1};
        double[] Ct21 = {1, 1, 1, 1};
        double[] Ct2m = {1000, 1000, 1000, 1000};

        int[][] Cr = {{-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //L1

                {1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //L6

                {0, 0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 1, 0, 1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //L11

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 1},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, 0}, //L16

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0}, //L21

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1},

                {0, 0, 0, -1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, -1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1, 0, 0, 0, 0, 0, 0}, //L26

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1, 0, 0},

                {0, -1, -1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1, 0, 0, 0, 0, 0, 0, 0, -1, 1, 0},

                {0, 0, 0, 0, 0, -1, -1, 1, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1, -1, 1, 0, 0, 0}, //L31

                {0, 0, 0, 0, 0, 0, 0, -1, -1, 1, 0, 0, 0, 0, 0, 0, 0, -1, 1, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1}};


        double [] Clr1 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        double[] Clrr = {30, 30, 40, 40, 50, 50, 40, 40, 40, 40, 50, 40, 50, 30, 40, 50, 40, 40, 40, 50, 40, 50, 50, 50, 50, 50, 50, 35, 35, 35, 35, 50, 50, 50};
         double[] Ctrm = {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};
        double[] Ctr1 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        double[] Ctrr =
                {20, 30, 30, 30, 30, 20, 20, 25, 25, 20, 20, 20, 20, 20, 20, 30, 20, 20, 30, 20, 25, 20, 20};
//Crear arreglos para cada alpha, beta y evaporacion




//            float alpha = 1.0f;         // Parámetro que controlan la influencia de la feromona.
//            float beta = 5.0f;          // Parámetro que controlan la influencia de la visibilidad.
//            float evaporation = 0.5f;   // Tasa de evaporación de las feromonas.

        float[] alphas = {1.0f, 2.0f};
        float[] betas = {2.5f, 5.0f};
        float[] evaporaciones = {0.25f, 0.5f};

        int numAnts = 100;           // Número de hormigas que participan en cada iteración.
        int numIterations = 10000;   // Número de iteraciones para ejecutar el algoritmo.
        float Q = 100.0f;  // Constante para la cantidad de feromona depositada.
        String direccionArchivo = "C:\\Users\\Doia\\Desktop\\MatrizReal.txt";
        String direccionArchivo2 = "C:\\Users\\Doia\\Desktop\\MatrizS.txt";

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



