import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class datosEstado {
    String path = "ficherosComunos/estadoJuegoDic.richi";
    HashMap<String, casilla> diccionarioCasilla;
    List<pareja> zeroGrados;
    List<pareja> piMedioGrado;
    List<pareja> piGrado;
    List<pareja> menoPiMedioGrado;
    List<casilla> posiblesHuecos;
    Integer tamañoAlto = 20;
    Integer tamañoAncho = 10;
    
    public void leerFichero(){
        diccionarioCasilla = new HashMap<>();
        zeroGrados = new ArrayList<>();
        piMedioGrado = new ArrayList<>();
        piGrado = new ArrayList<>();
        menoPiMedioGrado = new ArrayList<>();
        posiblesHuecos = new ArrayList<>();
        
        try(BufferedReader br = new BufferedReader(new FileReader(path))){
            String linea = "";
            Integer estado = 0;
            String[] divisiones; 
            String[] d;
            Integer x; 
            Integer y;
            while ((linea = br.readLine() )!= null){
                if("=====".equals(linea)){
                    estado++;
                } else if(estado == 0){
                    divisiones = linea.split(":");
                    x = Integer.valueOf(divisiones[0]);
                    y = Integer.valueOf(divisiones[1]);
                    diccionarioCasilla.put( 
                    x+","+y, 
                    new casilla(divisiones[2] , x , y));
                }else if(estado == 1){
                    //[(2, 0), (2, 1), (1, 1), (0, 1)]
                    divisiones = linea.replace("[", "").replace("]", "").split("\\),");
                    for(String i : divisiones){
                        d = i.replace("(", "").replace(")", "").split(",");
                        zeroGrados.add(new pareja(Integer.valueOf(d[0].strip()), Integer.valueOf(d[1].strip() )));
                        //pigrado
                    }
                }else if(estado == 2){
                    //[(2, 0), (2, 1), (1, 1), (0, 1)]
                    divisiones = linea.replace("[", "").replace("]", "").split("\\),");
                    for(String i : divisiones){
                        d = i.replace("(", "").replace(")", "").split(",");
                        piMedioGrado.add(new pareja(Integer.valueOf(d[0].strip()), Integer.valueOf(d[1].strip() )));
                    }
                }else if(estado == 3){
                    //[(2, 0), (2, 1), (1, 1), (0, 1)]
                    divisiones = linea.replace("[", "").replace("]", "").split("\\),");
                    for(String i : divisiones){
                        d = i.replace("(", "").replace(")", "").split(",");
                        piGrado.add(new pareja(Integer.valueOf(d[0].strip()), Integer.valueOf(d[1].strip() )));
                    }
                }else if(estado == 4){
                    //[(2, 0), (2, 1), (1, 1), (0, 1)]
                    divisiones = linea.replace("[", "").replace("]", "").split("\\),");
                    for(String i : divisiones){
                        d = i.replace("(", "").replace(")", "").split(",");
                        menoPiMedioGrado.add(new pareja(Integer.valueOf(d[0].strip()), Integer.valueOf(d[1].strip() )));
                    }
                }
                
            }
            Integer valorAux = 0;
            casilla casillaAux;
            
            for(String i : diccionarioCasilla.keySet()){
                valorAux = 0;
                casillaAux = diccionarioCasilla.get(i);
                String[] c = i.split(",");
                x = Integer.valueOf(c[0]);
                y = Integer.valueOf(c[1]);
                if("Casilla".equals(casillaAux.tipo)){
                    casillaAux.valor = -1;
                }else{
                    if(x == 0 && diccionarioCasilla.get( (x + 1)+","+y ).tipo.equals("Casilla")){
                        valorAux += 4;
                    }
                    if(x == tamañoAncho - 1 && diccionarioCasilla.get( (x - 1)+","+y ).tipo.equals("Casilla")){
                        valorAux += 4;
                    }
                    if(!(x == 0 || x == tamañoAncho - 1)  ){
                        if(diccionarioCasilla.get( (x + 1)+","+y ).tipo.equals("Casilla") && diccionarioCasilla.get( (x - 1)+","+y ).tipo.equals("Casilla")){
                            valorAux += 3;
                        }
                    }
                    //&& diccionarioCasilla.get( (x + 1)+","+y ).tipo.equals("Casilla")

                    if( y != 0){//i.getX()     , i.getY() - 1 
                        if( diccionarioCasilla.get( x+","+(y-1)).tipo.equals("Casilla") ) {
                            valorAux++;
                            if(y > tamañoAlto/2){
                                valorAux += 1;
                            }
                            if(y > tamañoAlto * 2/3){
                                valorAux += 1;
                            }
                        }
                    }
                    if( y + 1 != tamañoAlto){//i.getX()     , i.getY() + 1
                        if( diccionarioCasilla.get( x+","+(y+1) ).tipo.equals("Casilla") ) {
                            valorAux++;
                            if(y > tamañoAlto/2){
                                valorAux += 1;
                            }
                            if(y > tamañoAlto * 2/3 ){
                                valorAux += 1;
                            }
                        }
                    }else{
                        valorAux++;
                        if(y > tamañoAlto/2){
                            valorAux += 1;
                        }
                        if(y > tamañoAlto * 2/3){
                            valorAux += 1;
                        }
                        
                    }
                    if(valorAux > 0){
                        posiblesHuecos.add(casillaAux);
                    }
                    casillaAux.valor = valorAux;
                }
                
            }
            
            for(int yi = 1 ; yi < tamañoAlto ; yi++){
                for(int xi = 0 ; xi < tamañoAncho ; xi ++){
                    casillaAux = diccionarioCasilla.get(xi+","+yi);
                    if(casillaAux.valor >= 0){
                        valorAux = diccionarioCasilla.get(xi+","+(yi - 1)).valor;
                        if(valorAux >= 0){
                            casillaAux.valor += (valorAux);
                        }else{
                            casillaAux.valor = -1;
                            if(posiblesHuecos.contains(casillaAux)){
                                posiblesHuecos.remove(casillaAux);
                            }
                        }
                    }
                }
            }
            posiblesHuecos.sort( Comparator.comparingInt(t -> -t.valor) );
        }catch(IOException io){
            System.out.println("No luedo leer el fichero");
        }
    }
    
    public void enseñar(){
        String texto = "";
        int n;
        for(int y = 0 ; y < tamañoAlto ; y++){
            for(int x = 0 ; x < tamañoAncho ; x++){
                n = diccionarioCasilla.get( x+","+y ).valor ;
                if(n >= 0){
                    texto += n+" |";
                }else{
                    texto += n+"|";
                }
            }
            System.out.println(texto);
            texto = "";
        }
        posiblesHuecos.stream().forEach(t -> System.out.println(t));
    }
}
