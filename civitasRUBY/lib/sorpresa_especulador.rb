#encoding: utf-8

module Civitas
  class SorpresaEspeculador < Sorpresa
    def initialize(civitasJuego)
      super("El jugador pasa a ser Especulador")
      @juego=civitasJuego
    end
    
    #AplicarAJugador de SorpresaEspeculador
    def aplicarAJugador(actual,todos)
      if (jugadorCorrecto(actual, todos) and !todos[actual].esEspeculador)
        informe(actual, todos)
        @juego.jugadorAEspeculadorJug(todos.at(actual))
      end
    end
    
    #toString de SorpresaEspeculador
    def toString
      return "Sorpresa tipo IrACasilla("+self.to_s+")"
    end
    
  end
end