
import java.util.ArrayList;
import java.util.List;

public class nodoLista extends nodo {
    List<Integer> movIdel;
    List<Integer> movQ;
    List<Integer> movE;
    List<Integer> movW;
    List<Integer> base;
    Integer ejeX;
    Integer giro;
    Integer errorGrabe;
    Integer errorLeve;
    String comando;
    boolean aBajadoLaPieza;
    public nodoLista(List<Integer> ls,Integer rotacion,String comando) {
        movIdel = new ArrayList<>(ls.subList(0, 4));
        movE    = new ArrayList<>(ls.subList(4, 8));
        movW    = new ArrayList<>(ls.subList(8, 12));
        movQ    = new ArrayList<>(ls.subList(12, 16));
        base    = new ArrayList<>(ls.subList(16, 26));
        giro    = 0;
        ejeX    = ls.get(26);
        String id = rotacion.toString()+":"+ls.get(26).toString();
        errorGrabe = 0;
        errorLeve  = 0;
        this.comando = comando;
        aBajadoLaPieza = false;
        super(id);
    }
    public void calcularHeuristica(List<Integer> entrada){
        //la 0 de entrada es el tamaño del tetris en x 
        //la 1 es el tamaño en x de una pieza
        /*
         * Cambiar la heuristica de tal forma que compruebe el tamaño entero y solo no compruebe cuando hay un -1
         * Si sin estar -1 sale fuera no calculamos y colocamos el mega castigo
         */
        int heu = 0;
        Integer cantidadDeMenos1 = movIdel.stream().filter(t -> t == -1).toList().size();
        if(ejeX + entrada.get(1) - cantidadDeMenos1<= entrada.get(0)&& ejeX >= 0){
            List<Integer> restas  = new ArrayList<>();
            List<Integer> sombra    = new ArrayList<>();
            List<Integer> alturas = new ArrayList<>();
            
            for(int i = 0 ; i < entrada.get(1) - cantidadDeMenos1 ; i++){
                if(movIdel.get(i) >= 0){
                    restas.add(base.get( ejeX + i ) - (movIdel.get(i) + 1));
                    sombra.add(base.get( ejeX + i ));
                    alturas.add(- (movIdel.get(i) ) + (   movW.get(i) + 1) );
                    
                }
            }
            Integer maximaResta = restas.stream().max(Integer::compareTo).get();
            Integer minimaResta = restas.stream().min(Integer::compareTo).get();
            Integer maxBase     =   base.stream().min(Integer::compareTo).get();
            Integer maxSombra   = sombra.stream().min(Integer::compareTo).get();
            Integer maxAltura   =alturas.stream().max(Integer::compareTo).get();
            //System.out.println("resta -> "+restas);
            //System.out.println("sombra -> "+sombra);
            //System.out.println("altura -> "+alturas);
            //System.out.println("max base -> "+maxBase);
            //System.out.println("maxSombra -> "+maxSombra+" maxAltura -> "+maxAltura+" calculo -> "+ (maxSombra - maxAltura));
            if((maxSombra - maxAltura) >= maxBase){
                aBajadoLaPieza = true;
            }
            heu = maxSombra;
            errorLeve = (maximaResta - minimaResta);
        }else{
            errorGrabe++;
        }
        this.valorHeuristico = heu + errorLeve*-100 + errorGrabe * -100000;
    }
    @Override
    public boolean nodoObjetivo(List<Integer> entrada){
        return (errorLeve == 0) && (errorGrabe == 0) && aBajadoLaPieza;
    }
    public List<nodoLista> crearHijos(){
        List<nodoLista> res = new ArrayList<>();
        nodoLista unHijo;
        /*
         * Testear que llege hasta el fondo. 
         * Comprobar quitando el caso de parada y ver que pasen todos y enseñar por pantalla
         */
        //MoverDerecha
        List<Integer> moviendoD = new ArrayList<>();
        moviendoD.addAll(movIdel);moviendoD.addAll(movE);
        moviendoD.addAll(movW);   moviendoD.addAll(movQ);
        moviendoD.addAll(base);   moviendoD.add(ejeX+1);
        unHijo = new nodoLista(moviendoD, giro,"E");
        unHijo.setPadre(this);
        res.add( unHijo );

        //mover Izquierda
        if(ejeX - 1 >= 0){
            moviendoD = new ArrayList<>();
            moviendoD.addAll(movIdel);moviendoD.addAll(movE);
            moviendoD.addAll(movW);   moviendoD.addAll(movQ);
            moviendoD.addAll(base);   moviendoD.add(ejeX-1);
            unHijo = new nodoLista(moviendoD, giro,"Q");
            unHijo.setPadre(this);
            res.add( unHijo );
        }

        //girarDerecha una vez
        moviendoD = new ArrayList<>();
        moviendoD.addAll(movE);moviendoD.addAll(movW);
        moviendoD.addAll(movQ);   moviendoD.addAll(movIdel);
        moviendoD.addAll(base);   moviendoD.add(ejeX);
        unHijo = new nodoLista(moviendoD, (giro+1)%5,"A");
        unHijo.setPadre(this);
        res.add( unHijo );

        //girar dercha dos veces o invertir
        moviendoD = new ArrayList<>();
        moviendoD.addAll(movW);moviendoD.addAll(movQ);
        moviendoD.addAll(movIdel);   moviendoD.addAll(movE);
        moviendoD.addAll(base);   moviendoD.add(ejeX);
        unHijo = new nodoLista(moviendoD, (giro+2)%5,"W");
        unHijo.setPadre(this);
        res.add( unHijo );

        //girar derecha tres veces o girar izquierda
        moviendoD = new ArrayList<>();
        moviendoD.addAll(movQ);moviendoD.addAll(movIdel);
        moviendoD.addAll(movE);   moviendoD.addAll(movIdel);
        moviendoD.addAll(base);   moviendoD.add(ejeX);
        unHijo = new nodoLista(moviendoD, (giro+3)%5,"D");
        unHijo.setPadre(this);
        res.add( unHijo );

        return res;
    }
    @SuppressWarnings("unchecked")
    @Override
    public void getResultado(List<String> resultado){
        if(this.getPadre() != null){
            this.getPadre().getResultado(resultado);
            resultado.add(comando);
        }else{
            resultado.add(comando);
        }
    }
    
}
