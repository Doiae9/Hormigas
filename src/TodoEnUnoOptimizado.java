import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;

//A pesar del nombre este código no esta optimia
public class TodoEnUnoOptimizado {
    static class Worker extends RecursiveTask<List<long[][]>> {
        private static final long UMBRAL = 20_000_000L;
        private final long start, end;
        private final int n, m;
        private final long[][] C;
        private final double[] Cl, Ct;
        private final AtomicReference<Double> pesoMinimoGlobal;
        private final AtomicLong progresoGlobal;
        private final LongProgressBar barra;
        private final Map<Integer, LongAdder> frecuenciaSensoresActivosGlobal;

        public Worker(long start, long end, int n, int m, long[][] C, double[] Cl, double[] Ct,
                      AtomicReference<Double> pesoMinimoGlobal, AtomicLong progresoGlobal,
                      LongProgressBar barra, Map<Integer, LongAdder> frecuenciaSensoresActivosGlobal) {
            this.start = start;
            this.end = end;
            this.n = n;
            this.m = m;
            this.C = C;
            this.Cl = Cl;
            this.Ct = Ct;
            this.pesoMinimoGlobal = pesoMinimoGlobal;
            this.progresoGlobal = progresoGlobal;
            this.barra = barra;
            this.frecuenciaSensoresActivosGlobal = frecuenciaSensoresActivosGlobal;
        }

        @Override
        protected List<long[][]> compute() {
            if (end - start <= UMBRAL) {
                return ejecutarDirectamente();
            } else {
                long medio = (start + end) / 2;
                Worker left = new Worker(start, medio, n, m, C, Cl, Ct, pesoMinimoGlobal,
                        progresoGlobal, barra, frecuenciaSensoresActivosGlobal);
                Worker right = new Worker(medio, end, n, m, C, Cl, Ct, pesoMinimoGlobal,
                        progresoGlobal, barra, frecuenciaSensoresActivosGlobal);
                left.fork();
                List<long[][]> rightResult = right.compute();
                List<long[][]> leftResult = left.join();
                leftResult.addAll(rightResult);
                return leftResult;
            }
        }

        private List<long[][]> ejecutarDirectamente() {
            double pesoMinLocal = Double.MAX_VALUE;
            List<long[][]> locales = new ArrayList<>();
            long counter = 0;

            for (long num = start; num < end; num++) {
                long[][] S = ConvertirYdividirBinarios(num, n, m);
                long[] Sl = S[0];
                long[] St = S[1];

                if (VerificarED(C, Sl, St)) {
                    double pesoActual = CalcularPesoTotal(Cl, Ct, Sl, St);

                    if (pesoActual < pesoMinLocal) {
                        pesoMinLocal = pesoActual;
                        locales.clear();
                        locales.add(S);
                    } else if (pesoActual == pesoMinLocal) {
                        locales.add(S);
                    }

                    int activos = 0;
                    for (long bit : Sl) if (bit == 1) activos++;
                    frecuenciaSensoresActivosGlobal
                            .computeIfAbsent(activos, k -> new LongAdder())
                            .increment();
                }

                counter++;
                if (counter == 20_000_000) {
                    progresoGlobal.addAndGet(counter);
                    barra.update(progresoGlobal.get());
                    counter = 0;
                }
            }

            if (counter > 0) {
                progresoGlobal.addAndGet(counter);
                barra.update(progresoGlobal.get());
            }

            synchronized (pesoMinimoGlobal) {
                if (pesoMinLocal < pesoMinimoGlobal.get()) {
                    pesoMinimoGlobal.set(pesoMinLocal);
                    return locales;
                } else if (pesoMinLocal == pesoMinimoGlobal.get()) {
                    return locales;
                }
            }
            return new ArrayList<>();
        }
    }

    public static List<long[][]> BuscarEDMultihullConProgreso(long[][] C, double[] Cl, double[] Ct, int numHilos)
            throws InterruptedException, ExecutionException {
        int n = C.length;
        int m = C[0].length;
        long total = 1L << (n + m);

        AtomicReference<Double> pesoMinimoGlobal = new AtomicReference<>(Double.MAX_VALUE);
        AtomicLong progresoGlobal = new AtomicLong(0);
        Map<Integer, LongAdder> frecuenciaSensoresActivosGlobal = new ConcurrentHashMap<>();
        LongProgressBar barra = new LongProgressBar(total, 40);
        barra.start();

        ForkJoinPool pool = new ForkJoinPool(numHilos);
        List<long[][]> resultado = pool.invoke(new Worker(0, total, n, m, C, Cl, Ct, pesoMinimoGlobal,
                progresoGlobal, barra, frecuenciaSensoresActivosGlobal));
        pool.shutdown();

        barra.update(total);
        System.out.println("\nPeso mínimo encontrado: " + pesoMinimoGlobal.get());
        System.out.println("\nFrecuencia de sensores activos:");
        frecuenciaSensoresActivosGlobal.forEach((activos, adder) ->
                System.out.println(activos + " sensores activos: " + adder.sum() + " configuración(es)")
        );

        return resultado;
    }

    // Métodos auxiliares (deben estar en la misma clase)
    public static long[] convertirBinario(long decimal, int longitud) {
        long[] binarios = new long[longitud];
        for (int i = longitud - 1; i >= 0; i--) {
            binarios[i] = decimal & 1;
            decimal >>= 1;
        }
        return binarios;
    }

    public static long[][] DividirBinarios(long[] binarios, int n, int m) {
        long[] Sl = new long[n];
        long[] St = new long[m];
        System.arraycopy(binarios, 0, St, 0, m);
        System.arraycopy(binarios, m, Sl, 0, n);
        return new long[][]{Sl, St};
    }

    public static long[][] ConvertirYdividirBinarios(long decimal, int n, int m) {
        long[] binarios = convertirBinario(decimal, n + m);
        return DividirBinarios(binarios, n, m);
    }

    public static double CalcularPesoGeneral(double[] Costos, long[] Sensores) {
        double peso = 0;
        for (int i = 0; i < Sensores.length; i++) {
            peso += Sensores[i] * Costos[i];
        }
        return peso;
    }

    public static double CalcularPesoTotal(double[] Cl, double[] Ct, long[] Sl, long[] St) {
        return CalcularPesoGeneral(Cl, Sl) + CalcularPesoGeneral(Ct, St);
    }

    public static boolean VerificarED(long[][] C, long[] Sl, long[] St) {
        int n = Sl.length;
        int m = St.length;

        // Verificar columnas nulas
        for (int j = 0; j < m; j++) {
            if (St[j] == 0) {
                boolean allZero = true;
                for (int i = 0; i < n; i++) {
                    if (Sl[i] == 1 && C[i][j] != 0) {
                        allZero = false;
                        break;
                    }
                }
                if (allZero) return false;
            }
        }

        // Verificar columnas redundantes
        for (int j1 = 0; j1 < m; j1++) {
            if (St[j1] != 0) continue;
            for (int j2 = j1 + 1; j2 < m; j2++) {
                if (St[j2] != 0) continue;
                boolean iguales = true;
                for (int i = 0; i < n; i++) {
                    if (Sl[i] == 1 && C[i][j1] != C[i][j2]) {
                        iguales = false;
                        break;
                    }
                }
                if (iguales) return false;
            }
        }
        return true;
    }
}
