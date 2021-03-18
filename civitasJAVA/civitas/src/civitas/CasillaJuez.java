package civitas;

import java.util.ArrayList;


public class CasillaJuez extends Casilla {
    private String nombre;
    private int carcel;

    //Constructor
    CasillaJuez(int numCasillaCarcel, String nombre) {
        super(nombre);
        this.carcel = numCasillaCarcel;
    }

    //CasillaJuez recibe a jugador
    void recibeJugador(int actual, ArrayList<Jugador> todos) {
        if (this.jugadorCorrecto(actual, todos)) {
            this.informe(actual, todos);
            todos.get(actual).encarcelar(carcel);
        }
    }

    //toString
    @Override
    public String toString() {
        return super.toString()+
               (carcel>0 ? "\n\tValor carcel = " + carcel : "");
    }

}
