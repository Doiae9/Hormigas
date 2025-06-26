import java.util.List;

public class Main {
    public static void main(String[] args) {

        long[][] C = {{-1, -1, 0, 0, 1}, {1, 0, 0, -1, 0}, {0, 1, -1, 0, 0}, {0, 1, 0, 1, -1}, {1, 0, 1, 0, -1}};
        long[] Sl = {1, 0, 0, 0, 0};
        long[] St = {1, 0, 1, 0, 1};
        // double[] Cl = {1, 1, 1, 1, 1};
        // double[] Ct = {1000, 1000, 1000, 1000, 1000};
        // double [] Cl = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        double[] Cl = {30, 30, 40, 40, 50, 50, 40, 40, 40, 40, 50, 40, 50, 30, 40, 50, 40, 40, 40, 50, 40, 50, 50, 50, 50, 50, 50, 35, 35, 35, 35, 50, 50, 50};
        // double[] Ct = {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};
        double[] Ct = {20, 30, 30, 30, 30,
                20, 20, 25, 25, 20,
                20, 20, 20, 20, 20,
                30, 20, 20, 30, 20,
                25, 20, 20};

        long[][] matrizC = {{-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //L1

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

        int numHilos = 2;

        try {
            // Llamada al metod multihilo desde el main
            System.out.println("Con "+ numHilos +" hilos");
            System.out.println("Procesadores disponibles: (Nucleos lógicos) " + Runtime.getRuntime().availableProcessors());
            List<long[][]> resultados = TodoEnUno.BuscarEDMultihullConProgreso(matrizC, Cl, Ct, numHilos);
            // Mostrar resultados
            System.out.println("\n--- Resultados de configuraciones ED ---");
            for (long[][] config : resultados) {
                Proyector.ImprimirGranMatriz(config);
                System.out.println("-------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
        //int prueba = 10;
        //Imprimir matriz
        //Proyector.ImprimirMatiz(C);
        //System.out.println("--------------");
        //imprimimos los lugares
        //int[][] Clu = Proyector.ProyectarLugares(C, Sl);
        //Proyector.ImprimirMatiz(Clu);
        // System.out.println("--------------");
        //Imprimimos las transiciones
        //int[][] Ctr = Proyector.ProyectarTransiciones(Clu, St);
        //Proyector.ImprimirMatiz(Ct);
        //System.out.println("---------------");
        //System.out.println("Funcion para C_");
        //declaramos una variable para la C proyectada
        //Función que proyecta tanto Sl y St

        //Proyector.ImprimirMatiz(C_);
        //boolean esCero = Proyector.ComprobarCero(C_);
        //System.out.println(esCero);

        //Comparar Columnas
        //boolean ColumnasIguales = Proyector.ColumnasIguales(C_,0,1);
        //System.out.println(ColumnasIguales);

        //boolean Redundancia = Proyector.ComprobarRedundancia(C_);
        //System.out.println(Redundancia);
        //            if (Proyector.ED(C_)) {
//                //.out.println("El sistema cumple con las condiciones");
//            } else {
//                //System.out.println("El sistema NO cumple con las condiciones");
//            }
//          ? "El sistema es ED":
//        "El sistema no es ED";

        //System.out.println("Pesos sistema ED");
        //System.out.println("--------");
        // double PesoSt = Valuador.CalcularPesoTransicion(Ct, St);
        //System.out.println("Peso de St: " + PesoSt);
        //double PesoSl = Valuador.CalcularPesoLugar(Cl, Sl);
        //System.out.println("Peso de Sl: " + PesoSl);
        //double PesoTotal = Valuador.CalcularPesoTotal(Cl, Ct, Sl, St);
        //System.out.println("Peso Total: " + PesoTotal);
        //Math.pow(2,30)

        //=============================================================
//        List<long[][]> ConfiguracionesLigeras = new ArrayList<>();
//        List<Long> posicion= new ArrayList();
//        double pesoMinimo = Double.MAX_VALUE;
//
//        int n=C.length;
//        int m=C[0].length;
//        long total= (long) Math.pow(2,n+m);
//        long aux=0;
//        for (long num = 0; num <total; num++) {
//         long [] binarios =Generador.convertirBinario(num,30);
//         Generador.DividirBinarios(binarios,15,15);
//            //Generador de Sensores
//            long[][] S = Generador.ConvertirYdividirBinarios(num,n,m);
//            //Proyectar lugares y transiciones
//            //long[][] C_ = Proyector.ProyeccionC(C, S[0], S[1]);
//            Sl= S[0];
//            St= S[1];
//           if(Proyector.VerificarED(C,Sl, St)) {
//               double PesoActual = Valuador.CalcularPesoTotal(Cl, Ct, Sl, St);
//               aux++;
//
//               if (PesoActual < pesoMinimo) {
//                  pesoMinimo = PesoActual;
//                   ConfiguracionesLigeras.clear();
//                   ConfiguracionesLigeras.add(S);
//                   posicion.add(num);
//                   //System.out.println("--------" + num + "---------");
//                   //Proyector.ImprimirGranMatriz(S);
//               } else if (PesoActual == pesoMinimo) {
//                   ConfiguracionesLigeras.add(S);
//                   posicion.add(num);
//               }
//           }
//
//
//        }
        //Imprimir
//        System.out.println("----------------");
//
//        for (int i = 0; i < ConfiguracionesLigeras.size(); i++) {
//            long posiciones = posicion.get(i);
//
//           long[][] configuraciones= ConfiguracionesLigeras.get(i);
//            System.out.println("------"+posiciones+"------");
//            Proyector.ImprimirGranMatriz(configuraciones);
//        }
//        System.out.println("Total que fueron ED: " + aux);
//        System.out.println("Y con peso minimo fueron: "+ ConfiguracionesLigeras.size());
//        System.out.println("Peso final mínimo:" + pesoMinimo);
//        System.out.println("Sin hilos");
//        List<long[][]>ConfiguracionesED=EscudrinnadorED.BuscarED(matrizC, Cl, Ct);

        // List<long[][]>ConfiguracionesED=EscudrinnadorED.BuscarEDSoloLugares(matrizC, Cl, Ct);
        //EscudrinnadorED.ImprimirED(ConfiguracionesED);

//        try {
//                int hilos = 1;
//            System.out.println("Con "+ hilos +" hilos");
//            List<long[][]> resultados = BusquedaEDMultihiloConProgreso.BuscarEDMultihiloConProgreso(matrizC, Cl, Ct, hilos);
//            //List<long[][]>ConfiguracionesED=EscudrinnadorED.BuscarED(matrizC, Cl, Ct);
            // Puedes procesar `resultados` aquí
//            EscudrinnadorED.ImprimirED(resultados);
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace(); // O tu manejo de errores favorito
//             }
//
//        }
        //=====================================================================
