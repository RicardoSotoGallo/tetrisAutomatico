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
        System.out.println("Hola");
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
        int numeroAcciones = 0;
        //========
        juego j = new juego(20,10);
        j.configurarLimites(true , iteracionesMaximas , alturaMaxima, alturaMedia);
        String pieza =j.getNombrePieza().get(random.nextInt(j.getNombrePieza().size())); //j.getNombrePieza().get(random.nextInt(j.getNombrePieza().size()));
                    //j.getNombrePieza().get(0);
        String textoEstado = j.iniciarDesdePieza("0000000000",pieza);
        j.dibujar();

        while (textoEstado.equals("Correcto")){
            estado = j.devolverEstadoClase();
            estadoIncremento estado06 = devolverCorte(estado,0,6,false);
            a = refuerzo.obtenerMejorAccion(estado06.estado());
            estadoIncremento estado28 = devolverCorte(estado,2,8,false);
            b = refuerzo.obtenerMejorAccion(estado28.estado());
            estadoIncremento estado410 = devolverCorte(estado,4,10,true);
            c = refuerzo.obtenerMejorAccion(estado410.estado());

            res = new refuerzo.MejorAccion(a.posicion() + estado06.incremento , a.giro(), a.puntuacion());
            if(a.puntuacion() < b.puntuacion()){
                res = new refuerzo.MejorAccion( b.posicion()+estado28.incremento , b.giro(), b.puntuacion() );
            }
            if(b.puntuacion() < c.puntuacion()){
                res = new refuerzo.MejorAccion(c.posicion() + estado410.incremento , c.giro(), c.puntuacion());
            }
            textoEstado = j.realizarMovimientoDevClase(res.posicion(), res.giro(), -1);

            j.dibujar();
            numeroAcciones += 1;
        }
        System.out.println(textoEstado);
        System.out.println("Numero de acciones -> "+numeroAcciones);

        /*System.out.println(estado.accionPosicion());
        System.out.println(estado.accionGiro());
        //=========================
        estadoIncremento estado06 = devolverCorte(estado,0,6,false);
        System.out.println(estado06.incremento+"\n"+estado06.estado+"\n"+"puntuacion ->"+refuerzo.obtenerMejorAccion(estado06.estado())+"\n======================");
        //=========================
        estadoIncremento estado28 = devolverCorte(estado,2,8,false);
        System.out.println(estado28.incremento+"\n"+estado28.estado+"\n"+"puntuacion ->"+refuerzo.obtenerMejorAccion(estado28.estado())+"\n======================");
        //=========================
        estadoIncremento estado410 = devolverCorte(estado,4,10,true);
        System.out.println(estado410.incremento+"\n"+estado410.estado+"\n"+"puntuacion ->"+refuerzo.obtenerMejorAccion(estado410.estado())+"\n======================");
*/
        /*
            Conseguido
            Ahora el plan es devolver mejor estado con su puntuacion con cada estado
            La puntuacion se guarda aqui

            Luego seria realizar accion y repetir en bucle
            j.realizarMovimientoDevClase(  posicion + incremento, Integer giro, int siguientePieza );
         */

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
        Integer minimo = alturas.stream().min(Comparator.naturalOrder()).orElse(0);
        alturas = alturas.stream().map(s -> s - minimo).toList();
        //Ahora cogemos los movimientos =================
        for(int i = 0 ; i < estadoBase.accionPosicion().size();i++){
            if(inicioColumna == 0){
                if(estadoBase.accionPosicion().get(i) < finalColumna){
                    accionPosicion.add(estadoBase.accionPosicion().get(i));
                    accionGiro.add(estadoBase.accionGiro().get(i));
                }
            } else if (esFinal) {
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
                incremento
        );

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

    public record estadoIncremento(Estado estado , Integer incremento){}
}


