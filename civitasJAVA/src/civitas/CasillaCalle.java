
package civitas;
import java.util.ArrayList;


public class CasillaCalle extends Casilla {
    private TituloPropiedad titulo;
    
    //Constructor
    CasillaCalle(TituloPropiedad _titulo)
    {
        super(_titulo.getNombre());
        titulo = _titulo;
    }
    
    //CasillaCalle recibe a jugador
    @Override
    void recibeJugador(int actual, ArrayList<Jugador> todos){
        if(jugadorCorrecto(actual, todos))
        {
            informe(actual, todos);
            Jugador jugador = todos.get(actual);

            if (!titulo.tienePropietario()) {
                if(jugador.puedeComprarCasilla())
                    System.out.println("La casilla no está comprada: jugador puede comprar casilla");
            }
            else
                titulo.tramitarAlquiler(jugador);
        }
    }
    
    //toString
    @Override
    public String toString()
    {
        return super.toString()+ "\n\tTitulo propiedad = " + (titulo == null ? "null" : titulo.toString());
    }
    
    TituloPropiedad getTituloPropiedad() { return titulo; }
}