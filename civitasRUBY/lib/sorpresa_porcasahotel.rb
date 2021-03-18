#encoding: utf-8

require_relative "sorpresa"

module Civitas
  class SorpresaPorcasahotel < Sorpresa
    def initialize(valor, texto)
      super.new(texto)
      @valor = valor
    end
    
    #aplcarAJugador de porCasaHotel
    def aplicarAJugador(actual, todos)
      if (jugadorCorrecto(actual,todos))
        informe(actual, todos)
        valor = @valor * (todos[actual].cantidadCasasHoteles) #numCasas+todos[actual].numHoteles)
        todos[actual].modificarSaldo(valor)
      end
    end
    
    #toString de SorpresaPorcasahotel
    def toString
      return "Sorpresa tipo PorCasaHotel("+self.to_s+")"
    end
    
  end
end