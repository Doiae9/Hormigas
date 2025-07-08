package Pruebas;

import java.util.Arrays;

public class Configuracion implements Cloneable {
   public long [] Sl;
   public long [] St;

    public Configuracion(long [] Sl, long [] St){
        this.Sl = Sl;
        this.St = St;
    }

    public Configuracion(int n, int m){
         Sl = new long[n];
         St = new long[m];
    }
    @Override
    public Configuracion clone(){
        Configuracion copia =  new Configuracion(Sl.length,St.length);
        copia.Sl= Arrays.copyOf(Sl,Sl.length);
        copia.St= Arrays.copyOf(St,St.length);
        return copia;
    }

}
