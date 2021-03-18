#encoding: utf-8

require_relative "casilla"

module Civitas
  class CasillaImpuesto < Casilla
    def initialize(_importe, _nombre)
      super(_nombre)
      @importe = _importe
    end
    
    #RecibeJugador de impuesto
    def recibeJugador(actual,todos)
      if(jugadorCorrecto(actual,todos))
        informe(actual,todos)
        todos.at(actual).pagaImpuesto(@importe)
      end
    end
  end
end