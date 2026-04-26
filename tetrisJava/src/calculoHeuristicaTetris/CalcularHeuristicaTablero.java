package calculoHeuristicaTetris;

import tetrissimulador.Estado2;
import tetrissimulador.juego;
import tetrissimulador.vector2D;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/*
Vamos a explicar lo que hay que hacer
Toca hacer un rework de todo_

 */


public class CalcularHeuristicaTablero {
    //usamos stack o pilas para las nodos
    private juego game;
    private Integer altura,anchura;
    private HashMap<vector2D,Integer> puntuacionCasilla;
    private List<vector2D> posiblesHuecos;
    public resultadoHeuristica resultadoFinal;


    public CalcularHeuristicaTablero(Integer altura , Integer anchura ,
                                     Integer iteracionesMaximas, Integer alturaMaxima, Integer alturaMedia,
                                     Boolean visualizacion, Integer piezaEleguida){
        // -- Esta funcion tiene que sustituir a la anterior.
        // -- Una vez iniciada se incia el juego y se puntua
        // Se escoje la mejorAccion() y se juega
        // -- para cuando el juego se lo pide
        String resultado;
        Estado2 estado;
        game = new juego(altura , anchura);
        this.altura = altura;
        this.anchura = anchura;
        game.configurarLimites(true , iteracionesMaximas , alturaMaxima , alturaMedia);
        game.getNombrePieza().size();
        Random random = new Random();
        String elementoAleatorio = "";
        String tipoDePartida = "";
        if(piezaEleguida < 0){
            elementoAleatorio = game.getNombrePieza().get( random.nextInt(game.getNombrePieza().size())  );
            tipoDePartida = "Pieza_Azar";
        }else{
            elementoAleatorio = game.getNombrePieza().get(piezaEleguida);
            tipoDePartida = game.getNombrePieza().get(piezaEleguida);
        }
        resultado = game.iniciarDesdePieza("000000",elementoAleatorio);
        //System.out.println(resultado);
        GiroPuntuacionPosicion mejorAccionVariable;
        resultadoFinal = new resultadoHeuristica(tipoDePartida);
        long inicio, fin,duracion;
        Integer numeroPieza;
        inicio = System.nanoTime();
        fin = System.nanoTime();
        duracion = fin - inicio;
        while (resultado.equals("Correcto") ){
            if(visualizacion) game.dibujar();
            /*Este es el proceso*/
            inicio = System.nanoTime(); // 🔵 línea A

            estado = game.devolverEstado2Clase();

            calcularCasilla( estado );

            mejorAccionVariable = mejorAccion(estado , posiblesHuecos , puntuacionCasilla);

            fin = System.nanoTime(); // 🔴 línea B
            duracion = fin - inicio;
            //System.out.println("Tiempo en nanosegundos: " + duracion);
            //System.out.println("Tiempo en milisegundos: " + duracion / 1_000_000.0);
            numeroPieza = estado.tablero().values().stream()
                    .filter(c -> c.equals('o')).toList().size();
            //System.out.println("pieza Ocupada-> "+ numeroPieza
            //);
            /*
                Aqui es donde guardo datos
             */

            resultadoFinal.addAccion(mejorAccionVariable.posicion() , mejorAccionVariable.giro(),
                    game.getNombrePieza().get(estado.pieza()),
                    numeroPieza,duracion / 1_000_000.0
                    );


            resultado = game.realizarMovimientoDevClase(
                    mejorAccionVariable.posicion() ,
                    mejorAccionVariable.giro() ,
                    piezaEleguida);

        }
        resultadoFinal.setTipoFinal(resultado);
    }

