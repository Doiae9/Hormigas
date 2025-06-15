import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        int [][] C ={{-1,-1,0,0,1},{1,0,0,-1,0},{0,1,-1,0,0},{0,1,0,1,-1},{1,0,1,0,-1}};
        int [] Sl ={1,0,0,0,0};
        int [] St ={0,0,0,0,0};
        Proyector.ImprimirMatiz(C);
        System.out.println("--------------");
        int [][] Cl =Proyector.ProyectarLugares(C,Sl);
        Proyector.ImprimirMatiz(Cl);
        System.out.println("--------------");
        int [][] Ct =Proyector.ProyectarTransiciones(Cl,St);
        Proyector.ImprimirMatiz(Ct);
        System.out.println("---------------");
        System.out.println("Funcion para C_");
        //delaramos una variable para la C proyectada
        int [][]C_=Proyector.ProyeccionC(C,Sl,St);
        Proyector.ImprimirMatiz(C_);
        boolean esCero = Proyector.ComprobarCero(C_);
        System.out.println(esCero);

        //Comparar Columnas
        boolean ColumnasIguales = Proyector.ColumnasIguales(C_,0,1);
        System.out.println(ColumnasIguales);

        boolean Redundancia= Proyector.ComprobarRedundancia(C_);
        System.out.println(Redundancia);
        if(Proyector.ED(C_)){
            System.out.println("El sistema cumple con las condiciones");
        }else{
            System.out.println("El sistema NO cumple con las condiciones");

        }
        System.out.println(Proyector.VerificarED(C,Sl,St)?"El sistema es ED":"El sistema no es ED");

    }
}