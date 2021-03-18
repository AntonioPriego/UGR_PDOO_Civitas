package civitas;

import java.util.ArrayList;

public class SorpresaJugador extends Sorpresa {

    private String texto;
    private int valor;
    MazoSorpresas mazo;
    Tablero tablero;

    //Constructor
    SorpresaJugador(Tablero tablero, int valor, String texto)
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

            SorpresaPagarCobrar pagar = new SorpresaPagarCobrar(this.tablero, this.valor*-1, "Esta sorpresa hace al jugador tener que pagar");

            int i = 0;
            while(i<todos.size()) {
                if(i != actual)
                    pagar.aplicarAJugador(i, todos);
                i++;
            }

            SorpresaPagarCobrar cobrar = new SorpresaPagarCobrar(this.tablero, this.valor*(todos.size()-1), "Esta sorpresa hace al jugador cobrar");
            cobrar.aplicarAJugador(actual, todos);
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
        String sorpresa = "Tipo: Sorpresa Jugador por Jugador\n";

        sorpresa += this.texto;

        return sorpresa;
    }

}