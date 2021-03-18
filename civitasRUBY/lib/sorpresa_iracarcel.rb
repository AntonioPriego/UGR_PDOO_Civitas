#encoding: utf-8

require_relative "sorpresa"

module Civitas
  class SorpresaIracarcel < Sorpresa
    def initialize(tablero)
      super("IrACarcel")
      @tablero = tablero
    end
    
    #AplicarAJugador de SorpresaIracarcel
    def aplicarAJugador(actual, todos)
      if (jugadorCorrecto(actual, todos))
        informe(actual, todos)
        todos[actual].encarcelar(@tablero.carcel)
      end
    end
    
    #toString de SorpresaIracarcel
    def toString
      return "Sorpresa tipo IrACarcel("+self.to_s+")"
    end
    
  end
end