    private void calcularCasilla(Estado2 estado){
        HashMap<vector2D, Character> diccionarioCasilla = estado.tablero();
        puntuacionCasilla = new HashMap<>();
        posiblesHuecos = new ArrayList<>();
        Integer valorAux,valorAux2;
        char casillaAux;
        Integer x,y;
        vector2D vn , vm , vDw,vUp;

        for(vector2D i : diccionarioCasilla.keySet()){
            valorAux = 0;
            casillaAux = diccionarioCasilla.get(i);
            x = i.x();
            y = i.y();
            vn = new vector2D(x - 1,y);
            vm = new vector2D(x + 1,y);
            vDw = new vector2D(x,y-1);
            vUp = new vector2D(x,y+1);
            if(casillaAux == 'o'){
                valorAux = -1;
            }
            else{
                //(x + 1)+","+y
                if(x == 0 && diccionarioCasilla.get( vm ).equals('o')){
                    valorAux += 4;
                }
                //(x - 1)+","+y
                if(x == anchura - 1 && diccionarioCasilla.get( vn ).equals('o')){
                    valorAux += 4;
                }
                //(x + 1)+","+y -- (x - 1)+","+y
                if(!(x == 0 || x == anchura - 1)  ){
                    if(diccionarioCasilla.get( vm ).equals('o') && diccionarioCasilla.get( vn ).equals('o')){
                        valorAux += 3;
                    }
                }
                //x+","+(y-1)
                if( y != 0){//i.getX()     , i.getY() - 1
                    if( diccionarioCasilla.get( vDw).equals('o') ) {
                        valorAux++;
                        if(y > altura/2){
                            valorAux += 1;
                        }
                        if(y > altura * 2/3){
                            valorAux += 1;
                        }
                    }
                }
                //x+","+(y+1)
                if( y + 1 != altura){//i.getX()     , i.getY() + 1
                    if( diccionarioCasilla.get( vUp ).equals('o') ) {
                        valorAux++;
                        if(y > altura/2){
                            valorAux += 1;
                        }
                        if(y > altura * 2/3 ){
                            valorAux += 1;
                        }
                    }
                }else{
                    valorAux++;
                    if(y > altura/2){
                        valorAux += 1;
                    }
                    if(y > altura * 2/3){
                        valorAux += 1;
                    }

                }
                //
                if(valorAux > 0){
                    posiblesHuecos.add(new vector2D(x,y));
                }


            }
            puntuacionCasilla.put(new vector2D(x,y) , valorAux);


        }
        for(int yi = 1 ; yi < altura ; yi++){
            for(int xi = 0 ; xi < anchura ; xi ++){
                valorAux2 = puntuacionCasilla.get(new vector2D(xi,yi));
                if(valorAux2 >= 0){
                    valorAux = puntuacionCasilla.get( new vector2D(xi, yi-1));
                    if(valorAux >= 0){
                        puntuacionCasilla.replace(new vector2D(xi,yi) , valorAux + valorAux2);
                    }else{
                        puntuacionCasilla.replace(new vector2D(xi,yi) , -1);
                        posiblesHuecos.remove(new vector2D(xi,yi));
                    }
                }
            }
        }
        posiblesHuecos.sort( Comparator.comparingInt( t -> puntuacionCasilla.get(t)   ).reversed() );

    }


