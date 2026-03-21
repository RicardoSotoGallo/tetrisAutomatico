package tetrissimulador;

import java.util.HashMap;
import java.util.List;

public record Estado2(
        List<Integer> alturas,
        Integer pieza , Integer giro,
        List<Integer> accionPosicion,
        List<Integer> accionGiro,
        HashMap<vector2D,Character> tablero,
        pieza piezaActual) {

    public Estado2(List<Integer> alturas, Integer pieza, Integer giro, List<Integer> accionPosicion, List<Integer> accionGiro ,HashMap<vector2D,Character> tablero , pieza piezaActual) {
        this.alturas = alturas;
        this.pieza = pieza;
        this.giro = giro;
        this.accionPosicion = accionPosicion;
        this.accionGiro = accionGiro;
        this.tablero = tablero;
        this.piezaActual = piezaActual;

    }

    @Override
    public List<Integer> alturas() {
        return alturas;
    }

    @Override
    public Integer pieza() {
        return pieza;
    }

    @Override
    public Integer giro() {
        return giro;
    }

    @Override
    public List<Integer> accionPosicion() {
        return accionPosicion;
    }

    @Override
    public List<Integer> accionGiro() {
        return accionGiro;
    }

    public  String toString(){
        StringBuilder res = new StringBuilder();
        res.append("estado ->").append(alturas);
        res.append("\nPieza: ").append(pieza).append(" , Giro: ").append(giro);
        for( int i = 0 ; i < accionPosicion.size();i++ ){
            res.append("\n").append(accionPosicion.get(i)).append(" | ").append(accionGiro.get(i));
        }
        res.append("\nVersion 2.0");
        return res.toString();
    }

    public Estado2 copia(Integer p){
        return new Estado2( alturas , p, giro , accionPosicion ,accionGiro ,tablero , piezaActual);
    }
}
