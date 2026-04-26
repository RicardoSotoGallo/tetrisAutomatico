import DeteccionColor.ArbolColores;
import LectorCapturas.LectorCaputuraMedicion;
import LectorCapturas.pareja;
import LectorCapturas.posiciones;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class pruebasImagenes {
    public static void main(String[] args){
        juegos();
        //cosa();


    }

    public static void juegos(){
        int rojo = 255;
        int verde = 105;
        int azul = 180;

        int rosa = (0xFF << 24) | (rojo << 16) | (verde << 8) | azul;

        try {
            Robot robot = new Robot();

            int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
            int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

            // Obtener el tamaño de la pantalla
            Rectangle screenRect = new Rectangle(0, 0, screenWidth / 2, screenHeight);

            // Capturar la pantalla completa
            BufferedImage entrada = robot.createScreenCapture(screenRect);

            LectorCaputuraMedicion leer = new LectorCaputuraMedicion();
            //======================
            leer.realizarMedidas(entrada);
            //======================
            for(pareja par : leer.posicionDicc.keySet()){
                posiciones pos = leer.posicionDicc.get(par);
                for(int x = 0 ; x < pos.divisionX +1; x++ ){
                    for(int y = 0; y < pos.divisionY + 1;y++){
                        entrada.setRGB( pos.getpixelX(x),pos.getpixelY(y),rosa );
                    }
                }
            }


            System.out.println("Esperamos a iniciar Juegos");
            boolean inicio = true;
            while (inicio){
                entrada = robot.createScreenCapture(screenRect);
                leer.interpretarCasillas(entrada);
                inicio = !leer.casillasInterpretadas.values().stream().allMatch(c -> c.equals("n"));
                //leer.pintar();
                //System.out.println(inicio);
            }
            System.out.println("Juego empezado");
            //Esperamos a que aparezca pieza
            inicio = true;
            while (inicio){
                entrada = robot.createScreenCapture(screenRect);
                leer.interpretarCasillas(entrada);
                for(int x = 0 ; x < leer.tamX;x++){
                    if(leer.casillasInterpretadas.get(new pareja(x,0)).equals("o")){
                        inicio = false;

                    }
                }
            }

            System.out.println("Pieza aparecidas");

            //Esperamos a dejar la fila superior vacia
            inicio = true;
            while (inicio){
                entrada = robot.createScreenCapture(screenRect);
                leer.interpretarCasillas(entrada);
                inicio = false;
                for(int x = 0 ; x < leer.tamX;x++){
                    if(leer.casillasInterpretadas.get(new pareja(x,0)).equals("o")){
                        inicio = true;
                    }
                }
            }
            entrada = robot.createScreenCapture(screenRect);
            leer.interpretarCasillas(entrada);
            leer.pintar();
            aislarPieza(leer);




            // Guardar la imagen en un archivo
            /**/


            //File salida = new File( "tetrisJava/ImagenEntrenamiento/Tetris/Ficheros/"+"tetrisPruebasSal.png"  );
            //ImageIO.write(entrada,"png",salida);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void cosa(){

        /*
        Aqui se realiza en entrenamiento
         */
        /*ArbolColores arbol = new ArbolColores("tetrisJava/ImagenEntrenamiento/Tetris");
        arbol.obtenerTodaRaizes(0.2f);
        BufferedImage entrada;

        try {
            File archivo = new File("tetrisJava/ImagenEntrenamiento/Tetris/Imagen/tetris.png");
            entrada = ImageIO.read(archivo);
            //arbol.analizarUnaSeleccion(entrada , "src/ImagenEntrenamiento/Frieren/Ficheros" , "DosColores");
            arbol.analizarTodoSeleccion(entrada , "tetrisJava/ImagenEntrenamiento/Tetris/Ficheros");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        arbol.guardarDatos();*/


        BufferedImage entrada;
        int rojo = 255;
        int verde = 105;
        int azul = 180;

        int rosa = (0xFF << 24) | (rojo << 16) | (verde << 8) | azul;
        try {
            File archivo = new File("tetrisJava/ImagenEntrenamiento/Tetris/Imagen/tetris.png");
            entrada = ImageIO.read(archivo);
            LectorCaputuraMedicion leer = new LectorCaputuraMedicion();
            //======================
            leer.realizarMedidas(entrada);
            //======================
            int x;
            int y;

            y = entrada.getHeight()/2;
            for(x = 0 ; x < entrada.getWidth() ; x++){
                if(leer.xTableroMin <= x && leer.xTableroMax >= x){
                    entrada.setRGB(x,y,rosa);
                }
            }

            x = entrada.getWidth()/2;
            for(y = 0 ; y < entrada.getWidth() ; y++){
                if(leer.yTableroMin <= y && leer.yTableroMax >= y){
                    entrada.setRGB(x,y,rosa);
                }
            }
            for(pareja par : leer.posicionDicc.keySet()){
                posiciones pos = leer.posicionDicc.get(par);
                for(x = 0 ; x < pos.divisionX ; x++ ){
                    for(y = 0; y < pos.divisionY ;y++){
                        entrada.setRGB( pos.getpixelX(x),pos.getpixelY(y),rosa );
                    }
                }
            }


            File salida = new File( "tetrisJava/ImagenEntrenamiento/Tetris/Ficheros/"+"tetrisPruebasSal.png"  );
            ImageIO.write(entrada,"png",salida);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void aislarPieza(LectorCaputuraMedicion leer){
        List<pareja> piezaCord = new ArrayList<>();
        int xMin = leer.tamX;
        boolean noMasPieza = true;
        for(int y = 1; y < leer.tamY;y++){
            noMasPieza = true;
            for(int x = 0; x < leer.tamX;x++){
                if(leer.casillasInterpretadas.get(new pareja(x,y)).equals("o") ){
                    noMasPieza = false;
                    piezaCord.add(new pareja(x,y-1));
                    if(x < xMin){
                        xMin = x;
                    }

                }
            }
            if(noMasPieza) break;
        }
        String res ="";
        for(int y = 0; y < 5;y++){
            for(int x = xMin; x < xMin+ 5;x++){
                if(piezaCord.contains( new pareja(x,y) )){
                    res+="O";
                }else {
                    res+=" ";
                }
            }
            res+="\n";
        }

        System.out.println(res);

    }
}
