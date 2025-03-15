
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
    boolean condicionMejorPieza;
    public nodoLista(List<Integer> ls,Integer rotacion,String comando) {
        movIdel = new ArrayList<>(ls.subList(0, 8));
        movE    = new ArrayList<>(ls.subList(8, 16));
        movW    = new ArrayList<>(ls.subList(16, 24));
        movQ    = new ArrayList<>(ls.subList(24, 32));
        base    = new ArrayList<>(ls.subList(32, 42));
        giro    = 0;
        ejeX    = ls.get(42);
        Integer ejeXAux = ls.get(42);
        String id = rotacion.toString()+":"+ ejeXAux.toString();
        errorGrabe = 0;
        errorLeve  = 0;
        this.comando = comando;
        condicionMejorPieza = false;
        super(id);
    }
    public void calcularHeuristica(List<Integer> entrada){
        /*
         * entrada[0] = anchura del tablero
         * entrada[1] = anchura de la pieza en x
         * entrada[2] = altura del teblero
         */
        Integer minimo = -1;
        Integer maximo = -1;
        Integer maximaAltura = -1;
        Integer minimaAltura = -1;
        Integer colisionAux = 0;
        Integer alturaAux = 0;
        //List<Integer> listaColisiones = new ArrayList<>();
        /*if(id.equals("3:7")){
            System.out.println("buenas");
        }*/
        for(int i = 0 ; i < entrada.get(1) ; i++){
            if(movIdel.get(i) >= 0){
                if( i + ejeX < base.size()){
                    colisionAux = base.get(ejeX + i) + movIdel.get(i);
                    alturaAux   = movIdel.get(i + entrada.get(1)) + entrada.get(2) - base.get(ejeX + i);
                    //listaColisiones.add( colisionAux );
                    if(minimo == -1 || colisionAux < minimo){
                        minimo = colisionAux;
                    }
                    if(colisionAux > maximo){
                        maximo = colisionAux;
                    }
                    if( alturaAux > maximaAltura ){
                        maximaAltura = alturaAux;
                    }
                    if(minimaAltura == -1 || entrada.get(2) - base.get(ejeX + i) < minimaAltura ){
                        minimaAltura = entrada.get(2) - base.get(ejeX + i);
                    }
                }else{
                    errorGrabe++;
                }
            }
        }
        Integer maxAlturaPieza = movIdel.stream().max(Integer::compareTo).get(); 
        Integer minimaBajada   = base.stream().map(t -> entrada.get(2) - t ).min(Integer::compareTo).get();
        errorLeve = maximo - minimo;
        if(minimaAltura <= minimaBajada) condicionMejorPieza = true;
        this.valorHeuristico = - minimaAltura - errorLeve*100 - errorGrabe * 10000;

    }
    @Override
    public boolean nodoObjetivo(List<Integer> entrada){
        return (errorLeve == 0) && (errorGrabe == 0) && condicionMejorPieza;
    }
    public List<nodoLista> crearHijos(){
        List<nodoLista> res = new ArrayList<>();
        nodoLista unHijo;
        List<Integer> moveAux;
        /*
         * Testear que llege hasta el fondo. 
         * Comprobar quitando el caso de parada y ver que pasen todos y enseñar por pantalla
         */
        //MoverDerecha
        if(ejeX <= base.size() - 1){
            moveAux = new ArrayList<>();
            moveAux.addAll(movIdel);moveAux.addAll(movE);
            moveAux.addAll(movW);   moveAux.addAll(movQ);
            moveAux.addAll(base);   moveAux.add(ejeX+1);
            unHijo = new nodoLista(moveAux, giro,"E");
            unHijo.setPadre(this);
            res.add( unHijo );
        }
        

        //mover Izquierda
        if(ejeX - 1 >= 0){
            moveAux = new ArrayList<>();
            moveAux.addAll(movIdel);moveAux.addAll(movE);
            moveAux.addAll(movW);   moveAux.addAll(movQ);
            moveAux.addAll(base);   moveAux.add(ejeX-1);
            unHijo = new nodoLista(moveAux, giro,"Q");
            unHijo.setPadre(this);
            res.add( unHijo );
        }

        //girarDerecha una vez
        moveAux = new ArrayList<>();
        moveAux.addAll(movE);moveAux.addAll(movW);
        moveAux.addAll(movQ);   moveAux.addAll(movIdel);
        moveAux.addAll(base);   moveAux.add(ejeX);
        unHijo = new nodoLista(moveAux, (giro+1)%5,"A");
        unHijo.setPadre(this);
        res.add( unHijo );

        //girar dercha dos veces o invertir
        moveAux = new ArrayList<>();
        moveAux.addAll(movW);moveAux.addAll(movQ);
        moveAux.addAll(movIdel);   moveAux.addAll(movE);
        moveAux.addAll(base);   moveAux.add(ejeX);
        unHijo = new nodoLista(moveAux, (giro+2)%5,"W");
        unHijo.setPadre(this);
        res.add( unHijo );

        //girar derecha tres veces o girar izquierda
        moveAux = new ArrayList<>();
        moveAux.addAll(movQ);moveAux.addAll(movIdel);
        moveAux.addAll(movE);   moveAux.addAll(movIdel);
        moveAux.addAll(base);   moveAux.add(ejeX);
        unHijo = new nodoLista(moveAux, (giro+3)%5,"D");
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
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        boolean res = false;
        if(obj.getClass().equals(this.getClass())){
            if( ((nodoLista)obj).id.equals(this.id)){
                res = true;
            }
        }
        return res;
    }
    
}
