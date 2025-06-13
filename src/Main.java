import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        int [][] C ={{-1,-1,0,0,1},{1,0,0,-1,0},{0,1,-1,0,0},{0,1,0,1,-1},{1,0,1,0,-1}};
        int [] Sl ={1,0,1,0,1};
        int [] St ={0,1,0,1,0};
        Proyector.ImprimirMatiz(C);
        System.out.println("--------------");
        int [][] Cl =Proyector.ProyectarLugares(C,Sl);
        Proyector.ImprimirMatiz(Cl);
        System.out.println("--------------");
        int [][] Ct =Proyector.ProyectarTransiciones(Cl,St);
        Proyector.ImprimirMatiz(Ct);
        System.out.println("---------------");
        System.out.println("Funcion para C-");
        int [][]C_=Proyector.ProyeccionC(C,Sl,St);
        Proyector.ImprimirMatiz(C_);
        boolean esCero = Proyector.ComprobarCero(C_);
    }

}