package tetrissimulador;

import java.util.ArrayList;
import java.util.List;

public class pieza {
    List<Integer> posicionX;
    List<Integer> posicionY;
    String[] posi;
    public pieza(String in){
        /*
         * - separa entre ejex¡X y ejeY
         * | separacion entre diferentes vectores
         */
        posicionX = new ArrayList<>();
        posicionY = new ArrayList<>();
        posi = in.split("|");

    }
    public void devilver(){
        System.out.println(posi);
    }
    
}
