package calculoHeuristicaTetris;

import tetrissimulador.Estado2;
import tetrissimulador.vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalcularHeuristicaSingular {
    String tipo = "";
    Integer valor;
    Integer x;
    Integer y;
    Integer heuristica;
    Integer posicion;
    Integer angulo;

    public static GiroPuntuacionPosicion calcularHeuristica(Estado2 estado, vector2D hueco , HashMap<vector2D,Integer> puntuacionCasilla){


        GiroPuntuacionPosicion mejorPuntuacion = new GiroPuntuacionPosicion( -1 , -1 ,-10);
        GiroPuntuacionPosicion puntuacionAux;

        puntuacionAux = calcularUnaPosicion(estado , 0 , hueco ,puntuacionCasilla);
        if(puntuacionAux.puntuacion() > mejorPuntuacion.puntuacion() && puntuacionAux.puntuacion() >= 0){
            mejorPuntuacion = new GiroPuntuacionPosicion(0 ,puntuacionAux.posicion(),puntuacionAux.puntuacion() );
        }
        puntuacionAux = calcularUnaPosicion(estado , 1 , hueco ,puntuacionCasilla);
        if(puntuacionAux.puntuacion() > mejorPuntuacion.puntuacion() && puntuacionAux.puntuacion() >= 0){
            mejorPuntuacion = new GiroPuntuacionPosicion(1 ,puntuacionAux.posicion(),puntuacionAux.puntuacion() );
        }
        puntuacionAux = calcularUnaPosicion(estado , 2 , hueco ,puntuacionCasilla);
        if(puntuacionAux.puntuacion() > mejorPuntuacion.puntuacion() && puntuacionAux.puntuacion() >= 0){
            mejorPuntuacion = new GiroPuntuacionPosicion(2 ,puntuacionAux.posicion(),puntuacionAux.puntuacion() );
        }
        puntuacionAux = calcularUnaPosicion(estado , 3 , hueco ,puntuacionCasilla);
        if(puntuacionAux.puntuacion() > mejorPuntuacion.puntuacion() && puntuacionAux.puntuacion() >= 0){
            mejorPuntuacion = new GiroPuntuacionPosicion(3 ,puntuacionAux.posicion(),puntuacionAux.puntuacion() );
        }

        return mejorPuntuacion;

    }
    
    private static GiroPuntuacionPosicion calcularUnaPosicion(Estado2 estado2 , Integer giro , vector2D hueco , HashMap<vector2D,Integer> puntuacionCasilla){
        // -- aqui dado un hueco (eso ya esta dado)
        // -- dado un giro
        // -- se calcula una puntuacion y se devuelve
        // -- la mehor obcion es juntar todo_ pero bueno ya lo vere --
        //Primer Recursivo

        List<tetrissimulador.vector2D> piezaEspacio = new ArrayList<>(estado2.piezaActual().devolverPosiciones(giro));

        GiroPuntuacionPosicion calculo;
        Integer calculoAux;
        calculo = new GiroPuntuacionPosicion( 0, 0 , -1);
        //System.out.println( "Pieza ->" + piezaEspacio +" \n hueco -> "+hueco  );
        for(vector2D referencia : piezaEspacio){
            calculoAux = calcularUnaPosicionRecursivo( piezaEspacio ,referencia , hueco , puntuacionCasilla , piezaEspacio.size() -1 );
            if(calculo.puntuacion() < calculoAux){
                calculo = new GiroPuntuacionPosicion(0, referencia.x(), calculoAux);
            }
        }

        //System.out.println("Resultado : "+calculo);


        return calculo;

    }

    private static Integer calcularUnaPosicionRecursivo(List<tetrissimulador.vector2D> piezaEspacio, vector2D piezaRefrencia , vector2D hueco , HashMap<vector2D,Integer> puntuacionCasilla , Integer recursion){

        Integer res = casillaPiezaTablero(piezaEspacio.get(recursion), piezaRefrencia , hueco , puntuacionCasilla);
        Integer resAux;
        //System.out.println(res);
        if(res >= 0){
            if(recursion != 0 ){
                resAux =  calcularUnaPosicionRecursivo(piezaEspacio, piezaRefrencia , hueco , puntuacionCasilla , recursion - 1);
                if(resAux >= 0){
                    res = res + resAux;
                }else{
                    res = -1;
                }

            }
        }


        return res;
    }

    private static Integer casillaPiezaTablero(vector2D pieza , vector2D piezaReferncia , vector2D hueco , HashMap<vector2D,Integer> puntuacionCasilla){
        Integer res = -2;
        vector2D mirar = new vector2D( pieza.x() + hueco.x() - piezaReferncia.x(), pieza.y() + hueco.y() -piezaReferncia.y() );
        if( puntuacionCasilla.containsKey( mirar  ) ){
            res = puntuacionCasilla.get(mirar);
        }

        return res;
    }
    public List<String> obtenerAccion(){
        List<String> accion = new ArrayList<>();
        switch (angulo) {
            case 1:
                accion.add("D");
                break;
            case 2:
                accion.add("W");
                break;
            case 3:
                accion.add("A");
                break;
        }
        accion.add(String.valueOf(posicion) );
        accion.add( "S");
        return accion;
    }
    @Override
    public String toString(){
        return "[tipo:"+tipo+", valor: " +valor+" (" +x+","+y +") ,posicion:"+ posicion+" ,giro:"+angulo+"]";
    }
}

