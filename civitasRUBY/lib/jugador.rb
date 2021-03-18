#encoding: utf-8

require_relative 'jugador_especulador'

module Civitas
  class Jugador
    attr_reader :nombre, :numCasillaActual, :propiedades, :puedeComprar, :saldo
    attr_reader :salvoconducto, :encarcelado

    @@CasasMax        = 4
    @@CasasPorHotel   = 4
    @@HotelesMax      = 4
    @@PasoPorSalida   = 1000
    @@PrecioLibertad  = 200
    @@SaldoInicial    = 7500
    
    def initialize(nombre, encarcelado, numCasillaActual, puedeComprar, saldo, salvoconducto, propiedades)
      @nombre           = nombre
      @encarcelado      = encarcelado
      @numCasillaActual = numCasillaActual
      @puedeComprar     = puedeComprar
      @saldo            = saldo
      @salvoconducto    = salvoconducto
      @propiedades      = propiedades
    end
    
    #Constructor 1
    def self.new_nombre(nombre)
      new(nombre, false, 0, false, @@SaldoInicial, nil, Array.new)
    end
    
    #Constructor 2, de copia
    def self.new_otro(otro)
      new(otro.nombre, otro.encarcelado, otro.numCasillaActual, otro.puedeComprar, otro.saldo, otro.salvoconducto, otro.propiedades)
    end
    
    #Devuelve si el jugador debe ser encarcelado
    def debeSerEncarcelado
      
      if !@encarcelado
      
        if tieneSalvoconducto
          perderSalvoconducto
          Diario.instance.ocurreEvento("Jugador " + @nombre + " se libra de carcel por salvoconducto")
          
        else
          return true
        end
      end
      
      return false
    end

    public
    #toString de Jugador
    def toString(palabra="")
      cadena=  "{"+palabra+"\n\tNombre: #{@nombre}" +
        "\n\tCasilla: #{@numCasillaActual}"         +
        "\n\tSaldo: #{@saldo}"                      +
        "\n\tEstá encarcelado: "     + (@encarcelado ? "Sí" : "No")        +
        "\n\tTiene salvoconducto: "  + (@salvoconducto==nil ? "No" : "Sí") +
        "\n\tPropiedades:"
      
      for propiedad in @propiedades
        cadena+=propiedad.toString
      end
      cadena+="\n}"
      
      return cadena
    end
      
    #Devuelve si jugador está encarcelado
    def isEncarcelado
      return @encarcelado
    end

    #Realiza los pasos para encarcelar a jugador de ser posible. Devuelve si ha sido encarcelado
    def encarcelar(numCasillaCarcel)
      if debeSerEncarcelado
        moverACasilla(numCasillaCarcel)
        @encarcelado=true
        Diario.instance.ocurreEvento("Jugador " + @nombre + " ha sido enviado a la carcel")
      end

      return @encarcelado
    end

    #Otorga salvoconducto a jugador si no está encarcelado. Devuelve si se le otorga
    def obtenerSalvoconducto(sorpresa)
      if @encarcelado
        return false
      else

        @salvoconducto=sorpresa
        return true
      end
    end

    #Quita el salvoconducto a jugador
    def perderSalvoconducto
      @salvoconducto.usada
      @salvoconducto=nil      
    end

    #Devuelve si el jugador posee salvoconducto
    def tieneSalvoconducto
      if @salvoconducto != nil
        return true
      end
      
      return false
    end

    #Devuelve si el jugador puede comprar casillas
    def puedeComprarCasilla
      @puedeComprar= !@encarcelado
      return @puedeComprar
    end

    #Realiza el proceso de pago
    def paga(cantidad)
      return modificarSaldo(cantidad*-1)
    end

    #Realiza el proceso de pago de impuestos de ser posible. Devuelve si es posible
    def pagaImpuesto(cantidad)
      if @encarcelado
        return false
      else
        return paga(cantidad)
      end
    end

    #Realiza el proceso de pagar el alquiler de ser posible. Devuelve si es posible
    def pagaAlquiler(cantidad)
      if @encarcelado
        return false
      else
        return paga(cantidad)
      end
    end

    #Realiza el proceso de recibir dinero de ser posible. Devuelve si es posible
    def recibe(cantidad)
      if @encarcelado
        return false
      else
        return modificarSaldo(cantidad)
      end
    end

    #Incrementa el saldo
    def modificarSaldo(cantidad)
      @saldo+=cantidad
     
      Diario.instance.ocurreEvento("El jugador "+@nombre+(cantidad>0 ? " añade":" retira")+cantidad.to_s+" a su saldo")
      
      return true
    end

    #Mueve al jugador a la casilla indicada
    def moverACasilla(numCasilla)
      if !@encarcelado
      
        @numCasillaActual=numCasilla
        
        puedeComprar=false
       
        Diario.instance.ocurreEvento("El jugador "+@nombre+" ha sido trasladado a la casilla "+numCasilla.to_s)
        return true
      end
      return false
    end
    
    #Devuelve si el jugador puede gastar la cantidad indicada
    def puedoGastar(precio)
      if !@encarcelado && @saldo>precio
        return true
      end

      return false
    end

    #Realiza todo el procedimiento de venta de una propiedad ip, de ser posible. Devuelve si es posible
    def vender(ip)
      if !@encarcelado && existeLaPropiedad(ip)
        if @propiedades.at(ip).vender(self)
          Diario.instance.ocurreEvento("Jugador "+@nombre+" ha vendido propiedad "+@propiedades.at(ip).nombre)
          @propiedades.delete_at(ip)
          
          return true
        end
      end
      
      return false
    end

    #Devuelve si el jugador tiene propiedades
    def tieneAlgoQueGestionar
      tiene = false
      if @propiedades.size > 0
        tiene = true
      end
      return tiene
    end
    
    #Devuelve si tiene dinero para salir de la cárcel
    def puedeSalirCarcelPagando
      return @saldo>@@PrecioLibertad
    end

    #Saca de la carcel a jugador de ser posible. Devuelve si es posible
    def salirCarcelPagando
      if puedeSalirCarcelPagando
        paga(@@PrecioLibertad)
        @encarcelado=false
        Diario.instance.ocurreEvento("El jugador "+@nombre+" ha salido de la carcel pagando la fianza")

        return true
      end

      return false
    end

    #Saca de la carcela al jugador si la tirada es favorable. Devuelve si la tirada fue favorable
    def salirCarcelTirando
      if Dado.instance.tirar>=5 #reglas del juego
        @encarcelado=false
        Diario.instance.ocurreEvento("El jugador "+@nombre+" ha salido de la carcel mediante tirada de dado")
        return true
      end

      return false
    end

    #Realiza el proceso por el que se le ingresa dinero al jugador por pasar por salida
    def pasaPorSalida
      modificarSaldo(@@PasoPorSalida)
      Diario.instance.ocurreEvento("El jugador "+@nombre+" pasa por salida y se lleva "+@@PasoPorSalida.to_s)

      return true
    end

    #compareTo de Jugador
    def <=> (otro)
      @saldo <=> otro.saldo
    end

    #Devuelve la cantidad de casas y hoteles de jugador
    def cantidadCasasHoteles
      total=0
        
      for prop in @propiedades
        total += prop.numCasas
        total += prop.numHoteles
      end
        
      return total
    end

    #Devuelve si el jugador está en bancarrota
    def enBancarrota        
      if @saldo < 0
        return true
      else
        return false
      end
    end

    #Devuelve si existe la propiedad con índice ip
    def existeLaPropiedad(ip)
      existe = false
        
      if @propiedades.size>0  &&  propiedades.at(ip)!=nil
        if @propiedades.at(ip).propietario==self
          existe=true
        end
      end
        
      return existe
    end

    #Devuelve si el jugador puede edificar una casa en la Propiedad propiedad
    def puedoEdificarCasa(propiedad)
      if propiedad.numCasas<@@CasasMax
        return true
      else
        return false
      end
      
    end

    #Devuelve si el jugador puede edificar un hotel en la Propiedad propiedad
    def puedoEdificarHotel(propiedad)

      if propiedad.numHoteles<@@HotelesMax
        return true
      else
        return false
      end
      
    end
    
    #Devuelve si el jugador está encarcelado
    def isEncarcelado
      @encarcelado
    end
    
    #Cancela la hipoteca con índice ip
    def cancelarHipoteca(ip)
      if @encarcelado
        return false
      else
        if existeLaPropiedad(ip)
          propiedad   = @propiedades.at(ip)
          cantidad    = propiedad.importeCancelarHipoteca

          if puedoGastar(cantidad)
            propiedad.cancelarHipoteca(self)
            Diario.instance.ocurreEvento("El jugador "+@nombre+" cancela la hipoteca de la propiedad "+ip.to_s)

            return true
          end
        end
      end

      return false
    end
    
    #Comprar una propiedad de ser posible. Devuelve si ha sido posible
    def comprar(titulo)
      if (@encarcelado)
        return false
      end
      
      if (@puedeComprar)
        if (puedoGastar(titulo.precioCompra))
           
          if (titulo.comprar(self))
            @propiedades.push(titulo)
            Diario.instance.ocurreEvento("El jugador "+@nombre+" compra la propiedad "+titulo.nombre)
          
            @puedeComprar = false;
          end
        
          return true
        end
      end
      
      return false
    end
    
    #Construye un hotel en la propiedad ip
    def construirHotel (ip)
      seConstruye = false
      if (@encarcelado)
        return seConstruye
      end
      
      if (existeLaPropiedad(ip))
        propiedad = @propiedades[ip]
        
        if (puedoEdificarHotel(propiedad))
          seConstruye = propiedad.construirHotel(self)
          propiedad.derruirCasas(@@CasasPorHotel, self)
        end
        
        Diario.instance.ocurreEvento("El jugador "+@nombre+" construye un hotel en la propiedad "+propiedad.nombre)
      end
      
      return seConstruye
    end
    
    def construirCasa (ip)
      seConstruye = false
      
      if (@encarcelado)
        return seConstruye
      end
      
      if (existeLaPropiedad(ip))
        propiedad = @propiedades[ip]
        if (puedoEdificarCasa(propiedad))
          seConstruye = propiedad.construirCasa(self)
          if (seConstruye)
            Diario.instance.ocurreEvento("El jugador " + @nombre + " construye una casa en la propiedad " + propiedad.nombre)
          end
        end
      end
      
      return seConstruye
    end
    
    #Hipoteca la propiedad de índice ip de ser posible. Devuelve si fue posible
    def hipotecar(ip)
      if !@encarcelado  &&  existeLaPropiedad(ip)
        if propiedades.at(ip).hipotecar(self)
          Diario.instance.ocurreEvento("El jugador "+@nombre+ " hipoteca la propiedad "+ip.to_s)
          
          return true
        end
      end
      
      return false
    end
        
    #ESTE MÉTODO NO ES PARTE DEL GUIÓN. IMPLEMENTACIÓN PARA SOLUCIONAR VistasTextual::gestionar()
    #PARA EVITAR TENER QUE DEVOLVER TODO 'propiedades', devolvemos solo lo imprescindible
    #Devuelve el nombre de las propiedades
    def getNombrePropiedades
      nombres = Array.new;
        
      for propiedad in propiedades
        nombres<<propiedad.nombre;
      end
            
      return nombres;
    end
    
    #Devuelve jugador convertido a especulador
    def aEspeculador
      if !esEspeculador
        return JugadorEspeculador.new_otro(self, 2000)
      end
    end
    
    #ESTE MÉTODO NO ES PARTE DEL GUIÓN.
    # - Este método es para evitar tener que hacer públicas las propiedades
    #Jugador cede propiedades a otroJugador
    def cedePropiedadesA(otroJugador)
      for propiedad in @propiedades
        propiedad.actualizaPropietarioPorConversion(otroJugador);
      end
    end
    
    def self.casasMax
      return @@CasasMax
    end
    
    def self.casasPorHotel
      return @@CasasPorHotel
    end
    
    def self.hotelesMax
      return @@HotelesMax
    end
    
    def self.pasoPorSalida
      return @@PasoPorSalida
    end
    
    def self.precioLibertad
      return @@PrecioLibertad
    end
    
    def self.saldoInicial
      return @@SaldoInicial
    end
    
    #Devuelve si el jugador es especulador
    def esEspeculador
      return false
    end
  end
end