package tetrissimulador;

import java.util.List;

public record Estado(
        List<Integer> alturas,
        Integer pieza , Integer giro,
        List<Integer> accionPosicion,
        List<Integer> accionGiro) {
    public Estado(List<Integer> alturas, Integer pieza, Integer giro, List<Integer> accionPosicion, List<Integer> accionGiro) {
        this.alturas = alturas;
        this.pieza = pieza;
        this.giro = giro;
        this.accionPosicion = accionPosicion;
        this.accionGiro = accionGiro;

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
        return res.toString();
    }

    public Estado copia(Integer p){
        return new Estado( alturas , p, giro , accionPosicion ,accionGiro );
    }
}
