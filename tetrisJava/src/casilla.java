
import java.util.ArrayList;
import java.util.List;

public class casilla {
    String tipo = "";
    Integer valor;
    Integer x;
    Integer y;
    Integer heuristica;
    Integer posicion;
    Integer angulo;
    
    public casilla(String tipo , Integer x , Integer y){
        this.tipo = tipo;
        valor = 0;
        this.x = x;
        this.y = y;
        heuristica = 0;
    }

    public void calcularHeuristica(datosEstado datos ,boolean  enseñar){
        boolean seHaCambiado;
        seHaCambiado = calcularUnaPosicion(datos, datos.zeroGrados , enseñar);
        if(seHaCambiado) angulo = 0;
        seHaCambiado = calcularUnaPosicion(datos, datos.piMedioGrado , enseñar);
        if(seHaCambiado) angulo = 1;
        seHaCambiado = calcularUnaPosicion(datos, datos.piGrado , enseñar);
        if(seHaCambiado) angulo = 2;
        seHaCambiado = calcularUnaPosicion(datos, datos.menoPiMedioGrado , enseñar);
        if(seHaCambiado) angulo = 3;
        //System.out.println("====================");
        //System.out.println("Resultado -> \nheuristica-> "+heuristica+"\nposicionX-> "+posicion+"\ngiro-> "+angulo);
    }
    
    private boolean calcularUnaPosicion(datosEstado datos , List<pareja> listaAMirar , boolean enseñar){
        int maximo = listaAMirar.size();
        boolean esPosible;
        int valorParaUnaPieza;
        pareja parejaInicial;
        int mirarReal;
        String posicionAuxHueco;
        int valorAux;
        boolean res = false;
        for(int inicio = 0 ; inicio < maximo ; inicio ++){
            esPosible = true;
            valorParaUnaPieza = 0;
            parejaInicial = listaAMirar.get(inicio);
            for (int mirar = 0 ; mirar < maximo && esPosible ; mirar ++ ) {
                mirarReal = (mirar + inicio)%maximo;
                posicionAuxHueco = (this.x -(parejaInicial.getX() - listaAMirar.get(mirarReal).getX() ) )+","
                                  +(this.y -(parejaInicial.getY() - listaAMirar.get(mirarReal).getY() ) );//mirar esto
                if( datos.diccionarioCasilla.containsKey(posicionAuxHueco) ){
                    valorAux = datos.diccionarioCasilla.get(posicionAuxHueco).valor;
                    if(valorAux >= 0){
                        valorParaUnaPieza += valorAux;
                    }else{
                        esPosible = false;
                    }
                }else{
                    esPosible = false;
                }
                
            }
            if(enseñar){
                System.out.println("========================");
                System.out.println("valorParaUnaPieza->"+valorParaUnaPieza + 
                "\n huecoAMirar-> "+this
                +"\n inicio-> "+inicio
                +"\n parejaInicio-> "+parejaInicial
                +"\n listaAMirar-> "+listaAMirar
                +"\n esPosible->"+esPosible);
            }
            /*if(x == 4 && y == 19){
                System.out.println("AAA");
            }*/
            if(esPosible){
                if(heuristica < valorParaUnaPieza){
                    heuristica = valorParaUnaPieza;
                    res = true;
                    posicion = this.x - parejaInicial.getX();
                }
            }
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

