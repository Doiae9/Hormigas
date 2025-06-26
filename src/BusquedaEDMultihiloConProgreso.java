import java.util.concurrent.Future;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class BusquedaEDMultihiloConProgreso {
    static class Worker implements Callable<Void> {
        private final long start;
        private final long end;
        private final int n, m;
        private final long[][] C;
        private final double[] Cl, Ct;
        private final AtomicReference<Double> pesoMinimoGlobal;
        private final List<long[][]> mejoresConfigsGlobal;
        private final AtomicLong progresoGlobal;
        private final long total;
        private final LongProgressBar barra;
        private final Map<Integer, AtomicInteger> frecuenciaSensoresActivosGlobal;

        public Worker(long start, long end, int n, int m, long[][] C, double[] Cl, double[] Ct,
                      AtomicReference<Double> pesoMinimoGlobal, List<long[][]> mejoresConfigsGlobal,
                      AtomicLong progresoGlobal, long total, LongProgressBar barra,
                      Map<Integer, AtomicInteger> frecuenciaSensoresActivosGlobal) {
            this.start = start;
            this.end = end;
            this.n = n;
            this.m = m;
            this.C = C;
            this.Cl = Cl;
            this.Ct = Ct;
            this.pesoMinimoGlobal = pesoMinimoGlobal;
            this.mejoresConfigsGlobal = mejoresConfigsGlobal;
            this.progresoGlobal = progresoGlobal;
            this.total = total;
            this.barra = barra;
            this.frecuenciaSensoresActivosGlobal = frecuenciaSensoresActivosGlobal;
        }

        @Override
        public Void call() {
            double pesoMinLocal = Double.MAX_VALUE;
            List<long[][]> locales = new ArrayList<>();

            for (long num = start; num < end; num++) {
                long[][] S = Generador.ConvertirYdividirBinarios(num, n, m);
                long[] Sl = S[0];
                long[] St = S[1];

//                if(Proyector.esObviamenteInvalido(Sl,St)){ continue;}
//                double estimado = Valuador.estimarPesoMinimo(Sl, St, Cl, Ct);
//                if (estimado >= pesoMinimoGlobal.get()) continue;
                if (Proyector.VerificarED(C, Sl, St)) {
                    double pesoActual = Valuador.CalcularPesoTotal(Cl, Ct, Sl, St);

                    if (pesoActual < pesoMinLocal) {
                        pesoMinLocal = pesoActual;
                        locales.clear();
                        locales.add(S);
                    } else if (pesoActual == pesoMinLocal) {
                        locales.add(S);
                    }

                    // Actualizar frecuencia de sensores activos de manera segura
                    int activos = 0;
                    for (long bit : Sl) if (bit == 1) activos++;
                    frecuenciaSensoresActivosGlobal.computeIfAbsent(activos, k -> new AtomicInteger(0)).incrementAndGet();
                }

                // Actualizar progreso global y la barra cada 1 millón para no saturar consola
                if (num % 1_000_000 == 0) {
                    long progresoActual = progresoGlobal.addAndGet(1_000_000);
                    barra.update(progresoActual);

                    // Aquí podrías añadir lógica para imprimir peso mínimo global si quieres
                }
            }

            // Sincronizar actualización del peso mínimo global y lista de mejores configuraciones
            synchronized (mejoresConfigsGlobal) {
                if (pesoMinLocal < pesoMinimoGlobal.get()) {
                    pesoMinimoGlobal.set(pesoMinLocal);
                    mejoresConfigsGlobal.clear();
                    mejoresConfigsGlobal.addAll(locales);
                } else if (pesoMinLocal == pesoMinimoGlobal.get()) {
                    mejoresConfigsGlobal.addAll(locales);
                }
            }

            return null;
        }
    }

    public static List<long[][]> BuscarEDMultihiloConProgreso(long[][] C, double[] Cl, double[] Ct, int numHilos) throws InterruptedException, ExecutionException {
        int n = C.length;
        int m = C[0].length;
        long total = 1L << (n + m);

        ExecutorService executor = Executors.newFixedThreadPool(numHilos);

        AtomicReference<Double> pesoMinimoGlobal = new AtomicReference<>(Double.MAX_VALUE);
        List<long[][]> mejoresConfigsGlobal = Collections.synchronizedList(new ArrayList<>());
        AtomicLong progresoGlobal = new AtomicLong(0);
        Map<Integer, AtomicInteger> frecuenciaSensoresActivosGlobal = new ConcurrentHashMap<>();
        LongProgressBar barra = new LongProgressBar(total, 40);
        barra.start();

        long rango = total / numHilos;
        List<Future<Void>> futuros = new ArrayList<>();

        for (int i = 0; i < numHilos; i++) {
            long inicio = i * rango;
            long fin = (i == numHilos - 1) ? total : inicio + rango;

            Worker worker = new Worker(inicio, fin, n, m, C, Cl, Ct, pesoMinimoGlobal, mejoresConfigsGlobal,
                    progresoGlobal, total, barra, frecuenciaSensoresActivosGlobal);

            futuros.add(executor.submit(worker));
        }

        for (Future<Void> f : futuros) f.get();
        executor.shutdown();

        barra.update(total); // Mostrar 100%
        System.out.println("\nPeso mínimo encontrado: " + pesoMinimoGlobal.get());

        System.out.println("\nFrecuencia de sensores activos:");
        frecuenciaSensoresActivosGlobal.forEach((activos, count) ->
                System.out.println(activos + " sensores activos: " + count.get() + " configuración(es)")
        );

        return mejoresConfigsGlobal;
    }

}
