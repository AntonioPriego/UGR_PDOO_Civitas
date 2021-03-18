package JuegoTexto;


import java.util.ArrayList;
import civitas.CivitasJuego;
import civitas.GestionesInmobiliarias;
import civitas.Jugador;
import civitas.OperacionInmobiliaria;
import civitas.OperacionesJuego;
import civitas.Respuestas;
import civitas.SalidasCarcel;

public class Controlador {

    private CivitasJuego juego;
    private VistaTextual vista;

    //Constructor
    //MODIFICACIÓN AL MARGEN DE GUIÓN:
    //  -Lo he puesto 'public' ya que es necesario dado que el modelo se
    //   implementa en el paquete civitas
    public Controlador(CivitasJuego _juego, VistaTextual _vista)
    {
        juego = _juego;
        vista = _vista;
    }

    //Método único de Controlador. Donde se llevará a cabo la partida
    //MODIFICACIÓN AL MARGEN DE GUIÓN:
    //  -Lo he puesto 'public' ya que es necesario dado que el modelo se
    //   implementa en el paquete civitas
    public void juega()
    {
        OperacionesJuego operacion = OperacionesJuego.AVANZAR;

        System.out.println("\t======== 0 ========");
        vista.setCivitasJuego(juego);       // Muestra el estado del juego actualizado 

        int it = 0;
        while (!juego.finalDelJuego()) {
            it++;
            //Para separar en el terminal cada iteracion
            for (int i = 0; i < 3; i++) {
                System.out.println(" ");
            }

            System.out.println("\t======== " + it + " ========");

            for (int i = 0; i < 3; i++) {
                System.out.println(" ");
            }

            vista.actualizarVista();
            vista.pausa();
            operacion = juego.siguientePaso();
            vista.mostrarSiguienteOperacion(operacion);

            if (operacion != OperacionesJuego.PASAR_TURNO) {
                vista.mostrarEventos();
            }
            
            if (!juego.finalDelJuego()) {
                switch (operacion) {
                    case COMPRAR:
                        if (vista.comprar() == Respuestas.SI) {
                            juego.comprar();
                        }
                        juego.siguientePasoCompletado(operacion);
                        break;

                    case GESTIONAR:
                        vista.gestionar();
                        int iGestion = vista.getGestion();
                        int iPropiedad = vista.getPropiedad();

                        OperacionInmobiliaria opInmobiliaria
                                = new OperacionInmobiliaria(GestionesInmobiliarias.values()[iGestion], iPropiedad);

                        switch (opInmobiliaria.getGestion()) {
                            case VENDER:
                                juego.vender(iPropiedad);
                                break;
                            case HIPOTECAR:
                                juego.hipotecar(iPropiedad);
                                break;
                            case CANCELAR_HIPOTECA:
                                juego.cancelarHipoteca(iPropiedad);
                                break;
                            case CONSTRUIR_CASA:
                                juego.construirCasa(iPropiedad);
                                break;
                            case CONSTRUIR_HOTEL:
                                juego.construirHotel(iPropiedad);
                                break;
                            case TERMINAR:
                                juego.siguientePasoCompletado(operacion);
                                break;
                        }
                        break;

                    case SALIR_CARCEL:
                        if (vista.salirCarcel() == SalidasCarcel.PAGANDO) {
                            juego.salirCarcelPagando();
                        } else {
                            juego.salirCarcelTirando();
                        }

                        juego.siguientePasoCompletado(operacion);
                        break;
                }
            }
        }
        
        //RANKING
        int i = 1;
        System.out.print("**********Juego terminado**********\n");
        for (Jugador jugador:juego.ranking())  {
            System.out.print("\n====================================\n"+i+")"+jugador.toString());
            i++;
        }
    }
}
