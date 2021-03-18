#encoding: utf-8

require 'singleton'
require 'diario'

module Civitas
  class Dado
    include Singleton #Solo existe una instancia de Dado
    
    attr_reader :ultimoResultado
    
    @@SalidaCarcel = 5
    
    #Initialize
    def initialize
      @random          = Random.new()
      @ultimoResultado = 0      #Inicializada por iniciativa propia
      @debug           = false  #Inicializada por iniciativa propia
    end
    
    #Devuelve tirada [1,6] o 1 en debug
    def tirar
      if !@debug
        @ultimoResultado = @random.rand(6)+1
      else
        @ultimoResultado = 1
      end
      
      return @ultimoResultado
    end
    
    #Determina si sale de la carcel con una tirada
    def salgoDeLaCarcel
     
      if @ultimoResultado ==6
        return true
      else
        return false
      end
    end
    
    #Generador aleatorio que determina cual de los n jugadores empieza
    def quienEmpieza(n)
      return @random.rand(n)
    end
    
    #Devueleve el último valor arrojado por el dado
    def ultimoResultado
      return ultimoResultado
    end
    
    #Set de @debug
    def debug(_debug)
      @debug = _debug
     
      if @debug == true
        Diario.instance.ocurreEvento("Activacion modo debug")
      else 
        Diario.instance.ocurreEvento("Desactivacion modo debug")
      end
      
    
    end
    
  end
      private :initialize
  
end
