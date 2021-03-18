package civitas;

import java.util.Arrays;
import java.util.ArrayList;

import JuegoTexto.Controlador;
import JuegoTexto.VistaTextual;


public class testP3 {
    
     public static void main(String[] args) {
        CivitasJuego juego = new CivitasJuego(new ArrayList<>(Arrays.asList("Pepe","Juan","Marta")));
        VistaTextual vista = new VistaTextual();
        Controlador controlador = new Controlador(juego,vista);
        
        Dado.getInstance().setDebug(true);
        controlador.juega();
     }
}