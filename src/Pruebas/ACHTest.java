package Pruebas;

import java.util.Arrays;
import java.util.Random;

public class ACHTest {
    private final int numLugares;
    private final int numTransiciones;
    private final int numAnts;
    private final double[] Cl;
    private final double[] Ct;
    private  double[] pheromoneL;
    private  double[] pheromoneT;
    private final double alpha;
    private final double beta;
    private final double evaporation;
    private final double Q;
    private Configuracion  bestConfiguracion;
    private double bestCost;
    private final Random random;
    private final long[][] C;



    public ACHTest(long[][] C, double[] Cl,double[] Ct, int
            numAnts, int numIterations,
                   double alpha, double beta, double evaporation, double Q) {
        this.C= C;
        this.numLugares = Cl.length;
        this.numTransiciones = Ct.length;
        this.numAnts = numAnts;
        this.Cl = Cl;
        this.Ct = Ct;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporation = evaporation;
        this.Q = Q;//Cantidad de feromona depositada
        this.pheromoneL = new double[numLugares];
        this.pheromoneT = new double[numTransiciones];
        this.bestCost = Integer.MAX_VALUE;
        this.random = new Random();
        bestConfiguracion= new Configuracion(numLugares,numTransiciones);

        // Inicializa la matriz de feromonas con un pequeño valor inicial
        Arrays.fill(pheromoneL, 1.0);
        Arrays.fill(pheromoneT, 1.0);



        // Ejecuta el algoritmo de optimización
        for (int i = 0; i < numIterations; i++) {
            optimize();
        }
    }
    private void optimize() {
        Configuracion [] antConfigurations = new Configuracion[numAnts];
        double [] antCostos = new double[numAnts];

        for (int i = 0; i < numAnts; i++) {
            antConfigurations[i] = constructSolution();
            antCostos[i] = ValuadorACH.CalcularPesoTotal(Cl,Ct,antConfigurations[i].Sl,antConfigurations[i].St);

            if (antCostos[i] < bestCost) {
                bestCost = antCostos[i];
                bestConfiguracion = antConfigurations[i].clone();
            }
        }

        updatePheromones(antConfigurations, antCostos);
    }

    private Configuracion constructSolution() {
        Configuracion conf = new Configuracion(numLugares,numTransiciones);
        int RangoAccion = 2;
        int maxIntentos = (numLugares + numTransiciones)*RangoAccion; // o algo más grande
        int intentos = 0;
      do {
          selectNextNode(conf);
          intentos ++;
      }while (!ProyectorACH.VerificarED(C,conf.Sl, conf.St)&& intentos < maxIntentos);

      return conf;
    }
    private void selectNextNode(Configuracion conf) {
        double[] probabilities = new double[numLugares+numTransiciones];
        double sum = 0.0;

        //System.out.println("Sl antes: " + Arrays.toString(conf.Sl));
        //System.out.println("St antes: " + Arrays.toString(conf.St));

        for (int i = 0; i < numLugares; i++) {
            if (conf.Sl[i]==0) {
                probabilities[i] = Math.pow(pheromoneL[i], alpha) * Math.pow(1.0 / Cl[i], beta);
                sum += probabilities[i];
            }
        }       // 0                    5+5= 10
        for (int j =0; j <numTransiciones; j++) {
            int indx= numLugares +j; //Aqui se accede al tamaño real de la ruleta
            if (conf.St[j]==0) { //Aqui se accede al tamaño real del arreglo St
                probabilities[indx] = Math.pow(pheromoneT[j], alpha) * Math.pow(1.0 / Ct[j], beta);
                sum += probabilities[indx];
            }
        }

        double r = random.nextDouble() * sum;
        sum = 0.0;

        for (int i = 0; i < numLugares; i++) {
            if (conf.Sl[i]==0) {
                sum += probabilities[i];
                if (sum >= r) {
                    conf.Sl[i] = 1;
                    return;
                }
            }
        }
        for (int j = 0; j < numTransiciones; j++) {
            int indx= numLugares+j;
            if (conf.St[j]==0) {
                sum += probabilities[indx];
                if (sum >= r) {
                    conf.St[j] = 1;
                   // System.out.println("✅ Transición activada en St[" + j + "]");
                    return;
                }
            }
        }

    }

    private void updatePheromones(Configuracion [] antConfigurations, double[] antCosts) {
        // Evaporación
        for (int i = 0; i < numLugares; i++) {
            pheromoneL[i] *= (1 - evaporation);
        }
        for (int i = 0; i < numTransiciones; i++) {
            pheromoneT[i] *= (1 - evaporation);
        }

        // Actualización basada en la calidad de la solución
        for (int i = 0; i < numAnts; i++) {
            for (int j = 0; j < numLugares; j++) {
                if(antConfigurations[i].Sl[j]==1) {
                    pheromoneL[j] += Q / antCosts[i];
                }
            }
            for (int k = 0; k < numTransiciones; k++) {
                if(antConfigurations[i].St[k]==1) {
                    pheromoneT[k] += Q / antCosts[i];
                }
            }


        }
    }



    public Configuracion getConfiguracionBest() {
        return bestConfiguracion;
    }

    public double getBestCost() {
        return bestCost;
    }
}



