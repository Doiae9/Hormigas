import java.util.ArrayList;
import java.util.List;

public class Proyector {
    //Variable auxiliar para guardar las proyecciones
    public static long[][] ultimaProyeccion;

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
        ultimaProyeccion= ProyectarTransiciones(lugares,St);
        return ultimaProyeccion;
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
    public static boolean VerificarED(long[][] C, long [] Sl, long [] St){
        long[][] proyeccion = ProyeccionC(C, Sl, St);
        if (proyeccion.length == 0 || proyeccion[0].length == 0) {
            // Evitar evaluaciones innecesarias si no hay datos
            return false;
        }
        return ED(proyeccion);
    }


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
    //Validación diferente antes de entrar al ciclo
//    public static boolean esObviamenteInvalido(long[] Sl, long[] St) {
//        // 🎯 Filtro 1: muy pocos sensores activos (ej. config trivial)
//        int activosL = 0;
//        for (long b : Sl) if (b == 1) activosL++;
//        if (activosL == 0) return true; // Ningún lugar activo = no interesante
//
//        // 🎯 Filtro 2: transiciones contradictorias (ajusta según tu modelo)
//        if (St.length >= 2 && St[0] == 1 && St[1] == 1) return true; // T0 y T1 no pueden estar activas juntas
//
//        // 🎯 Filtro 3: zonas muertas conocidas (puedes definir zonas inválidas)
//        // if (Sl[3] == 1 && Sl[7] == 1) return true;
//
//        return false; // Si nada inválido, continuar evaluación
//    }


}
