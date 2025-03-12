
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
    List<Integer> listaPiezaDesdeIzquierda;
    List<Integer> litsaPiezaDesdeDerecha;
    List<Integer> vectorFinal;
    Integer tamPiezaX = 4;
    Integer tamPezaY = 4;
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
            int maxAr = listaPiezaDesdeArriba.stream().max(Integer::compareTo).get();
            int maxAb = listaPiezaDesdeAbajo.stream().max(Integer::compareTo).get();
            int maxIz = listaPiezaDesdeIzquierda.stream().max(Integer::compareTo).get();
            int maxDe = litsaPiezaDesdeDerecha.stream().max(Integer::compareTo).get();
            int posicion = 0;
            for(int i = 0 ; i < listaPiezaDesdeArriba.size();i++){
                if( !listaPiezaDesdeArriba.get(i).equals(-1) ){
                    posicion = i - 1;
                    break;
                }
            }
            listaPiezaDesdeArriba    = listaPiezaDesdeArriba.stream().filter(t -> !t.equals(-1)).map(t -> maxAr - t).collect(Collectors.toCollection(ArrayList::new));
            for(int i = listaPiezaDesdeArriba.size() ; i < tamPiezaX;i++) listaPiezaDesdeArriba.add(-1);
            listaPiezaDesdeAbajo     = listaPiezaDesdeAbajo.stream().filter(t -> !t.equals(-1)).map(t -> maxAb - t).collect(Collectors.toCollection(ArrayList::new));
            for(int i = listaPiezaDesdeAbajo.size() ; i < tamPiezaX;i++) listaPiezaDesdeAbajo.add(-1);
            listaPiezaDesdeIzquierda = listaPiezaDesdeIzquierda.stream().filter(t -> !t.equals(-1)).map(t -> maxIz - t).collect(Collectors.toCollection(ArrayList::new));
            for(int i = listaPiezaDesdeIzquierda.size() ; i < tamPiezaX;i++) listaPiezaDesdeIzquierda.add(-1);
            litsaPiezaDesdeDerecha   = litsaPiezaDesdeDerecha.stream().filter(t -> !t.equals(-1)).map(t -> maxDe - t).collect(Collectors.toCollection(ArrayList::new));
            for(int i = litsaPiezaDesdeDerecha.size() ; i < tamPiezaX ;i++) litsaPiezaDesdeDerecha.add(-1);
            listaAlturasBloques.remove(0);
            listaAlturasBloques.remove(listaAlturasBloques.size() - 1);
            for(int i = 0; i < listaAlturasBloques.size() ; i++){
                if(listaAlturasBloques.get(i).equals(-1)){
                    listaAlturasBloques.set(i, altura - 1);
                }
            }
            vectorFinal = new ArrayList<>( listaPiezaDesdeAbajo );
            vectorFinal.addAll( listaPiezaDesdeIzquierda );
            vectorFinal.addAll( listaPiezaDesdeArriba );
            vectorFinal.addAll( litsaPiezaDesdeDerecha );
            vectorFinal.addAll( listaAlturasBloques );
            vectorFinal.add( posicion );
            // 4 Abajo(stand) , 4 izquierda , 4 arriba (reves) , 4 derecha , suelo , posicion x


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
                litsaPiezaDesdeDerecha.set(y, x);
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
