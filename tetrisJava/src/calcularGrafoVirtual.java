
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class calcularGrafoVirtual {
    //usamos stack o pilas para las nodos
    public static void main(String[] args) throws Exception {
        leerFichero lf = new leerFichero();
        lf.leerFichero();

        /*System.out.println(lf.tablero);
        System.out.println("lista alturas \n "+lf.listaAlturasBloques);
        System.out.println("lista PiezaArriba \n"+lf.listaPiezaDesdeArriba);
        System.out.println("lista piezaAbajo \n"+lf.listaPiezaDesdeAbajo);
        System.out.println("lista piezaIzquierda \n"+lf.listaPiezaDesdeIzquierda);
        System.out.println("lista piezaDerecha \n"+lf.litsaPiezaDesdeDerecha);
        System.out.println("vectorTotal \n"+lf.vectorFinal);
        System.out.println("altura -> "+lf.altura);
        System.out.println("anchura -> "+lf.anchura);*/

        List<Integer> objetivo = new ArrayList<>();
        objetivo.add( lf.anchura );
        objetivo.add( lf.tamPiezaX);
        objetivo.add( lf.altura);
        /*Integer mejorInicio = lf.listaAlturasBloques.subList(0, lf.anchura - lf.tamPiezaX).stream().max(Integer::compareTo).get();
        Integer posicionMejor = 0;
        for(int i : lf.listaAlturasBloques){
            if(i == mejorInicio){
                break;
            }else{
                posicionMejor++;
            }
        }*/
        //nodoLista n = new nodoLista(lf.vectorFinal, 0, "inicio");

        //n.calcularHeuristica(objetivo);

        List<Integer> inicio = new ArrayList<>(lf.vectorFinal);
        inicio.set(inicio.size() - 1, 5);
        nodoLista n = new nodoLista(inicio, 0, "5");
        List<String> resultado = calcularGrafoLista( n , objetivo);
        try (FileWriter writer = new FileWriter("ficherosComunos/resultadoHeuristico.richi")) {
            writer.write(resultado.toString());
        }catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }

        /*List<Integer> objetivo = new ArrayList<>();
        List<Integer> entrada = new ArrayList<>();
        entrada.add(1);  entrada.add(0);  entrada.add(0);  entrada.add(1);
        objetivo.add(0); objetivo.add(1); objetivo.add(1); objetivo.add(0);
        calcularGrafoLista(entrada,objetivo);*/
    }
    public static  List<String> calcularGrafoLista(nodoLista entrada , List<Integer> objetivo){
        LinkedList<nodoLista> porVisitar = new LinkedList<>();
        List<nodoLista> visitados = new ArrayList<>();
        nodoLista n = entrada;
        n.calcularHeuristica(objetivo);
        porVisitar.add(n);
        nodoLista actual = null;
        boolean noEncontrado = true;
        actual = porVisitar.getFirst();
        nodoLista mejor = actual;
        visitados.add(actual);
        int cansacionMaximo = 100;
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
            actual = porVisitar.getFirst();
            if(actual.nodoObjetivo(objetivo)){
                noEncontrado = false;
                mejor = actual;
            }else{
                visitados.add(porVisitar.getFirst());
                porVisitar.removeFirst();
                if( mejor.valorHeuristico < actual.valorHeuristico ){
                    cansancio = cansacionMaximo;
                    mejor = actual;
                    
                }else{
                    cansancio -= 1;
                }
                if( cansancio <= 0 ){
                    noEncontrado = false;
                }
                /*System.out.println("visitados --------------------------------------");
                visitados.stream().forEach(t -> System.out.println("nodo -> "+ t.id + " huristica -> "+t.valorHeuristico) );
                System.out.println("===============================================");
                porVisitar.stream().forEach(t -> System.out.println("nodo -> "+t.id + " huristica -> "+t.valorHeuristico) );
                System.out.println("encontrado = "+actual.id);
                System.out.println("mejor nodo = "+mejor.id);
                System.out.println("Cansancio -> "+cansancio);
                System.out.println("------------------------------------------------");*/
            }
            
        }
        
        System.out.println("encontrado = "+mejor.id+" puntiacion -> "+mejor.valorHeuristico);
        System.out.println(" cansancio -> "+cansancio+" es correcto -> "+!noEncontrado);
        List<String> resultado = new ArrayList<>();
        mejor.getResultado(resultado);
        System.out.println(resultado);
        return resultado;
        
        /*
MMMMMMMMMMMM
MVVVVVVPVVVM
MVVVVVPPPVVM
MVVVVVVVVVVM
MVVVVCVVVVVM
MVVVCCVVVVVM
MVVVCCVVVVVM
MVVVCCCCVVVM
MVVVCVCCVVVM
MVVVCVVCCCVM
MVVVCCVCVVVM
MVVVCCCCCVVM
MVVVCVVVCVVM
MVVVCVCCCCVM
MVVVCVCVVVVM
MVVVCCCCCVVM
MVVVVCVCVVVM
MVVVCCVCCVVM
MVVVCCVVCVVM
MVVVCCCCCVVM
MCVCCCVVCVVM
MMMMMMMMMMMM
         */
        //}
        //System.out.println("Buenas tardes");
        //leerFichero lf = new leerFichero();
        //lf.leerFichero();
    }
}
