#encoding: utf-8


module Civitas
  class GestorEstados

    def estadoInicial
      return (EstadosJuego::INICIO_TURNO)
    end

    def operacionesPermitidas(jugador,estado)
      op = nil
 
      case estado

      when EstadosJuego::INICIO_TURNO
        if (jugador.isEncarcelado)
          op = OperacionesJuego::SALIR_CARCEL
        else
          op = OperacionesJuego::AVANZAR
        end

      when EstadosJuego::DESPUES_CARCEL
        op = OperacionesJuego::PASAR_TURNO

      when EstadosJuego::DESPUES_AVANZAR
        if (jugador.isEncarcelado)
          op = OperacionesJuego::PASAR_TURNO
        elsif (jugador.puedeComprar)
          op = OperacionesJuego::COMPRAR
        elsif (jugador.tieneAlgoQueGestionar)
          op = OperacionesJuego::GESTIONAR
        else
          op = OperacionesJuego::PASAR_TURNO
        end

      when EstadosJuego::DESPUES_COMPRAR
        if (jugador.tieneAlgoQueGestionar)
          op = OperacionesJuego::GESTIONAR
        else
          op = OperacionesJuego::PASAR_TURNO
        end

      when EstadosJuego::DESPUES_GESTIONAR
        op = OperacionesJuego::PASAR_TURNO
      end

      return op
    end



    def siguienteEstado(estado,operacion)
      siguiente = nil

      case estado

      when EstadosJuego::INICIO_TURNO
        if (operacion==OperacionesJuego::SALIR_CARCEL)
          siguiente = EstadosJuego::DESPUES_CARCEL
        elsif (operacion==OperacionesJuego::AVANZAR)
          siguiente = EstadosJuego::DESPUES_AVANZAR
        end


      when EstadosJuego::DESPUES_CARCEL
        if (operacion==OperacionesJuego::PASAR_TURNO)
          siguiente = EstadosJuego::INICIO_TURNO
        end

      when EstadosJuego::DESPUES_AVANZAR
        case operacion
        when OperacionesJuego::PASAR_TURNO
          siguiente = EstadosJuego::INICIO_TURNO
        when OperacionesJuego::COMPRAR
          siguiente = EstadosJuego::DESPUES_COMPRAR
        when OperacionesJuego::GESTIONAR
          siguiente = EstadosJuego::DESPUES_GESTIONAR
        end


      when EstadosJuego::DESPUES_COMPRAR
     
        if (operacion==OperacionesJuego::GESTIONAR)
          siguiente = EstadosJuego::DESPUES_GESTIONAR
   
        else
          if (operacion==OperacionesJuego::PASAR_TURNO)
            siguiente = EstadosJuego::INICIO_TURNO
          end
        end

      when EstadosJuego::DESPUES_GESTIONAR
        if (operacion==OperacionesJuego::PASAR_TURNO)
          siguiente = EstadosJuego::INICIO_TURNO
        end
      end

      Diario.instance.ocurreEvento("De: "+estado.to_s+ " con "+operacion.to_s+ " sale: "+siguiente.to_s)

      return siguiente
    end

  end
end
