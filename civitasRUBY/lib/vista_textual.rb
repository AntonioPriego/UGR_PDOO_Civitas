#encoding: utf-8

require_relative 'operaciones_juego'
require_relative 'respuestas'
require_relative 'salidas_carcel'
require_relative 'respuestas'
require 'io/console'

module Civitas
  class VistaTextual

    def mostrar_estado(estado)
      puts estado
    end

    def pausa
      print "Pulsa una enter(↵) para continuar"
      STDIN.getch
      print "\n"
    end

    def lee_entero(max,msg1,msg2)
      ok = false
      begin
        print msg1
        cadena = gets.chomp
        begin
          if (cadena =~ /\A\d+\Z/)
            numero = cadena.to_i
            ok = true
          else
            raise IOError
          end
        rescue IOError
          puts msg2
        end
        if (ok)
          if (numero >= max)
            ok = false
          end
        end
      end while (!ok)

      return numero
    end
    

    def menu(titulo,lista)
      tab = "  "
      puts titulo
      index = 0
      lista.each { |l|
        puts tab+index.to_s+"-"+l
        index += 1
      }

      opcion = lee_entero(lista.size,
        "\n"+tab+"Elige una opción: ",
        tab+"Valor erróneo")
      
      return opcion
    end

    #Menú para comprar la calle actual
    def comprar
      opcion = menu("¿Desea comprar esta calle? ",["Si", "No"]);
      
      return ListaRespuestas.at(opcion)
    end

    #Para declarar una gestión y la propiedad a gestionar
    def gestionar
      gestion = menu("¿Qué gestión desea realizar?", ["Vender","Hipotecar","Cancelar hipoteca",
          "Construir casa","Construir hotel","Terminar"])
      
      if gestion != 5
        propiedad = menu("¿Sobre qué propiedad quiere operar?", @juegoModel.getJugadorActual.getNombrePropiedades)
      end
      
      @iPropiedad = propiedad;
      @iGestion   = gestion;
    end

    #Get de iGestion
    def iGestion
      return @iGestion
    end

    #Get de iPropiedad
    def iPropiedad
      return @iPropiedad
    end
    
    #Muestra en texto la siguiente operación
    def mostrarSiguienteOperacion(operacion)
      puts "La siguiente operación es: " + operacion.to_s
    end
    
    #Muestra los eventos pendientes en el Diario
    def mostrarEventos
      while (Diario.instance.eventosPendientes)
        puts Diario.instance.leerEvento
      end
    end

    #Da valor al atributo civitas y muestra el estado actual del juego
    def setCivitasJuego(civitas)
      @juegoModel=civitas
      self.actualizarVista
    end
    
    #Muestra la informacion actual del jugador, sus propiedades y la casilla
    def actualizarVista
      puts "JUGADOR:" + @juegoModel.getJugadorActual.toString
      puts "CASILLA:" + @juegoModel.getCasillaActual.toString
    end
    
    #Menú para salir de cárcel
    def salirCarcel
      opcion = menu("Elige la forma para intentar salir de la carcel", ["Pagando", "Tirando el dado"])
      
      return ListaSalidas[opcion]
    end
    
  end

end