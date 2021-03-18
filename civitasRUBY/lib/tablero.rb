#encoding: utf-8

require_relative "casilla"

module Civitas 
  class Tablero
    attr_reader :numCasillaCarcel, :casillas, :porSalida, :tieneJuez
    
    def initialize(_numCasillaCarcel)
      @numCasillaCarcel
      
      if (_numCasillaCarcel >= 1)
        @numCasillaCarcel = _numCasillaCarcel
      else
        @numCasillaCarcel = 1
      end

      @casillas= Array.new
      @casillas<<Casilla.new("salida")
      @porSalida = 0
      @tieneJuez = false
    end
    
    #Devuelve True si el número de casillas es mayor que la posición de la casilla Carcel
    def correcto()
      return (@casillas.size>@numCasillaCarcel && @tieneJuez)
    end
    
    #Devuelve True si correcto() y hay más casillas que numCasillas
    def correcto2(numCasilla)
      return (correcto() && @casillas.size()>numCasilla)
    end
    
    #Consultor numCasillaCarcel
    def carcel()
      return @numCasillaCarcel
    end
    
    #Consultor porSalida con acciones condicionadas
    def porSalida()
      if (@porSalida>0)
        @porSalida -= 1
        return @porSalida+1
      else
        return @porSalida
      end
    end
    
    #Añade una casilla a casillas, además tambien añade Carcel de ser necesario
    def aniadeCasilla(casilla)
      if (@casillas.size() == @numCasillaCarcel)
        aniadeJuez
      end
      
      @casillas << casilla 
    
      if(@casillas.size() == @numCasillaCarcel)
        aniadeJuez
      end
    end
    
    #Añade Juez si no ha sido añadido aun
    def aniadeJuez
      if (!@tieneJuez)
        @casillas.push(CasillaJuez.new(@numCasillaCarcel))
      
      @tieneJuez = true
      end
    end
    
    #Consultor casilla posición numCasilla
    def casilla(numCasilla)
      if(correcto)
        return @casillas.at(numCasilla)
      else
        return nil
      end
    end
    
    #Devuelve la posición que resulta de tirada
    def nuevaPosicion(actual , tirada)
      if (!correcto)
        return -1
      else
        if ((actual+tirada)>@casillas.size)
          @porSalida+=1
        end
        return (actual+tirada)%@casillas.size
      end
    end
    
    #Calcula el tamaño de la tirada para ir de origen a destino
    def calcularTirada(origen,destino)
      tirada = destino-origen
      
      if(tirada<0)
        tirada = tirada + @casillas.size
      end
      
      return tirada
    end 
  end  
end