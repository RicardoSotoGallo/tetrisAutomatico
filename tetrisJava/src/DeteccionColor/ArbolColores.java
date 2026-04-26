package DeteccionColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.List;

public class ArbolColores {
    File carpetaEntrenar;
    public ObtenerImagen lector;
    public HashMap<String , HojasColores> diccionarioRaices;
    //HashMap<ColorRgb , ConteoColores> conteoImagen;
    //public HojasColores raiz;

    public ArbolColores(String direImagenes){
        try {
            carpetaEntrenar = new File(direImagenes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HashMap<ColorRgb , ConteoColores> crearDatos(float erro , String nombreSeleccion){


        int ancho , alto;
        HashMap<ColorRgb , ConteoColores> conteoImagen = new HashMap<>();
        conteoImagen = new HashMap<>();
        ancho = lector.imagenReal.getWidth();
        alto = lector.imagenReal.getHeight();
        BufferedImage seleccionImagen = lector.obtenerUnaSeleccion(nombreSeleccion);
        for(int y = 0 ; y < lector.imagenReal.getHeight();y++){
            for(int x = 0 ; x < lector.imagenReal.getWidth();x++){
                int pixel = lector.imagenReal.getRGB(x,y);
                int pixelSen = seleccionImagen.getRGB(x,y);
                        //imagenSeleccion.getRGB(x,y);
                int azulSen = pixelSen & 0xff;
                int rojo = (pixel >> 16) & 0xff;
                int verde = (pixel >> 8) & 0xff;
                int azul = pixel & 0xff;
                ColorRgb color = new ColorRgb(rojo,verde,azul);
                if(!conteoImagen.containsKey(color)){
                    conteoImagen.put(color , new ConteoColores());
                }
                if(azulSen > 120){
                    conteoImagen.get(color).sumaPosi(1);
                }else{
                    conteoImagen.get(color).sumaNega(1);
                }

                //System.out.println("Pixel (" + x + "," + y + "): R=" + rojo + " G=" + verde + " B=" + azul);

            }
        }
        //Vale ya hemos cogido lo que necesitamos ahora vamos a quitar los piexeles random

        float totalPixelASuperar = ancho*alto*erro/100;
        List<ColorRgb> coloresAEliminar = new ArrayList<>();
        for(ColorRgb color : conteoImagen.keySet()){
            int p  = conteoImagen.get(color).getPositivo();
            int n  = conteoImagen.get(color).getNegativo();
            if(Math.abs(p-n) < totalPixelASuperar){
                coloresAEliminar.add(color);
            }
        }
        for( ColorRgb color :  coloresAEliminar){
            conteoImagen.remove(color);
        }
        for(ColorRgb color : conteoImagen.keySet()){
            int p  = conteoImagen.get(color).getPositivo();
            int n  = conteoImagen.get(color).getNegativo();
            //System.out.println(color.toHexa() +" -> "+conteoImagen.get(color));
        }
        return conteoImagen;
    }

    private HojasColores crearIntervalos(HashMap<ColorRgb , ConteoColores> conteoImagen){

        HashMap<Integer,Boolean> capaRoja = new HashMap<>();
        HashMap<Integer,Integer> capaRojaNum = new HashMap<>();
        HashMap<Integer,Boolean> capaAzul = new HashMap<>();
        HashMap<Integer,Integer> capaAzulNum = new HashMap<>();
        HashMap<Integer,Boolean> capaVerde = new HashMap<>();
        HashMap<Integer,Integer> capaVerdeNum = new HashMap<>();
        List<Integer> lsRoja = new ArrayList<>();
        List<Integer> lsAzul = new ArrayList<>();
        List<Integer> lsVerde = new ArrayList<>();
        ConteoColores auxCont;
        int rojoPunto, verdePunto, azulPunto;
        //Esto es para quitar repetidos
        //Se hace las tres capas juntas
        for(ColorRgb color : conteoImagen.keySet()){
            auxCont = conteoImagen.get(color);
            rojoPunto = capaRojaNum.getOrDefault(color.getRojo(), 0);
            verdePunto = capaVerdeNum.getOrDefault(color.getVerde(), 0);
            azulPunto = capaAzulNum.getOrDefault(color.getAzul(), 0);

            if(auxCont.getPositivo() > 0){
                if(rojoPunto < 0) rojoPunto = 0;
                if(verdePunto < 0) verdePunto = 0;
                if(azulPunto < 0) azulPunto = 0;
                capaRojaNum.put(  color.getRojo() , 10 + rojoPunto );
                capaAzulNum.put(  color.getAzul() , 10 + azulPunto );
                capaVerdeNum.put( color.getVerde() , 10 + verdePunto );
            }else {
                if(rojoPunto <= 5){
                    capaRojaNum.put(  color.getRojo() , -10 + rojoPunto );
                }
                if(verdePunto <= 5){
                    capaVerdeNum.put(  color.getVerde() , -10 + verdePunto );
                }
                if(azulPunto <= 5){
                    capaAzulNum.put(  color.getAzul() , -10 + azulPunto );
                }
            }

        }
        //En nuestro casi la mayor division es la que mas numero tiene
        //Ya que escoger una de true 1000 divide al resto false 1000

        //Ahora si lo creamos
        //Aqui por desgracia se tiene que realizar las capas por separadas
        Integer auxInt;
        for( Integer r : capaRojaNum.keySet() ){
            auxInt = capaRojaNum.get(r);
            lsRoja.add(r);
            if(auxInt > 0){
                capaRoja.put(r,true);
            }else{
                capaRojaNum.put(r,-auxInt);
                capaRoja.put(r,false);
            }
        }
        lsRoja.sort(Comparator.naturalOrder());
        //=====
        for( Integer r : capaAzulNum.keySet() ){
            auxInt = capaAzulNum.get(r);
            lsAzul.add(r);
            if(auxInt > 0){
                capaAzul.put(r,true);
            }else{
                capaAzulNum.put(r,-auxInt);
                capaAzul.put(r,false);
            }
        }
        lsAzul.sort(Comparator.naturalOrder());
        //====
        for( Integer r : capaVerdeNum.keySet() ){
            auxInt = capaVerdeNum.get(r);
            lsVerde.add(r);
            if(auxInt > 0){
                capaVerde.put(r,true);
            }else{
                capaVerdeNum.put(r,-auxInt);
                capaVerde.put(r,false);
            }
        }
        lsVerde.sort(Comparator.naturalOrder());

        List<Intervalo> lsRojoRes = crearIntervaloUna( capaRoja , lsRoja , capaRojaNum );
        /*System.out.println("Capa Rojo");
        for(Intervalo i : lsRojoRes){
            System.out.println(i);
        }*/

        List<Intervalo> lsAzulRes = crearIntervaloUna( capaAzul , lsAzul , capaAzulNum );
        /*System.out.println("Capa Azul");
        for(Intervalo i : lsAzulRes){
            System.out.println(i);
        }*/

        List<Intervalo> lsVerdeRes = crearIntervaloUna( capaVerde , lsVerde , capaVerdeNum );
        /*System.out.println("Capa Verde");
        for(Intervalo i : lsVerdeRes){
            System.out.println(i);
        }*/
        //System.out.println("================ Creando el arbol ======================");
        System.out.println(lsRojoRes.size() + lsAzulRes.size() + lsVerdeRes.size());
        return new HojasColores(lsRojoRes , lsAzulRes ,lsVerdeRes);

    }

    private List<Intervalo> crearIntervaloUna( HashMap<Integer,Boolean> capa , List<Integer> capaOrdenada , HashMap<Integer , Integer> pesos){
        List<Intervalo> res = new ArrayList<>();
        int valorInicio = 0;
        int valorFinal = 0;
        int valorSiguiente = 0;
        boolean estadoActual = false;
        boolean estadoMirar;
        int pesoSuma = 0;
        for(int i : capaOrdenada){
            pesoSuma += pesos.get(i);
            estadoMirar = capa.get(i);
            valorSiguiente = i;
            if(estadoMirar != estadoActual){
                if(valorSiguiente != 0){
                    res.add(new Intervalo( valorInicio , valorFinal , estadoActual , pesoSuma ));
                    pesoSuma = 0;
                    res.add(new Intervalo( valorFinal ,valorSiguiente, estadoActual , estadoMirar ));
                    estadoActual = estadoMirar;
                    valorInicio = valorSiguiente;
                }else{
                    estadoActual = estadoMirar;
                }

            }
            valorFinal = valorSiguiente;

        }
        if(valorInicio != 255){
            if(!estadoActual){
                res.add(new Intervalo(valorInicio,255 , estadoActual , pesoSuma));
            }else{
                if(valorInicio != valorSiguiente){
                    res.add(new Intervalo(valorInicio,valorSiguiente , estadoActual , pesoSuma));
                    res.add(new Intervalo(valorSiguiente,255 , true, false) );
                }
            }

        }

        return res;
    }

    public void obtenerTodaRaizes(float error){
        this.lector = new ObtenerImagen();
        boolean correcto = lector.obtenerImagenes(carpetaEntrenar.getPath());
        diccionarioRaices = new HashMap<>();
        /*
        Vamos a Quitar la salida de los datos
         */
        if(correcto){
            for(String nombres : lector.setNombres()){
                System.out.println("Nombre seleccion => "+nombres +" ============================");
                HashMap<ColorRgb, ConteoColores> a = crearDatos(error , nombres);
                HojasColores raiz = crearIntervalos(a);
                diccionarioRaices.put(nombres , raiz);
            }
        }else{
            System.out.println("Fichero no encontrado");
        }

    }

    public float devilverUnPixel(BufferedImage imaOriginal , String seleccion , Integer x , Integer y){
        HojasColores raiz = diccionarioRaices.get(seleccion);
        int pixel = imaOriginal.getRGB(x,y);
        Color color = new Color(pixel);
        int pRojo;
        int pAzul;
        int pVerde;
        pRojo = color.getRed();
        pVerde = color.getGreen();
        pAzul = color.getBlue();
        return raiz.preguntaInter( pRojo , pAzul , pVerde );
    }

    public void analizarUnaSeleccion(BufferedImage entrada , String direccionCarpetaSalida , String seleccion){
        float valorPixel;
        try {
            int alto = entrada.getHeight();
            int ancho = entrada.getWidth();
            BufferedImage salida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    valorPixel = devilverUnPixel(entrada , seleccion , x,y);
                    salida.setRGB(x,y,
                            (Math.round( valorPixel*255) << 16) |
                                    (Math.round( valorPixel*255) << 8) |
                                    (Math.round( valorPixel*255))
                            );

                }}
            File archivo = new File(direccionCarpetaSalida+"/"+seleccion+".png");
            ImageIO.write(salida,"png",archivo);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void analizarTodoSeleccion(BufferedImage entrada , String direccionCarpetaSalida){
        for(String nombre : diccionarioRaices.keySet()){
            analizarUnaSeleccion(entrada , direccionCarpetaSalida , nombre);
        }
    }

    public void guardarDatos(){

        for(String nombre : diccionarioRaices.keySet()){
            String texto = "";
            HashMap<Intervalo,Integer> diccionarioIntervalo = new HashMap<>();
            HashMap<String , String> diccionarioArbol = new HashMap<>();
            //String textoArbol =
            diccionarioRaices.get(nombre).devolverEstructura(diccionarioIntervalo ,diccionarioArbol,"raiz");
            texto += "=============="+nombre+"==============\n";
            for(Intervalo inter: diccionarioIntervalo.keySet()){
                texto +=diccionarioIntervalo.get(inter)+";"+
                        inter.getInicio()+";"+
                        inter.getFin()+";"+
                        inter.getCapa()+";"+
                        inter.getTipo()+";"+
                        inter.getPesos()+"\n";
            }
            texto+="============================\n";
            for(String vertice : diccionarioArbol.keySet()){
                texto+= vertice+":"+diccionarioArbol.get(vertice)+"\n";
            }
            try {
                FileWriter writer = new FileWriter(carpetaEntrenar+"/Ficheros/parametros@"+nombre+".txt");
                writer.write(texto);
                writer.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }




    }

    public void obtenerDatos(){
        diccionarioRaices = new HashMap<>();
        File ficheroParametros = new File(carpetaEntrenar+"/Ficheros");
        List<File> direParametro = Arrays.stream(ficheroParametros.listFiles()).filter(t -> t.getName().contains("parametros")).toList();
        for(File para : direParametro){
            String nombre = para.getName().split("@")[1].replace(".txt","");
            System.out.println(para.getName());
            System.out.println(nombre);
            //if(nombre.equals("Verde")){
                try {
                    BufferedReader br = new BufferedReader(new FileReader(para));
                    String linea;
                    int estado = 0;
                    HashMap<Integer,Intervalo> diccIntervalo = new HashMap<>();
                    HashMap<String,HojasColores> diccHojas = new HashMap<>();
                    Intervalo intervaloAux;
                    String[] auxString;
                    String[] auxString2;
                    while ((linea = br.readLine()) != null){
                        if(estado == 0){
                            if(linea.contains("=")) {
                                estado++;
                            }
                        }
                        else if(estado == 1){
                            if(linea.contains("=")) {
                                estado++;
                            }
                            else {

                                auxString = linea.split(";");
                                intervaloAux = new Intervalo(
                                        auxString[1],
                                        auxString[2],
                                        auxString[3],
                                        auxString[4],
                                        auxString[5]
                                        );
                                diccIntervalo.put(Integer.valueOf(auxString[0]),intervaloAux);
                            }
                        }
                        else if(estado == 2){
                            auxString = linea.split(":");
                            auxString2 = auxString[1].split(";");
                            diccHojas.put(
                                    auxString[0],
                                    new HojasColores(auxString2[0],auxString2[1],diccIntervalo)
                            );

                        }


                        //System.out.println(linea + " -> "+estado);

                    }
                    /*
                    Ahora solo queda reconstruir las aristas
                     */
                    //System.out.println(diccIntervalo);
                    diccionarioRaices.put(nombre,diccHojas.get("raiz"));
                    diccionarioRaices.get(nombre).contruirEnlaces(diccHojas , "");
                    //diccionarioRaices.get(nombre).showArbol("");
                    /*for(String h : diccHojas.keySet()){
                        System.out.println(h+"->"+diccHojas.get(h).toString());
                    }*/
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }



            //}
        }
    }
}

