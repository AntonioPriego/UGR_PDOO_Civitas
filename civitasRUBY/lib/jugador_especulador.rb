#encoding: utf-8

require_relative 'diario'
require_relative 'jugador.rb'

module Civitas  
  class JugadorEspeculador < Jugador
    
    @@FactorEspeculador = 2
    @@CasasMax          = 8
    @@HotelesMax        = 8
    
    
    #Constructor de copia de Jugador a Especulador
    def self.new_otro(otro, fianza)
      nuevo=super(otro)
      nuevo.estableceFianza(fianza)
        
      otro.cedePropiedadesA(nuevo) #Método para transicionar las propiedades a especulador
    
      return nuevo
    end

    #Devuelve si debe ser encarcelado
    def debeSerEncarcelado
      
      if !super
        return false
      elsif puedoGastar(@fianza)
        Diario.instance.ocurreEvento("Jugador especulador "+@nombre+" paga fianza para librarse de la cárcel")
        paga(@fianza)
      
        return false
      else
        return true
      end
    end
    
    #Método para pagar impuestos. La mitad en el caso de especulador
    def pagaImpuesto(cantidad)
      if @encarcelado
        return false
      else
        return paga(cantidad)
      end
    end
    
    #Devuelve si puede edificarse una casa
    def puedoEdificarCasa(titulo)
      return titulo.numCasas < @@CasasMax*@@FactorEspeculador
    end
    
    #Devuelve si puede edificarse un hotel
    def puedoEdificarHotel(titulo)
      return titulo.numHoteles < @@HotelesMax*@@FactorEspeculador && titulo.numCasas >= @@CasasPorHotel
    end
    
    #Set de fianza
    def estableceFianza(fianza)
      @fianza=fianza
    end
    
    #toString de JugadorEspeculador
    def toString
      super("\n\t(Especulador)")
    end
    
    #Devuelve si el jugador es especulador
    def esEspeculador
      return true
    end
  end
end