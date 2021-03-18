package civitas;

import java.util.ArrayList;


public class SorpresaSalirCarcel extends Sorpresa {

    MazoSorpresas mazo;

    //Constructor
    SorpresaSalirCarcel(MazoSorpresas mazo, String texto)
    {
        this.mazo = new MazoSorpresas();
        this.texto = "Esta sorpresa te lleva a la cárcel";

    }

    //Aplicar sorpresa a jugador pasado como parámetro
    void aplicarAJugador(int actual, ArrayList<Jugador> todos)
    {
        if (this.jugadorCorrecto(actual, todos)) {

            this.informe(actual, todos);
            todos.get(actual).obtenerSalvoconducto(new SorpresaSalvoconducto(mazo));
            salirDelMazo();
        }
    }

    //Devuelve si el jugador contiene irregularidades
    public boolean jugadorCorrecto(int actual, ArrayList<Jugador> todos)
    {
        return (actual < todos.size());
    }

    //Deshabilita propiedad especial
    void salirDelMazo()
    {
        mazo.inhabilitarCartaEspecial(this);
    }

    //Habilita propiedad especial
    void usada()
    {
        mazo.habilitarCartaEspecial(this);
    }

    //toString
    @Override
    public String toString() {
        return ("Salir Cárcel");
    }

}
