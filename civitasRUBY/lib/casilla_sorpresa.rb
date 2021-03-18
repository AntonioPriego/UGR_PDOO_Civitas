#encoding: utf-8

require_relative "casilla"

module Civitas
  class CasillaSorpresa < Casilla
    def initialize(_mazo,_nombre)
      super(_nombre)
      @mazo = _mazo
      @sorpresa=nil
    end
    
    #RecibeJugador
    def recibeJugador(actual, todos)
      if (jugadorCorrecto(actual,todos))
        informe(actual,todos)

        @sorpresa=@mazo.siguiente
        @sorpresa.aplicarAJugador(actual,todos)
      end
    end
  end
end