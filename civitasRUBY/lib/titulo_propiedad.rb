# encoding: utf-8

module Civitas
  class TituloPropiedad
    attr_reader :hipotecado, :nombre, :numCasas, :numHoteles, :precioCompra, :precioEdificar, :propietario
    
    def initialize( nombre, alquilerBase, factorRevalorizacion, hipotecaBase, precioCompra ,precioEdificar)
      @alquilerBase             = alquilerBase
      @@factorInteresesHipoteca = 1.1;
      @factorRevalorizacion     = factorRevalorizacion
      @hipotecaBase             = hipotecaBase
      @nombre                   = nombre
      @precioEdificar           = precioEdificar
      @hipotecado               = false
      @numCasas                 = 0
      @numHoteles               = 0
      @propietario              = nil
      @precioCompra             = precioCompra
    end
    
    #Devuelve cadena con toda la info del titulo de propiedad
    def toString
      if @propietario!=nil
        nombre = @propietario.nombre
      else
        nombre="No hay propietario"
      end

      cadena = "{\nPropietario :"+nombre                   +
      "\nNum_casas: "+@numCasas.to_s                         +
      "\nNum_hoteles: "+@numHoteles.to_s                     +
      "\nPrecio_compra: "+@precioCompra.to_s                 +
      "\nPrecio_edificar: "+@precioEdificar.to_s             +
      "\nHipoteca_base: "    +@hipotecaBase.to_s             +
      "\nFactor_revalorización: "+@factorRevalorizacion.to_s +
      "\nAlquiler_base: "          +@alquilerBase.to_s+"\n}"                        
    
      return cadena
    end
    
    #Devuelve el cálculo del precio del alquiler según las reglas del juego
    def precioAlquiler
      if @hipotecado || @propietario.isEncarcelado() 
        return 0
      else 
        resultado = @alquilerBase*(1 + (@numCasas*0.5)+(@numHoteles*2.5))
        return resultado

      end
    end
    
    #Devuelve el importe que se obtiene al cancelar la hipoteca
    def importeCancelarHipoteca
      resultado = (@hipotecaBase*(1+(@numCasas*0.5)+(@numHoteles*2.5)))*@@factorInteresesHipoteca;
      return resultado
    end
    
    #Tramita el alquiler del Jugador jugador. Pago y recibo
    def tramitarAlquiler(jugador)

      if @propietario == true
        if jugador.nombre != @proietario.nombre
          jugador.pagaAlquiler(getPrecioAlquiler)
          @propietario.recibe(getPrecioAlquiler)

        end
      end
    end
    
    #Devuelve true si el propietario está encarcelado
    def propietarioEncarcelado
      resultado = false

      if @propietario.encarcelado == true
        resultado = true
      end
    
      return resultado
    end
      
    #Devuelve la cantidad de casas y hoteles  
    def cantidadCasasHoteles

      cantidad = 0

      cantidad = @numCasas + @numHoteles
      return cantidad
    end
  
    #Devuelve el precio de venta según las relgas del juego
    def precioVenta

      resultado = (@precioEdificar*@numCasas+@precioEdificar*4*@numHoteles)*@factorRevalorizacion

      return resultado
    end
  
    #Se derrue la casa de ser posible. Se devuelve si ha sido posible
    def derruirCasas(n,jugador)

      if jugador.nombre==@propietario.nombre  and  @numCasas>=n
        @numCasas -= n
        return true
      else

        return false
      end
    end

    #Tramita la venta del título de ser posible. Devueleve si ha sido posible.
    def vender(jugador)
      if jugador.nombre == @propietario.nombre and !@hipotecado

        @propietario.recibe(precioVenta)
        @propietaio = nil
        @numCasas   = 0
        @numHoteles = 0

        return true
      else 

        return false
      end
    end
  
    #Cambia el propietario al Jugador indicado
    def actualizaPropietarioPorConversion(jugador)
      @propietario = jugador
    end

    #Tramita compra del título.
    def comprar(jugador)

      if @propietario==nil
        jugador.paga(@precioCompra)
        @propietario = jugador

        return true
      else
        
        return false
      end
    end
  
    #Devuelve si jugador es el propietario
    def esEsteElPropietario(jugador)
      resultado = false

      if jugador.nombre == @propietario.nombre
        resultado = true
      end

      return resultado
    end
    
    #Devuelve el importe de hipoteca
    def importeHipoteca
      cantidadRecibida = @hipotecaBase *(1 +(@numCasas*0.5)+(@numHoteles*2.5))

      return cantidadRecibida
    end
    
    #Cancela la hipoteca para el jugador indicado
    def cancelarHipoteca(jugador)
      if @hipotecado  &&  esEsteElPropietario(jugador)
        jugador.paga(importeCancelarHipoteca)
        @hipotecado = false
        
        return true
      else
        return false
      end
    end
    
    #Construye hotel en la propiedad
    def construirHotel(jugador)
      if esEsteElPropietario(jugador) && @numCasas>=4
        seConstruye = jugador.paga(precioEdificar)
        @numHoteles+=1
        
        return true
      end
      
      return false
    end
    
    #Construye casa para la propiedad
    def construirCasa (jugador)
      seConstruye = false
      
      if esEsteElPropietario(jugador)
        seConstruye= @propietario.paga(@precioEdificar)
        @numCasas += 1
      end
      
      return seConstruye
    end
    
    #Hipoteca la propiedad para jugador
    def hipotecar(jugador)
      if !hipotecado  &&  esEsteElPropietario(jugador)
        jugador.recibe(importeHipoteca)
        
        @hipotecado=true
        
        return true
      end
      
      return false
    end
    
    #Devuelve si tiene propietario
    def tienePropietario
      return (@propietario==nil ? false : true)
    end
    
    #Tramita el alquiler al jugador pasado como parámetro
    def tramitarAlquiler(jugador)
      if jugador.nombre != @propietario.nombre
        precio=precioAlquiler
        jugador.pagaAlquiler(precio)
        @propietario.recibe(precio)
      end
    end
  end
end
  
 
  
  

  
