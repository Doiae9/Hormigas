public class Valuador {

    public static double CalcularPesoTransicion(double []Ct, long []St){
        int aux=0;
        for(int i=0; i<St.length; i++){
                aux += Ct[i] * St[i];

        }
        return aux;
    }
    public static double CalcularPesoLugar(double[] Cl, long []Sl){
        int aux=0;
        for(int i=0; i<Sl.length; i++){
                aux += Cl[i] * Sl[i];
        }
        return aux;
    }
    public static double CalcularPesoTotal(double[] Cl, double []Ct, long[] Sl, long[] St){
        double total=0.0;
        total = CalcularPesoLugar(Cl,Sl) + CalcularPesoTransicion(Ct,St);
        return total;
    }

}
