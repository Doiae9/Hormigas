public class Valuador {

    public static double CalcularPesoTransicion(double []Ct, long []St){
        double aux=0;
        for(int i=0; i<St.length; i++){
            if(St[i]==1)
                aux += Ct[i];

        }
        return aux;
    }
    public static double CalcularPesoLugar(double[] Cl, long []Sl){
        double aux=0;
        for(int i=0; i<Sl.length; i++) {
            if (Sl[i] == 1) {
                aux += Cl[i];
            }
        }
        return aux;
    }
    public static double CalcularPesoTotal(double[] Cl, double []Ct, long[] Sl, long[] St){
       return CalcularPesoLugar(Cl,Sl) + CalcularPesoTransicion(Ct,St);
    }

    //funcion eulistica para comprobar antes de calcular:
    public static double estimarPesoMinimo(long[] Sl, long[] St, double[] Cl, double[] Ct) {
        double peso = 0;
        for (int i = 0; i < Sl.length; i++) {
            if (Sl[i] == 1) peso += Cl[i];
        }
        for (int i = 0; i < St.length; i++) {
            if (St[i] == 1) peso += Ct[i];
        }
        return peso;
    }

}
