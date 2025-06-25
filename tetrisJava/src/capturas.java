

//https://tetrismania.net/

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;

public class capturas {
    public static void main(String[] args) throws InterruptedException, IOException {
        //hacerCapturas();
        String etiqueta = "verde";
        String imagen = "tetrisJava/imagenes/tocho.png";
        String pesos = "tetrisJava/imagenes/tochoNaranja.png";
        leerCapturas(imagen
        ,pesos
        ,etiqueta
        ,0.1 
        , 100 
        ,1000
        ,0.0001);
        gradiente grad = obtenerGradiente("tetrisJava/gradienteColores/"+etiqueta+".txt");
        //========================= Vamos a hacer la prueba del alogodon
        File archivoImagenC = new File(imagen);
        BufferedImage imagenC = ImageIO.read(archivoImagenC);
        Color colorAux;
        double salida;
        for(int w = 0; w < imagenC.getWidth();w++){
            for(int h = 0; h < imagenC.getHeight();h++){
                colorAux = new Color( imagenC.getRGB(w, h) );
                salida = grad.esColor(colorAux.getRed(), colorAux.getBlue(), colorAux.getGreen());
                if(salida >= 0.5){
                    imagenC.setRGB(w, h, Color.BLUE.getRGB());
                }else{
                    imagenC.setRGB(w, h, Color.GREEN.getRGB());
                }
            }
        }
        File algodonArchivo = new File("tetrisJava/imagenes/algodon"+etiqueta+".png");
        ImageIO.write(imagenC, "png", algodonArchivo);

    }

