import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class leerPantalla{
    private int altura,anchura;
    public static void main(String[] args) throws IOException{
        System.out.println("Buenas");
        leerPantalla lector = new leerPantalla();
        HashMap<String,gradiente> mapaGradiente = lector.iniciarGradiente();

        System.out.println(mapaGradiente);
        //=========================
        //Aqui empezamos a realizar recortes y medidas
        lector.medidas("tetrisJava/imagenes/tocho.png",mapaGradiente);
    }

    private void medidas(String ubi,HashMap<String,gradiente> mapaGradiente) throws IOException{
        gradiente margen,fondo1,fondo2;
        margen = mapaGradiente.get("margen");
        fondo1 = mapaGradiente.get("fondo1");
        fondo2 = mapaGradiente.get("fondo2");
        File imagenF = new File(ubi);
        BufferedImage imagen = ImageIO.read(imagenF);
        int h,w;
        h = imagen.getHeight()/2;
        w = imagen.getWidth();
        int estado = 0;
        int inicioCuadradoJuegoW = 0;
        int inicioCuadradoJuegoH = 0;
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