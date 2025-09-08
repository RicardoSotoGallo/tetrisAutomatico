package tetrissimulador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class juego {
    private HashMap<String,Character> tablero;
    private HashMap<String,pieza> piezas;
    List<String> nombrePieza;
    public static final String RESET = "\u001B[0m";
    public static final String ROJO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARILLO = "\u001B[33m";
    public static final String AZUL = "\u001B[34m";
    Integer piezaActual;
    Integer giroActual;
    Integer ejeXActual;
    public juego(){
        nombrePieza = new ArrayList<String>();
        tablero = new HashMap<String,Character>();
        piezas = new HashMap<>();
        for(int x = 0; x < 10;x++){
            for(int y = 0; y < 20;y++){
                tablero.put(x+"-"+y, 'n');
            }
        }
        piezas.put("palo", 
            new pieza(
                "0-1a1-1a2-1a3-1",
                "2-0a2-1a2-2a2-3",
                "0-2a1-2a2-2a3-2",
                "1-0a1-1a1-2a1-3"
            )
        );
        nombrePieza.add("palo");
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
        /*for(String i : nombrePieza){
            System.out.println(i);
            for(int n = 0 ; n < 4;n++){
                piezas.get(i).mostrarPieza(n);    
            }
            System.out.println("=============================");
        }*/
        
        //piezas.get("gusano").mostrarPieza(0);
            

    }

    public void iniciar(){
        piezaActual = 6;
        giroActual = 0;
        ejeXActual = 0;
        dibujar();

    }
    private void dibujar(){
        String texto = "";
        int posiblePieza = 5;
        for(int y = -1 ; y < 21 ; y++){
            for(int x = -1 ; x < 11; x++){
                if( y == -1 || x == -1 || x == 10 || y == 20){
                    texto += ROJO+"M"+RESET;
                }else{
                    
                    if(posiblePieza > 0){
                        if(piezas.get(nombrePieza.get(piezaActual)).estaPieza(x+ejeXActual, y, giroActual)) {
                            texto += AZUL+"P"+RESET;
                        }else{
                            texto += "X";
                        }
                        posiblePieza--;
                    }else{
                        texto += "X";
                    }
                    
                }
            }
            texto += "\n";
        }
        System.out.println(texto);
        
    }


}
