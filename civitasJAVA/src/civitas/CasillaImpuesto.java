package civitas;

import java.util.ArrayList;


public class CasillaImpuesto extends Casilla {
    private float importe;
    private String nombre;
    
    //Constructor
    CasillaImpuesto(float cantidad, String nombre)
    {
        super(nombre);
        importe = cantidad;
    }
    
    //CasillaImpuesto recibe a jugador
    @Override
    void recibeJugador(int actual, ArrayList<Jugador> todos){
        if (jugadorCorrecto(actual, todos) == true)
        {
            
            this.informe(actual, todos);
            todos.get(actual).pagaImpuesto(importe);
        }
    }
    
    //toString
    @Override
    public String toString()
    {
        return super.toString()+ (importe > 0 ? "\n\tImporte = " + importe : "");
    }
}