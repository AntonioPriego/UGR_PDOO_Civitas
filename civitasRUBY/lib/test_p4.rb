#encoding: utf-8

require_relative "civitas_juego"
require_relative "vista_textual"
require_relative "dado"

module Civitas
  class TestP4
    
    def self.main
      
      juego=CivitasJuego.new( ["Pepe","Juan","Marta"] )
      vista = VistaTextual.new
      controlador= Controlador.new(juego,vista)
      #Dado.instance.debug(true)
      
      #juego.jugadorAEspeculador(0) #Pepe será especulador
      #juego.jugadorAEspeculador(2) #Marta será especuladora
      
      controlador.juega
    end
  end
  
  Civitas::TestP4.main
end
