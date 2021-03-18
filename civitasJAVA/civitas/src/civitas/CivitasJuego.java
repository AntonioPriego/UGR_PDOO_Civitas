package civitas;

import java.util.ArrayList;
import java.util.Collections;


public class CivitasJuego {

    private int indiceJugadorActual;
    MazoSorpresas mazo;
    Tablero tablero;
    ArrayList<Jugador> jugadores;
    GestorEstados gestorEstados;
    EstadosJuego estado;

    //Constructor
    public CivitasJuego(ArrayList<String> nombres) {
        jugadores = new ArrayList<Jugador>();

        for (String n : nombres) {
            jugadores.add(new Jugador(n));
        }
        gestorEstados = new GestorEstados();

        //inicializar
        tablero = new Tablero(3);
        mazo = new MazoSorpresas();
        inicializarMazoSorpresas();
        inicializaTablero();

        estado = gestorEstados.estadoInicial();

        indiceJugadorActual = Dado.getInstance().quienEmpieza(jugadores.size());
    }

    //Crea e inicializa tablero
    //MODIFICACIÓN DE GUIÓN
    //  -Eliminar MazoSorpresa como parámetro, ya que es un atributo de instancia
    private void inicializaTablero() {
        TituloPropiedad titulo1 = new TituloPropiedad(
                "Propiedad alfa", 1200f, 80f, 550f, 3500f, 1700f);
        TituloPropiedad titulo2 = new TituloPropiedad(
                "Propiedad omega", 1000f, 70f, 470f, 3200f, 1600f);
        TituloPropiedad titulo3 = new TituloPropiedad(
                "Propiedad fi", 900f, 60f, 300f, 3000f, 1500f);

        //El tablero por defecto, crea la casilla de Salida                 //Casilla 1
        tablero.añadeCasilla(new CasillaImpuesto(800.0f, "Impuesto por pensar"));
        tablero.añadeCasilla(new CasillaSorpresa(mazo, "sorpresa1"));               //Casilla 2
        tablero.añadeCasilla(new CasillaImpuesto(700.0f, "Impuesto por jugar"));    //Casilla 3
        tablero.añadeCasilla(new CasillaSorpresa(mazo, "sorpresa2"));               //Casilla 4
        tablero.añadeCasilla(new CasillaImpuesto(720.0f, "Impuesto por teclear"));  //Casilla 5
        tablero.añadeCasilla(new CasillaCalle(titulo1));                         //Casilla 7
        tablero.añadeCasilla(new CasillaImpuesto(830.0f, "Impuesto por avanzar"));
        tablero.añadeCasilla(new CasillaCalle(titulo2));                         //Casilla 8
        tablero.añadeCasilla(new CasillaImpuesto(730.0f, "Impuesto por leer"));    //Casilla 10
        tablero.añadeCasilla(new CasillaSorpresa(mazo, "sorpresa 3"));              //Casilla 13
        tablero.añadeCasilla(new CasillaCalle(titulo3));                         //Casilla 14
        tablero.añadeCasilla(new CasillaImpuesto(830.0f, "Impuesto por pagar"));
        tablero.añadeCasilla(new Casilla("Parking"));
    }

    //Crea e inicializa mazo de sorpresas
    //MODIFICACIÓN DE GUIÓN
    //  -Eliminar Tablero como parámetro, ya que es un atributo de instancia
    private void inicializarMazoSorpresas() {
        mazo.alMazo(new SorpresaPagarCobrar( tablero, 750 , "GANA 750"));//TipoSorpresa.PAGARCOBRAR,
        mazo.alMazo(new SorpresaPagarCobrar( tablero, -750, "PAGA 750"));//TipoSorpresa.PAGARCOBRAR,
        mazo.alMazo(new SorpresaSalirCarcel( mazo,"Salir cárcel"));//TipoSorpresa.SALIRCARCEL,
        mazo.alMazo(new SorpresaIrACasilla( tablero, 1, "Vuelve al principio(Casilla siguiente a salida)"));//TipoSorpresa.IRACASILLA,
        mazo.alMazo(new SorpresaPagarCobrar( tablero, -850, "PAGA 850"));//TipoSorpresa.PAGARCOBRAR,
        mazo.alMazo(new SorpresaSalirCarcel( mazo,"Salir Cárcel"));//TipoSorpresa.SALIRCARCEL,
        mazo.alMazo(new SorpresaIrACasilla( tablero, 5, "Ve a la casilla 5(A pagar por teclear)"));//TipoSorpresa.IRACASILLA,
        mazo.alMazo(new SorpresaEspeculador(this));
    }

    //Contabiliza las veces que jugadorActual pasa por salida para que pueda cobrar
    private void contabilizarPasosPorSalida(Jugador jugadorActual) {
        while (tablero.getPorSalida() > 0) {
            jugadorActual.pasaPorSalida();
        }
    }

    //Método para que el jugador actual pase el turno
    private void pasarTurno() {
        if (indiceJugadorActual == jugadores.size() - 1) {
            indiceJugadorActual = 0;
        } else {
            indiceJugadorActual++;
        }
    }

