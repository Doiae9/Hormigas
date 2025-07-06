package Pruebas;

public class Configuracion {
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

    public Configuracion clone(){
        return new Configuracion(Sl.clone(),St.clone());
    }

}
