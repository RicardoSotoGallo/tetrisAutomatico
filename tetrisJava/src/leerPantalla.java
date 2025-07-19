import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

//https://tetrismania.net/
/*
 * Esta es el codigo de leer capturas
 */
public class leerPantalla{
    private int altura,anchura,inicioCuadradoJuegoW,inicioCuadradoJuegoH;
    HashMap<String,posiciones> casilla = new HashMap<>();

    public static void main(String[] args) throws IOException, AWTException,InterruptedException{
        System.out.println("Buenas");
        leerPantalla lector = new leerPantalla();
        HashMap<String,gradiente> mapaGradiente = lector.iniciarGradiente();

        System.out.println(mapaGradiente);
        //=========================
        //Aqui empezamos a realizar recortes y medidas
        //"tetrisJava/imagenes/carpetaAuxiliar/captura.png"
        //"tetrisJava/imagenes/tocho.png"
        /*File imagenF = new File("tetrisJava/imagenes/carpetaAuxiliar/captura.png");
        BufferedImage imagen = ImageIO.read(imagenF);
        
        lector.medidas(imagen,mapaGradiente);

        imagenF = new File("tetrisJava/imagenes/tocho.png");
        imagen = ImageIO.read(imagenF);

        lector.interpretarImagen(imagen, mapaGradiente);*/
        BufferedImage imagen = lector.hacerCapturas();
        lector.medidas(imagen,mapaGradiente);
        System.out.println("Medidas realizadas se inicia dentro de un tiempo 10 segundos");
        Thread.sleep(10000);
        while(true){
            imagen = lector.hacerCapturas();
            lector.interpretarImagen(imagen, mapaGradiente);
            Thread.sleep(500);
        }
        //File archivo = new File("tetrisJava/pruebas.png");
        //ImageIO.write(imagen, "png", archivo);
        /*
         * File archivo = new File("tetrisJava/imagenes/captura"+i+".png");
                ImageIO.write(imagen, "png", archivo);
         */


    }

    public static BufferedImage hacerCapturas() throws InterruptedException, AWTException{
        BufferedImage imagen = new BufferedImage(100, 100, 1);
        try {
            // Crear instancia de Robot
            Robot robot = new Robot();

            // Obtener el tamaño de la pantalla
            Rectangle areaPantalla = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            areaPantalla.setSize(areaPantalla.width/2, areaPantalla.height);
            imagen = robot.createScreenCapture(areaPantalla);

        } catch (AWTException e) {
            System.err.println("Error al capturar pantalla: " + e.getMessage());
            }
        return imagen;
    }

    private void interpretarImagen(BufferedImage imagen,HashMap<String,gradiente> mapaGradiente){
        BufferedImage recorte = imagen.getSubimage(inicioCuadradoJuegoW, inicioCuadradoJuegoH,
                            anchura, altura);
        gradiente fondo1 = mapaGradiente.get("fondo1");
        HashMap<String,String> interpretacion = new HashMap<>();
        String ROJO = "\u001B[31m";
        String AZUL = "\u001B[34m";
        String RESET = "\u001B[0m";
        String texto;
        posiciones pos;
        double puntacion = 0;
        int div = 0;
        Color c;
        for(int i = 0; i < 10;i++){
            for(int j = 0; j < 20;j++){
                texto = i+"-"+j;
                pos = casilla.get(texto);
                puntacion = 0;
                div = (pos.divisionX+1)*(pos.divisionY+1);
                for(int a = 0; a < (pos.divisionX+1);a++){
                    for(int b = 0; b < (pos.divisionY+1);b++){
                        c =new Color(recorte.getRGB(pos.getpixelX(a), pos.getpixelY(b)));
                        puntacion = puntacion + fondo1.esColor(c.getRed(), c.getBlue(), c.getGreen());
                    }
                }
                if(puntacion/div > 0.75){
                    interpretacion.put(texto, ROJO+"U"+RESET);
                }else{
                    interpretacion.put(texto, AZUL+"P"+RESET);
                }

            }
        }

        
        String textoEnviar = "";
        for(int j = 0; j < 20;j++){
            for(int i = 0; i < 10;i++){
                texto = i+"-"+j;
                textoEnviar += interpretacion.get(texto);
            }
            textoEnviar += "\n";
        }
        textoEnviar += "==========================";
        System.out.println(textoEnviar);
    }

