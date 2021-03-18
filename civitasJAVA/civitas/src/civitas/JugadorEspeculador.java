package civitas;


public class JugadorEspeculador extends Jugador {

    protected static int CasasMax = 8;
    protected static int HotelesMax = 8;
    static int FactorEspeculador = 2;
    float fianza;

    //Constructor
    public JugadorEspeculador(Jugador otro, float _fianza) {
        super(otro);
        fianza = _fianza;

        otro.cedePropiedadesA(this); //Método para transicionar las propiedades a especulador
    }

    //Devuelve si el jugador debe ser encarcelado
    @Override
    protected boolean debeSerEncarcelado() {

        if (!super.debeSerEncarcelado())
            return false;
        else if (super.puedoGastar(fianza)) {
            Diario.getInstance().ocurreEvento("Jugador especulador " + super.getNombre() + " paga fianza para librarse de la cárcel");
            paga(fianza);
            
            return false;
        }
        else
            return true;
    }

    //Realiza el proceso de pago de impuestos de ser posible. Devuelve si es posible
    @Override
    boolean pagaImpuesto(float cantidad) {

        if (encarcelado) {
            return false;
        } else {
            return paga(cantidad / 2); //Especuladores pagan la mitad
        }
    }

    //Get de casasMax
    int getCasasMax() {
        return CasasMax * FactorEspeculador;
    }

    //Get de HotelesMax
    int getHotelesMax() {
        return HotelesMax * FactorEspeculador;
    }
    
    //Devuelve si el jugador puede edificar una casa en la Propiedad propiedad
    @Override
    boolean puedoEdificarCasa(TituloPropiedad propiedad) {
        return (propiedad.getNumCasas() < CasasMax ? true : false);
    }

    //Devuelve si el jugador puede edificar un hotel en la Propiedad propiedad
    @Override
    boolean puedoEdificarHotel(TituloPropiedad propiedad) {
        return (propiedad.getNumHoteles() < HotelesMax ? true : false);
    }

    //toString
    @Override
    public String toString() {
        return "(Especulador) " + super.toString();
    }
}
