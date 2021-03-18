#encoding: utf-8

require_relative "casilla"

module Civitas
  class CasillaJuez < Casilla
    def initialize(_carcel)
      super("Carcel")
      @carcel = _carcel
    end
    
    #RecibeJugador de juez
    def recibeJugador(actual,todos)
        if(jugadorCorrecto(actual,todos))
          informe(actual,todos)
          todos[actual].encarcelar(@carcel)
        end 
    end
  end
end