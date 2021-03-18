package civitas;

import java.util.ArrayList;

public class Casilla {

    private String nombre;

    //Constructor DESCANSO
    Casilla(String _nombre) {
        nombre = _nombre;
    }

    //toString de Casilla
    @Override
    public String toString() {
        return "Casilla " + nombre + ":";

    }

    //Get de nombre
    public String getNombre() {
        return nombre;
    }

    //Devuelve si es un índice válido para acceder a la lista de jugadores
    public boolean jugadorCorrecto(int actual, ArrayList<Jugador> todos) {
        return todos.size() > actual;
    }

    //Registra en diario que jugador ha caido en casilla
    void informe(int actual, ArrayList<Jugador> todos) {
        if (jugadorCorrecto(actual, todos)) {
            Diario.getInstance().ocurreEvento("Jugador " + todos.get(actual).getNombre() + " ha caido en casilla " + nombre);
        }
    }

    //RecibeJugador por defecto:DESCANSO
    void recibeJugador(int actual, ArrayList<Jugador> todos) {
        informe(actual, todos);
    }
}