    private void medidas(BufferedImage imagen,HashMap<String,gradiente> mapaGradiente) throws IOException{
        gradiente margen,fondo1,fondo2;
        margen = mapaGradiente.get("margen");
        fondo1 = mapaGradiente.get("fondo1");
        fondo2 = mapaGradiente.get("fondo2");
        
        
        int h,w;
        h = imagen.getHeight()/2;
        w = imagen.getWidth();
        int estado = 0;
        inicioCuadradoJuegoW = 0;
        inicioCuadradoJuegoH = 0;
        int correctoAux = 0; 
        

        Color color;
        altura = 0;
        anchura = 0;
        for(int i = 0; i < w; i++){
            color = new Color(imagen.getRGB(i, h));
            if(estado == 0){

                if(margen.esColor(color.getRed(), color.getBlue(), color.getGreen()) == 1.0){
                    estado = 1;
                }else{
                    //imagen.setRGB(i, h, Color.blue.getRGB());
                }
            }else if(estado == 1){
                if (fondo1.esColor(color.getRed(), color.getBlue(), color.getGreen()) == 1.0) {
                    estado = 2;
                    inicioCuadradoJuegoW = i;
                }else{
                    //imagen.setRGB(i, h, Color.red.getRGB());
                    
                }

            }else if(estado == 2){
                anchura = anchura +1;
                if(margen.esColor(color.getRed(), color.getBlue(), color.getGreen()) == 1.0){
                    estado = 3;
                }else{
                    //imagen.setRGB(i, h, Color.pink.getRGB());
                }
            }
        }
        estado = 0;
        h = h*2;
        w = inicioCuadradoJuegoW + anchura/2;
        for(int i = 0; i < h;i++){
            color = new Color(imagen.getRGB(w, i));
            if(estado == 0){
                if(margen.esColor(color.getRed(), color.getBlue(), color.getGreen()) == 1.0){
                    estado = 1;
                }else{
                    //imagen.setRGB(w, i, Color.blue.getRGB());
                }
            }else if(estado == 1){
                if(fondo1.esColor(color.getRed(), color.getBlue(), color.getGreen()) == 1.0){
                    estado = 2;
                    inicioCuadradoJuegoH = i;
                }else{
                    //imagen.setRGB(w, i, Color.red.getRGB());
                }
            }else if(estado == 2){
                altura = altura +1;
                if(margen.esColor(color.getRed(), color.getBlue(), color.getGreen()) == 1.0){
                    estado = 3;
                }else{
                    //imagen.setRGB(w, i, Color.pink.getRGB());
                }
            }
        }


        //===============================================0
        //para comprobar
        /*for(int x = 0 ; x < imagen.getWidth();x++){
            for(int y = 0; y < imagen.getHeight();y++){
                if (x == inicioCuadradoJuegoW           ||
                    y == inicioCuadradoJuegoH           ||
                    x == anchura + inicioCuadradoJuegoW ||
                    y == altura + inicioCuadradoJuegoH) {
                        imagen.setRGB(x, y, Color.green.getRGB());
                    
                }
            }
        }*/

        BufferedImage recorte = imagen.getSubimage(inicioCuadradoJuegoW, inicioCuadradoJuegoH,
                            anchura, altura);


        //=============================
        //ahora obtenemos los cuadraditos y los puntos
        h = recorte.getHeight()/2;
        w = recorte.getWidth();
        correctoAux = 0;
        int aux = 0;
        int maximo = 0;
        //EjeX
        while(correctoAux == 0){
            for(int i = 0; i < w ; i ++){
                if(correctoAux%4 == 0){
                    casilla.put(correctoAux/4+"x", new posiciones());
                    maximo = correctoAux/4;
                }
                color = new Color(recorte.getRGB(i, h));
                //recorte.setRGB(i, h, Color.pink.getRGB());
                if(correctoAux%2 == 0){
                    if(fondo2.esColor(color.getRed(), color.getBlue(), color.getGreen()) == 1.0){
                        //recorte.setRGB(i, h, Color.blue.getRGB());
                        if(aux == 0){
                            casilla.get(maximo+"x").XMin = i;
                            aux = 1;
                        }else{
                            casilla.get(maximo+"x").XMax = i;
                            aux = 0;
                        }
                        correctoAux++;
                    }
                }else{
                    if(fondo2.esColor(color.getRed(), color.getBlue(), color.getGreen()) != 1.0){
                        //recorte.setRGB(i, h, Color.red.getRGB());
                        correctoAux++;
                    }
                }
            }
            if(correctoAux <= 35){
                correctoAux = 0;
                h = h -1;
                aux = 0;
            }
        }
        /*for(int i = 0; i < maximo;i++){
            System.out.println(casilla.get(i+"x")+"->"+i +"x");
        }
        System.out.println("X -> "+maximo);*/
        //EjeY
        h = recorte.getHeight();
        w = recorte.getWidth()/2;
        correctoAux = 0;
        aux = 0;
        maximo = 0;
        while(correctoAux == 0){
            for(int i = 0; i < h ; i ++){
                if(correctoAux%4 == 0){
                    casilla.put(correctoAux/4+"y", new posiciones());
                    maximo = correctoAux/4;
                }
                color = new Color(recorte.getRGB(w, i));
                //recorte.setRGB(w, i, Color.pink.getRGB());
                if(correctoAux%2 == 0){
                    if(fondo2.esColor(color.getRed(), color.getBlue(), color.getGreen()) == 1.0){
                        //recorte.setRGB(w, i, Color.blue.getRGB());
                        if(aux == 0){
                            casilla.get(maximo+"y").YMin = i;
                            aux = 1;
                        }else{
                            casilla.get(maximo+"y").YMax = i;
                            aux = 0;
                        }
                        correctoAux++;
                    }
                }else{
                    if(fondo2.esColor(color.getRed(), color.getBlue(), color.getGreen()) != 1.0){
                        //recorte.setRGB(w, i, Color.red.getRGB());
                        correctoAux++;
                    }
                }
            }
            if(correctoAux <= 75){
                correctoAux = 0;
                w = w -1;
                aux = 0;
            }
        }
        /*for(int i = 0; i < maximo;i++){
            System.out.println(casilla.get(i+"y")+"->"+i+"y");
        }
        System.out.println("Y -> "+maximo);*/

        //Ahora juntamos todo
        posiciones casillaAux;
        String texto;
        for(int i = 0 ; i < 10; i++){
            for(int j = 0; j < 20; j++){
                casillaAux = new posiciones();
                texto = i+"-"+j;
                //System.out.println(texto);
                casillaAux.XMin = casilla.get(i+"x").XMin;
                casillaAux.XMax = casilla.get(i+"x").XMax;
                casillaAux.YMin = casilla.get(j+"y").YMin;
                casillaAux.YMax = casilla.get(j+"y").YMax;
                casilla.put(texto, casillaAux);
            }
        }

        for(int i = 0 ; i < 10; i++){
            for(int j = 0; j < 20; j++){
                texto = i+"-"+j;
                //System.out.println(casilla.get(texto)+"->"+texto);
                casillaAux = casilla.get(texto);
                casillaAux.cacularDivisiones(3, 3, 4, 4);
                //recorte.setRGB(casillaAux.XMin, casillaAux.YMin, Color.pink.getRGB());
                //recorte.setRGB(casillaAux.XMax, casillaAux.YMax, Color.blue.getRGB());
                //System.out.println(casillaAux);
                for(int ii = 0; ii < 4;ii++){
                    for(int jj = 0;jj<4;jj++){
                        recorte.setRGB(casillaAux.getpixelX(ii), casillaAux.getpixelY(jj), Color.red.getRGB());
                    }
                }
            }
        }
        //=============================
        //Guardamos
        
        File resultado = new File("tetrisJava/recortes/resultado.png");
        ImageIO.write(imagen, "png", resultado);

        File recort = new File("tetrisJava/recortes/recorte.png");
        ImageIO.write(recorte, "png", recort);

    }
    
