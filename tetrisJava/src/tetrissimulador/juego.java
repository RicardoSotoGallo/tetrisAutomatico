package tetrissimulador;

import java.util.*;

//https://www.baeldung.com/java-record-keyword

public class juego {
    private HashMap<vector2D,Character> tablero;
    private HashMap<String,pieza> piezas;
    List<String> nombrePieza;
    private boolean hayFinal;
    private Integer iteracionesMaxima;
    private Integer alturaMaxima;
    private Integer mediaAlturaMaxima;
    private Integer contarIteraciones;

    private static final String RESET = "\u001B[0m";
    private static final String ROJO = "\u001B[31m";
    private static final String VERDE = "\u001B[32m";
    private static final String AMARILLO = "\u001B[33m";
    private static final String AZUL = "\u001B[34m";
    Integer alto;
    Integer ancho;
    Integer piezaActual, giroActual,ejeXActual;

    /*
        Hay que crear una funcion que eliga una pieza al azar de las disponibles y
        Otra que automatice el juego ("Lo empezamos con posiciones y movimiento al azar")
     */

    public juego(Integer altura, Integer anchura){

        alto = altura; //Altura -> alturo del juego
        ancho = anchura; // Anchura -> anchura del juego
        nombrePieza = new ArrayList<>(); //nombrePieza -> es la lista del nombre de la pieza Se usa para eleguir un nombre al azar
        tablero = new HashMap<>(); //Es el estado del tablero
        piezas = new HashMap<>(); //Es un diccionario que relaciona el nombre de las piezas con su forma
        hayFinal = false;       // Si es True significa que hay que prestar atencion a las condiciones finales
        iteracionesMaxima = -1; // numero de iteraciones maxima
        alturaMaxima = -1;      // numero de altura maxima antes de que termine
        mediaAlturaMaxima = -1; // numero de altura media maxima

        /*
        Iniciamos el tablero vacio
         */
        for(int x = 0; x < ancho;x++){
            for(int y = 0; y < alto;y++){
                tablero.put(new vector2D(x, y), 'n');
            }
        }
        /*
            Iniciamos las piezas
            x {->} , y {v} { y0-x0ay1-x1a... }
            Va por giros
            por ejemplo en el palo
            0 -> "0-1a1-1a2-1a3-1"
            1 -> "2-0a2-1a2-2a2-3"
            2 -> "0-2a1-2a2-2a3-2"
            3 -> "1-0a1-1a1-2a1-3"
         */

        /*piezas.put("punto",
            new pieza(
                "0-0",
                "0-0",
                "0-0",
                "0-0"
            )
        );
        nombrePieza.add("punto");*/

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

         */


        /*
        piezas.put("ele", 
            new pieza(
                "0-1a1-1a2-1a2-0",
                "1-0a1-1a1-2a2-2",
                "0-1a0-2a1-1a2-1",
                "0-0a1-0a1-1a1-2")
        );
        nombrePieza.add("ele");

         */

        piezas.put("cuadrado", 
            new pieza(
                "0-0a1-1a0-1a1-0",
                "0-0a1-1a0-1a1-0",
                "0-0a1-1a0-1a1-0",
                "0-0a1-1a0-1a1-0"
                )
        );
        nombrePieza.add("cuadrado");


        /*
        piezas.put("gusano", 
            new pieza(
            "0-1a1-1a1-0a2-0",
            "1-0a1-1a2-1a2-2",
            "0-2a1-2a1-1a2-1",
            "0-0a1-0a1-1a2-1"
            )
        );
        nombrePieza.add("gusano");

         */
        /*
        piezas.put("te", 
            new pieza(
                "0-1a1-1a1-0a2-1",
                "1-0a1-1a1-2a2-1",
                "0-1a1-1a2-1a1-2",
                "1-0a1-1a1-2a0-1"
                )
        );
        nombrePieza.add("te");

         */

        /*
        piezas.put("gusanoInv",
            new pieza(
            "0-0a1-0a1-1a2-1",
            "2-0a2-1a1-1a1-2",
            "0-1a1-1a1-2a2-2",
            "1-0a1-1a0-1a0-2"
            )
        );
        nombrePieza.add("gusanoInv");

         */


    }

