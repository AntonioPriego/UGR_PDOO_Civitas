package civitas;

import java.util.ArrayList;


public class CasillaSorpresa extends Casilla {

    MazoSorpresas mazo;
    Sorpresa sorpresa;

    //Constructor
    CasillaSorpresa(MazoSorpresas mazo, String nombre) {
        super(nombre);
        this.mazo = mazo;
    }

    //CasillaSorpresa recibe a jugador
    @Override
    void recibeJugador(int actual, ArrayList<Jugador> todos) {
        //  todos.get(actual).getPuedeComprar(false);
        if (super.jugadorCorrecto(actual, todos)) {
            this.sorpresa = mazo.siguiente();
            this.informe(actual, todos);

            Sorpresa sorpresa = mazo.siguiente();
            System.out.println(sorpresa.toString() + " es el tipo de la sorpresa");
            sorpresa.aplicarAJugador(actual, todos);

        }
    }

    //toString
    @Override
    public String toString() {
        return super.toString()
                + "\n\tSorpresa = " + (sorpresa == null ? "null" : sorpresa.toString()
                        + "\n\tMazo = " + (mazo == null ? "null" : mazo.toString()));
    }
}
