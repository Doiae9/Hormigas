import java.util.ArrayList;
import java.util.List;

public class EscudrinnadorED {
    public static List<long[][]> BuscarED(long [][] C, double []Cl, double []Ct ){
        List<long[][]> ConfiguracionesLigeras = new ArrayList<>();
        List<Long> posicion= new ArrayList();
        double pesoMinimo = Double.MAX_VALUE;

        int n=C.length;
        int m=C[0].length;
        long total= (long) Math.pow(2,n+m);
        long aux=0;
        for (long num = 0; num <total; num++) {
            long[][] S = Generador.ConvertirYdividirBinarios(num,n,m);
           long [] Sl= S[0];
           long [] St= S[1];
            if(num%100000==0){
                System.out.println(num);
            }
            if(Proyector.VerificarED(C,Sl, St)) {
                double PesoActual = Valuador.CalcularPesoTotal(Cl, Ct, Sl, St);
                aux++;

                if (PesoActual < pesoMinimo) {
                    pesoMinimo = PesoActual;
                    ConfiguracionesLigeras.clear();
                    ConfiguracionesLigeras.add(S);
                    posicion.add(num);
                    //System.out.println("--------" + num + "---------");
                    //Proyector.ImprimirGranMatriz(S);
                } else if (PesoActual == pesoMinimo) {
                    ConfiguracionesLigeras.add(S);
                    posicion.add(num);
                }
            }
        }
        System.out.println("Total que fueron ED: " + aux);
        System.out.println("Y con peso minimo fueron: "+ ConfiguracionesLigeras.size());
        System.out.println("Peso final mínimo:" + pesoMinimo);
        return ConfiguracionesLigeras;
    }

    public static List<long[][]> BuscarEDSoloLugares(long[][] C, double[] Cl, double[] Ct) {
        List<long[][]> ConfiguracionesLigeras = new ArrayList<>();
        List<Long> posicion = new ArrayList<>();
        double pesoMinimo = Double.MAX_VALUE;

        int n = C.length;       // Cantidad de lugares
        int m = C[0].length;    // Cantidad de transiciones

        long[] StFijo = new long[m]; // Transiciones todas apagadas (0)

        long total = 1L << n; // 2^n combinaciones de lugares
        long aux = 0;

        for (long num = 0; num < total; num++) {
            long[] Sl = Generador.convertirBinario(num, n);
            long[][] S = new long[][] { Sl, StFijo };

            if (num % 10000 == 0) {
                System.out.println("Procesando: " + num);
            }

            if (Proyector.VerificarED(C, Sl, StFijo)) {
                double PesoActual = Valuador.CalcularPesoTotal(Cl, Ct, Sl, StFijo);
                aux++;

                if (PesoActual < pesoMinimo) {
                    pesoMinimo = PesoActual;
                    ConfiguracionesLigeras.clear();
                    ConfiguracionesLigeras.add(S);
                    posicion.clear();
                    posicion.add(num);
                } else if (PesoActual == pesoMinimo) {
                    ConfiguracionesLigeras.add(S);
                    posicion.add(num);
                }
            }
        }

        System.out.println("Total de configuraciones ED: " + aux);
        System.out.println("Configuraciones con peso mínimo: " + ConfiguracionesLigeras.size());
        System.out.println("Peso mínimo encontrado: " + pesoMinimo);
        return ConfiguracionesLigeras;
    }


    public static void ImprimirED(List<long[][]> ConfiguracionesLigeras){
        for (int i = 0; i < ConfiguracionesLigeras.size(); i++) {
            //long posiciones = posicion.get(i);
            long[][] configuraciones= ConfiguracionesLigeras.get(i);
            //System.out.println("------"+posiciones+"------");
            System.out.println("-------------");
            Proyector.ImprimirGranMatriz(configuraciones);
        }

    }
}
