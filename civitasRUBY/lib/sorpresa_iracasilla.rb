#encoding: utf-8

require_relative "sorpresa"

module Civitas
  class SorpresaIracasilla < Sorpresa
    
    def initialize(tablero, valor, texto)
      super(texto)
      
      @tablero = tablero
      @valor   = valor
    end

    #AplicarAJugador de SorpresaIracasilla
    def aplicarAJugador(actual,todos)
      jugador = todos.at(actual)
      
      if (jugadorCorrecto(actual, todos))
        informe(actual, todos)
        jugador.moverACasilla(@tablero.nuevaPosicion(
            jugador.numCasillaActual,
            @tablero.calcularTirada(jugador.numCasillaActual, @valor)))        
       
        @tablero.casilla(@valor).recibeJugador(actual, todos)
      end
    end
    
    #toString de SorpresaIracasilla
    def toString
      return "Sorpresa tipo IrACasilla("+self.to_s+")"
    end
    
  end
end