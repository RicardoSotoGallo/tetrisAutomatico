
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class calcularGrafoVirtual {
    //usamos stack o pilas para las nodos
    public static void main(String[] args) throws Exception {
        
        List<Integer> objetivo = new ArrayList<>();
        List<Integer> entrada = new ArrayList<>();
        entrada.add(1);  entrada.add(0);  entrada.add(0);  entrada.add(1);
        objetivo.add(0); objetivo.add(1); objetivo.add(1); objetivo.add(0);
        calcularGrafo(entrada,objetivo);
    }
    public static  void calcularGrafo(List<Integer> entrada , List<Integer> objetivo){
        LinkedList<nodoLista> porVisitar = new LinkedList<>();
        List<nodoLista> visitados = new ArrayList<>();
        nodoLista n = new nodoLista(entrada);
        n.calcularHeuristica(objetivo);
        porVisitar.add(n);
        nodoLista actual = null;
        nodoLista anterior = null;
        boolean noEncontrado = true;
        actual = porVisitar.getFirst();
        visitados.add(actual);
        int cansacionMaximo = 10;
        int cansancio = cansacionMaximo;
        while(!porVisitar.isEmpty() && noEncontrado){
            List<nodoLista> hijos = actual.crearHijos();
            for(nodoLista h : hijos){
                if(!visitados.stream().anyMatch(t -> t.id.equals(h.id) ) && !porVisitar.stream().anyMatch(t -> t.id.equals(h.id))){
                    h.calcularHeuristica(objetivo);
                    int contador = 0;
                    for(nodoLista j : porVisitar){
                        if(j.valorHeuristico < h.valorHeuristico){
                            break;
                        }else{
                            contador += 1;
                        }
                    }
                    porVisitar.add(contador , h);
                }
            }
            anterior = actual;
            actual = porVisitar.getFirst();
            if(actual.nodoObjetivo(objetivo)){
                noEncontrado = false;
            }else{
                visitados.add(porVisitar.getFirst());
                porVisitar.removeFirst();
                if( actual.valorHeuristico <= anterior.valorHeuristico ){
                    cansancio -= 1;
                }else{
                    cansancio = cansacionMaximo;
                }
                if( cansancio <= 0 ){
                    noEncontrado = false;
                }
                System.out.println("visitados --------------------------------------");
                visitados.stream().forEach(t -> System.out.println("nodo -> "+ t.id + " huristica -> "+t.valorHeuristico) );
                System.out.println("===============================================");
                porVisitar.stream().forEach(t -> System.out.println("nodo -> "+t.id + " huristica -> "+t.valorHeuristico) );
                System.out.println("encontrado = "+actual.id);
                System.out.println("Cansancio -> "+cansancio);
                System.out.println("------------------------------------------------");
            }
            
        }
        
        System.out.println("encontrado = "+actual.id);
        List<String> resultado = new ArrayList<>();
        actual.getResultado(resultado);
        System.out.println(resultado);
        //}
        //System.out.println("Buenas tardes");
        //leerFichero lf = new leerFichero();
        //lf.leerFichero();
    }
}