    public void configurarLimites(boolean hayF , Integer iterMax , Integer altMax , Integer medAltMax){
        /*
        Configura las condiciones finales
         */
        hayFinal = hayF;
        iteracionesMaxima = iterMax;
        alturaMaxima = altMax;
        mediaAlturaMaxima = medAltMax;
        contarIteraciones = 0;
    }

    public void configurarLimites(boolean  hayF){
        configurarLimites(hayF, -1, -1, -1);
    }

    public void configurarLimites(boolean  hayF , Integer iterMax){
        configurarLimites(hayF, iterMax, -1, -1);
    }

    public void iniciar(){
        /*
        La idea es que inicia el juego
        Pero lo inicia controlando el juegdor manualmente por teclado
         */
        piezaActual = 0;
        giroActual = 0;
        ejeXActual = 0;
        boolean abierto = true;
        Scanner sc = new Scanner(System.in);
        //dibujar();
        while(abierto){
            abierto = leerTeclado(sc);
            borrarLlena();
            //dibujar();
            
            //System.out.println(devolverEstadoClase().toString());
        }

    }

    public String iniciarDesdePieza(String estado , String pieza){
        String res = "";


        if( nombrePieza.contains( pieza ) && estado.matches("\\d+") ){
            if(estado.length() == ancho ){
                //System.out.println("Esa pieza si esta y el estado es correcto");
                piezaActual = nombrePieza.indexOf(pieza);
                giroActual = 0;
                ejeXActual = 0;

                tablero = new HashMap<>();
                int cAux;
                Integer xx = 0;
                for(char c : estado.toCharArray()){
                    cAux = Character.getNumericValue(c);
                    for(int i = 0; i < alto;i++){
                        if (alto - i > cAux) {
                            tablero.put(new vector2D(xx, i), 'n');
                        }else{
                            tablero.put(new vector2D(xx, i), 'o');
                        }
                    }
                    xx ++;
                }

                res = "Correcto";
            }else{
                //System.out.println("El estado no tiene el tamaño concreto o no son numeros");
                res = "!errorEstado";
            }

        }else {
            //System.out.println("Lo siento esa pieza no esta");
            res = "!errorPieza";
        }
        return res;
    }

    public String iniciarDesdeEstadoCubo(String estado){
        /*
        EL OBJETIVO ES ELIMINAR ESTA FUNCION

        Inicia el juego desde un estado concreto y devuelve el estado
        Hay que crear uno pero con el resto de piezas
         */
        String res = "";
        piezaActual = 0;
        giroActual = 0;
        ejeXActual = 0;
        tablero = new HashMap<>();
        Integer cAux;
        Integer xx = 0;
        for(char c : estado.toCharArray()){
            cAux = Character.getNumericValue(c);
            for(int i = 0; i < alto;i++){
                if (alto - i > cAux) {
                    tablero.put(new vector2D(xx, i), 'n');
                }else{
                    tablero.put(new vector2D(xx, i), 'o');
                }
            }
            xx ++;
        }
        return devolverEstado();

    }
    
    public String realizarMovimiento(Integer posicion , Integer giro){
        /*
        EL OBJETIVO ES ELIMIANR ESTA FUNCION
        Genera el siguiente estado o juego un estado
        Para ello se le introduce un giro y una pasicion
         */
        contarIteraciones += 1;
        giroActual = giro;
        ejeXActual = -posicion;
        bajarPieza();
        borrarLlena();
        String res = devolverEstado();
        //dibujar();
        if (hayFinal) {
            if(iteracionesMaxima > 0){
                if(contarIteraciones >= iteracionesMaxima) res = "final";
            }
            if (alturaMaxima > 0) {
                if(
                    devolverAlturas().stream().anyMatch(t -> t > alturaMaxima)
                ) res = "final";
            }
            if (mediaAlturaMaxima > 0) {
                if(
                    devolverAlturas().stream().mapToInt(Integer::intValue)
                    .average()
                    .orElse(0.0) > mediaAlturaMaxima
                ) res = "final";
            }
        }
        return res;

    }


