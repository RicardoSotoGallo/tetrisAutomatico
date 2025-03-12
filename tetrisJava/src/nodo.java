
import java.util.List;

public class nodo {
    String id;
    Integer valorHeuristico;
    private nodo padre;    
    public nodo(String i){
        this.id = i;
        this.padre = null;
        //System.out.println(i);
    }
    void calcularHeuristica(List<Integer> entrada){
        this.valorHeuristico = 0;
    }
    boolean nodoObjetivo(List<Integer> entrada){
        return false;
    }
    public void setPadre(nodo p){
        this.padre = p;
    }
    public nodo getPadre(){
        return  this.padre;
    }
    public void getResultado(List<String> resultado){
        if(this.padre != null){
            padre.getResultado(resultado);
            resultado.add(id);
        }else{
            resultado.add(id);
        }
    }
}
