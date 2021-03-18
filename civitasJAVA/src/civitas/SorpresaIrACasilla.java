package civitas;

import java.util.ArrayList;


public class SorpresaIrACasilla extends Sorpresa{
    
    private String texto;
    private int valor;
    MazoSorpresas mazo;
    Tablero tablero;

    //Constructor
    SorpresaIrACasilla(Tablero tablero,int valor, String texto)
    {
        this.valor = valor;
        this.tablero = tablero;
        this.texto = "Vuelve al principio(Casilla siguiente a salida)";
        this.mazo = new MazoSorpresas();
    }

    //Aplicar sorpresa al jugador pasado como parámetro
    void aplicarAJugador(int actual, ArrayList<Jugador> todos)
    {
        if(this.jugadorCorrecto(actual, todos)){

            this.informe(actual, todos);
            int casillaActual = todos.get(actual).getNumCasillaActual();
            int tirada = tablero.calcularTirada(casillaActual, this.valor);

            int nuevaPosicion = tablero.nuevaPosicion(casillaActual, tirada);
            todos.get(actual).moverACasilla(nuevaPosicion);
            tablero.getCasilla(this.valor).recibeJugador(actual, todos);
        }        
    }

    //Devuelve si el jugador pasado como parámetro contiene irregularidades
    public boolean jugadorCorrecto(int actual, ArrayList<Jugador> todos)
    {
        return (actual < todos.size());
    }

    //Desactiva propiedad especial
    void salirDelMazo()
    {
        if(this.valor == 1)
            mazo.inhabilitarCartaEspecial(this);
    }

    //Activa propiedad especial
    void usada()
    {
        if(this.valor == 1)
            mazo.habilitarCartaEspecial(this);
    }

    //toString
    @Override
    public String toString()
    {
        return("Vuelve al principio(Casilla siguiente a salida)");
    }
}