    public static gradiente obtenerGradiente(String dire){
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

    public static void leerCapturas(String direImagen , String direPesos, String nombre, Double margenDeError, Integer numeroDeEstaciones, Integer tamañoEstacion , Double factorAprendizaje ) throws IOException{
        Color colorC;
        Color colorS;
        double peso;
        List<clasePixel> ls = new ArrayList<>();
        HashMap<Integer,Double> mapaRojo = new HashMap<>();
        HashMap<Integer,Double> mapaAzul = new HashMap<>();
        HashMap<Integer,Double> mapaVerde = new HashMap<>();
        gradiente grad = new gradiente(3);
        try (FileWriter escritor = new FileWriter("tetrisJava/gradienteColores/"+nombre+".txt")){
                //Aqui se entrena el trapecio
                File archivoImagenC = new File(direImagen);
                BufferedImage imagenC = ImageIO.read(archivoImagenC);
                File archivoImagenS = new File(direPesos);
                BufferedImage imagenS = ImageIO.read(archivoImagenS);
                for(int w = 0; w < imagenC.getWidth();w++){
                    for(int h = 0; h < imagenC.getHeight();h++){
                        colorC = new Color(imagenC.getRGB(w, h));
                        colorS = new Color(imagenS.getRGB(w, h));
                        
                        peso = 1 - (double)colorS.getRed()/255;
                        
                        ls.add( new clasePixel(h, w, colorC, peso, 0) );
                        if(mapaRojo.containsKey( colorC.getRed() )){
                            mapaRojo.put(colorC.getRed(), mapaRojo.get(colorC.getRed())+ peso);
                        }else{
                            mapaRojo.put(colorC.getRed(), peso);
                        }

                        if(mapaAzul.containsKey( colorC.getBlue() )){
                            mapaAzul.put(colorC.getBlue(), mapaAzul.get(colorC.getBlue())+ peso);
                        }else{
                            mapaAzul.put(colorC.getBlue(), peso);
                        }

                        if(mapaVerde.containsKey( colorC.getGreen() )){
                            mapaVerde.put(colorC.getGreen(), mapaVerde.get(colorC.getGreen())+ peso);
                        }else{
                            mapaVerde.put(colorC.getGreen(), peso);
                        }
                        
                        //System.out.println(colorC+" |"+w+","+h+"| -> "+peso);
                        
                        


                    }
                }
                grad.contruizFuzzy(mapaRojo, mapaVerde, mapaAzul, margenDeError);
                escritor.write(grad.trapecioRojo.get(0)+";"+grad.trapecioRojo.get(1)+";"+grad.trapecioRojo.get(2)+";"+grad.trapecioRojo.get(3)+"\n");
                escritor.write(grad.trapecioAzul.get(0)+";"+grad.trapecioAzul.get(1)+";"+grad.trapecioAzul.get(2)+";"+grad.trapecioAzul.get(3)+"\n");
                escritor.write(grad.trapecioVerde.get(0)+";"+grad.trapecioVerde.get(1)+";"+grad.trapecioVerde.get(2)+";"+grad.trapecioVerde.get(3)+"\n");
                
                //Ahora toca entrenar el perceptron
                List<Double> aux;
                int porDondeVamos = 0;
                List<clasePixel> Unos = new ArrayList<>();
                List<clasePixel> Zeros = new ArrayList<>();
                for(clasePixel c : ls){
                    if(c.getValor() > 0.5){
                        Unos.add(c);
                    }else{
                        Zeros.add(c);
                    }
                }
                
                // Ahora entrenamos el perceptron
                List<List<Double>> coloresEntrenar;
                List<Double> salida;
                Integer indiceAzar;
                Color color;
                Random random = new Random();
                List<Color> entradaTest = new ArrayList<>();
                List<Double> salidaTest = new ArrayList<>();
                for (clasePixel i : ls) {
                    entradaTest.add(i.getColor());
                    salidaTest.add(i.getValor());
                }
                for(int etapa = 0; etapa < numeroDeEstaciones;etapa++){
                    coloresEntrenar = new ArrayList<>();
                    salida = new ArrayList<>();
                    for(int i = 0; i < tamañoEstacion / 2 ; i++){
                        aux = new ArrayList<>();
                        indiceAzar = random.nextInt(Unos.size());
                        color = Unos.get(indiceAzar).getColor();
                        //Aqui meto la salida
                        salida.add(Unos.get(indiceAzar).getValor());
  
                        aux.add(
                            grad.fuzzyRojo(color.getRed())
                            );

                        aux.add(
                            grad.fuzzyAzul(color.getBlue())
                            );

                        aux.add(
                            grad.fuzzyVerde(color.getGreen())
                            );
                        //Aqui meto el color
                        coloresEntrenar.add(aux);


                        aux = new ArrayList<>();
                        indiceAzar = random.nextInt(Zeros.size());
                        color = Zeros.get(indiceAzar).getColor();
                        //Aqui meto la salida
                        salida.add(Zeros.get(indiceAzar).getValor());
                        //
                        aux.add(
                            grad.fuzzyRojo(color.getRed())
                            );

                        aux.add(
                            grad.fuzzyAzul(color.getBlue())
                            );

                        aux.add(
                            grad.fuzzyVerde(color.getGreen())
                            );
                        //Aqui meto el color
                        coloresEntrenar.add(aux);
                    }
                    grad.entrenarPerceptron(coloresEntrenar, salida, 1, factorAprendizaje);
                    
                    //===========================
                    //Esto es solo para comprobar el error
                    String mensaje = String.format("error -> %.4f%n", grad.errorCometido(entradaTest, salidaTest))
                    + "pesos son -> " + grad.pesos;
                    System.out.println(mensaje);//;
                    //%.4f%n
                }
            escritor.write(grad.pesos.get(0)+";"+ grad.pesos.get(1)+";"+grad.pesos.get(2)+";"+grad.pesos.get(3)+";"+"\n");
            escritor.close();
                

        }catch (Exception e) {

        }
        /*List<clasePixel> ls = new ArrayList<>();
        List<List<Double>> entrenamiento;
        List<Double> salida;
        HashMap<Color,Double> mapaColor = new HashMap<>();
        HashMap<Integer,Double> rojo = new HashMap<>();
        HashMap<Integer,Double> verde = new HashMap<>();
        HashMap<Integer,Double> azul = new HashMap<>();
        clasePixel pixel;
        Color colorC;
        Color colorS;
        String nombreFichero = "tetrisJava/imagenes/pixeles.txt";
        
        try (FileWriter escritor = new FileWriter(nombreFichero)) {
            for(int i = 0; i < numeroImagenes;i++){
                System.out.println("imagen numero: "+i);
                // Ruta relativa desde el directorio de ejecución
                File archivoImagenC = new File("tetrisJava/imagenes/captura"+i+".png");
                BufferedImage imagenC = ImageIO.read(archivoImagenC);
                File archivoImagenS = new File("tetrisJava/imagenes/captura"+i+"S.png");
                BufferedImage imagenS = ImageIO.read(archivoImagenS);
                for(int w = 0; w < imagenC.getWidth();w++){
                    for(int h = 0; h < imagenC.getHeight();h++){
                        colorC = new Color(imagenC.getRGB(w, h));
                        colorS = new Color(imagenS.getRGB(w, h));
                        pixel = new clasePixel(w, h, colorC , 1-(double)colorS.getRed()/255, i);
                        ls.add(pixel);
                        //System.out.println(1- (double)colorS.getRed()/255);
                        if(mapaColor.containsKey(pixel.getColor())){
                            rojo.put(pixel.getColor().getRed(),  rojo.get(pixel.getColor().getRed())+pixel.getValor() );
                            azul.put(pixel.getColor().getBlue(),  azul.get(pixel.getColor().getBlue())+pixel.getValor() );
                            verde.put(pixel.getColor().getGreen(),  verde.get(pixel.getColor().getGreen())+pixel.getValor() );
                            
                            if(mapaColor.get(pixel.getColor()) < pixel.getValor()){
                                mapaColor.put(pixel.getColor(), pixel.getValor());
                            }
                        }else{
                            mapaColor.put(pixel.getColor(), pixel.getValor());
                            rojo.put(pixel.getColor().getRed(),  pixel.getValor() );
                            azul.put(pixel.getColor().getBlue(),  pixel.getValor() );
                            verde.put(pixel.getColor().getGreen(),  pixel.getValor() );
                        }
                        
                    }
                }
            }
            gradiente grad = new gradiente(3);
            grad.contruizFuzzy(rojo, verde, azul, 0.1);
            escritor.write(grad.trapecioRojo.get(0)+";"+grad.trapecioRojo.get(1)+";"+grad.trapecioRojo.get(2)+";"+grad.trapecioRojo.get(3)+"\n");
            escritor.write(grad.trapecioAzul.get(0)+";"+grad.trapecioAzul.get(1)+";"+grad.trapecioAzul.get(2)+";"+grad.trapecioAzul.get(3)+"\n");
            escritor.write(grad.trapecioVerde.get(0)+";"+grad.trapecioVerde.get(1)+";"+grad.trapecioVerde.get(2)+";"+grad.trapecioVerde.get(3)+"\n");
            List<Double> aux;
            int porDondeVamos = 0;
            List<Color> Unos = new ArrayList<>();
            List<Color> Zeros = new ArrayList<>();
            for(clasePixel i:ls){
                if(i.getValor() >= 0.5){
                    Unos.add(i.getColor());
                }else{
                    Zeros.add(i.getColor());
                }
            }

            List<Color> colocresEntrenado;
            int indiceAleatorio;
            System.out.println("pesos Inicial -> "+grad.pesos);
            for(int j = 0; j < 500; j++){
                entrenamiento = new ArrayList<>();
                colocresEntrenado = new ArrayList<>();
                salida = new ArrayList<>();
                    for(int i = 0; i < 250 ;i++){
                        aux = new ArrayList<>();
                        Random random = new Random();
                        indiceAleatorio = random.nextInt(Unos.size());
                        aux.add( grad.fuzzyRojo(Unos.get(indiceAleatorio).getRed()) );
                        aux.add( grad.fuzzyAzul(Unos.get(indiceAleatorio).getBlue()) );
                        aux.add( grad.fuzzyVerde(Unos.get(indiceAleatorio).getGreen()) );
                        entrenamiento.add(aux);
                        colocresEntrenado.add(Unos.get(indiceAleatorio));
                        salida.add(1.0);
                        //====================================
                        aux = new ArrayList<>();
                        indiceAleatorio = random.nextInt(Zeros.size());
                        aux.add( grad.fuzzyRojo(Zeros.get(indiceAleatorio).getRed()) );
                        aux.add( grad.fuzzyAzul(Zeros.get(indiceAleatorio).getBlue()) );
                        aux.add( grad.fuzzyVerde(Zeros.get(indiceAleatorio).getGreen()) );
                        entrenamiento.add(aux);
                        colocresEntrenado.add(Zeros.get(indiceAleatorio));
                        salida.add(0.0);

                    }
                grad.entrenarPerceptron(entrenamiento, salida, 10, 0.01);
                System.out.println("pesos son -> "+grad.pesos +" error -> "+grad.errorCometido(colocresEntrenado,salida));
                
            }
            escritor.write(grad.pesos.get(0)+";"+ grad.pesos.get(1)+";"+grad.pesos.get(2)+";"+grad.pesos.get(3)+";"+"\n");
            escritor.close();
            

        } catch (Exception e) {
        }*/
    }

    public static void hacerCapturas() throws InterruptedException, AWTException{
        try {
            // Crear instancia de Robot
            Robot robot = new Robot();

            // Obtener el tamaño de la pantalla
            Rectangle areaPantalla = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            areaPantalla.setSize(areaPantalla.width/2, areaPantalla.height);
            for(int i = 0; i < 20; i++){
                Thread.sleep(30000);
                    // Capturar la imagen de la pantalla
                BufferedImage imagen = robot.createScreenCapture(areaPantalla);

                //Color i = new Color(imagen.getRGB(100, 100));
                //System.out.println(i.getBlue());
                // Guardar la imagen en un archivo
                File archivo = new File("tetrisJava/imagenes/captura"+i+".png");
                ImageIO.write(imagen, "png", archivo);

                System.out.println("Captura de pantalla guardada como 'captura.png'");
            
            }
            } catch (AWTException | java.io.IOException e) {
            System.err.println("Error al capturar pantalla: " + e.getMessage());
        }
    }
}