    private HashMap<String,gradiente> iniciarGradiente(){
        HashMap<String,gradiente> res = new HashMap<>();
        res.put("fondo1", 
                obtenerGradiente("tetrisJava/gradienteColores/fondo1.txt") );
        
        res.put("fondo2", 
                obtenerGradiente("tetrisJava/gradienteColores/fondo2.txt"));

        res.put("margen", 
                obtenerGradiente("tetrisJava/gradienteColores/margen.txt"));
        return res;
    }
    
    private static gradiente obtenerGradiente(String dire){
        gradiente res = new gradiente(3);
        

        try (BufferedReader lector = new BufferedReader(new FileReader(dire))) {
            String linea;
            String[] aux;
            //==============================
            linea = lector.readLine();
            aux = linea.split(";");
            res.trapecioRojo.add( Integer.valueOf(aux[0]));
            res.trapecioRojo.add( Integer.valueOf(aux[1]));
            res.trapecioRojo.add( Integer.valueOf(aux[2]));
            res.trapecioRojo.add( Integer.valueOf(aux[3]));
            //==============================
            linea = lector.readLine();
            aux = linea.split(";");
            res.trapecioAzul.add( Integer.valueOf(aux[0]));
            res.trapecioAzul.add( Integer.valueOf(aux[1]));
            res.trapecioAzul.add( Integer.valueOf(aux[2]));
            res.trapecioAzul.add( Integer.valueOf(aux[3]));
            //==============================
            linea = lector.readLine();
            aux = linea.split(";");
            res.trapecioVerde.add( Integer.valueOf(aux[0]));
            res.trapecioVerde.add( Integer.valueOf(aux[1]));
            res.trapecioVerde.add( Integer.valueOf(aux[2]));
            res.trapecioVerde.add( Integer.valueOf(aux[3]));
            //==============================
            linea = lector.readLine();
            aux = linea.split(";");
            res.pesos.set(0, Double.valueOf(aux[0]));
            res.pesos.set(1, Double.valueOf(aux[1]));
            res.pesos.set(2, Double.valueOf(aux[2]));
            res.pesos.set(3, Double.valueOf(aux[3]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;

    }
}