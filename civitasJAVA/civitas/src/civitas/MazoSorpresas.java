package civitas;

import java.util.ArrayList;
import java.util.Collections;


public class MazoSorpresas {
    private Boolean barajada;
    private Boolean debug;
    private Sorpresa ultimaSorpresa;
    private int usadas;

    private ArrayList<Sorpresa> sorpresas;
    private ArrayList<Sorpresa> cartasEspeciales;


    //Constructor debug
    public MazoSorpresas(Boolean debug) {
        this.debug = debug;
        init();
        if (debug == true) {
            Diario.getInstance().ocurreEvento("MazoSorpresa debug true");
        }
    }

    //Constructor sin parámetros
    public MazoSorpresas() {
        init();
        debug = false;
    }

    //Método de inicialización común a todos los constructores
    private void init() {
        sorpresas = new ArrayList<>();
        cartasEspeciales = new ArrayList<>();
        barajada = false;
        usadas = 0;
    }
    
    //Añade la sorpresa s al mazo si no ha sido barajado
    public void alMazo(Sorpresa sorpresa) {
        if (barajada == false) {
            sorpresas.add(sorpresa);
        }
    }

    //Mecánica para activación de carta especial
    public void habilitarCartaEspecial(Sorpresa sorpresa) {
        Boolean encontrada = false;
        for (int i = 0; i < cartasEspeciales.size() && !encontrada; i++) {
            if (cartasEspeciales.get(i) == sorpresa) {
                encontrada = true;
                cartasEspeciales.remove(i);
                sorpresas.add(sorpresa);
                Diario.getInstance().ocurreEvento("Carta especial habilitada");
            }
        }
    }

    //Inhabilita carta sorpresa de mazo, convirtiéndola en especial
    public void inhabilitarCartaEspecial(Sorpresa sorpresa) {
        Boolean encontrada = false;
        for (int i = 0; i < sorpresas.size() && !encontrada; i++) {
            if (sorpresas.get(i) == sorpresa) {
                encontrada = true;
                sorpresas.remove(i);
                cartasEspeciales.add(sorpresa);
                Diario.getInstance().ocurreEvento("Carta especial inhabilitada");
            }
        }
    }

    //Devuelve la siguiente sorpresa, barajando si no está barajado
    public Sorpresa siguiente() {
        if (barajada == false || usadas == sorpresas.size()) {
            if (debug == false) {
                Collections.shuffle(sorpresas);
                usadas = 0;
                barajada = true;
            }

        }

        usadas++;
        ultimaSorpresa = sorpresas.get(0);
        sorpresas.remove(0);
        sorpresas.add(ultimaSorpresa);

        return ultimaSorpresa;
    }

}
