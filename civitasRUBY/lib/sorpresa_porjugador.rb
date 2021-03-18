#encoding: utf-8

require_relative "sorpresa"

module Civitas
  class SorpresaPorjugador < Sorpresa
    def initialize(valor, texto)
      super.new(texto)
      @valor = valor
    end
  
    #aplicarAjugador de porJugador
    def aplicarJugador(actual, todos)
      if (jugadorCorrecto(actual, todos))
        informe(actual, todos)

        pagcobResto = Sorpresa.new(Civitas::Tipo_sorpresa::PAGARCOBRAR, @valor*(-1), "Por sorpresa porJugador para el resto")
        i = 0
        todos.size().times do
          if (i!=actual)
            pagcobResto.aplicarAjugador_pagarCobrar(i, todos)
            i += 1
          end
        end
        pagcob_propio = Sorpresa.new(Civitas::Tipo_sorpresa::PAGARCOBRAR, @valor*(todos.size()-1), "Por sorpresa porJugador para él mismo")
        pagcob_propio.aplicarAjugador_pagarCobrar(actual, todos)
      end
    end
    
    #toString de SorpresaPorjugador
    def toString
      return "Sorpresa tipo PorJugador("+self.to_s+")"
    end
    
  end
end