
import java.util.Arrays;
public class Generador {
    //Colocamos un uso de longitud (si el número generado no alcanza este número se rellena con 0
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
}
