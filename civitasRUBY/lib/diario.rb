#encoding: utf-8

require 'singleton'

module Civitas
  class Diario
    include Singleton
  
    def self.get_instance
      return @instance
    end
    
    private
    def initialize
      @eventos = Array.new
    end
    
    public
    def ocurreEvento(e)
      @eventos<<e
    end
  
    def eventosPendientes
      return (@eventos.size > 0)
    end
  
    def leerEvento
      e = @eventos.shift
      
      return e
    end 
  
  end
end
