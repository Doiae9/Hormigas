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

    //Comprobar que ninguna columna tenga 0 unicamente
    public static boolean ComprobarCero(int [][] C_){
        if(C_==null) {
            //System.out.println("No existen proyecciones para mostrar");
            return false;
        }
        //Desglozamos los lugares
        int Lugares = C_.length;
        int Transiciones = C_[0].length;
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
    public static boolean ColumnasIguales(int [][] C_, int i, int j){
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
    public static boolean ComprobarRedundancia(int [][]C_){
        //Columnas
        int Transiciones =C_[0].length;
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
    public static boolean ED(int C_[][]){
       return !ComprobarCero(C_) && !ComprobarRedundancia(C_);


    }
    public static boolean VerificarED(int C[][],int [] Sl, int [] St){

        return  ED(ProyeccionC(C, Sl, St)) ? true : false;
    }


    //Imprimir Matriz
    public static void ImprimirMatiz(int [][] matriz){
        for(int i=0; i<matriz.length; i++){
            for(int j=0; j<matriz[i].length; j++){
               // System.out.print(matriz[i][j]+"\t");

            }
            System.out.println(" ");

        }
    }

    //Funcion de referencia
    public static int [][] ObtenerUltimaProyeccion(){
        return ultimaProyeccion;
    }
}
