
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class leerFichero {
    String localizacion;
    List<String> lineasLs;
    HashMap<pareja, String> tablero;
    List<Integer> listaAlturasBloques;
    List<Integer> listaPiezaDesdeArriba;
    List<Integer> listaPiezaDesdeAbajo;
    List<Integer> listaPiezaDesdeArribaInv;
    List<Integer> listaPiezaDesdeAbajoInv;

    List<Integer> listaPiezaDesdeIzquierda;
    List<Integer> litsaPiezaDesdeDerecha;
    List<Integer> listaPiezaDesdeIzquierdaInv;
    List<Integer> litsaPiezaDesdeDerechaInv;

    List<Integer> vectorFinal;
    Integer tamPiezaX = 4;
    Integer tamPezaY = 4;
    Integer posicionX;
    Integer anchura;
    Integer altura;
    public leerFichero(){
        localizacion = "ficherosComunos/estadoJuego.richi";
        
    }
    public void leerFichero(){
        try(BufferedReader br = new BufferedReader(new FileReader(localizacion))){
            lineasLs = new ArrayList<>();
            tablero = new HashMap<>();
            String linea = "";
            int y = 0;
            anchura = 0;
            altura = -2;
            listaAlturasBloques = new ArrayList<>();
            
            listaPiezaDesdeArriba = new ArrayList<>();
            listaPiezaDesdeAbajo = new ArrayList<>();


            listaPiezaDesdeIzquierda = new ArrayList<>();
            litsaPiezaDesdeDerecha = new ArrayList<>();
            while ((linea = br.readLine() )!= null) { 
                lineasLs.add(linea);
                //System.out.println(linea);
                listaPiezaDesdeIzquierda.add(-1);
                litsaPiezaDesdeDerecha.add(-1);
                mirarLinea(linea,y);
                y++;
                if(anchura < linea.length() - 2){
                    anchura = linea.length() - 2;
                }
                altura++;
            }
            int maxAb = listaPiezaDesdeAbajo.stream().max(Integer::compareTo).get();
            int minIz = listaPiezaDesdeIzquierda.stream().filter(t -> t >= 0).min(Integer::compareTo).get();
            int posicion = 0;
            for(int i = 0 ; i < listaPiezaDesdeArriba.size();i++){
                if( !listaPiezaDesdeArriba.get(i).equals(-1) ){
                    posicion = i - 1;
                    break;
                }
            }
            /*
            Y posiblemento esto acarrea todos los problemas
            Hay que reacerlo junto a la heuristica
            */
            //alturas de las casillas visto desde arriba en invertido es si se da la media vuelta
            listaPiezaDesdeArriba    = listaPiezaDesdeArriba.stream().filter(t -> !t.equals(-1)).map(t -> maxAb - t + 1).collect(Collectors.toCollection(ArrayList::new));
            Integer maxAuxA  = listaPiezaDesdeArriba.stream().max(Integer::compareTo).get();
            listaPiezaDesdeArribaInv = new ArrayList<>(listaPiezaDesdeArriba).reversed().stream().map(t -> maxAuxA - t ).collect(Collectors.toCollection(ArrayList::new));
            for(int i = listaPiezaDesdeArriba.size() ; i < tamPiezaX;i++) {listaPiezaDesdeArriba.add(-1); listaPiezaDesdeArribaInv.add(-1);}
            
            //alturas de las casillas visto desde abajo en invertido es si se da la media vuelta
            listaPiezaDesdeAbajo     = listaPiezaDesdeAbajo.stream().filter(t -> !t.equals(-1)).map(t -> maxAb - t).collect(Collectors.toCollection(ArrayList::new));
            listaPiezaDesdeAbajoInv  = new ArrayList<>(listaPiezaDesdeAbajo.reversed());
            listaPiezaDesdeAbajoInv  = listaPiezaDesdeAbajoInv.stream().map(t -> maxAuxA - t).collect(Collectors.toCollection(ArrayList::new));
            for(int i = listaPiezaDesdeAbajo.size() ; i < tamPiezaX;i++) {listaPiezaDesdeAbajo.add(-1);listaPiezaDesdeAbajoInv.add(-1);}
            
            //alturas de las casillas visto desde derecha en invertido es si se da la media vuelta
            litsaPiezaDesdeDerecha   = litsaPiezaDesdeDerecha.stream().filter(t -> !t.equals(-1)).map(t -> t - minIz + 1).collect(Collectors.toCollection(ArrayList::new));
            Integer maxAuxB = litsaPiezaDesdeDerecha.stream().max(Integer::compareTo).get();
            litsaPiezaDesdeDerechaInv = new ArrayList<>(litsaPiezaDesdeDerecha.reversed()).stream().map(t -> maxAuxB - t).collect(Collectors.toCollection(ArrayList::new));
            for(int i = litsaPiezaDesdeDerecha.size() ; i < tamPiezaX ;i++) {litsaPiezaDesdeDerecha.add(-1); litsaPiezaDesdeDerechaInv.add(-1);}
            
            //alturas de las casillas visto desde izquierda en invertido es si se da la media vuelta
            listaPiezaDesdeIzquierda = listaPiezaDesdeIzquierda.stream().filter(t -> !t.equals(-1)).map(t -> t - minIz).collect(Collectors.toCollection(ArrayList::new));
            listaPiezaDesdeIzquierdaInv = new ArrayList<>(listaPiezaDesdeIzquierda.reversed());
            listaPiezaDesdeIzquierdaInv = listaPiezaDesdeIzquierdaInv.stream().map(t -> maxAuxB - t).collect(Collectors.toCollection(ArrayList::new));
            for(int i = listaPiezaDesdeIzquierda.size() ; i < tamPiezaX;i++) {listaPiezaDesdeIzquierda.add(-1); listaPiezaDesdeIzquierdaInv.add(-1);}
            
            
            listaAlturasBloques.remove(0);
            listaAlturasBloques.remove(listaAlturasBloques.size() - 1);
            for(int i = 0; i < listaAlturasBloques.size() ; i++){
                if(listaAlturasBloques.get(i).equals(-1)){
                    listaAlturasBloques.set(i, altura - 1);
                }
            }
            /*
             * Asuminedo que estas lista en realidad es una pero todas sumadas
             * Ab  = pieza desde abajo
             * Arb = pieza desde arriba
             * Izq = pieza desde Izquierda
             * Der = pieza desde Derecha
             * sufujo Inv = es invertido
             * [Ab1     , Ab2     , Ab3     , Ab4     , Arb1    , Arb2    , Arb3    , Arb4   ]
             * [Izq1    , Izq2    , Izq3    , Izq4    , Der1    , Der2    , Der3    , Der4   ]
             * [ArbInv1  , ArbInv2  , ArbInv3  , ArbInv4  , AbInv1 , AbInv2 , AbInv3 , AbInv4]
             * [IzqInv1 , IzqInv2 , IzqInv3 , IzqInv4 , DerInv1 , DerInv2 , DerInv3 , DerInv4]
             * [alturas de las piezas colocadas de la posicion del 1 al 10]
             * [posicion de la pieza segun el eje x]
             */
            vectorFinal = new ArrayList<>( listaPiezaDesdeAbajo );
            vectorFinal.addAll( listaPiezaDesdeArriba);

            vectorFinal.addAll( listaPiezaDesdeIzquierda );
            vectorFinal.addAll( litsaPiezaDesdeDerecha);

            vectorFinal.addAll( listaPiezaDesdeArribaInv);
            vectorFinal.addAll( listaPiezaDesdeAbajoInv);
            
            vectorFinal.addAll( litsaPiezaDesdeDerechaInv );
            vectorFinal.addAll( listaPiezaDesdeIzquierdaInv );

            vectorFinal.addAll( listaAlturasBloques );
            vectorFinal.add( posicion );
            posicionX = posicion;


        }catch(IOException io){
            System.out.println("No luedo leer el fichero");
        }
    }
    public void mirarLinea(String linea,int y){
        int x = 0;
        boolean primera = true;
        for(char c : linea.toCharArray()){
            if(y == 1){
                listaAlturasBloques.add(-1);
                listaPiezaDesdeAbajo.add(-1);
                listaPiezaDesdeArriba.add(-1);
            }
            if( c == 'P'){
                tablero.put( new pareja(x - 1, y -2)
                    , "P");
                if(primera){
                    listaPiezaDesdeIzquierda.set(y, x);
                    primera = false;
                }
                if(listaPiezaDesdeArriba.get(x) == -1){
                    listaPiezaDesdeArriba.set(x, y - 2);
                }
                litsaPiezaDesdeDerecha.set(y ,x);
                listaPiezaDesdeAbajo.set(x, y - 2);
                
            }else if( c == 'C'){
                tablero.put( new pareja(x - 1, y -2)
                    , "C");
                if( listaAlturasBloques.get(x).equals(-1) ){
                    listaAlturasBloques.set(x, y - 2);
                }
            }
            x++;
        }
    }
}