    //Devuelve al jugador actual
    public Jugador getJugadorActual() {

        return jugadores.get(indiceJugadorActual);
    }

    //Pasa al siguiente estado del juego
    public void siguientePasoCompletado(OperacionesJuego operacion) {
        estado = gestorEstados.siguienteEstado(getJugadorActual(), estado, operacion);
    }

    public OperacionesJuego siguientePaso() {
        Jugador jugadorActual = jugadores.get(indiceJugadorActual);
        OperacionesJuego operacion = gestorEstados.operacionesPermitidas(jugadorActual, estado);

        if (operacion == OperacionesJuego.PASAR_TURNO) {
            pasarTurno();
            siguientePasoCompletado(operacion);
        } else if (operacion == OperacionesJuego.AVANZAR) {
            avanzaJugador();
            siguientePasoCompletado(operacion);
        }

        return operacion;
    }

    //Vende la propiedad de índice ip de ser posible. Devuelve si fue posible
    public boolean vender(int ip) {
        return jugadores.get(indiceJugadorActual).vender(ip);
    }

    //Saca de la cárcel pagando de ser posible. Devuelve si fue posible
    public boolean salirCarcelPagando() {
        return jugadores.get(indiceJugadorActual).salirCarcelPagando();
    }

    //Saca de la cárcel tirando de ser posible. Devuelve si fue posible
    public boolean salirCarcelTirando() {
        return jugadores.get(indiceJugadorActual).salirCarcelTirando();
    }

    //Cacela la hipoteca de propiedad de índice ip de ser posible. Devuelve si fue posible
    public boolean cancelarHipoteca(int ip) {

        return jugadores.get(indiceJugadorActual).cancelarHipoteca(ip);
    }

    //Construye una casa de índice ip de ser posible. Devuelve si fue posible
    public boolean construirCasa(int ip) {
        return jugadores.get(indiceJugadorActual).construirCasa(ip);
    }

    //Construye un hotel de índice ip de ser posible. Devuelve si fue posible
    public boolean construirHotel(int ip) {
        return jugadores.get(indiceJugadorActual).construirHotel(ip);
    }

    //Hipoteca la propiedad de índice ip de ser posible. Devuelve si fue posible
    public boolean hipotecar(int ip) {
        return jugadores.get(indiceJugadorActual).hipotecar(ip);
    }

    //Crea  y devuelve un ranking de jugadores    
    public ArrayList<Jugador> ranking() {
        ArrayList<Jugador> ordenado = new ArrayList<>(jugadores);
        Collections.sort(ordenado);
        Collections.reverse(ordenado);
        return ordenado;
    }

    //Devuelve la casilla actual
    public Casilla getCasillaActual() {
        return tablero.getCasilla(jugadores.get(indiceJugadorActual).getNumCasillaActual());
    }

    //Devuelve la información del jugador actual
    public String infoJugadorTexto() {
        return jugadores.get(indiceJugadorActual).toString();
    }

    //Determina si ha acabado el juego. Devuelve si ha acabado el juego
    public boolean finalDelJuego() {
        boolean bancarrota = false;

        //recorrer el array jugadores
        for (Jugador jugador : jugadores) {
            if (!bancarrota) { //Si resultado ya es true, no necesitamos evaluar más
                bancarrota = jugador.enBancarrota();
            }
        }

        return bancarrota;
    }

    //Hace avanzar al jugador actual
    private void avanzaJugador() {
        int tirada = Dado.getInstance().tirar();
        Jugador jugadorActual = jugadores.get(indiceJugadorActual);
        int posicionActual = jugadorActual.getNumCasillaActual();

        int posicionNueva = tablero.nuevaPosicion(posicionActual, tirada);
        Casilla casilla = tablero.getCasilla(posicionNueva);
        this.contabilizarPasosPorSalida(jugadorActual);
        jugadorActual.moverACasilla(posicionNueva);
        casilla.recibeJugador(indiceJugadorActual, jugadores);
        this.contabilizarPasosPorSalida(jugadorActual);

    }

    //Realiza la compra de la casilla actual por el jugador actual de ser posible
    //Devuelve si es posible
    public boolean comprar() {
        int numCasillaActual = getJugadorActual().getNumCasillaActual();
        CasillaCalle casilla = (CasillaCalle)tablero.getCasilla(numCasillaActual);
        TituloPropiedad titulo = casilla.getTituloPropiedad();

        System.out.println("Nombre: " + casilla.getNombre());
        System.out.println("Indice: " + numCasillaActual);

        return getJugadorActual().comprar(titulo);
    }

    //Convierte a jugador indice en JugadorEspeculador
    void jugadorAEspeculador(int indice) {
        jugadores.add(indice, jugadores.get(indice).aEspeculador());
        jugadores.remove(indice + 1);
    }

    //Convierte a jugador jug en JugadorEspeculador
    void jugadorAEspeculador(Jugador jug) {
        
        for (int i=0; i<jugadores.size()-1; i++) {
            if (jug == jugadores.get(i)) {
                jugadores.add(i, jugadores.get(i).aEspeculador());
                jugadores.remove(i + 1);
            }
        }
    }
}
