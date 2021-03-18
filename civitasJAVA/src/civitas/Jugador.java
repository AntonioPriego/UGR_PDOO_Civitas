package civitas;

import java.util.ArrayList;


public class Jugador implements Comparable<Jugador> {

    protected static int CasasMax         = 4;
    protected static int CasasPorHotel    = 4;
    protected static int HotelesMax       = 4;
    private int numCasillaActual          = -1;
    protected static float PasoPorSalida  = 1000;
    protected static float PrecioLibertad = 200;
    private static float SaldoInicial     = 7500;
    private boolean puedeComprar          = true;
    protected boolean encarcelado         = false;
    private String nombre;
    private float saldo;
    private ArrayList<TituloPropiedad> propiedades;
    private SorpresaSalvoconducto salvoconducto = null;

    //Constructor 1
    Jugador(String _nombre) {
        nombre = _nombre;
        numCasillaActual = 0;
        puedeComprar = false;
        encarcelado = false;
        saldo = SaldoInicial;
        propiedades = new ArrayList<TituloPropiedad>();
        salvoconducto = null;

    }

    //Constructor 2, de copia
    protected Jugador(Jugador otro) {
        this.nombre = otro.nombre;
        this.encarcelado = otro.encarcelado;
        this.numCasillaActual = otro.numCasillaActual;
        this.puedeComprar = otro.puedeComprar;
        this.saldo = otro.saldo;
        this.propiedades = otro.propiedades;
        this.salvoconducto = otro.salvoconducto;
    }

    //Devuelve si el jugador debe ser encarcelado
    protected boolean debeSerEncarcelado() {
        if (!encarcelado) {
            if (tieneSalvoconducto()) {
                perderSalvoconducto();
                Diario.getInstance().ocurreEvento("Jugador " + nombre + " se libra de carcel por salvoconducto");

                return false;
            }
        }

        return true;
    }

    //Realiza los pasos para encarcelar a jugador de ser posible. Devuelve si ha sido encarcelado
    boolean encarcelar(int numCasillaCarcel) {

        if (debeSerEncarcelado()) {
            moverACasilla(numCasillaCarcel);
            encarcelado = true;
            Diario.getInstance().ocurreEvento("Jugador " + nombre + " ha sido enviado a la carcel");
        }
        return encarcelado;
    }

    //Otorga salvoconducto a jugador si no está encarcelado. Devuelve si se le otorga
    boolean obtenerSalvoconducto(SorpresaSalvoconducto sorpresa) {
        if (encarcelado) {
            return false;
        } else {
            salvoconducto = sorpresa;
            return true;
        }

    }

    //CAMBIO RESPECTO A GUIÓN
    // -Eliminar visibilidad private para poder llamar a este método desde JugadorEspeculador
    //Quita el salvoconducto a jugador
    void perderSalvoconducto() {
        salvoconducto.usada();
        salvoconducto = null;
    }

    //Devuelve si el jugador posee salvoconducto
    boolean tieneSalvoconducto() {

        if (salvoconducto == null) {
            return false;
        } else {
            return true;
        }
    }

    //Devuelve si el jugador puede comprar casillas
    boolean puedeComprarCasilla() {
        puedeComprar = !encarcelado;

        return puedeComprar;
    }

    //Realiza el proceso de pago
    boolean paga(float cantidad) {
        return modificarSaldo(cantidad * - 1);
    }

    //Realiza el proceso de pago de impuestos de ser posible. Devuelve si es posible
    boolean pagaImpuesto(float cantidad) {

        if (encarcelado) 
            return false;
        else
            return paga(cantidad);
    }

    //Realiza el proceso de pagar el alquiler de ser posible. Devuelve si es posible
    boolean pagaAlquiler(float cantidad) {
        boolean resultado = false;

        if (!encarcelado)
            resultado = paga(cantidad);

        return resultado;
    }

    //Realiza el proceso de recibir dinero de ser posible. Devuelve si es posible
    boolean recibe(float cantidad) {
        boolean resultado = false;
        if (!encarcelado) {
            resultado = modificarSaldo(cantidad);

        }
        return resultado;
    }

    //Incrementa el saldo
    boolean modificarSaldo(float cantidad) {
        boolean verdadero = true;

        saldo = saldo + cantidad;
        Diario.getInstance().ocurreEvento("El jugador " + nombre + " tiene de saldo " + saldo);

        return verdadero;
    }

    //Mueve al jugador a la casilla indicada
    boolean moverACasilla(int numCasilla) {
        if (!encarcelado) {
            if (numCasilla < 0) {
                System.out.print("ADJNSKJNS");
            }
            numCasillaActual = numCasilla;
            
            puedeComprar = false;
            
            Diario.getInstance().ocurreEvento("El jugador " + nombre + " ha sido trasladado a la casilla " + numCasilla);

            return true;
        }

        return false;
    }

