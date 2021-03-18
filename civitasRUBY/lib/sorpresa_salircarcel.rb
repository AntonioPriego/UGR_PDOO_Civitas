#encoding: utf-8

require_relative "sorpresa"

module Civitas
  class SorpresaSalircarcel < Sorpresa
    def initialize(mazo)
      super("SaliCarcel")
      @mazo = mazo
    end
  
    #AplicarAJugador de SorpresaSalircarcel
    def aplicarAJugador(actual, todos)
      if (jugadorCorrecto(actual, todos))
        informe(actual, todos)

        haySalvoconducto = false        
        for jugador in todos
          if (jugador.tieneSalvoconducto())
            haySalvoconducto = true
          end
        end

        if (!haySalvoconducto)
          todos[actual].obtenerSalvoconducto(self)

        end
      end
    end
  
    #Inhabilita la carta especial en el mazo
    def salirDelMazo
      @mazo.inhabilitarCartaEspecial(self)
    end
  
    #Habilita la carta especial en el mazo
    def usada
      @mazo.habilitarCartaEspecial(self)
    end
    
    #toString de SorpresaSalircarcel
    def toString
      return "Sorpresa tipo SalirCarcel("+self.to_s+")"
    end
    
  end
end