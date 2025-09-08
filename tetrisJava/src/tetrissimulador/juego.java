package tetrissimulador;

import java.util.HashMap;

public class juego {
    private HashMap<String,String> tablero;
    private HashMap<String,pieza> piezas;
    public juego(){
        piezas = new HashMap<>();
        piezas.put("palo", 
            new pieza(
                "0-1a1-1a2-1a3-1",
                "2-0a2-1a2-2a2-3",
                "0-2a1-2a2-2a3-2",
                "1-0a1-1a1-2a1-3"
            )
        );
        piezas.put("eleInv", 
            new pieza(
                "0-0a0-1a1-1a2-1",
                "1-0a2-0a1-1a1-2",
                "0-1a1-1a2-1a2-2",
                "1-0a1-1a1-2a0-2"

            )
        );
        piezas.put("ele", 
            new pieza(
                "0-1a1-1a2-1a2-0",
                "1-0a1-1a1-2a2-2",
                "0-1a0-2a1-1a2-1",
                "0-0a1-0a1-1a1-2")
        );
        piezas.put("cuadrado", 
            new pieza(
                "0-0a1-1a0-1a1-0",
                "0-0a1-1a0-1a1-0",
                "0-0a1-1a0-1a1-0",
                "0-0a1-1a0-1a1-0"
                )
        );
        piezas.put("gusano", 
            new pieza(
            "0-1a1-1a1-0a2-0",
            "1-0a1-1a2-1a2-2",
            "0-2a1-2a1-1a2-1",
            "0-0a1-0a1-1a1-2"
            )
        );
        piezas.put("te", 
            new pieza(
                "0-1a1-1a1-0a2-1",
                "1-0a1-1a1-2a2-1",
                "0-1a1-1a2-1a1-2",
                "1-0a1-1a1-2a0-1"
                )
        );
        piezas.put("gusanoInv",
            new pieza(
            "0-0a1-0a1-1a2-1",
            "2-0a2-1a1-1a1-2",
            "0-1a1-1a1-2a2-2",
            "1-0a1-1a0-1a0-2"
            )
        );
        String texto;
        for(String i : piezas.keySet()){
            texto = "";
            System.out.println(i);
            for(int n = 0 ; n < 4;n++){
                
                for(int y = 0; y < 4;y++){
                    for(int x = 0 ; x < 4; x++){
                        if(piezas.get(i).estaPieza(x, y, n)){
                            texto += "X";
                        }else{
                            texto += " ";
                        }
                    }
                    texto += "\n";
                }
                texto += "---------------\n";
            }
            
            System.out.println(texto +"\n======");
        }

    }
}
