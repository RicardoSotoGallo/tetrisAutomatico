import java.util.ArrayList;
import java.util.List;

public class pruebas {
    public static void main(String[] args) {
        
        visitarTodaPosivilidad();

        //comprobarLectura();

    }
    public static void saludo(){
        System.out.println("Saludamos esta");
    }
    public static void comprobarLectura(){
        leerFichero lf = new leerFichero();
        lf.leerFichero();
        System.out.println("Abajo    , Arriba    " +lf.listaPiezaDesdeAbajo + lf.listaPiezaDesdeArriba);
        System.out.println("Izq      , Dech      " +lf.listaPiezaDesdeIzquierda + lf.litsaPiezaDesdeDerecha);
        System.out.println("ArribaInv, AbajoInv  " +lf.listaPiezaDesdeArribaInv + lf.listaPiezaDesdeAbajoInv);
        System.out.println("DerchInv , IzqInv    " +lf.litsaPiezaDesdeDerechaInv + lf.listaPiezaDesdeIzquierdaInv);
    }
    public static void visitarTodaPosivilidad(){
        Integer cont = 100;
        leerFichero lf = new leerFichero();
        lf.leerFichero();
        List<Integer> objetivo = new ArrayList<>();
        objetivo.add( lf.anchura );
        objetivo.add( lf.tamPiezaX);
        objetivo.add( lf.altura);
        List<Integer> inicio = new ArrayList<>(lf.vectorFinal);
        inicio.set(inicio.size() - 1, 0);
        nodoLista n = new nodoLista(inicio, 0, "0");
        List<nodoLista> visitados = new ArrayList<>();
        List<nodoLista> porVisitar = new ArrayList<>();
        porVisitar.add(n);
        List<Integer> datosEntrada = new ArrayList<>();
        datosEntrada.add(10);
        datosEntrada.add(4);
        datosEntrada.add(20);
        n.calcularHeuristica(datosEntrada);
        while(!porVisitar.isEmpty() && cont > 0){
            List<nodoLista> hijos = porVisitar.get(0).crearHijos();
            visitados.add(porVisitar.get(0));
            porVisitar.remove(0);
            for(nodoLista i : hijos){
                if(!visitados.contains(i) && !porVisitar.contains(i)){
                    i.calcularHeuristica(datosEntrada);
                    porVisitar.add(i);
                }
            }
            cont -- ;
        }
        for(int i = 0; i < 10 ; i++){
            Integer a = i;
            visitados.stream().filter(t -> t.ejeX == a).forEach(t -> System.out.println( t.id +" heu -> " + t.valorHeuristico + " pesosPieza -> "+t.movIdel) );
        }
        
        
    }
    /*
MMMMMMMMMMMM
MVVVVVPVVVVM
MVVVVVPPPVVM
MVVVVVVVVVVM
MVVVVVVVVVVM
MVVVVVVVVVVM
MVVVVVVVVVVM
MVVVVVVVVVVM
MVVVVVVVVVVM
MVVVVVVVVVVM
MVVVVVVVVVVM
MVVVVVVVVVVM
MVVVVVVVVVVM
MVVVVVVVVVVM
MVVVVVVVVVVM
MVVVVVVVVVVM
MVVVVVVVVVVM
MVVVVVVVVCVM
MVVVCVVVCCCM
MCCVCCCVCCCM
MCCCCCCVVCVM
MMMMMMMMMMMM
     */
}
