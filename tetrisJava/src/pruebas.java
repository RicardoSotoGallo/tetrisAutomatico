
import algoritmoAprendizajePorRefuerzo.refuerzo;
import tetrissimulador.Estado;
import tetrissimulador.juego;

import java.util.List;
import java.util.Random;


public class pruebas {
    public static void main(String[] args) {

        refuerzo i = new refuerzo();
        i.entrenar( false  , 6 , 10 , 20, 2000 , 3);

    }
}