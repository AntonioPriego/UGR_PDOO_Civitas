package JuegoTexto;


import civitas.CivitasJuego;
import civitas.Diario;
import civitas.OperacionesJuego;
import civitas.SalidasCarcel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class VistaTextual {

    CivitasJuego juegoModel;
    int iGestion = -1;
    int iPropiedad = -1;
    private static String separador = "=====================";

    private Scanner in;

    //Constructor
    //MODIFICACIÓN AL MARGEN DE GUIÓN:
    //  -Lo he puesto 'public' ya que es necesario dado que el modelo se
    //   implementa en el paquete civitas
    public VistaTextual()
    {
        in = new Scanner(System.in);
    }

    void mostrarEstado(String estado)
    {
        System.out.println(estado);
    }

    void pausa()
    {
        System.out.print("Pulsa una enter(↵) para continuar");
        in.nextLine();
    }

    int leeEntero(int max, String msg1, String msg2)
    {
        Boolean ok;
        String cadena;
        int numero = -1;
        do {
            System.out.print(msg1);
            cadena = in.nextLine();
            try {
                numero = Integer.parseInt(cadena);
                ok = true;
            } catch (NumberFormatException e) { // No se ha introducido un entero
                System.out.println(msg2);
                ok = false;
            }
            if (ok && (numero < 0 || numero >= max)) {
                System.out.println(msg2);
                ok = false;
            }
        } while (!ok);

        return numero;
    }

    int menu(String titulo, ArrayList<String> lista)
    {
        String tab = "  ";
        int opcion;
        System.out.println(titulo);
        for (int i = 0; i < lista.size(); i++) {
            System.out.println(tab + i + "-" + lista.get(i));
        }

        opcion = leeEntero(lista.size(),
                "\n" + tab + "Elige una opción: ",
                tab + "Valor erróneo");
        return opcion;
    }

    //Menú para salir de cárcel
    SalidasCarcel salirCarcel()
    {
        int opcion = menu("Elige la forma para intentar salir de la carcel",
                new ArrayList<>(Arrays.asList("Pagando", "Tirando el dado")));
        return (SalidasCarcel.values()[opcion]);
    }

    //Menú para comprar la calle actual
    civitas.Respuestas comprar()
    {
        int opcion = menu("¿Desea comprar esta calle?",
                new ArrayList<>(Arrays.asList("Sí", "No")));
        return civitas.Respuestas.values()[opcion];
    }

    //Para declarar una gestión y la propiedad a gestionar
    void gestionar()
    {
        int gestion, propiedad;
        
        gestion = menu("¿Qué gestión desea realizar?",
                new ArrayList<>(Arrays.asList("Vender", "Hipotecar", "Cancelar hipoteca",
                        "Construir casa", "Construir hotel", "Terminar")));
        
        if (gestion != 5) {
            propiedad = menu("¿Sobre qué propiedad quiere operar?",
                juegoModel.getJugadorActual().getNombrePropiedades());
        }
        else
            propiedad = -1;
            

        iGestion = gestion;
        iPropiedad = propiedad;
    }

    //Get de iGestion
    public int getGestion() { return iGestion; }

    //Get de iPropiedad
    public int getPropiedad() { return iPropiedad; }

    //Muestra en texto la siguiente operación
    void mostrarSiguienteOperacion(OperacionesJuego operacion)
    {
        System.out.print("La siguiente operación es: " + operacion.toString());
    }

    //Muestra los eventos pendientes en el Diario
    void mostrarEventos()
    {
        while (Diario.getInstance().eventosPendientes()) {
            System.out.print(Diario.getInstance().leerEvento());
        }
    }

    //Da valor al atributo civitas y muestra el estado actual del juego
    public void setCivitasJuego(CivitasJuego civitas)
    {
        juegoModel = civitas;
        this.actualizarVista();
    }

    //Muestra la informacion actual del jugador, sus propiedades y la casilla
    void actualizarVista()
    {
        System.out.println("JUGADOR:\n" + juegoModel.getJugadorActual().toString());
        System.out.println("CASILLA:\n" + juegoModel.getCasillaActual().toString());
    }
}
