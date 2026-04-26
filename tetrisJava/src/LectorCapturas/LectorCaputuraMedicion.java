package LectorCapturas;

import DeteccionColor.ArbolColores;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LectorCaputuraMedicion {
    public ArbolColores arbol;
    public Integer xTableroMin,xTableroMax;
    public Integer yTableroMin,yTableroMax;
    public HashMap<pareja,posiciones> posicionDicc;
    public List<Integer> xPosi = new ArrayList<>();
    public List<Integer> yPosi = new ArrayList<>();
    public HashMap<pareja,String> casillasInterpretadas;
    public Integer tamX,tamY;

    String reset = "\u001B[0m";
    String rojo = "\u001B[31m";
    String verde = "\u001B[32m";
    String amarillo = "\u001B[33m";
    String azul = "\u001B[34m";

    public void realizarMedidas(BufferedImage imagenEntrada) throws IOException {
        tamX = 0;
        tamY = 0;
        arbol = new ArbolColores("tetrisJava/ImagenEntrenamiento/Tetris");
        arbol.obtenerDatos();
        int altura = imagenEntrada.getHeight();
        int anchura = imagenEntrada.getWidth();
        int y = altura/2;
        int rojo,azul,verde,rgb,alfa;
        int rojo2,azul2,verde2,rgb2,alfa2;
        int estados = 0;
        xTableroMin = 0;
        xTableroMax = 0;
        yTableroMin = 0;
        yTableroMax= 0;

        for(int x = 1; x < anchura;x++){
            //System.out.println(x+","+y);
            rgb = imagenEntrada.getRGB(x,y);
            alfa = (rgb >> 24) & 0xff;
            rojo  = (rgb >> 16) & 0xff;
            verde = (rgb >> 8) & 0xff;
            azul  = rgb & 0xff;

            rgb2 = imagenEntrada.getRGB(x-1,y);
            alfa2 = (rgb2 >> 24) & 0xff;
            rojo2  = (rgb2 >> 16) & 0xff;
            verde2 = (rgb2 >> 8) & 0xff;
            azul2  = rgb2 & 0xff;
            //System.out.println(arbol.diccionarioRaices.get("Fondo").preguntaInter(rojo,azul,verde));
            if(estados == 0){
                if(  arbol.diccionarioRaices.get("Fondo").preguntaInter(rojo,azul,verde) > 0.7f &&
                        arbol.diccionarioRaices.get("marco").preguntaInter(rojo2,azul2,verde2) > 0.7f
                ){
                    estados++;
                    xTableroMin = x;
                }
            }
            else if (estados == 1) {
                if(  (arbol.diccionarioRaices.get("marco").preguntaInter(rojo,azul,verde) > 0.7f) &&
                        (arbol.diccionarioRaices.get("Fondo").preguntaInter(rojo2,azul2,verde2) > 0.7f)
                ){
                    estados++;
                    xTableroMax = x-1;
                }
            }
        }
        int x = anchura/2;
        estados = 0;
        for(y = 1; y < altura;y++){
            rgb = imagenEntrada.getRGB(x,y);
            alfa = (rgb >> 24) & 0xff;
            rojo  = (rgb >> 16) & 0xff;
            verde = (rgb >> 8) & 0xff;
            azul  = rgb & 0xff;

            rgb2 = imagenEntrada.getRGB(x,y-1);
            alfa2 = (rgb2 >> 24) & 0xff;
            rojo2  = (rgb2 >> 16) & 0xff;
            verde2 = (rgb2 >> 8) & 0xff;
            azul2  = rgb2 & 0xff;
            if(estados == 0){
                if(arbol.diccionarioRaices.get("Fondo").preguntaInter(rojo,azul,verde) > 0.7f &&
                        arbol.diccionarioRaices.get("marco").preguntaInter(rojo2,azul2,verde2) > 0.7f
                ){
                    estados++;
                    yTableroMin = y;
                }
            }
            else if (estados == 1) {
                if(arbol.diccionarioRaices.get("marco").preguntaInter(rojo,azul,verde) > 0.7f &&
                        arbol.diccionarioRaices.get("Fondo").preguntaInter(rojo2,azul2,verde2) > 0.7f
                ){
                    estados++;
                    yTableroMax = y-1;
                }
            }
        }
        estados = 0;
        // Medimos la x
        /*
        Estado 0 -> buscar primer encuentro
         */
        int xMin = 0;
        int xMax = 0;
        xPosi = new ArrayList<>();
        yPosi = new ArrayList<>();
        y = yTableroMin;
        x = xTableroMin;
        boolean parar;

        //Primero vamos a quitar la parte fondo
        parar = true;
        while (parar){
            x++;

            rgb = imagenEntrada.getRGB(x,y);
            alfa = (rgb >> 24) & 0xff;
            rojo  = (rgb >> 16) & 0xff;
            verde = (rgb >> 8) & 0xff;
            azul  = rgb & 0xff;

            if(x == xTableroMax){
                y++;
                x = xTableroMin;
            }
            else if (arbol.diccionarioRaices.get("Fondo2").preguntaInter(rojo,azul,verde) > 0.7f) {
                //xPosi.add(x);
                //yPosi.add(y);
                parar = false;
            }
        }
        //Ahora buscamos las x
        y += 5;
        x = xTableroMin;
        parar = true;
        int contarCasillas = 0;
        while (parar){
            x++;
            rgb = imagenEntrada.getRGB(x,y);
            alfa = (rgb >> 24) & 0xff;
            rojo  = (rgb >> 16) & 0xff;
            verde = (rgb >> 8) & 0xff;
            azul  = rgb & 0xff;

            rgb2 = imagenEntrada.getRGB(x-1,y);
            alfa2 = (rgb2 >> 24) & 0xff;
            rojo2  = (rgb2 >> 16) & 0xff;
            verde2 = (rgb2 >> 8) & 0xff;
            azul2  = rgb2 & 0xff;

            if(x == xTableroMax){
                parar = false;
            }

            if(
                    arbol.diccionarioRaices.get("Fondo2").preguntaInter(rojo2,azul2,verde2) > 0.7f &&
                            arbol.diccionarioRaices.get("Fondo").preguntaInter(rojo,azul,verde) > 0.7f
            ){

                contarCasillas++;
                if((contarCasillas+2)%4 == 0){
                    xPosi.add(x);
                }

            }

            if(
                    arbol.diccionarioRaices.get("Fondo").preguntaInter(rojo2,azul2,verde2) > 0.7f &&
                            arbol.diccionarioRaices.get("Fondo2").preguntaInter(rojo,azul,verde) > 0.7f
            ){
                contarCasillas++;
                if((contarCasillas+1)%4 == 0){
                    xPosi.add(x-1);
                }
            }
        }

        parar = true;
        x = xPosi.get(0)+4;
        y = yTableroMin;

        //=====
        //xPosi = new ArrayList<>();
        //yPosi = new ArrayList<>();
        //====
        contarCasillas = 0;
        while (parar){
            y++;
            rgb = imagenEntrada.getRGB(x,y);
            alfa = (rgb >> 24) & 0xff;
            rojo  = (rgb >> 16) & 0xff;
            verde = (rgb >> 8) & 0xff;
            azul  = rgb & 0xff;

            rgb2 = imagenEntrada.getRGB(x,y-1);
            alfa2 = (rgb2 >> 24) & 0xff;
            rojo2  = (rgb2 >> 16) & 0xff;
            verde2 = (rgb2 >> 8) & 0xff;
            azul2  = rgb2 & 0xff;
            if(y == yTableroMax){
                parar = false;
            }

            if(
                    arbol.diccionarioRaices.get("Fondo2").preguntaInter(rojo2,azul2,verde2) > 0.7f &&
                            arbol.diccionarioRaices.get("Fondo").preguntaInter(rojo,azul,verde) > 0.7f
            ){

                contarCasillas++;
                if((contarCasillas+2)%4 == 0){
                    yPosi.add(y);
                }

            }

            if(
                    arbol.diccionarioRaices.get("Fondo").preguntaInter(rojo2,azul2,verde2) > 0.7f &&
                            arbol.diccionarioRaices.get("Fondo2").preguntaInter(rojo,azul,verde) > 0.7f
            ){
                contarCasillas++;
                if((contarCasillas+1)%4 == 0){
                    yPosi.add(y-1);
                }
            }

        }
        posicionDicc = new HashMap<>();
        int xCuadro = 0;
        int yCuadros = 0;
        //System.out.println(yPosi+" -> "+yPosi.size());
        //System.out.println(xPosi+" -> "+xPosi.size());
        int yfinal,xfinal;
        if(yPosi.size()%2 == 0){
            yfinal = yPosi.size();
        }else {
            yfinal = yPosi.size()-1;
        }

        if(xPosi.size()%2 == 0){
            xfinal = xPosi.size();
        }else {
            xfinal = xPosi.size()-1;
        }
        for(y = 0 ; y < yfinal ; y += 2){
            for(x = 0 ; x < xfinal; x += 2){
                posiciones pos = new posiciones();
                pos.XMin = xPosi.get(x);
                pos.XMax = xPosi.get(x+1);
                pos.YMin = yPosi.get(y);
                pos.YMax = yPosi.get(y+1);
                pos.cacularDivisiones(2,2,5,5);
                posicionDicc.put(new pareja(xCuadro,yCuadros),pos);
                xCuadro++;
            }

            xCuadro = 0;
            yCuadros++;
        }
        tamX = xfinal/2;
        tamY = yfinal/2;


    }

    public void interpretarCasillas(BufferedImage imagenEntrada){
        int diviones = 4;
        casillasInterpretadas = new HashMap<>();
        posiciones posAux;
        float puntuacionCasilla;
        //System.out.println(posicionDicc);
        for(int x = 0 ; x < tamX;x++){
            for(int y = 0; y < tamY;y++){
                puntuacionCasilla = 0.0f;
                posAux = posicionDicc.get(new pareja(x,y));
                diviones = (posAux.divisionX)*(posAux.divisionY);
                for(int xx = 0; xx < posAux.divisionX; xx++){
                    for(int yy = 0; yy < posAux.divisionY;yy++){
                        puntuacionCasilla += arbol.devilverUnPixel(imagenEntrada,"Fondo",
                                posAux.getpixelX(xx),
                                posAux.getpixelY(yy)
                        );
                    }
                }
                //puntuacionCasilla = puntuacionCasilla/diviones;
                if(puntuacionCasilla < 0.4){
                    casillasInterpretadas.put(new pareja(x,y) , "o");
                }else{
                    casillasInterpretadas.put(new pareja(x,y) , "n");
                }
            }
        }
        //System.out.println(casillasInterpretadas);
    }

    public void pintar(){
        String res = rojo;
        String mirar;
        //System.out.println(casillasInterpretadas);
        for(int x = 0 ; x < tamX+2;x++) {
            res += "M";
        }
        res+= reset+"\n";
        for(int y = 0 ; y < tamY;y++) {
            res+=rojo+"M"+reset;
            for (int x = 0; x < tamX; x++) {
                mirar = casillasInterpretadas.get(new pareja(x,y));
                if(mirar.equals("n")){
                    res+="n";
                }
                else{
                    res+=azul+"o"+reset;
                }
            }
            res+=rojo+"M"+reset+"\n";
        }
        res += rojo;
        for(int x = 0 ; x < tamX+2;x++) {
            res += "M";
        }
        res+= reset+"\n";
        System.out.println(res);
    }
}
