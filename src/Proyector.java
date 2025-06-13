import java.util.ArrayList;
import java.util.List;

public class Proyector {
    //Variable auxiliar para guardar las proyecciones
    public static int[][] ultimaProyeccion;
    public static int [][] ProyectarLugares(int [][] C, int [] Sl) {
        List<int[]> lista = new ArrayList<>();
        for(int i=0; i<Sl.length; i++){
            if(Sl[i]==1){
                lista.add(C[i]);
            }
        }
        int[][] Cl = lista.toArray(new int[lista.size()][]);
        return Cl;

    }

    public static int [][] ProyectarTransiciones(int [][] C, int [] St){
        List<int[]> lista = new ArrayList<>();
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
//Proyeccion de ´c o, quitar lugares y transiciones
    public static int [][] ProyeccionC(int [][]C, int [] Sl, int [] St){
        int [][] lugares=  ProyectarLugares(C,Sl);
        ultimaProyeccion= ProyectarTransiciones(lugares,St);
        return ultimaProyeccion;
    }

    public static boolean ComprobarCero(int [][] C_){
        if(C_==null) {
            System.out.println("No existen proyecciones para mostrar");
            return false;
        }
        //Desglozamos los lugares
        int Lugares = C_.length;
        int Transiciones = C_[0].length;

        for (int i=0; i<Transiciones; i++) {
            //Cada vez que cambie de columna el bool cambia
            boolean esCero = true;
            for (int j=0; j<Lugares; j++) {
                if(C_[i][j]!=0){
                    esCero = false;
                    break;
                }
            }
            if(esCero){
                System.out.println("El sistema no es viable");
                return false;
        }

        }
        System.out.println("El sistema sigue siendo viable");
        return true;
    }


    public static void ImprimirMatiz(int [][] matriz){
        for(int i=0; i<matriz.length; i++){
            for(int j=0; j<matriz[i].length; j++){
                System.out.print(matriz[i][j]+" ");

            }
            System.out.println(" ");

        }
    }

    //Funcion de referencia
    public static int [][] ObtenerUltimaProyeccion(){
        return ultimaProyeccion;
    }
}
