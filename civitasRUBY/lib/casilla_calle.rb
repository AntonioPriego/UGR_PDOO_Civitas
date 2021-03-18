#encoding: utf-8

require_relative "casilla"

module Civitas

  class CasillaCalle < Casilla   
    def initialize(titulo)
      super(titulo.nombre)
      @tituloPropiedad = titulo
    end
    
    #RecibeJugador de calle
    def recibeJugador(actual, todos)
      if jugadorCorrecto(actual, todos)
        informe(actual, todos)
        jugador=todos.at(actual)
        
        if !@tituloPropiedad.tienePropietario
          if jugador.puedeComprarCasilla
            puts "La casilla no está comprada: jugador puede comprar casilla"
          end
        else
          @tituloPropiedad.tramitarAlquiler(jugador)
        end
      end
    end
    
    #toString
    def toString
      return "{\n\tCasilla:  #{@nombre}" +
             (@tituloPropiedad!=nil ? "\n\tNumHoteles:  #{@tituloPropiedad.numHoteles}" : "") +
             (@tituloPropiedad!=nil ? "\n\tNumCasas:  #{@tituloPropiedad.numCasas}" : "")     +
             "\n}"
    end
    
  end

end