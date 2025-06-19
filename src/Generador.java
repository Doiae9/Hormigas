
import java.util.Arrays;
public class Generador {
    //Colocamos un uso de longitud (si el número generado no alcanza este número se rellena con 0
    public static long[] convertirBinario(long decimal, int longitud) {
        // Casos especiales
//        if (decimal == 0) return new long[]{0};
//        if (decimal == 1) return new long[]{1};
        String temp= Long.toBinaryString(decimal);
        // Creamos el array de resultado
        long[] binarios =  new long[longitud];
        for (int i = longitud-temp.length(), k=0; i < longitud; i++, k++) {
            // Convertir cada carácter '0' o '1' a long 0 o 1
            binarios[i] = Long.parseLong(String.valueOf(temp.charAt(k)));
        }

        return binarios;
    }
    public static long[][] DividirBinarios (long [] binarios, int n, int m){
//        int total = binarios.length;
        //Mitad, desglozada

        long [] Sl = new long[n];
        long [] St = new long[m];


        System.arraycopy(binarios, 0, Sl, 0, n);
        System.arraycopy(binarios, n, St, 0, m);

        return new long [][] {Sl,
                              St};
    }

    public static long[][] ConvertirYdividirBinarios(long decimal, int m, int n){
        long[] binarios = convertirBinario(decimal,n+m);
        return DividirBinarios(binarios,n,m);
    }
}