    //MODIFICACIÓN DE GUIÓN
    //  -Eliminar visibilidad private para poder llamar al método desde JugadorEspeculador
    //Devuelve si el jugador puede gastar la cantidad indicada
    boolean puedoGastar(float precio) {

        if (!encarcelado && saldo > precio) {
            return true;
        } else {
            return false;
        }
    }

    //Realiza todo el procedimiento de venta de una propiedad ip, de ser posible. Devuelve si es posible
    boolean vender(int ip) {

        if (!encarcelado && existeLaPropiedad(ip)) {
            if (propiedades.get(ip).vender(this)) {
                Diario.getInstance().ocurreEvento("Jugador " + nombre + " ha vendido propiedad " + propiedades.get(ip).getNombre());
                propiedades.remove(ip);

                return true;
            }
        }

        return false;
    }

    //Devuelve la cantidad de casas y hoteles de jugador
    int cantidadCasasHoteles() {
        int total = 0;

        //llamamos a la función cantidadCasasHoteles de TituloPropiedad
        for (int i = 0; i < propiedades.size(); i++) {
            total = propiedades.get(i).cantidadCasasHoteles();
        }

        return total;
    }

    //compareTo de Jugador
    public int compareTo(Jugador otro) {

        //se encarga de comparar el resultado de dos jugadores
        int resultado = Float.compare(saldo, otro.saldo);

        return resultado;
    }

    //Devuelve si el jugador está en bancarrota
    boolean enBancarrota() {
        boolean enBancarrota = false;

        if (saldo < 0) {
            enBancarrota = true;
        }

        return enBancarrota;
    }

    //Devuelve si existe la propiedad con índice ip
    private boolean existeLaPropiedad(int ip) {
        boolean existe = false;

        if (!propiedades.isEmpty()) {
            if (propiedades.get(ip).getPropietario() == this) {
                existe = true;
            }
        }

        return existe;
    }

    //CAMBIO RESPECTO A GUIÓN
    // - Elimino visibilidad private para poder acceder desde JugadorEspeculador
    //Get de casasMax
    int getCasasMax() {
        return CasasMax;
    }

    //CAMBIO RESPECTO A GUIÓN
    // - Elimino visibilidad private para poder acceder desde JugadorEspeculador
    //Get de HotelesMax
    int getHotelesMax() {
        return HotelesMax;
    }

    //Get de CasasPorHotel
    int getCasasPorHotel() {
        return CasasPorHotel;
    }

    //Get de nombre
    protected String getNombre() {
        return nombre;
    }

    //Get de numCasillaActual
    int getNumCasillaActual() {
        return numCasillaActual;
    }

    //Get de PrecioLibertad
    float getPrecioLibertad() {
        return PrecioLibertad;
    }

    //Get de PasoPorSalida
    float PasoPorSalida() {
        return PasoPorSalida;
    }

    //Get de propiedades
    protected ArrayList<TituloPropiedad> getPropiedades() {
        return propiedades;
    }

    //Get de puedeComprar
    boolean getPuedeComprar() {
        return puedeComprar;
    }

    //Get de saldo
    protected float getSaldo() {
        return saldo;
    }

    //Devuelve si jugador está encarcelado
    public boolean isEncarcelado() {
        return encarcelado;
    }

    //Realiza el proceso por el que se le ingresa dinero al jugador por pasar por salida
    boolean pasaPorSalida() {
        modificarSaldo(PasoPorSalida);

        Diario.getInstance().ocurreEvento("El jugador " + nombre + " pasa por salida y se lleva " + PasoPorSalida + "€");

        return true;
    }

    //Devuelve si tiene dinero para salir de la cárcel
    private boolean puedeSalirCarcelPagando() {
        return saldo > PrecioLibertad;
    }

    //Devuelve si el jugador puede edificar una casa en la Propiedad propiedad
    boolean puedoEdificarCasa(TituloPropiedad propiedad) {
        return (propiedad.getNumCasas() < CasasMax ? true : false);
    }

    //Devuelve si el jugador puede edificar un hotel en la Propiedad propiedad
    boolean puedoEdificarHotel(TituloPropiedad propiedad) {
        return (propiedad.getNumHoteles() < HotelesMax ? true : false);
    }

    //Saca de la carcel a jugador de ser posible. Devuelve si es posible
    boolean salirCarcelPagando() {

        if (puedeSalirCarcelPagando()) {
            encarcelado = false;
            paga(PrecioLibertad);
            Diario.getInstance().ocurreEvento("El jugador " + nombre + " ha salido de la carcel pagando la fianza");
            return true;
        }
        return false;
    }

    //Saca de la carcela al jugador si la tirada es favorable. Devuelve si la tirada fue favorable
    boolean salirCarcelTirando() {
        boolean salgo = Dado.getInstance().tirar() >= 5;
        if (salgo) {
            encarcelado = false;
            Diario.getInstance().ocurreEvento("El jugador " + nombre + " ha salido de la carcel mediante tirada de dado");
            salgo = true;
        }
        return salgo;
    }

