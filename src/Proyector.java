import java.util.ArrayList;
import java.util.List;

public class Proyector {
    public static int [][] ProyectarLugares(int [][] C, int [] Sl) {
        List<int[]> lista = new ArrayList();
        for(int i=0; i<Sl.length; i++){
            if(Sl[i]==1){
                lista.add(C[i]);
            }
        }
        int[][] Cl = lista.toArray(new int[lista.size()][]);
        return Cl;

    }

    public static int [][] ProyectarTransiciones(int [][] C, int [] St){
        List<int[]> lista = new ArrayList();
        int columnas = C[0].length;
        int nuevasTransiciones = 0;
        //conteo de columnas
        for (int j=0; j<St.length; j++){
            if(St[j]==0){
                nuevasTransiciones++;
            }
        }
       for (int i=0; i<C.length; i++) {
           int[] nuevaFila = new int[nuevasTransiciones];
           int colIndex = 0;

           for (int j = 0; j < columnas; j++) {
               if (St[j] == 0) {
                   nuevaFila[colIndex] = C[i][j];
                   colIndex++;
               }
           }
           lista.add(nuevaFila);
       }
       int [][]resultado = lista.toArray(new int[lista.size()][]);

       return resultado;

    }


    public static void ImprimirMatiz(int [][] matriz){
        for(int i=0; i<matriz.length; i++){
            for(int j=0; j<matriz[i].length; j++){
                System.out.print(matriz[i][j]+" ");

            }
            System.out.println("");
        }
    }
}
