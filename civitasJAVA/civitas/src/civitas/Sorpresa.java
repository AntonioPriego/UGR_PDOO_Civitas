package civitas;
import java.util.ArrayList;

public abstract class Sorpresa {
    String texto;
    
    //Devuelve si es un índice válido para acceder a la lista de jugadores
    public boolean jugadorCorrecto(int actual, ArrayList<Jugador> todos)
    {
        return todos.size()>actual;
    }
    
    //Indica en el diario que se está aplicando una sorpresa a un jugador
    public void informe(int actual, ArrayList<Jugador> todos)
    {
        if (jugadorCorrecto(actual, todos))
            Diario.getInstance().ocurreEvento("Aplicando sorpresa("+toString()+") a "
                                               +todos.get(actual).getNombre());
    }

    //Aplica la sorpresa al jugador pasado como parámetro
    void aplicarAJugador(int actual, ArrayList<Jugador> todos)
    {
        if (this.jugadorCorrecto(actual, todos)) {
            this.informe(actual, todos);
        }
    }
}
