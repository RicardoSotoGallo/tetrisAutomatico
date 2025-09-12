package tetrissimulador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

//https://www.baeldung.com/java-record-keyword

public class juego {
    private HashMap<vector2D,Character> tablero;
    private HashMap<String,pieza> piezas;
    List<String> nombrePieza;
    public static final String RESET = "\u001B[0m";
    public static final String ROJO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARILLO = "\u001B[33m";
    public static final String AZUL = "\u001B[34m";
    Integer alto;
    Integer ancho;
    Integer piezaActual, giroActual,ejeXActual;
    
    public juego(){
        alto = 20;
        ancho = 10;
        nombrePieza = new ArrayList<>();
        tablero = new HashMap<>();
        piezas = new HashMap<>();
        
        for(int x = 0; x < ancho;x++){
            for(int y = 0; y < alto;y++){
                tablero.put(new vector2D(x, y), 'n');
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
        piezas.put("palo", 
            new pieza(
                "0-1a1-1a2-1a3-1",
                "2-0a2-1a2-2a2-3",
                "0-2a1-2a2-2a3-2",
                "1-0a1-1a1-2a1-3"
            )
        );
        System.out.println(piezas.get("palo"));
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
            

    }
    
    public void iniciar(){
        piezaActual = 1;
        giroActual = 0;
        ejeXActual = 0;
        boolean abierto = true;
        Scanner sc = new Scanner(System.in);
        dibujar();
        while(abierto){
            abierto = leerTeclado(sc);
            borrarLlena();
            dibujar();
            
            System.out.println(devolverAlturas());
        }

    }
    
    private void dibujar(){
        String res = "";
        List<vector2D> posicionesPieza = piezas.get(nombrePieza.get(piezaActual)).devolverPosiciones(giroActual);
        vector2D auxVector,auxVectorPieza;
        for(int yy = 0; yy < alto;yy++){
            for(int xx = 0; xx < ancho; xx++){
                auxVector = new vector2D(xx, yy);
                auxVectorPieza = new vector2D(xx+ejeXActual, yy);
                if (posicionesPieza.contains(auxVectorPieza)) {
                    res += ROJO+"P"+RESET;
                }else if(tablero.get(auxVector).equals('n')){
                    res += AZUL+"N"+RESET;
                }else{
                    res += VERDE+"O"+RESET;
                }
            }
            res += "\n";
        }
        System.out.println(res);
    }

    private boolean leerTeclado(Scanner sc){
        boolean res = true;
        String entrada = sc.nextLine();
        Integer maximo = piezas.get(nombrePieza.get(piezaActual)).devolverMaximo(giroActual);
        Integer minimo = piezas.get(nombrePieza.get(piezaActual)).devolverMin(giroActual);
        switch (entrada) {
            case "d":
                if(-ejeXActual+maximo < ancho-1){
                    ejeXActual += -1;
                }
               
                break;
            case "a":
                if(-ejeXActual + minimo > 0){
                ejeXActual += 1;
                }
                break;
            case "w":
                giroActual = (giroActual+1)%4;
                break;
            case "q":
                piezaActual = (piezaActual+1)%nombrePieza.size();
                break;
            case "s":
                bajarPieza();
                break;
            
            default:
                res = false;
        }
        System.out.println(-ejeXActual+maximo+" -> "+ancho+" : "+ejeXActual);
        return res;
    }
    
    private void bajarPieza(){
        List<Integer> alturas = piezas.get(nombrePieza.get(piezaActual)).devolverAlturas(giroActual);
        List<vector2D> piezaWA = piezas.get(nombrePieza.get(piezaActual)).devolverPosiciones(giroActual);
        List<Integer> altTablero = devolverAlturas();
        Integer posMirar = -ejeXActual;
        Integer posicionMaxima = 0;
        Integer bajadaMaxima = 0;
        for(Integer i : alturas){
            if (i != 0) {
                if(i+altTablero.get(posMirar) > posicionMaxima-1){
                bajadaMaxima = alto - altTablero.get(posMirar) - i;
                posicionMaxima = i+altTablero.get(posMirar) ;
                }
                //-----------------------

            }
            posMirar++;

        }
        vector2D aux;
        System.out.println("Bajada -> "+bajadaMaxima);
        for(vector2D i : piezaWA){
            aux = new vector2D(  i.x()-ejeXActual, i.y()+bajadaMaxima);
            System.out.println(aux);
            tablero.put(aux, 'o');
        }
    }
    
    private List<Integer> devolverAlturas(){
        List<Integer> res = new ArrayList<>();
        Integer altAux;
        Integer ultimaAlturaAux = 0;
        vector2D posi;
        for(int xx = 0; xx < ancho ; xx++){
            altAux = 0;
            ultimaAlturaAux = 0;
            for(int yy = alto-1 ; yy >= 0;yy--){
                posi = new vector2D(xx, yy);
                if(tablero.get(posi).equals('o')){
                    ultimaAlturaAux = altAux+1;
                }
                altAux ++;
            }
            res.add(ultimaAlturaAux);
        }
        return res;
    }

    private void borrarLlena(){
        boolean llena;
        HashMap<vector2D,Character> tableroAux;
        vector2D v,v2;
        for(int yy = alto-1 ; yy >= 0 ; yy--){
            llena = true;
            for(int xx = 0 ; xx < ancho ; xx++){
                v = new vector2D(xx, yy);
                if(tablero.get(v) != 'o'){
                    llena = false;
                }
            }
            if(llena){
                tableroAux = new HashMap<>(tablero);
                for(int yi = yy ; yi >= 1 ; yi--){
                    for(int xi = 0 ; xi < ancho ; xi++){
                        v = new vector2D(xi, yi);
                        v2 = new vector2D(xi, yi-1);
                        tableroAux.put(v, tablero.get(v2));
                        
                    }
                }
                yy++;
                tablero = tableroAux;

            }

        }
    }
/*
    public void iniciar(){
        
        Scanner sc = new Scanner(System.in);
        String entrada = "";
        boolean continuarJuego = true;
        piezaActual = 1;
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
                bajarPieza();
                break;
            default:
                res = false;
        }
        return res;
    }

    private void bajarPieza(){
        pieza pie = piezas.get(nombrePieza.get(piezaActual));
        List<Integer> alturas = pie.devolverAlturas(giroActual);
        Integer maximaBajada, altAux , tengoQueBajar;
        String[] p;
        Integer px,py;
        maximaBajada = -1;
        tengoQueBajar = alto-1;
        for(int i = 0; i < pie.max.get(giroActual)+1;i++){
            if(alturas.get(i) != 0){
                altAux = alto;
                
                for( int j = 0; j < alto;j++ ){
                    if(tablero.get(String.valueOf(ejeXActual + i)+"-"+j) == 'u'){
                        altAux = j;
                        break;
                    }
                }
                if(altAux + alturas.get(i) > maximaBajada){
                    maximaBajada = altAux + alturas.get(i);
                    tengoQueBajar = altAux;
                }
            }
        }

        for(String i : pie.devolverPosiciones(giroActual)){
            p = i.split("-");
            px = Integer.valueOf(p[0]);
            py = Integer.valueOf(p[1]);
            tablero.put(String.valueOf(px+ejeXActual)+"-"+String.valueOf(py+tengoQueBajar-1), 'u');
        }


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
                        }else if(tablero.get(x+"-"+y) == 'u'){
                            texto += VERDE+"U"+RESET;
                        }
                        else{
                            texto += "X";
                        }
                        
                    }else{
                        if(tablero.get(x+"-"+y) == 'u'){
                            texto += VERDE+"U"+RESET;
                        }else{
                            texto += "X";
                        }
                        
                    }
                    
                }
                
            }
            texto += "\n";
            posiblePieza--;
        }
        System.out.println(texto);
        
    }

*/
}
