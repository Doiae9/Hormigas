package Pruebas;

import java.util.Arrays;

public class Configuracion implements Cloneable {
   public int[] Sl;
   public int [] St;

    public Configuracion(int [] Sl, int [] St){
        this.Sl = Sl;
        this.St = St;
    }

    public Configuracion(int n, int m){
         Sl = new int[n];
         St = new int[m];
    }
    @Override
    public Configuracion clone(){
        Configuracion copia =  new Configuracion(Sl.length,St.length);
        copia.Sl= Arrays.copyOf(Sl,Sl.length);
        copia.St= Arrays.copyOf(St,St.length);
        return copia;
    }

}
