package tetrissimulador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

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
    Integer alto;
    Integer ancho;
    public juego(){
        alto = 6;
        ancho = 5;
        nombrePieza = new ArrayList<String>();
        tablero = new HashMap<String,Character>();
        piezas = new HashMap<>();
        for(int x = 0; x < ancho;x++){
            for(int y = 0; y < alto;y++){
                tablero.put(x+"-"+y, 'n');
            }
        }
        piezas.put("punto", 
            new pieza(
                "0-0",
                "0-0",
                "0-0",
                "0-0"
            )
        );
        nombrePieza.add("punto");
        System.out.println(piezas.get("punto").alt0);
        System.out.println(piezas.get("punto").alt1);
        System.out.println(piezas.get("punto").alt2);
        System.out.println(piezas.get("punto").alt3);
        piezas.put("palo", 
            new pieza(
                "0-1a1-1a2-1a3-1",
                "2-0a2-1a2-2a2-3",
                "0-2a1-2a2-2a3-2",
                "1-0a1-1a1-2a1-3"
            )
        );
        nombrePieza.add("palo");
        System.out.println(piezas.get("palo").alt0);
        System.out.println(piezas.get("palo").alt1);
        System.out.println(piezas.get("palo").alt2);
        System.out.println(piezas.get("palo").alt3);
        /*
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
        nombrePieza.add("gusanoInv");*/
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
        
        Scanner sc = new Scanner(System.in);
        String entrada = "";
        boolean continuarJuego = true;
        piezaActual = 0;
        giroActual = 0;
        ejeXActual = 0;
        while(continuarJuego){
            dibujar();
            continuarJuego = leerMover(sc, entrada);


        }
        
    }
    
    private boolean leerMover(Scanner sc,String entrada){
        int giroAux;
        boolean res = true;
        entrada = sc.nextLine();
        switch (entrada) {
            case "d":
                if(piezas.get(nombrePieza.get(piezaActual)).max.get(giroActual)+ejeXActual+1 < ancho){
                    ejeXActual += 1;
                }
                break;
            case "a":
                
                if(piezas.get(nombrePieza.get(piezaActual)).min.get(giroActual)+ejeXActual-1 >= 0){
                    ejeXActual -= 1;
                }
                break;
            
            case "q":
                giroAux = (giroActual +1)%4;
                if(
                (   piezas.get(nombrePieza.get(piezaActual)).min.get(giroAux)+ejeXActual >= 0 &&
                    piezas.get(nombrePieza.get(piezaActual)).max.get(giroAux)+ejeXActual < ancho)
                ){
                    giroActual = giroAux;
                        
                }else{
                    System.out.println("No se puede girar");
                }
                break;
            case "w":
                piezaActual = (piezaActual + 1)%nombrePieza.size();
                break;
            case "s":
                break;
            default:
                res = false;
        }
        return res;
    }

    private void bajarPieza(){
    }

    private void dibujar(){
        String texto = "";
        int posiblePieza = 5;
        for(int y = -1 ; y < alto +1 ; y++){
            for(int x = -1 ; x < ancho +1; x++){
                if( y == -1 || x == -1 || x == ancho || y == alto){
                    texto += ROJO+"M"+RESET;
                }else{
                    
                    if(posiblePieza > 0){
                        if(piezas.get(nombrePieza.get(piezaActual)).estaPieza(x-ejeXActual, y, giroActual)) {
                            texto += AZUL+"P"+RESET;
                        }else{
                            texto += "X";
                        }
                        
                    }else{
                        texto += "X";
                    }
                    
                }
                
            }
            texto += "\n";
            posiblePieza--;
        }
        System.out.println(texto);
        
    }


}
