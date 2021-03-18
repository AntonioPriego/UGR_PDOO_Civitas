package civitas;
import java.util.Random;
import java.util.ArrayList;


public class Dado {
  
    private Random random = new Random();
    private int ultimoResultado;
    private Boolean debug;
    private static final Dado instance = new Dado();
    private static int SalidaCarcel = 5;
    

    //Constructor privado
    private Dado(){
        debug = false;
        ultimoResultado = 0;
        random = new Random();
    }
    
    //Implementación de clase Singleton
    static Dado getInstance() { return instance; }
    
    //Devuelve número aleatorio de tirada
    int tirar(){
        int resultado;
        if(debug == false)
            resultado = random.nextInt(6)+1;
        else 
            resultado = 1;

        ultimoResultado = resultado;
        return resultado;
    }
   
    //Determina aleatoriamente si se sale de la cárcel
    boolean salgoDeLaCarcel(){
        Boolean resultado = false;
        if(getUltimoResultado()==6)
            resultado = true;

        return resultado;
    }
       
    //Dertermina aleatoriamente quién empieza
    int quienEmpieza(int n){
        return  random.nextInt(n);
    }
    
    //Devuelve el último resultado que arrojó el dado
    int getUltimoResultado(){
        return ultimoResultado;
    }
   
    //Setter de modo debug
    public void setDebug(Boolean d) {
        debug = d;
        if(debug==true)
            Diario.getInstance().ocurreEvento("Dado debug true");
        else 
            Diario.getInstance().ocurreEvento("Dado debug false");
    }
   
}

    

