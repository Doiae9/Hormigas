public class Valuador {

    public static double CalcularPesoTransicion(double []Ct, int []St){
        int aux=0;
        for(int i=0; i<St.length; i++){
                aux += Ct[i] * St[i];

        }
        return aux;
    }
    public static double CalcularPesoLugar(double[] Cl, int []Sl){
        int aux=0;
        for(int i=0; i<Sl.length; i++){
                aux += Cl[i] * Sl[i];
        }
        return aux;
    }
    public static double CalcularPesoTotal(double[] Cl, double []Ct, int[] Sl, int[] St){
        double total=0.0;
        total = CalcularPesoLugar(Cl,Sl) + CalcularPesoTransicion(Ct,St);
        return total;
    }

}
