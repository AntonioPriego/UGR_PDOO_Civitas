package civitas;

import civitas.GestionesInmobiliarias;


public class OperacionInmobiliaria {
    GestionesInmobiliarias gestion;
    int numPropiedad;
    
    //Constructor por defecto
    public OperacionInmobiliaria()
    {
        gestion      = GestionesInmobiliarias.TERMINAR;
        numPropiedad = -1;        
    }

    //Constructor
    public OperacionInmobiliaria(GestionesInmobiliarias _gestion, int _numPropiedad) {
        gestion      = _gestion;
        numPropiedad = _numPropiedad;
    }
    
    //Get de gestion
    public GestionesInmobiliarias getGestion() { return gestion; }
    
    //Get de indice_propiedad
    public int getIndice() { return numPropiedad; }
}
