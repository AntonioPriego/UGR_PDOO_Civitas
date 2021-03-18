package civitas;

import java.util.ArrayList;


public class SorpresaEspeculador extends Sorpresa {
    CivitasJuego juego;

    //Contstructor    
    SorpresaEspeculador(CivitasJuego _juego)
    {
        this.texto = "Sorpresa convierte a Especulador";
        juego      = _juego;
    }
    
    //Aplica la sorpresa al jugador pasado como parámetro
    void aplicarAJugador(int actual, ArrayList<Jugador> todos)
    {
        if (jugadorCorrecto(actual,todos)  &&  !todos.get(actual).esEspeculador()) {
            informe(actual,todos);
            juego.jugadorAEspeculador(todos.get(actual));
        }
    }
}
