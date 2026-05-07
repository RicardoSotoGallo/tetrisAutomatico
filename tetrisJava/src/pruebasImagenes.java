import DeteccionColor.ArbolColores;
import LectorCapturas.LectorCaputuraMedicion;
import LectorCapturas.pareja;
import LectorCapturas.posiciones;
import algoritmoAprendizajePorRefuerzo.refuerzo;
import tetrissimulador.Estado;
import tetrissimulador.juego;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;

public class pruebasImagenes {
    public static void main(String[] args){
        //juegos();
        //cosa();
        System.out.println("Iniciando");
        pruebasCon20X10();
    }

    public static void pruebasCon20X10(){
        Integer iteracionesMaximas = 50;
        Integer alturaMaxima = 18;
        Integer alturaMedia = 18;
        Random random = new Random();
        refuerzo refuerzo = new refuerzo();
        refuerzo.iniciarSoloEjecutar();
        Estado estado;
        refuerzo.MejorAccion a,b,c,res = null;
        List<refuerzo.MejorAccion> lsComparacion;
        int numeroAcciones = 0;
        //========
        juego j = new juego(20,10);
        j.configurarLimites(true , iteracionesMaximas , alturaMaxima, alturaMedia);
        String pieza =j.getNombrePieza().get(random.nextInt(j.getNombrePieza().size())); //j.getNombrePieza().get(random.nextInt(j.getNombrePieza().size()));
                    //j.getNombrePieza().get(0);
        String textoEstado = j.iniciarDesdePieza("0000000000",pieza);
        j.dibujar();
        String secciones;
        while (textoEstado.equals("Correcto")){
            estado = j.devolverEstadoClase();

            lsComparacion = new ArrayList<>();

            for(int i = 0 ; i < 5 ; i++){
                if(i < 4){
                    estadoIncremento estadoCorte = devolverCorte(estado,i,i+6,false);
                    res = refuerzo.obtenerMejorAccion(estadoCorte.estado());
                    lsComparacion.add(
                            new refuerzo.MejorAccion(res.posicion(),res.giro(),res.puntuacion() ) //-estado06.alturaRecorte
                    );
                }else{
                    estadoIncremento estadoCorte = devolverCorte(estado,i,i+6,true);
                    res = refuerzo.obtenerMejorAccion(estadoCorte.estado());
                    lsComparacion.add(
                            new refuerzo.MejorAccion(res.posicion(),res.giro(),res.puntuacion() ) //-estado06.alturaRecorte
                    );
                }

            }
            //c = refuerzo.obtenerMejorAccion(estado410.estado());
            float maximo = -Float.MAX_VALUE;
            res = new refuerzo.MejorAccion(0,0,0.0f);
            secciones = "";
            for(int i = 0 ; i < lsComparacion.size() ;i++){
                if(lsComparacion.get(i).puntuacion() > maximo){
                    res = new refuerzo.MejorAccion(lsComparacion.get(i).posicion() + i , lsComparacion.get(i).giro(), lsComparacion.get(i).puntuacion());
                    secciones = "incremento: "+i;
                    maximo = res.puntuacion();
                }

            }

            textoEstado = j.realizarMovimientoDevClase(res.posicion(), res.giro(), -1);

            j.dibujar();
            System.out.println(res+"\n"+secciones);
            numeroAcciones += 1;
        }
        System.out.println(textoEstado);
        System.out.println("Numero de acciones -> "+numeroAcciones);

    }

    public static estadoIncremento devolverCorte(Estado estadoBase , Integer inicioColumna , Integer finalColumna , boolean esFinal){
        /*
        finalColumna es la la primera fila que no se escoge. Recuerdalo
         */
        Integer incremento = inicioColumna;

        Integer pieza = estadoBase.pieza();
        Integer giro = estadoBase.giro();
        List<Integer> accionPosicion = new ArrayList<>();
        List<Integer> accionGiro = new ArrayList<>();
        // Primero cogemos los elemoentos ================
        List<Integer> alturas = new ArrayList<>(estadoBase.alturas().subList(inicioColumna , finalColumna));

        int recorte = recortarAltura(alturas);
        //Ahora cogemos los movimientos =================
        for(int i = 0 ; i < estadoBase.accionPosicion().size();i++){
            if(inicioColumna == 0){
                if(estadoBase.accionPosicion().get(i) < finalColumna){
                    accionPosicion.add(estadoBase.accionPosicion().get(i));
                    accionGiro.add(estadoBase.accionGiro().get(i));
                }
            }else if (esFinal) {
                if(estadoBase.accionPosicion().get(i) >= inicioColumna){
                    accionPosicion.add(estadoBase.accionPosicion().get(i) - incremento);
                    accionGiro.add(estadoBase.accionGiro().get(i));
                }
            }else{
                if(
                        estadoBase.accionPosicion().get(i) >= inicioColumna &&
                        estadoBase.accionPosicion().get(i) < finalColumna
                ){
                    accionPosicion.add(estadoBase.accionPosicion().get(i) - incremento);
                    accionGiro.add(estadoBase.accionGiro().get(i));
                }
            }


        }
        return new estadoIncremento(
                new Estado(alturas,pieza,giro,accionPosicion,accionGiro,0,0,0),
                incremento,
                recorte
        );

    }

    public static Integer recortarAltura(List<Integer> alturas){
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for(int h : alturas){
            if(h < min) min = h;
            if(h > max) max = h;
        }

        for (int i = 0; i < alturas.size(); i++) {
            alturas.set(i, alturas.get(i) - min);
        }

        max -= min;
        int valor;
        if(max > 8){
            for(int i = 0 ; i < alturas.size(); i++){
                 valor = alturas.get(i);
                 alturas.set(i, Math.round( (valor*8f) / max));
            }

        }
        return min;
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

    public record estadoIncremento(Estado estado , Integer incremento, Integer alturaRecorte){}
}


