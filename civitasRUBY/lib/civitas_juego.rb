# encoding:utf-8

require_relative "dado"
require_relative "gestor_estados"
require_relative "controlador"
require_relative "jugador"
require_relative "titulo_propiedad"
require_relative "estados_juego"
require_relative "tablero"
require_relative "mazo_sorpresas"
require_relative "sorpresa"
require_relative "casilla_sorpresa"
require_relative "casilla_calle"
require_relative "casilla_impuesto"
require_relative "casilla_juez"
require_relative "sorpresa_iracarcel"
require_relative "sorpresa_iracasilla"
require_relative "sorpresa_pagarcobrar"
require_relative "sorpresa_salircarcel"
require_relative "sorpresa_especulador"



module Civitas
  class CivitasJuego
    attr_reader :estado
    
    #Constructor
    def initialize(nombres)
      @indiceJugadorActual
      @gestorEstados       = GestorEstados.new
      @estado              = @gestorEstados.estadoInicial
      @jugadores           = Array.new
      
      for nombre in nombres do @jugadores<<Jugador.new_nombre(nombre) end
      
      @indiceJugadorActual = Dado.instance.quienEmpieza(@jugadores.length)
      @gestorEstados       = GestorEstados.new
      @mazo                = MazoSorpresas.new
      @tablero             = Tablero.new(3)
      
      inicializaTablero
      inicializarMazoSorpresas
    end

    #Crea e inicializa tablero
    def inicializaTablero
      titulo1 = TituloPropiedad.new("Propiedad alfa", 1500, 150, 750, 3700, 1900)
      titulo2 = TituloPropiedad.new("Propiedad omega", 1200, 110, 490, 3400, 1800)
      titulo3 = TituloPropiedad.new("Propiedad fi", 1000, 100, 400, 3200, 1700)
       
      @tablero.aniadeCasilla(CasillaImpuesto.new(800.0, "Impuesto por pensar"))
      @tablero.aniadeCasilla(CasillaSorpresa.new(@mazo,"sorpresa1"))
      @tablero.aniadeCasilla(CasillaImpuesto.new(700.0, "Impuesto por jugar"))
      @tablero.aniadeCasilla(CasillaSorpresa.new(@mazo, "sorpresa2"))
      @tablero.aniadeCasilla(CasillaSorpresa.new(@mazo, "sorpresa3"))
      @tablero.aniadeCasilla(CasillaImpuesto.new(730.0, "Impuesto por leer"))
      @tablero.aniadeCasilla(CasillaCalle.new(titulo1))
      @tablero.aniadeCasilla(CasillaImpuesto.new(830.0, "Impuesto por avanzar"))
      @tablero.aniadeCasilla(CasillaCalle.new(titulo2))
      @tablero.aniadeCasilla(CasillaSorpresa.new(@mazo, "sorpresa4"))
      @tablero.aniadeCasilla(CasillaImpuesto.new(720.0, "Impuesto por teclear"))
      @tablero.aniadeCasilla(CasillaSorpresa.new(@mazo, "sorpresa5"))
      @tablero.aniadeCasilla(CasillaImpuesto.new(830.0, "Impuesto por pagar"))
      @tablero.aniadeCasilla(CasillaCalle.new(titulo3))
    end

    #Crea e inicializa mazo de sorpresas
    def inicializarMazoSorpresas
      @mazo.alMazo(SorpresaSalircarcel.new(@mazo))
      @mazo.alMazo(SorpresaPagarcobrar.new(-750, "Paga 750"))
      @mazo.alMazo(SorpresaPagarcobrar.new(750, "Gana 750"))
      @mazo.alMazo(SorpresaPagarcobrar.new(-850, "Paga 850"))
      @mazo.alMazo(SorpresaIracarcel.new(@tablero))
      @mazo.alMazo(SorpresaEspeculador.new(self))
    end

    #Contabiliza las veces que jugadorActual pasa por salida para que pueda cobrar
    def contabilizarPasosPorSalida
      jugadorActual = getJugadorActual
      
      while @tablero.porSalida>0
        jugadorActual.pasaPorSalida
      end
    end

    #Método para que el jugador actual pase el turno
    def pasarTurno
      
      if @indiceJugadorActual==@jugadores.size-1
        @indiceJugadorActual=0
      else
        @indiceJugadorActual+=1
      end
    end
    

    #Pasa al siguiente estado del juego
    def siguientePasoCompletado(operacion)
      @estado=@gestorEstados.siguienteEstado(@estado, operacion)
    end
    
    def siguientePaso
      operacion = @gestorEstados.operacionesPermitidas(getJugadorActual, @estado)
     
      if (operacion == Civitas::OperacionesJuego::PASAR_TURNO)
        pasarTurno
        siguientePasoCompletado(operacion)
      elsif (operacion == Civitas::OperacionesJuego::AVANZAR)
        avanzaJugador
        siguientePasoCompletado(operacion)
      end
      
      return operacion
    end

    #Construye una casa de índice ip de ser posible. Devuelve si fue posible
    def construirCasa(ip)
      return @jugadores.at(@indiceJugadorActual).construirCasa(ip)
    end

    #Construye un hotel de índice ip de ser posible. Devuelve si fue posible
    def construirHotel(ip)
      return @jugadores.at(@indiceJugadorActual).construirHotel(ip)
    end

    #Vende la propiedad de índice ip de ser posible. Devuelve si fue posible
    def vender(ip)
      return getJugadorActual.vender(ip)
    end

    #Hipoteca la propiedad de índice ip de ser posible. Devuelve si fue posible
    def hipotecar(ip)
      return @jugadores.at(@indiceJugadorActual).hipotecar(ip)
    end

    #Cacela la hipoteca de propiedad de índice ip de ser posible. Devuelve si fue posible
    def cancelarHipoteca(ip)
      return @jugadores.at(@indiceJugadorActual).cancelarHipoteca(ip)
    end

    #Saca de la cárcel pagando de ser posible. Devuelve si fue posible
    def salirCarcelPagando
      return @jugadores.at(@indiceJugadorActual).salirCarcelPagando()
    end

    #Saca de la cárcel tirando de ser posible. Devuelve si fue posible
    def salirCarcelTirando
      return @jugadores.at(@indiceJugadorActual).salirCarcelTirando()
    end

    #Determina si ha acabado el juego. Devuelve si ha acabado el juego
    def finalDelJuego
      bancarrota = false
      
      for jugador in @jugadores
        bancarrota = jugador.enBancarrota
        
        if bancarrota
          return bancarrota
        end
      end
      
      return bancarrota
    end

    #Crea  y devuelve un ranking de jugadores
    def ranking
      ranking = Array.new(@jugadores)

      ranking.sort{ |j1,j2| j1<=>j2}
      
      return ranking
    end

    #Devuelve la casilla actual
    def casillaActual
      return @jugadores.at(@indiceJugadorActual).numCasillaActual
    end

    #Devuelve al jugador actual
    def jugadorActual
      return @jugadores.at(@indiceJugadorActual)
    end

    #Devuelve la información del jugador actual
    def infoJugadorTexto
      return @jugadores.at(@indiceJugadorActual).toString
    end

    #Hace avanzar al jugador actual
    def avanzaJugador
      tirada      = Dado.instance.tirar
      jugadorActu = getJugadorActual
      posActu     = jugadorActu.numCasillaActual
      posNuev     = @tablero.nuevaPosicion(posActu, tirada)
      
      casilla = @tablero.casilla(posNuev)
      
      contabilizarPasosPorSalida
      jugadorActu.moverACasilla(posNuev)
      casilla.recibeJugador(@indiceJugadorActual, @jugadores)
      contabilizarPasosPorSalida
    end
    
    #Devuelve al jugador que juega en el momento que se llama al método
    def getJugadorActual
      return @jugadores.at(@indiceJugadorActual)
    end
    
    #Devuelve la casilla en la que se encuentra el jugador actual
    def getCasillaActual
      return @tablero.casilla( getJugadorActual.numCasillaActual )
    end
    
    #Hace que el jugador actual compre la casilla actual
    def comprar
      jugadorActual = getJugadorActual
      titulo = getCasillaActual.tituloPropiedad
      
      return jugadorActual.comprar(titulo)
    end
    
    #Convierte a jugador indice en JugadorEspeculador
    def jugadorAEspeculador(indice)
      if !@jugadores.at(indice).esEspeculador
        @jugadores[indice] = @jugadores.at(indice).aEspeculador
      end
    end
    
    #Convierte a jugador jug en JugadorEspeculador
    def jugadorAEspeculadorJug(jug)
      for i in 0 .. @jugadores.size-1
        if jug == @jugadores[i]  and  !jug.esEspeculador
          @jugadores[i] = jug.aEspeculador
        end
      end
    end

  end
end