import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        int[][] C = {{-1, -1, 0, 0, 1}, {1, 0, 0, -1, 0}, {0, 1, -1, 0, 0}, {0, 1, 0, 1, -1}, {1, 0, 1, 0, -1}};
        int[] Sl = {1, 0, 0, 0, 0};
        int[] St = {1, 0, 1, 0, 1};
        double[] Cl = {1, 1, 1, 1, 1};
        double[] Ct = {1, 1, 1, 1, 1};
        int prueba = 10;
        //Imprimir matriz
        Proyector.ImprimirMatiz(C);
        System.out.println("--------------");
        //imprimimos los lugares
        int[][] Clu = Proyector.ProyectarLugares(C, Sl);
        Proyector.ImprimirMatiz(Clu);
        System.out.println("--------------");
        //Imprimimos las transiciones
        int[][] Ctr = Proyector.ProyectarTransiciones(Clu, St);
        //Proyector.ImprimirMatiz(Ct);
        System.out.println("---------------");
        System.out.println("Funcion para C_");
        //delaramos una variable para la C proyectada
        int[][] C_ = Proyector.ProyeccionC(C, Sl, St);
        Proyector.ImprimirMatiz(C_);
        boolean esCero = Proyector.ComprobarCero(C_);
        System.out.println(esCero);

        //Comparar Columnas
        //boolean ColumnasIguales = Proyector.ColumnasIguales(C_,0,1);
        //System.out.println(ColumnasIguales);

        boolean Redundancia = Proyector.ComprobarRedundancia(C_);
        System.out.println(Redundancia);
        if (Proyector.ED(C_)) {
            System.out.println("El sistema cumple con las condiciones");
        } else {
            System.out.println("El sistema NO cumple con las condiciones");

        }
        System.out.println(Proyector.VerificarED(C, Sl, St) ? "El sistema es ED" : "El sistema no es ED");
        System.out.println("Pesos sistema ED");
        System.out.println("--------");
        double PesoSt = Valuador.CalcularPesoTransicion(Ct, St);
        System.out.println("Peso de St: " + PesoSt);
        double PesoSl = Valuador.CalcularPesoLugar(Cl, Sl);
        System.out.println("Peso de Sl: " + PesoSl);
        double PesoTotal = Valuador.CalcularPesoTotal(Cl, Ct, Sl, St);
        System.out.println("Peso Total: " + PesoTotal);

        for (long num = 0; num < Math.pow(2,30); num++) {
            System.out.println("--------"+num+"---------");
            long [] binarios =Generador.convertirBinario(num,30);
            Proyector.ImprimirGranArreglo(binarios);
            long[][] S =Generador.DividirBinarios(binarios,15,15);
            System.out.println("------------------");
            Proyector.ImprimirGranMatriz(S);
        }

    }
}