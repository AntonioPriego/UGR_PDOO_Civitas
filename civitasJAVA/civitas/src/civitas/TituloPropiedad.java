package civitas;


public class TituloPropiedad {
    private String nombre;
    private float alquilerBase;
    private static float factorInteresesHipoteca=1.1f;
    private float factorRevalorizacion;
    private float hipotecaBase;
    private boolean hipotecado;
    private int numCasas;
    private int numHoteles;
    private float precioCompra;
    private float precioEdificar;
    private Jugador propietario;
    
    //Constructor
    TituloPropiedad (String _nombre, float _alquilerBase, float _factorRevalorizacion,
                     float _hipotecaBase, float _precioCompra, float _precioEdificar)
    {
        nombre               = _nombre;
        alquilerBase         = _alquilerBase;
        factorRevalorizacion = _factorRevalorizacion;
        hipotecaBase         = _hipotecaBase;
        precioCompra         = _precioCompra;
        precioEdificar       = _precioEdificar;
        numCasas    = 0;
        numHoteles  = 0;
        propietario = null;
        hipotecado  = false;
    }
    
    //Devuelve cadena con toda la info del titulo de propiedad
    public String toString()
    {
        return "TituloPropiedad "             + nombre + ":"
             + "\n\tAlquilerBase = "            + alquilerBase
             + "\n\tFactorInteresesHipoteca = " + factorInteresesHipoteca
             + "\n\tFactorRevalorizacion = "    + factorRevalorizacion
             + "\n\tHipotecaBase = "            + hipotecaBase
             + "\n\tNumCasas = "                + numCasas
             + "\n\tNumHoteles = "              + numHoteles
             + "\n\tPrecioCompra = "            + precioCompra
             + "\n\tPrecioEdificar = "          + precioEdificar
             + "\n\tJugador = "                 + (propietario==null ? "No hay" : propietario.getNombre());
    }
    
    //Devuelve el cálculo del precio del alquiler según las reglas del juego
    public float getPrecioAlquiler()
    {
        if (hipotecado  ||  propietarioEncarcelado() )
            return 0;
        else
            return alquilerBase*(1+numCasas*0.5f)+(numHoteles*2.5f);
    }
    
    //Devuelve el importe que se obtiene al cancelar la hipoteca
    public float getImporteCancelarHipoteca()
    {
        return (hipotecaBase*(1+(numCasas*0.5f)+(numHoteles*2.5f)))*factorInteresesHipoteca;
    }

    //Cancela la hipoteca para el jugador indicado
    boolean cancelarHipoteca (Jugador jugador)
    {  
        if (hipotecado && esEsteElPropietario(jugador)) {
            jugador.paga(getImporteCancelarHipoteca());
            hipotecado = false;
            
            return  true;
        }
        
       return false;         
    }

    //Hipoteca la propiedad para jugador
    boolean hipotecar(Jugador jugador)
    {   
        if (!hipotecado && esEsteElPropietario(jugador)) {
            jugador.recibe(getImporteHipoteca());
            hipotecado = true;
            
            return true;
        }

        return false;
    }
    
    //Tramita el alquiler del Jugador jugador. Pago y recibo
    void tramitarAlquiler(Jugador jugador)
    {        
        if (propietario != null)
            if (jugador.getNombre() != propietario.getNombre()) {
                jugador.pagaAlquiler(getPrecioAlquiler());
                propietario.recibe(getPrecioAlquiler());
            }
    }
    
    //Devuelve true si el propietario está encarcelado
    private boolean propietarioEncarcelado() { return propietario.isEncarcelado(); }
    
    //Devuelve la cantidad de casas y hoteles
    int cantidadCasasHoteles() { return numCasas+numHoteles; }
    
    //Devuelve el precio de venta según las relgas del juego
    private float getPrecioVenta()
    {
        return (precioEdificar*numCasas+precioEdificar*4*numHoteles)*factorRevalorizacion;
    }
    
    //Se derrue la casa de ser posible. Se devuelve si ha sido posible
    boolean derruirCasas(int n, Jugador jugador)
    {
        if (jugador.getNombre()==propietario.getNombre()  &&  numCasas>=n) {
            numCasas -= n;
            return true;
        }
        
        return false;    
    }
    
    //Tramita la venta del título de ser posible. Devueleve si ha sido posible.
    boolean vender(Jugador jugador)
    {
        if (jugador.getNombre()==propietario.getNombre()  &&  !hipotecado) {
            propietario.recibe(getPrecioVenta());
            propietario = null;
            numCasas    = 0;
            numHoteles  = 0;
            
            return true;
        }
            
        return false;
    }
    
    //Cambia el propietario al Jugador indicado
    void actualizaPropietarioPorConversion(Jugador jugador)
    {
        propietario = jugador;
    }
    
    //Tramita compra del título.
    boolean comprar(Jugador jugador)
    {
        if (propietario==null  &&  jugador.getSaldo()>=precioCompra) {
            jugador.paga(precioCompra);
            propietario = jugador;
            
            return true;
        }
        return false;
    }
    
    //Devuelve si jugador es el propietario
    private boolean esEsteElPropietario(Jugador jugador)
    {
        if (propietario==null)
            return false;
        else
            return jugador.getNombre()==propietario.getNombre();
    }
    
    //Devuelve hipotecado
    public boolean getHipotecado() { return hipotecado; }
    
    //Devuelve el importe de hipoteca
    float getImporteHipoteca()
    {
        return (hipotecaBase*(1+(numCasas*0.5f)+(numHoteles*2.5f)));
    }
    
    //Devuelve nombre
    String getNombre() { return nombre; }
    
    //Devuelve numCasas
    int getNumCasas() { return numCasas; }
    
    //Devuelve numHoteles
    int getNumHoteles() { return numHoteles; }
    
    //Devuelve el precioCompra
    float getPrecioCompra() { return precioCompra; }
    
    //Devuelve el precioEdificar
    float getPrecioEdificar() { return precioEdificar; }
    
    //Devuelve al propietario actual
    Jugador getPropietario() { return propietario; }
    
    //Devuelve si tiene propietario
    boolean tienePropietario() { return propietario!=null; }
   
    //Método para construir una casa en la propiedad
    boolean construirCasa (Jugador jugador)
    {
        boolean seConstruye = false;
        
        if (esEsteElPropietario(jugador)) {
            seConstruye = propietario.paga(precioEdificar);
            numCasas++;
        }
        
        return seConstruye;
    }

    //Método para construir un hotel en la propiedad
    boolean construirHotel(Jugador jugador)
    {
        boolean seConstruye = false;
        
        if (esEsteElPropietario(jugador)  &&  numCasas>=4) {
            seConstruye = jugador.paga(precioEdificar);
            numHoteles++;
        }
        
        return seConstruye;
    }   
}
