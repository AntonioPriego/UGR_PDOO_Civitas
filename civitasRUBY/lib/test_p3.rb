#encoding: utf-8

require_relative "civitas_juego"
require_relative "vista_textual"
require_relative "dado"

module Civitas
  class TestP3
    
    def self.main
      
      juego=CivitasJuego.new( ["Pepe","Juan","Marta"] )
      vista = VistaTextual.new
      controlador= Controlador.new(juego,vista)
      Dado.instance.debug(true)
      
      controlador.juega()     
    end
  end
  Civitas::TestP3.main
end