    private void pintarNum(){
        String texto = "";
        for(int y = 0 ; y < altura ; y++){
            for(int x = 0; x < anchura ; x++){
                texto += puntuacionCasilla.get(new vector2D(x,y)).toString()+"\t";
            }
            System.out.println(texto);
            texto = "";
        }
    }
    /*
    for(String i : diccionarioCasilla.keySet()){
                valorAux = 0;
                casillaAux = diccionarioCasilla.get(i);
                String[] c = i.split(",");
                x = Integer.valueOf(c[0]);
                y = Integer.valueOf(c[1]);
                if("Casilla".equals(casillaAux.tipo)){
                    casillaAux.valor = -1;
                }else{
                    if(x == 0 && diccionarioCasilla.get( (x + 1)+","+y ).tipo.equals("Casilla")){
                        valorAux += 4;
                    }

                    if(x == tamañoAncho - 1 && diccionarioCasilla.get( (x - 1)+","+y ).tipo.equals("Casilla")){
                        valorAux += 4;
                    }

                    if(!(x == 0 || x == tamañoAncho - 1)  ){
                        if(diccionarioCasilla.get( (x + 1)+","+y ).tipo.equals("Casilla") && diccionarioCasilla.get( (x - 1)+","+y ).tipo.equals("Casilla")){
                            valorAux += 3;
                        }
                    }

                    //&& diccionarioCasilla.get( (x + 1)+","+y ).tipo.equals("Casilla")

                    if( y != 0){//i.getX()     , i.getY() - 1
                        if( diccionarioCasilla.get( x+","+(y-1)).tipo.equals("Casilla") ) {
                            valorAux++;
                            if(y > tamañoAlto/2){
                                valorAux += 1;
                            }
                            if(y > tamañoAlto * 2/3){
                                valorAux += 1;
                            }
                        }
                    }

                    if( y + 1 != tamañoAlto){//i.getX()     , i.getY() + 1
                        if( diccionarioCasilla.get( x+","+(y+1) ).tipo.equals("Casilla") ) {
                            valorAux++;
                            if(y > tamañoAlto/2){
                                valorAux += 1;
                            }
                            if(y > tamañoAlto * 2/3 ){
                                valorAux += 1;
                            }
                        }
                    }else{
                        valorAux++;
                        if(y > tamañoAlto/2){
                            valorAux += 1;
                        }
                        if(y > tamañoAlto * 2/3){
                            valorAux += 1;
                        }

                    }
                    ----
                    if(valorAux > 0){
                        posiblesHuecos.add(casillaAux);
                    }
                    casillaAux.valor = valorAux;
                }

            }

            for(int yi = 1 ; yi < tamañoAlto ; yi++){
                for(int xi = 0 ; xi < tamañoAncho ; xi ++){
                    casillaAux = diccionarioCasilla.get(xi+","+yi);
                    if(casillaAux.valor >= 0){
                        valorAux = diccionarioCasilla.get(xi+","+(yi - 1)).valor;
                        if(valorAux >= 0){
                            casillaAux.valor += (valorAux);
                        }else{
                            casillaAux.valor = -1;
                            if(posiblesHuecos.contains(casillaAux)){
                                posiblesHuecos.remove(casillaAux);
                            }
                        }
                    }
                }
            }
            posiblesHuecos.sort( Comparator.comparingInt(t -> -t.valor) );
     */
    // -- hay que crear una funcion de dando un tablero que devuelva en una lista los posibles huecos --

    private static GiroPuntuacionPosicion mejorAccion(Estado2 estado , List<vector2D> huecosVacios , HashMap<vector2D,Integer> puntuacionCasilla){

        // -- Aqui se recorreria los huecos de forma ordenada y se pararia cuando se encuentre uno bueno --
        // -- al final esto va a devolver una accion --

        GiroPuntuacionPosicion mejor =  mejorAccionRecursivo(
                estado ,
                new ArrayList<>( huecosVacios ) ,
                puntuacionCasilla , new ArrayList<>() ,
                estado.piezaActual().devolverAlturas(0).size()+1,
                new GiroPuntuacionPosicion(-1,-1,-1)

        );
        return mejor;

    }

    private static GiroPuntuacionPosicion mejorAccionRecursivo(Estado2 estado,
                                             List<vector2D> huecosVaciosCopia,
                                             HashMap<vector2D,Integer> puntuacionCasilla,
                                             List<Integer> peoresPuntuaciones,
                                             Integer tamanoPieza,
                                             GiroPuntuacionPosicion puntucionMaxima
                                             ){
        if(!huecosVaciosCopia.isEmpty()){
            vector2D hueco = huecosVaciosCopia.getFirst();
            huecosVaciosCopia.removeFirst();
            peoresPuntuaciones.add( puntuacionCasilla.get(hueco) );
            if( peoresPuntuaciones.size() > tamanoPieza ){
                peoresPuntuaciones.removeFirst();
            }
            Integer minimaPuntuacion = peoresPuntuaciones.stream().mapToInt(Integer::intValue).sum();
            GiroPuntuacionPosicion giroPuntuacion = CalcularHeuristicaSingular.calcularHeuristica(estado , hueco , puntuacionCasilla);
            //System.out.println(giroPuntuacion);
            if( giroPuntuacion.puntuacion() > 0  ){
                puntucionMaxima = new GiroPuntuacionPosicion(giroPuntuacion.giro(), hueco.x() - giroPuntuacion.posicion(), giroPuntuacion.puntuacion());

            }
            //&& puntucionMaxima.puntuacion() < giroPuntuacion.puntuacion()

            if(puntucionMaxima.puntuacion() < minimaPuntuacion){
                puntucionMaxima = mejorAccionRecursivo(estado,huecosVaciosCopia,puntuacionCasilla , peoresPuntuaciones , tamanoPieza , puntucionMaxima);
            }
        }
        return puntucionMaxima;
    }


}
