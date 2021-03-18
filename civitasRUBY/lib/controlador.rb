#encoding:utf-8

require_relative 'salidas_carcel'
require_relative 'operaciones_juego'
require_relative 'operacion_inmobiliaria'
require_relative 'gestiones_inmobiliarias'

module Civitas
  class Controlador
    
    #Constructor
    def initialize(_juego, _vista)
      @juego = _juego
      @vista = _vista      
    end

    #Método único de Controlador. Donde se llevará a cabo la partida
    def juega
      @vista.setCivitasJuego(@juego)
      it=0
      
      until @juego.finalDelJuego
        it+=1
        puts "\n\n=========== "+it.to_s+" ===========\n\n"
        
        @vista.actualizarVista
        @vista.pausa
        operacion = @juego.siguientePaso
        @vista.mostrarSiguienteOperacion(operacion)
        
        if operacion != OperacionesJuego::PASAR_TURNO
          @vista.mostrarEventos
        end
        
        if !@juego.finalDelJuego
          case operacion
          when Civitas::OperacionesJuego::COMPRAR
            if @vista.comprar == ListaRespuestas.at(0)
              @juego.comprar
            end
            @juego.siguientePasoCompletado(operacion)
          
          when Civitas::OperacionesJuego::GESTIONAR            
            @vista.gestionar
            gestion   = @vista.iGestion
            propiedad = @vista.iPropiedad
            
            operacionInmob = OperacionInmobiliaria.new(Civitas::ListaGestiones[gestion], propiedad)
            
            case operacionInmob.gestion
              when Civitas::GestionesInmobiliarias::VENDER
                @juego.vender(propiedad)
              when Civitas::GestionesInmobiliarias::HIPOTECAR
                @juego.hipotecar(propiedad)
              when Civitas::GestionesInmobiliarias::CANCELAR_HIPOTECA
                @juego.cancelarHipoteca(propiedad)
              when Civitas::GestionesInmobiliarias::CONSTRUIR_CASA
                @juego.construirCasa(propiedad)
              when Civitas::GestionesInmobiliarias::CONSTRUIR_HOTEL
                @juego.construirHotel(propiedad)
              when Civitas::GestionesInmobiliarias::TERMINAR
                @juego.siguientePasoCompletado(operacion)
            end
          
          when Civitas::OperacionesJuego::SALIR_CARCEL
            if (@vista.salirCarcel == Civitas::SalidasCarcel::PAGANDO)
              @juego.salirCarcelPagando
            else
              @juego.salirCarcelTirando
            end
            
            @juego.siguientePasoCompletado(operacion)
          end
        end
      end
      
      i=1
      puts "**********Juego terminado**********\n"
      for jugador in @juego.ranking
        puts i.to_s+") Jugador "+jugador.nombre+"(Saldo: "+jugador.saldo.to_s+")"
        i+=1
      end
    end
  
  end

end