    //Devuelve si el jugador tiene propiedades
    boolean tieneAlgoQueGestionar() {
        boolean tiene = false;

        if (!propiedades.isEmpty()) {
            tiene = true;
        }

        return tiene;
    }

    //toString de Jugador
    @Override
    public String toString() {
        return "Jugador :"                   + nombre
                + "\n\tCasilla = "           + numCasillaActual
                + "\n\tPuede comprar =  "    + puedeComprar
                + "\n\tEstá encarcelado =  " + encarcelado
                + "\n\tSaldo = "             + saldo
                + "\n\tTiene salvoconducto " + salvoconducto
                + "\n\tPropiedades = "       + getPropiedades().toString();

    }

    //Cancela la hipoteca con índice ip
    boolean cancelarHipoteca(int ip) {
        if (encarcelado) {
            return false;
        }

        if (existeLaPropiedad(ip)) {
            TituloPropiedad propiedad = propiedades.get(ip);
            float cantidad = propiedad.getImporteCancelarHipoteca();

            if (puedoGastar(cantidad)) {
                propiedad.cancelarHipoteca(this);
                Diario.getInstance().ocurreEvento("El jugador " + nombre + " cancela la hipoteca de la propiedad " + ip);
                return true;
            }
        }

        return false;
    }

    //Hipoteca la propiedad de índice ip de ser posible. Devuelve si fue posible
    boolean hipotecar(int ip) {
        boolean result = false;

        if (encarcelado) {
            return result;
        }

        if (existeLaPropiedad(ip)) {

            TituloPropiedad propiedad = propiedades.get(ip);
            result = propiedad.hipotecar(this);
        }

        if (result) {
            Diario.getInstance().ocurreEvento("El jugador " + nombre + " hipoteca la propiedad " + ip);
        }

        return result;
    }

    //Comprar una propiedad de ser posible. Devuelve si ha sido posible
    boolean comprar(TituloPropiedad titulo) {
        if (encarcelado) {
            return false;
        }

        if (puedeComprar) {
            if (puedoGastar(titulo.getPrecioCompra())) {
                if (titulo.comprar(this)) {
                    propiedades.add(titulo);
                    Diario.getInstance().ocurreEvento("El jugador " + nombre + " compra la propiedad " + titulo.toString());
                    puedeComprar = false;
                }

                return true;
            }
        }

        return false;
    }

    //Construye una casa
    boolean construirCasa(int ip) {
        if (encarcelado)
            return false;

        if (existeLaPropiedad(ip)) {
            TituloPropiedad propiedad = propiedades.get(ip);
           
            if (puedoEdificarCasa(propiedad)) {
                if (propiedad.construirCasa(this)) {
                    Diario.getInstance().ocurreEvento("El jugador " + nombre + " construye casa en la propiedad " + ip);
                    
                    return true;
                }
            }
        }

        return false;
    }

    //Construir un hotel
    boolean construirHotel(int ip) {
        if (this.isEncarcelado()) 
            return false;

        if (existeLaPropiedad(ip)) {
            TituloPropiedad propiedad = propiedades.get(ip);
            
            if (puedoEdificarHotel(propiedad)) {
                propiedad.construirHotel(this);
                propiedad.derruirCasas(CasasPorHotel, this);
                Diario.getInstance().ocurreEvento("El jugador " + getNombre() + " construye hotel en la propiedad " + ip);
            
                return true;
            }
        }

        return false;
    }

    //ESTE MÉTODO NO ES PARTE DEL GUIÓN. IMPLEMENTACIÓN PARA SOLUCIONAR VistasTextual::gestionar()
    //PARA EVITAR TENER QUE DEVOLVER TODO 'propiedades', devolvemos solo lo imprescindible
    //Devuelve el número de propiedades
    public ArrayList<String> getNombrePropiedades() {
        ArrayList<String> nombres = new ArrayList<String>();

        for (TituloPropiedad propiedad : propiedades) {
            nombres.add(propiedad.getNombre());
        }

        return nombres;
    }

    //ESTE MÉTODO NO ES PARTE DEL GUIÓN.
    // - Este método es para evitar tener que hacer públicas las propiedades
    //Jugador cede propiedades a otroJugador
    public void cedePropiedadesA(Jugador otroJugador) {
        for (TituloPropiedad propiedad : propiedades) {
            propiedad.actualizaPropietarioPorConversion(otroJugador);
        }
    }

    //Devuelve jugador convertido a especulador
    public JugadorEspeculador aEspeculador() {
        return new JugadorEspeculador(this, 2000);
    }
    
    //Devuelve si el jugador es especulador
    public boolean esEspeculador()
    {
        return false;
    }
}
