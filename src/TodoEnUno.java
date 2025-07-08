import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;

public class TodoEnUno {
    static class Worker extends RecursiveTask<List<long[][]>> {
        private static final long UMBRAL = 20_000_000L;

        private final long start, end;
        private final int n, m;
        private final long[][] C;
        private final double[] Cl, Ct;
        private final AtomicReference<Double> pesoMinimoGlobal;
        private final AtomicLong progresoGlobal;
        private final LongProgressBar barra;
        private final Map<Integer, LongAdder> frecuenciaSensoresActivosGlobal;  // Cambiado a LongAdder

        public Worker(long start, long end, int n, int m, long[][] C, double[] Cl, double[] Ct,
                      AtomicReference<Double> pesoMinimoGlobal,
                      AtomicLong progresoGlobal, LongProgressBar barra,
                      Map<Integer, LongAdder> frecuenciaSensoresActivosGlobal) {
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
            long counter = 0; // Contador para progreso

            for (long num = start; num < end; num++) {
                long[][] S = ConvertirYdividirBinarios(num, n, m);
                long[] Sl = S[0];
                long[] St = S[1];

                // VERIFICACIÓN DIRECTA SIN PROYECCIONES
                if (VerificarED(C, Sl, St)) {
                    double pesoActual = CalcularPesoTotal(Cl, Ct, Sl, St);

                    if (pesoActual < pesoMinLocal) {
                        pesoMinLocal = pesoActual;
                        locales.clear();
                        locales.add(S);
                    } else if (pesoActual == pesoMinLocal) {
                        locales.add(S);
                    }

                    // Actualizar frecuencias con LongAdder
                    int activos = Long.bitCount(
                            Arrays.stream(Sl).reduce(0, (a, b) -> (a << 1) | (b & 1))
                    );
                    frecuenciaSensoresActivosGlobal
                            .computeIfAbsent(activos, k -> new LongAdder())
                            .increment();
                }

                // Optimización: Contador simple sin módulo
                counter++;
                if (counter == 20_000_000) {
                    progresoGlobal.addAndGet(counter);
                    barra.update(progresoGlobal.get());
                    counter = 0;
                }
            }

            // Actualizar resto del progreso
            if (counter > 0) {
                progresoGlobal.addAndGet(counter);
                barra.update(progresoGlobal.get());
            }

            // Sincronizar peso mínimo global
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

    public static List<long[][]> BuscarEDMultihullConProgreso(long[][] C, double[] Cl, double[] Ct, int numHilos) throws InterruptedException, ExecutionException {
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
        frecuenciaSensoresActivosGlobal.forEach((activos, count) ->
                System.out.println(activos + " sensores activos: " + count.longValue() + " configuración(es)")
        );

        return resultado;
    }

    //=================================================================================================
    //Generador
    public static long[] convertirBinario(long decimal, int longitud) {
        long[] binarios = new long[longitud];
        for (int i = longitud - 1; i >= 0; i--) {
            binarios[i] = decimal & 1;  // Guarda el bit menos significativo
            decimal >>= 1;              // Desplaza a la derecha para el siguiente bit
        }
        return binarios;
    }

    public static long[][] DividirBinarios (long [] binarios, int n, int m){
//        int total = binarios.length;
        //Mitad, desglozada

        long [] Sl = new long[n];
        long [] St = new long[m];


//        System.arraycopy(binarios, 0, Sl, 0, n);
//        System.arraycopy(binarios, n, St, 0, m);
        System.arraycopy(binarios, 0, St, 0, m);
        System.arraycopy(binarios, m, Sl, 0, n);

        return new long [][] {Sl,
                              St};
    }

    public static long[][] ConvertirYdividirBinarios(long decimal, int n, int m){
        long[] binarios = convertirBinario(decimal,n+m);
        return DividirBinarios(binarios,n,m);
    }
    //===============================================================================================
    //Calcular peso
    public static double CalcularPesoGeneral(double [] Costos, long [] Sensores){
        double peso=0;
        for(int i=0; i<Sensores.length; i++){
            peso +=Sensores[i]*Costos[i];
        }
        return peso;
    }
    public static double CalcularPesoTotal(double[] Cl, double []Ct, long[] Sl, long[] St){
        return CalcularPesoGeneral(Cl,Sl) + CalcularPesoGeneral(Ct,St);
    }
    //Proyeccion
    //===================================================================================
    public static long[][] ProyectarLugares(long[][] C, long[] Sl) {
        int conteo = 0;
        for (long b : Sl) if (b == 1) conteo++;

        long[][] resultado = new long[conteo][];
        int idx = 0;

        for (int i = 0; i < Sl.length; i++) {
            if (Sl[i] == 1) {
                resultado[idx++] = C[i];
            }
        }

        return resultado;
    }
    //optimizado
    public static long[][] ProyectarTransiciones(long[][] C, long[] St) {
        // Validación de entrada
        if (C == null || St == null || C.length == 0 || C[0].length != St.length) {
            return new long[0][0];
        }

        final int filas = C.length;
        final int columnas = C[0].length;

        // Contar columnas a conservar (St[j] == 0)
        int nuevasTransiciones = 0;
        for (long t : St) {
            nuevasTransiciones += (t == 0) ? 1 : 0;
        }

        if (nuevasTransiciones == 0) {
            return new long[0][0];
        }

        long[][] resultado = new long[filas][nuevasTransiciones];

        // Precalcular índices de columnas a conservar
        int[] columnasConservar = new int[nuevasTransiciones];
        for (int j = 0, k = 0; j < columnas; j++) {
            if (St[j] == 0) {
                columnasConservar[k++] = j;
            }
        }

        // Llenar la matriz resultado
        for (int i = 0; i < filas; i++) {
            for (int k = 0; k < nuevasTransiciones; k++) {
                resultado[i][k] = C[i][columnasConservar[k]];
            }
        }

        return resultado;
    }

    //Proyeccion de ´c o, quitar lugares y transiciones
    public static long [][] ProyeccionC(long[][] C, long[] Sl, long[] St){
        long [][] lugares=  ProyectarLugares(C,Sl);
        if (lugares.length == 0 || lugares[0].length == 0) {
            return new long[0][0];
        }
        return ProyectarTransiciones(lugares,St);
    }

    //Comprobar que ninguna columna tenga 0 unicamente
    //No se puede optimizar más
    public static boolean ComprobarCero(long[][] C_){
        if(C_==null) {
            //System.out.println("No existen proyecciones para mostrar");
            return false;
        }
        //Desglozamos los lugares
        final int Lugares = C_.length;
        final  int Transiciones = C_[0].length;
        //[filas][columnas]
        for (int i=0; i<Transiciones; i++) {
            //Cada vez que cambie de columna el bool cambia
            boolean esCero = true;
            for (int j=0; j<Lugares; j++) {
                //Se comprueba que exista por lo menos un valor diferente a 0
                if(C_[j][i]!=0){
                    esCero = false;
                    break;
                }
            }
            if(esCero){
                // System.out.println("El sistema no es viable");
                return true;
            }
        }
        // System.out.println("El sistema sigue siendo viable");
        return false;
    }
    //Funcion auxiliar para comparar columnas -> Recibe la matriz, Recibe una columna,
    //Recibe otra columna a comparar
    //No se puede optimizar mas en la configuración actual
    public static boolean ColumnasIguales(long[][] C_, int i, int j){
        int filas = C_.length;
        for (int k=0; k<filas; k++){
            //Compara que exista una diferencia
            if(C_[k][i]!=C_[k][j]) {
                //System.out.println("Comparando columna..." + i + " con: " + j);
                return false;
            }
        }
        return true;
    }

    //Comprueba la redundancia de las columnas
    //Recibe la C Proyectada
    public static boolean ComprobarRedundancia(long[][] C_){
        //Columnas
        long Transiciones =C_[0].length;
        for (int i = 0; i < Transiciones-1; i++) {
            //Comparamos la siguiente fila
            for (int j =i + 1; j<Transiciones; j++){
                if(ColumnasIguales(C_,i,j)){
                    //Si las columnas son iguales...
                    return true;
                }
            }
        }
        return false;
    }
    //Comprobamos si existe un 0 o alguna redundancia
    public static boolean ED(long[][] C_){
        return !ComprobarCero(C_) && !ComprobarRedundancia(C_);

    }
    //Verificamos que sea ED
    // Nueva implementación de VerificarED sin proyecciones
    public static boolean VerificarED(long[][] C, long[] Sl, long[] St) {
        int n = Sl.length;
        int m = St.length;

        // 1. Verificar columnas nulas
        for (int j = 0; j < m; j++) {
            if (St[j] == 0) {  // Columna activa
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

        // 2. Verificar columnas redundantes
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

    // Inicializar mapa de frecuencias con LongAdder
    Map<Integer, LongAdder> frecuenciaSensoresActivosGlobal = new ConcurrentHashMap<>();


    //Imprimir Matriz
    public static void ImprimirMatiz(int [][] matriz){
        for(int i=0; i<matriz.length; i++){
            for(int j=0; j<matriz[i].length; j++){
                System.out.print(matriz[i][j]+"\t");

            }
            System.out.println(" ");

        }
    }
    public static void ImprimirGranArreglo(long [] arreglo){
        for(int i=0; i<arreglo.length; i++){
            System.out.print(arreglo[i]+"\t");
        }
        System.out.println(" ");
    }
    public static void ImprimirGranMatriz(long [][] matriz){
        for(int i=0; i<matriz.length; i++){
            for(int j=0; j<matriz[i].length; j++){
                System.out.print(matriz[i][j]+"\t");
            }
            System.out.println(" ");
        }
    }
}
