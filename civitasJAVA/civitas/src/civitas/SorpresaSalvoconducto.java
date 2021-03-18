package civitas;

import java.util.ArrayList;


public class SorpresaSalvoconducto extends Sorpresa {

    private String texto;
    private int valor;
    MazoSorpresas mazo;

    //Constructor
    SorpresaSalvoconducto(MazoSorpresas mazo)
    {
        this.mazo = mazo;
        this.texto = "Esta sorpresa evita que caigas en la cárcel";
    }

    //Aplicar sopresa a jugador pasado como parámetro
    void aplicarAJugador(int actual, ArrayList<Jugador> todos)
    {
        if(this.jugadorCorrecto(actual, todos)){

            this.informe(actual, todos);
            int i = 0;
            boolean tiene = false;

            while(i<todos.size()){

                tiene = todos.get(i).tieneSalvoconducto();
                i++;
            }

            if(!tiene){

                todos.get(actual).obtenerSalvoconducto(this);
                this.salirDelMazo();
            }
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
        String sorpresa = "Tipo: Sorpresa Salvoconducto";

        sorpresa = sorpresa+"("+this.texto+")";

        return sorpresa;
    }
}