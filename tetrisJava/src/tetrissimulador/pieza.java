package tetrissimulador;

import java.util.ArrayList;
import java.util.List;

public class pieza {
    List<Integer> posicionX0;
    List<Integer> posicionY0;

    List<Integer> posicionX1;
    List<Integer> posicionY1;

    List<Integer> posicionX2;
    List<Integer> posicionY2;

    List<Integer> posicionX3;
    List<Integer> posicionY3;
    String[] posi;
    Integer tamano;
    public pieza(String in0, String in1, String in2, String in3){
        /*
         * - separa entre ejex¡X y ejeY
         * | separacion entre diferentes vectores
         */
        posicionX0 = new ArrayList<>();
        posicionY0 = new ArrayList<>();

        posicionX1 = new ArrayList<>();
        posicionY1 = new ArrayList<>();

        posicionX2 = new ArrayList<>();
        posicionY2 = new ArrayList<>();

        posicionX3 = new ArrayList<>();
        posicionY3 = new ArrayList<>();
        
        posi = in0.split("a");
        for(String i : posi){
            String[] p = i.split("-");
            posicionX0.add(Integer.parseInt(p[0]));
            posicionY0.add(Integer.parseInt(p[1]));
            
        }

        posi = in1.split("a");
        for(String i : posi){
            String[] p = i.split("-");
            posicionX1.add(Integer.parseInt(p[0]));
            posicionY1.add(Integer.parseInt(p[1]));
            
        }

        posi = in2.split("a");
        for(String i : posi){
            String[] p = i.split("-");
            posicionX2.add(Integer.parseInt(p[0]));
            posicionY2.add(Integer.parseInt(p[1]));
            
        }

        posi = in3.split("a");
        for(String i : posi){
            String[] p = i.split("-");
            posicionX3.add(Integer.parseInt(p[0]));
            posicionY3.add(Integer.parseInt(p[1]));
            
        }
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
    
}
