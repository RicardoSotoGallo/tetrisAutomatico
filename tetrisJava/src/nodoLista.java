
import java.util.ArrayList;
import java.util.List;

public class nodoLista extends nodo<nodoLista> {
    List<Integer> listaActual;
    public nodoLista(List<Integer> ls) {
        String id = "";
        this.listaActual = ls;
        for( int i : ls){
            id = id + String.valueOf(i);
        }
        super(id);
    }
    @Override
    public void calcularHeuristica(List<Integer> entrada){
        int heu = 0;
        for(int i = 0; i < this.listaActual.size();i++){
            if (entrada.get(i).equals(this.listaActual.get(i))){
                heu += 1;
            }
        }
        this.valorHeuristico = heu;
    }
    @Override
    public boolean nodoObjetivo(List<Integer> entrada){
        return entrada.size() == this.valorHeuristico;
    }
    @Override
    public List<nodoLista> crearHijos(){
        List<nodoLista> res = new ArrayList<>();
        for(int i = 0; i < listaActual.size();i++){
            List<Integer> aux = new ArrayList<>(this.listaActual);
            if(aux.get(i) == 0){
                aux.set(i, 1);
            }else{
                aux.set(i, 0);
            }
            nodoLista h = new nodoLista(aux);
            h.setPadre(this);
            res.add(h);

        }
        return res;
    }
    
}
