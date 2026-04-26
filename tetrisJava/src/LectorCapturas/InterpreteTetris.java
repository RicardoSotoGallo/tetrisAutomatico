package LectorCapturas;

import tetrissimulador.pieza;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InterpreteTetris {
    private HashMap<String, pieza> piezas;
    List<String> nombrePieza;

    public InterpreteTetris(){
        piezas = new HashMap<>();
        nombrePieza = new ArrayList<>();

        piezas.put("palo",
                new pieza(
                        "0-1a1-1a2-1a3-1",
                        "2-0a2-1a2-2a2-3",
                        "0-2a1-2a2-2a3-2",
                        "1-0a1-1a1-2a1-3"
                )
        );
        nombrePieza.add("palo");

        //System.out.println(piezas.get("palo"));

        piezas.put("eleInv",
                new pieza(
                        "0-0a0-1a1-1a2-1",
                        "1-0a2-0a1-1a1-2",
                        "0-1a1-1a2-1a2-2",
                        "1-0a1-1a1-2a0-2"

                )
        );

        nombrePieza.add("eleInv");



        piezas.put("ele",
                new pieza(
                        "0-1a1-1a2-1a2-0",
                        "1-0a1-1a1-2a2-2",
                        "0-1a0-2a1-1a2-1",
                        "0-0a1-0a1-1a1-2")
        );
        nombrePieza.add("ele");


        piezas.put("cuadrado",
                new pieza(
                        "0-0a1-1a0-1a1-0",
                        "0-0a1-1a0-1a1-0",
                        "0-0a1-1a0-1a1-0",
                        "0-0a1-1a0-1a1-0"
                )
        );
        nombrePieza.add("cuadrado");


        piezas.put("gusano",
                new pieza(
                        "0-1a1-1a1-0a2-0",
                        "1-0a1-1a2-1a2-2",
                        "0-2a1-2a1-1a2-1",
                        "0-0a1-0a1-1a2-1"
                )
        );
        nombrePieza.add("gusano");


        piezas.put("te",
                new pieza(
                        "0-1a1-1a1-0a2-1",
                        "1-0a1-1a1-2a2-1",
                        "0-1a1-1a2-1a1-2",
                        "1-0a1-1a1-2a0-1"
                )
        );
        nombrePieza.add("te");


        piezas.put("gusanoInv",
                new pieza(
                        "0-0a1-0a1-1a2-1",
                        "2-0a2-1a1-1a1-2",
                        "0-1a1-1a1-2a2-2",
                        "1-0a1-1a0-1a0-2"
                )
        );
        nombrePieza.add("gusanoInv");
    }
}
