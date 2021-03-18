package civitas;
import java.util.ArrayList;


public class Tablero {
    private int numCasillaCarcel;        //Posición de la casilla Carcel
    private ArrayList<Casilla> casillas; //Contenedor de casillas
    private int porSalida;               //Contador de veces que se ha pasado por Salida
    private Boolean tieneJuez;           //True si tablero dispone de casilla Juez
    
    Tablero(int _numCasillaCarcel)
    {
        numCasillaCarcel = (_numCasillaCarcel>1) ? _numCasillaCarcel : 1;
        
        casillas = new ArrayList<Casilla>();
        casillas.add(new Casilla("Salida"));
        
        porSalida = 0;
        
        tieneJuez = false;
    }
    
    //Devuelve True si el número de casillas es mayor que la posición de la casilla Carcel
    private Boolean correcto() { return (casillas.size()>numCasillaCarcel) ? true : false; }
    
    //Devuelve True si correcto() y hay más casillas que numCasillas
    private Boolean correcto(int numCasilla) 
    { 
        return (correcto() && casillas.size()>numCasilla); 
    }
    
    //Consultor numCasillaCarcel
    int getCarcel() { return numCasillaCarcel; }
    
    //Consultor porSalida con acciones condicionadas
    int getPorSalida()
    {
        if (porSalida>0) {
            porSalida--;
            return porSalida+1;
        }
        else 
            return porSalida;
    }
    
    //Añade una casilla a casillas, además tambien añade Carcel de ser necesario
    void añadeCasilla(Casilla casilla) 
    {
        if (casillas.size()==numCasillaCarcel)
            añadeJuez();
        
        casillas.add(casilla);
        
        if (casillas.size()==numCasillaCarcel)
            añadeJuez();
    }
    
    //Añade Juez si no ha sido añadido aun
    void añadeJuez()
    {
        if (!tieneJuez) {
            casillas.add(new CasillaJuez(numCasillaCarcel, "Juez"));
             casillas.add(new CasillaJuez(this.numCasillaCarcel, "Juez"));
            tieneJuez = true;
        }
    }
    
    //Consultor casilla posición numCasilla
    Casilla getCasilla(int numCasilla)
    {
        if (correcto(numCasilla))
            return casillas.get(numCasilla);
        else {
            return null;
        }
    }
    
    //Devuelve la posición que resulta de tirada
    int nuevaPosicion(int actual, int tirada)
    {
        if (correcto(actual)) {
            if (actual+tirada > casillas.size())
                porSalida += 1;
                return (actual+tirada)%casillas.size();
        }
        else
            return -1;
    }
    
    //Calcula el tamaño de la tirada para ir de origen a destino
    int calcularTirada(int origen, int destino)
    {
        if (destino-origen < 0)
            return (destino-origen)+casillas.size();
        else
            return destino-origen;
    }
    
}