    public String realizarMovimientoDevClase(Integer posicion , Integer giro , int siguientePieza){
        /*
        Genera el siguiente estado o juego un estado
        Para ello se le introduce un giro y una pasicion
         */
        Random azar = new Random();
        contarIteraciones += 1;
        giroActual = giro;
        ejeXActual = -posicion;

        bajarPieza();
        borrarLlena();
        String res = "Correcto";
        //dibujar();
        if (hayFinal) {
            if(iteracionesMaxima > 0){
                if(contarIteraciones >= iteracionesMaxima) res = "!final_Por_Iteraciones";
            }
            if (alturaMaxima > 0) {
                if(
                        devolverAlturas().stream().anyMatch(t -> t > alturaMaxima)
                ) res = "!final_Por_AlturaMaxima";
            }
            if (mediaAlturaMaxima > 0) {
                if(
                        devolverAlturas().stream().mapToInt(Integer::intValue)
                                .average()
                                .orElse(0.0) > mediaAlturaMaxima
                ) res = "!final_Por_MediaMaxima";
            }
        }
        if(siguientePieza < 0){
            setPiezas(  getNombrePieza().get(azar.nextInt( getNombrePieza().size() ) )  );
        }else{
            setPiezas( getNombrePieza().get(siguientePieza) );
        }
        return res;

    }

    public void setPiezas(String p){
        piezaActual = nombrePieza.indexOf(p);
        giroActual = 0;
        ejeXActual = 0;
    }
    
    public void dibujar(){
        /*
        Dibuja por pantalla el estado actual
         */
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
        /*
        Lee el teclado y lo interpreta
         */
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
        /*
        Baja la pieza actual
         */
        List<Integer> alturas = piezas.get(nombrePieza.get(piezaActual)).devolverAlturas(giroActual);
        List<vector2D> piezaWA = piezas.get(nombrePieza.get(piezaActual)).devolverPosiciones(giroActual);
        List<Integer> altTablero = devolverAlturas();
        Integer posMirar = -ejeXActual;
        Integer posicionMaxima = 0;
        Integer bajadaMaxima = 0;
        for(Integer i : alturas){
            //System.out.println( altTablero +" - "+posMirar + " - " + alturas + " - "+nombrePieza.get(piezaActual));
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
        //System.out.println("Bajada -> "+bajadaMaxima);
        for(vector2D i : piezaWA){
            aux = new vector2D(  i.x()-ejeXActual, i.y()+bajadaMaxima);
            //System.out.println(aux);
            tablero.put(aux, 'o');
        }
    }
    
    private List<Integer> devolverAlturas(){
        /*
        Devuelve la lista de alturas
         */
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
        /*
        Borra las filas ya completadas
         */
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

    public String devolverEstado(){
        /*
        EL OBJETIVO ES ELIMINAR
        Devuelve el estado actual del juego y la pieza y giro actual
         */
        Integer maximo;
        Integer minimo;
        String res = "";
        List<Integer> ls = devolverAlturas();
        Integer m = ls.stream().min(Comparator.naturalOrder()).get();
        for(int i : devolverAlturas()){
            res += (i-m);
        }
        res += "-"+piezaActual+"-"+giroActual;
        res += "a";
        for(int giro = 0; giro < 4;giro ++){

            maximo = piezas.get(nombrePieza.get(piezaActual)).devolverMaximo(giro);
            minimo = piezas.get(nombrePieza.get(piezaActual)).devolverMin(giro);
            for(int xx = 0 ; xx < ancho ; xx++){
                if(xx + minimo >= 0 && xx+maximo <= ancho){
                    res += xx+"/"+giro+"-";
                }
            }
        }
        return res.substring(0,res.length()-1);
    }


    public Estado devolverEstadoClase(){

        /*
        Devuelve el estado actual del juego y la pieza y giro actual
         */
        Integer maximo;
        Integer minimo;
        List<Integer> accionesAux = new ArrayList<>();
        List<Integer> girosAux = new ArrayList<>();
        for(int giro = 0; giro < 4;giro ++){
            maximo = piezas.get(nombrePieza.get(piezaActual)).devolverMaximo(giro);
            minimo = piezas.get(nombrePieza.get(piezaActual)).devolverMin(giro);
            for(int xx = 0 ; xx < ancho ; xx++){
                if(xx + minimo >= 0 && xx+maximo < ancho){
                    accionesAux.add(xx);
                    girosAux.add(giro);
                }
            }
        }


        return new Estado(devolverAlturas() ,
                piezaActual , giroActual ,
                accionesAux , girosAux
                );

    }
    public List<String> getNombrePieza(){
        return new ArrayList<>(nombrePieza);
    }
}
