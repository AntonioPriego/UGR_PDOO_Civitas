#encoding: utf-8

require_relative "sorpresa"

module Civitas

  class SorpresaPagarcobrar < Sorpresa
    def initialize(valor, texto)
      super(texto)
      @valor = valor
    end
  
    #aplicar a jugadro de pagarcobrar
    def aplicarAJugador(actual, todos)
      if (jugadorCorrecto(actual,todos))
        informe(actual, todos)
        todos[actual].modificarSaldo(@valor)
      end
    end
    
    #toString de SorpresaPagarCobrar
    def toString
      return "Sorpresa tipo PagarCobrar("+self.to_s+")"
    end
  end
end