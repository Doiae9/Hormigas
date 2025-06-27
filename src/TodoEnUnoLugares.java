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

public class TodoEnUnoLugares {

        static class Worker extends RecursiveTask<List<long[][]>> {
            private static final long UMBRAL = 1_000_000L;
            private final long start, end;
            private final int n, m;
            private final long[][] C;
            private final double[] Cl, Ct;
            private final long[] StFijo;
            private final AtomicReference<Double> pesoMinimoGlobal;
            private final AtomicLong progresoGlobal;
            private final LongProgressBar barra;
            private final Map<Integer, LongAdder> frecuenciaSensoresActivosGlobal;

            public Worker(long start, long end, int n, int m, long[][] C, double[] Cl, double[] Ct, long[] StFijo,
                          AtomicReference<Double> pesoMinimoGlobal, AtomicLong progresoGlobal,
                          LongProgressBar barra, Map<Integer, LongAdder> frecuenciaSensoresActivosGlobal) {
                this.start = start;
                this.end = end;
                this.n = n;
                this.m = m;
                this.C = C;
                this.Cl = Cl;
                this.Ct = Ct;
                this.StFijo = StFijo;
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
                    Worker left = new Worker(start, medio, n, m, C, Cl, Ct, StFijo, pesoMinimoGlobal,
                            progresoGlobal, barra, frecuenciaSensoresActivosGlobal);
                    Worker right = new Worker(medio, end, n, m, C, Cl, Ct, StFijo, pesoMinimoGlobal,
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
                    long[] Sl = convertirBinario(num, n);
                    long[][] S = new long[][]{Sl, StFijo};

                    if (VerificarED(C, Sl, StFijo)) {
                        double pesoActual = CalcularPesoTotal(Cl, Ct, Sl, StFijo);

                        if (pesoActual < pesoMinLocal) {
                            pesoMinLocal = pesoActual;
                            locales.clear();
                            locales.add(S);
                        } else if (pesoActual == pesoMinLocal) {
                            locales.add(S);
                        }

                        int activos = Long.bitCount(num);
                        frecuenciaSensoresActivosGlobal
                                .computeIfAbsent(activos, k -> new LongAdder())
                                .increment();
                    }

                    counter++;
                    if (counter == 100_000) {
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

        public static List<long[][]> BuscarEDSoloLugares(long[][] C, double[] Cl, double[] Ct, int numHilos)
                throws InterruptedException, ExecutionException {
            int n = C.length;
            int m = C[0].length;
            long[] StFijo = new long[m]; // Transiciones fijas apagadas
            long total = 1L << n; // 2^n combinaciones

            AtomicReference<Double> pesoMinimoGlobal = new AtomicReference<>(Double.MAX_VALUE);
            AtomicLong progresoGlobal = new AtomicLong(0);
            Map<Integer, LongAdder> frecuenciaSensoresActivosGlobal = new ConcurrentHashMap<>();
            LongProgressBar barra = new LongProgressBar(total, 40);
            barra.start();

            ForkJoinPool pool = new ForkJoinPool(numHilos);
            List<long[][]> resultado = pool.invoke(
                    new Worker(0, total, n, m, C, Cl, Ct, StFijo, pesoMinimoGlobal,
                            progresoGlobal, barra, frecuenciaSensoresActivosGlobal));
            pool.shutdown();

            barra.update(total);

            // Estadísticas
            long totalED = frecuenciaSensoresActivosGlobal.values().stream()
                    .mapToLong(LongAdder::sum).sum();

            System.out.println("\nTotal de configuraciones ED: " + totalED);
            System.out.println("Configuraciones con peso mínimo: " + resultado.size());
            System.out.println("Peso mínimo encontrado: " + pesoMinimoGlobal.get());

            System.out.println("\nFrecuencia de sensores activos:");
            frecuenciaSensoresActivosGlobal.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry ->
                            System.out.println(entry.getKey() + " sensores activos: " + entry.getValue().sum() + " configuración(es)")
                    );

            return resultado;
        }

        // Métodos auxiliares (optimizados)
        private static long[] convertirBinario(long decimal, int longitud) {
            long[] binarios = new long[longitud];
            for (int i = longitud - 1; i >= 0; i--) {
                binarios[i] = decimal & 1;
                decimal >>= 1;
            }
            return binarios;
        }

        private static double CalcularPesoTotal(double[] Cl, double[] Ct, long[] Sl, long[] St) {
            double peso = 0;
            for (int i = 0; i < Sl.length; i++) peso += Sl[i] * Cl[i];
            for (int j = 0; j < St.length; j++) peso += St[j] * Ct[j];
            return peso;
        }

        private static boolean VerificarED(long[][] C, long[] Sl, long[] St) {
            int n = Sl.length;
            int m = St.length;

            // Verificar columnas no nulas
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

            // Verificar redundancia
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

