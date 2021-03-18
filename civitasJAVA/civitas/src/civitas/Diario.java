package civitas;
import java.util.ArrayList;


public class Diario {
    static final private Diario instance = new Diario();
    private ArrayList<String> eventos;

    //Implementación de clase Singleton
    static public Diario getInstance() {
        return instance;
    }

    //Constructor
    private Diario () {
        eventos = new ArrayList<>();
    }

    //Aniade evento al diario
    void ocurreEvento (String e) {
        eventos.add (e);
    }

    //Devuelve si hay eventos sin leer
    public boolean eventosPendientes () {
        return !eventos.isEmpty();
    }

    //Devuelve el siguiente evento sin leer
    public String leerEvento () {
        String salida="";
        
        if (!eventos.isEmpty()) {
             salida = eventos.remove(0);
        }
        
        return "[DIARIO]"+salida+"\n";
    }
}
