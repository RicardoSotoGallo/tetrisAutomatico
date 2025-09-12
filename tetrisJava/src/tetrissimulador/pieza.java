package tetrissimulador;

import java.util.ArrayList;
import java.util.List;

public class pieza {
    Integer maximo = 100;
    Integer minimo = -1;
    Integer minAux = minimo;
    Integer maxAux = maximo;
//-------------------------------------
    private List<vector2D> pos0;
    private List<Integer> alt0;
    private int min0;
    private int max0;
//-------------------------------------
    private List<vector2D> pos1;
    private List<Integer> alt1;
    private int min1;
    private int max1;
//-------------------------------------
    private List<vector2D> pos2;
    private List<Integer> alt2;
    private int min2;
    private int max2;
//-------------------------------------
    private List<vector2D> pos3;
    private List<Integer> alt3;
    private int min3;
    private int max3;
//-------------------------------------
    String[] posi;
//-------------------------------------

    private void crearPosicion(String in,Integer giro){
        minAux = minimo;
        maxAux = maximo;
        Integer aux;
        List<vector2D> pos = new ArrayList<>();
        List<Integer> alt = new ArrayList<>();
        
        //Aqui creamos la lista de vectores
        posi = in.split("a");
        for(String i : posi){
            String[] p = i.split("-");
            aux = Integer.valueOf(p[0]);
            if(minAux.equals(minimo)){
                minAux = aux;
                maxAux = aux;
            }else{
                if(minAux > aux){
                    minAux = aux;
                }else if(maxAux < aux){
                    maxAux = aux;
                }
            }

            pos.add(new vector2D(aux, Integer.valueOf(p[1])));
            
        }
        //Aqui creamos la lista de alturas
        Integer l,o;
        for(int i = 0; i < maxAux + 1; i++){
            alt.add(0);
            for(vector2D j : pos){
                l = j.x();
                o = j.y();
                if(l == i){
                    if(o >= alt.get(i)){
                        alt.set(i, o +1);
                    }
                }
            }
        }
        switch (giro) {
            case 0 -> {
                pos0 = pos;
                alt0 = alt;
                min0 = minAux;
                max0 = maxAux;
            }
            case 1 -> {
                pos1 = pos;
                alt1 = alt;
                min1 = minAux;
                max1 = maxAux;
            }
            case 2 -> {
                pos2 = pos;
                alt2 = alt;
                min2 = minAux;
                max2 = maxAux;
            }
            case 3 -> {
                pos3 = pos;
                alt3 = alt;
                min3 = minAux;
                max3 = maxAux;
            }
        }
    }
    
    public pieza(String in0, String in1, String in2, String in3){
        /*
         * - separa entre ejex¡X y ejeY
         * | separacion entre diferentes vectores
         */
        pos0 = new ArrayList<>();
        alt0 = new ArrayList<>();

        pos1 = new ArrayList<>();
        alt1 = new ArrayList<>();

        pos2 = new ArrayList<>();
        alt2 = new ArrayList<>();

        pos3 = new ArrayList<>();
        alt3 = new ArrayList<>();

        crearPosicion(in0, 0);
        crearPosicion(in1, 1);
        crearPosicion(in2, 2);
        crearPosicion(in3, 3);

    }
    
    @Override
    public String toString(){
        String res = "0  ==========\n";
        res += pos0.toString()+"\n";
        res += alt0.toString()+"\n";
        
        res += "1  ==========\n";
        res += pos1.toString()+"\n";
        res += alt1.toString()+"\n";
        res += "2  ==========\n";
        res += pos2.toString()+"\n";
        res += alt2.toString()+"\n";
        res += "3   ==========\n";
        res += pos3.toString()+"\n";
        res += alt3.toString()+"\n";
        res += "==========\n";

        return res;
    }
    
    public List<vector2D> devolverPosiciones(Integer giro){
        List<vector2D> res = new ArrayList<>();
        switch (giro) {
            case 0:
                res = pos0;
                break;
            case 1:
                res = pos1;
                break;
            case 2:
                res = pos2;
                break;
            case 3:
                res = pos3;
                break;
            default:
                throw new AssertionError();
        }
        return res;
    }

    public List<Integer> devolverAlturas(Integer giro){
        List<Integer> res = new ArrayList<>();
        switch (giro) {
            case 0:
                res = alt0;
                break;
            case 1:
                res = alt1;
                break;
            case 2:
                res = alt2;
                break;
            case 3:
                res = alt3;
                break;
            default:
                throw new AssertionError();
        }
        return res;
    }

    public Integer devolverMin(Integer giro){
        Integer res;
        switch (giro) {
            case 0:
                res = min0;
                break;
            case 1:
                res = min1;
                break;
            case 2:
                res = min2;
                break;
            case 3:
                res = min3;
                break;
            default:
                throw new AssertionError();
        }
        return res;
    }

    public Integer devolverMaximo(Integer giro){
        Integer res;
        switch (giro) {
            case 0:
                res = max0;
                break;
            case 1:
                res = max1;
                break;
            case 2:
                res = max2;
                break;
            case 3:
                res = max3;
                break;
            default:
                throw new AssertionError();
        }
        return res;
    }
}
