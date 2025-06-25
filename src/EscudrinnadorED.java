import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EscudrinnadorED {

    public static List<long[][]> BuscarED(long [][] C, double []Cl, double []Ct ) {
        List<long[][]> ConfiguracionesLigeras = new ArrayList<>();
        //List<Long> posicion= new ArrayList();
        double pesoMinimo = Double.MAX_VALUE;
        double ultimoPesoMinimoMostrado = Double.MAX_VALUE;
        Map<Integer, Integer> frecuenciaSensoresActivos = new HashMap<>();
        int porcentajeAnterior = -1;

        int n = C.length;
        int m = C[0].length;
        long total = (long) Math.pow(2, n + m);
        long aux = 0;
        LongProgressBar barra = new LongProgressBar(total, 40);
        // Mensaje cuando se cumple la condición

        for (long num = 0; num < total; num++) {
            long[][] S = Generador.ConvertirYdividirBinarios(num, n, m);
            long[] Sl = S[0];
            long[] St = S[1];

            if (num % 1_000_000 == 0) {
                barra.update(num);

                // Solo imprimimos si el peso mínimo ha cambiado desde la última vez
                if (pesoMinimo < ultimoPesoMinimoMostrado) {
                    System.out.println("\n[Progreso: " + barra.getPorcentajeActual() + "%] Nuevo peso mínimo: " + pesoMinimo);
                    ultimoPesoMinimoMostrado = pesoMinimo;
                }
            }
            if(Proyector.esObviamenteInvalido(Sl,St)){ continue;}
                if (Proyector.VerificarED(C, Sl, St)) {
                    double PesoActual = Valuador.CalcularPesoTotal(Cl, Ct, Sl, St);
                    aux++;

                    int activos = 0;
                    for (long bit : Sl) {
                        if (bit == 1) activos++;
                    }
                    frecuenciaSensoresActivos.put(activos,
                            frecuenciaSensoresActivos.getOrDefault(activos, 0) + 1);

                    if (PesoActual < pesoMinimo) {
                        pesoMinimo = PesoActual;
                        ConfiguracionesLigeras.clear();
                        ConfiguracionesLigeras.add(S);
                    } else if (PesoActual == pesoMinimo) {
                        ConfiguracionesLigeras.add(S);
                        //posicion.add(num);
                    }
                }
        }
            //Asegura que se muestre el 100%
            barra.update(total);
            System.out.println(" ");

            System.out.println("Total que fueron ED: " + aux);
            System.out.println("Y con peso minimo fueron: " + ConfiguracionesLigeras.size());
            System.out.println("Peso final mínimo:" + pesoMinimo);

            System.out.println("\nFrecuencia de sensores activos en configuraciones ED:");
            frecuenciaSensoresActivos.forEach((cantidad, veces) ->
                    System.out.println(cantidad + " sensores activos: " + veces + " configuración(es)")
            );
            return ConfiguracionesLigeras;
    }


    public static List<long[][]> BuscarEDSoloLugares(long[][] C, double[] Cl, double[] Ct) {
        List<long[][]> ConfiguracionesLigeras = new ArrayList<>();
        //List<Long> posicion = new ArrayList<>();
        double pesoMinimo = Double.MAX_VALUE;
        double ultimoPesoMinimoMostrado = Double.MAX_VALUE;
        Map<Integer, Integer> frecuenciaSensoresActivos = new HashMap<>();
        int porcentajeAnterior = -1;

        int n = C.length;       // Cantidad de lugares
        int m = C[0].length;    // Cantidad de transiciones

        long[] StFijo = new long[m]; // Transiciones todas apagadas (0)

        long total = 1L << n; // 2^n combinaciones de lugares
        long aux = 0;

        //llamamos a la barra de progreso
        LongProgressBar barra = new LongProgressBar(total, 40);
        barra.start();
        for (long num = 0; num < total; num++) {


            long[] Sl = Generador.convertirBinario(num, n);
            long[][] S = new long[][] { Sl, StFijo };

            //Actualiza hasta este numero para evitar sobrecarga
            if (num % 1_000_000 == 0) {
                barra.update(num);

                // Solo imprimimos si el peso mínimo ha cambiado desde la última vez
                if (pesoMinimo < ultimoPesoMinimoMostrado) {
                    System.out.println("\n[Progreso: " + barra.getPorcentajeActual() + "%] Nuevo peso mínimo: " + pesoMinimo);
                    ultimoPesoMinimoMostrado = pesoMinimo;
                }
            }

            if (Proyector.VerificarED(C, Sl, StFijo)) {
                double PesoActual = Valuador.CalcularPesoTotal(Cl, Ct, Sl, StFijo);
                aux++;

                int activos = 0;
                for (long bit : Sl) {
                    if (bit == 1) activos++;
                }

                frecuenciaSensoresActivos.put(activos,
                        frecuenciaSensoresActivos.getOrDefault(activos, 0) + 1);

                if (PesoActual < pesoMinimo) {
                    pesoMinimo = PesoActual;
                    ConfiguracionesLigeras.clear();
                    ConfiguracionesLigeras.add(S);
                  //  posicion.clear();
                  //  posicion.add(num);
                } else if (PesoActual == pesoMinimo) {
                    ConfiguracionesLigeras.add(S);
                   // position.add(num);
                }
            }
        }
        //Asegura que se muestre el 100%
        barra.update(total);
        System.out.println(" ");
        System.out.println("Total de configuraciones ED: " + aux);
        System.out.println("Configuraciones con peso mínimo: " + ConfiguracionesLigeras.size());
        System.out.println("Peso mínimo encontrado: " + pesoMinimo);

        System.out.println("\nFrecuencia de sensores activos en configuraciones ED:");
        frecuenciaSensoresActivos.forEach((cantidad, veces) ->
                System.out.println(cantidad + " sensores activos: " + veces + " configuración(es)")
        );
        return ConfiguracionesLigeras;
    }


    public static void ImprimirED(List<long[][]> ConfiguracionesLigeras){
        for (int i = 0; i < ConfiguracionesLigeras.size(); i++) {
            //long posiciones = posicion.get(i);
            long[][] configuraciones= ConfiguracionesLigeras.get(i);
            //System.out.println("------"+posiciones+"------");
            System.out.println("---------------------------------------------------------------");
            Proyector.ImprimirGranMatriz(configuraciones);
            System.out.println("Posiciones: ");
            obtenerIndicesActivosPorFila(configuraciones).forEach(System.out::println);
        }

    }
    public static List<List<Integer>> obtenerIndicesActivosPorFila(long[][] matriz) {
        List<List<Integer>> resultado = new ArrayList<>();
        for (int i = 0; i < matriz.length; i++) {
            //guarda cada fila
            List<Integer> indicesFila = new ArrayList<>();
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] == 1) {
                    indicesFila.add(j);
                }
            }
            //el boche de filas se guarda en otra lista en donde se encuentran los resultados
            resultado.add(indicesFila);
        }
        return resultado;
    }
    //frecuencias dentro de hasmap
    public static Map<Integer, Integer> contarFrecuenciaSensores(long[][] matriz) {
        Map<Integer, Integer> frecuencia = new HashMap<>();

        for (long[] fila : matriz) {
            int activos = 0;
            for (long valor : fila) {
                if (valor == 1) {
                    activos++;
                }
            }
            // Incrementamos el contador para ese número de sensores activos
            frecuencia.put(activos, frecuencia.getOrDefault(activos, 0) + 1);
        }

        return frecuencia;
    }

}
