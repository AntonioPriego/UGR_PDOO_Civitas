#encoding: utf-8

require_relative "sorpresa"

module Civitas

  class Casilla
    attr_reader :tituloPropiedad, :nombre
  
    @@carcel= 10
  
    def initialize(nombre)
      @nombre = nombre
    end
    
    #toString
    def toString
      return "{\n\tCasilla:  #{@nombre}\n}"
    end
    
    #Devuelve si es un indice válido para acceder a la lista de jugadores
    def jugadorCorrecto(actual, todos)
      correcto = false
      if (actual < todos.size && actual >= 0)
        correcto = true
      end
      return correcto
    end
    
    #Registra en diario que jugador ha caido en casilla
    def informe(actual, todos)
      if(jugadorCorrecto(actual,todos))
        Diario.instance.ocurreEvento("Jugador #{todos.at(actual).nombre} ha caido en casilla #{@nombre}" )
      end
    end
    
    #recibeJugador DESCANSO
    def recibeJugador(actual, todos)
      informe(actual, todos)
    end
    
  end
end 