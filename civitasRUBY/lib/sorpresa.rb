#encoding: utf-8

module Civitas

  class Sorpresa  
    def initialize(texto)
      @texto = texto
    end
    
    #Devuelve si es un índice válido para acceder a la lista de jugadores
    def jugadorCorrecto(actual, todos)
      correcto = false
      
      if(actual < todos.size && actual >= 0)
        correcto = true
      end
      
      return correcto
    end
    
    #Indica en el diario que se está aplicando una sorpresa a un jugador
    def informe(actual, todos)
      if (jugadorCorrecto(actual, todos))
        Diario.instance.ocurreEvento( "El jugador #{todos[actual].nombre}, va a recibir una sorpresa #{toString}")
      end
    end
    
  end
end
