package civitas;

import java.util.ArrayList;


public class SorpresaPagarCobrar extends Sorpresa{

    private String texto;
    private int valor;
    MazoSorpresas mazo;
    Tablero tablero;

    //Constructor
    SorpresaPagarCobrar(Tablero tablero, int valor, String texto)
    {
        this.tablero = tablero;
        this.texto = texto;
        this.valor = valor;
        this.mazo = new MazoSorpresas();
    }

    //Aplicar sorpresa al jugador pasado como parámetro
    void aplicarAJugador(int actual, ArrayList<Jugador> todos)
    {
        if(this.jugadorCorrecto(actual, todos)){

            this.informe(actual, todos);
            todos.get(actual).modificarSaldo(this.valor);
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
        if(this.valor == 1)
            mazo.inhabilitarCartaEspecial(this);
    }

    //Habilita propiedad especial
    void usada()
    {
        if(this.valor == 1)
            mazo.habilitarCartaEspecial(this);
    }

    //toString
    @Override
    public String toString()
    {
        String sorpresa = "Tipo: Sorpresa Pagar Cobrar\n";

        sorpresa += this.texto;

        return sorpresa;
    }

}