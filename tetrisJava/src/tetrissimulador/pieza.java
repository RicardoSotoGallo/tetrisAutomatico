package tetrissimulador;

import java.util.ArrayList;
import java.util.List;

public class pieza {
    Integer maximo = 100;
    Integer minimo = -1;

    List<Integer> posicionX0;
    List<Integer> posicionY0;
    List<String> pos0;
    Integer min0 = minimo;
    Integer max0 = maximo;


    List<Integer> posicionX1;
    List<Integer> posicionY1;
    List<String> pos1;

    List<Integer> posicionX2;
    List<Integer> posicionY2;
    List<String> pos2;

    List<Integer> posicionX3;
    List<Integer> posicionY3;
    List<String> pos3;
    List<Integer> min;
    List<Integer> max;
    String[] posi;
    Integer tamano;
    public pieza(String in0, String in1, String in2, String in3){
        /*
         * - separa entre ejex¡X y ejeY
         * | separacion entre diferentes vectores
         */
        posicionX0 = new ArrayList<>();
        posicionY0 = new ArrayList<>();
        pos0 = new ArrayList<>();

        posicionX1 = new ArrayList<>();
        posicionY1 = new ArrayList<>();
        pos1 = new ArrayList<>();

        posicionX2 = new ArrayList<>();
        posicionY2 = new ArrayList<>();
        pos2 = new ArrayList<>();

        posicionX3 = new ArrayList<>();
        posicionY3 = new ArrayList<>();
        pos3 = new ArrayList<>();
        min = new ArrayList<>();
        max = new ArrayList<>();

        Integer aux;
        
        posi = in0.split("a");
        for(String i : posi){
            String[] p = i.split("-");
            aux = Integer.valueOf(p[0]);
            if(min0 == minimo){
                min0 = aux;
                max0 = aux;
            }else{
                if(min0 > aux){
                    min0 = aux;
                }else if(max0 < aux){
                    max0 = aux;
                }
            }

            posicionX0.add(aux);
            posicionY0.add(Integer.valueOf(p[1]));
            pos0.add(aux+"-"+p[1]);
            
        }
        min.add(min0);
        max.add(max0);
        min0 = minimo;
        max0 = maximo;
        posi = in1.split("a");
        for(String i : posi){
            String[] p = i.split("-");

            aux = Integer.valueOf(p[0]);
            if(min0 == minimo){
                min0 = aux;
                max0 = aux;
            }else{
                if(min0 > aux){
                    min0 = aux;
                }else if(max0 < aux){
                    max0 = aux;
                }
            }
            
            
            posicionX1.add(aux);
            posicionY1.add(Integer.valueOf(p[1]));
            pos1.add(aux+"-"+p[1]);
            
        }
        min.add(min0);
        max.add(max0);
        min0 = minimo;
        max0 = maximo;

        posi = in2.split("a");
        for(String i : posi){
            String[] p = i.split("-");
            aux = Integer.valueOf(p[0]);
            if(min0 == minimo){
                min0 = aux;
                max0 = aux;
            }else{
                if(min0 > aux){
                    min0 = aux;
                }else if(max0 < aux){
                    max0 = aux;
                }
            }

            posicionX2.add(aux);
            posicionY2.add(Integer.valueOf(p[1]));
            pos2.add(aux+"-"+p[1]);
            
            
        }

        min.add(min0);
        max.add(max0);
        min0 = minimo;
        max0 = maximo;

        posi = in3.split("a");
        for(String i : posi){
            String[] p = i.split("-");
            aux = Integer.valueOf(p[0]);
            if(min0 == minimo){
                min0 = aux;
                max0 = aux;
            }else{
                if(min0 > aux){
                    min0 = aux;
                }else if(max0 < aux){
                    max0 = aux;
                }
            }

            posicionX3.add(aux);
            posicionY3.add(Integer.valueOf(p[1]));
            pos3.add(aux+"-"+p[1]);
            
        }
        min.add(min0);
        max.add(max0);
        tamano = posicionX0.size();

    }
    public void devolver(){
        for(int i = 0; i < tamano;i++){
            System.out.println(posicionX0.get(i));
            System.out.println(posicionY0.get(i));
            System.out.println("====");
        }
    }
    public boolean estaPieza(int x,int y,int p){
        boolean res = false;
        List posX = new ArrayList<>();
        List posY = new ArrayList<>();
        if(p == 0){
            posX = posicionX0;
            posY = posicionY0;
        }else if(p == 1){
            posX = posicionX1;
            posY = posicionY1;
        }else if(p == 2){
            posX = posicionX2;
            posY = posicionY2;
        }else if(p == 3){
            posX = posicionX3;
            posY = posicionY3;
        }


        for(int i = 0; i < tamano;i++){
            if(posX.get(i).equals(x) && posY.get(i).equals(y)){
                res = true;
                break;
            }
        }
        return res;
    }
    public void mostrarPieza(int pose){
        String texto = "";
        for(int i = 0; i < 4;i++){
            for(int j = min.get(pose) ; j < max.get(pose)+1;j++){
                if(pose == 0){
                    if(pos0.contains(j+"-"+i)){
                        texto+="X";
                    }else{
                        texto+=" ";
                    }
                }else if(pose == 1){
                    if(pos1.contains(j+"-"+i)){
                        texto+="X";
                    }else{
                        texto+=" ";
                    }

                }else if(pose == 2){
                    if(pos2.contains(j+"-"+i)){
                        texto+="X";
                    }else{
                        texto+=" ";
                    }

                }else if(pose == 3){
                    if(pos3.contains(j+"-"+i)){
                        texto+="X";
                    }else{
                        texto+=" ";
                    }

                }
            }
            texto+="\n";
        }
        System.out.println(texto);
        
    }
    
}
