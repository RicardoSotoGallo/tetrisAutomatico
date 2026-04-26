package DeteccionColor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class ObtenerImagen {
    public BufferedImage imagenReal;
    private BufferedImage imagenSeleccion;
    private HashMap<String,BufferedImage> imagenSeleccionDict;
    public boolean obtenerImagenes(String direFichero){
        boolean res = false;
        imagenSeleccionDict = new HashMap<>();
        try {
            File carpetaImagenes = new File(direFichero+"/Imagen");
            File carpetaRecorte = new File(direFichero+"/Seleccion");
            String auxString;
            if(carpetaRecorte.exists() && carpetaImagenes.exists() && carpetaRecorte.isDirectory() && carpetaImagenes.isDirectory() &&
                Objects.requireNonNull(carpetaImagenes.listFiles()).length > 0 && Objects.requireNonNull(carpetaRecorte.listFiles()).length > 0){
                File direIm = Objects.requireNonNull(carpetaImagenes.listFiles())[0];
                File direRec = Objects.requireNonNull(carpetaRecorte.listFiles())[0];
                for(File i : Objects.requireNonNull(carpetaRecorte.listFiles())){
                    auxString = i.getName().split("\\.")[0];
                    imagenSeleccionDict.put(auxString , ImageIO.read(i));
                }

                imagenReal = ImageIO.read(direIm);
                imagenSeleccion = ImageIO.read(direRec);
                //System.out.println(imagenSeleccionDict);
                res = true;
                //System.out.println("Entre");

            }else{
                System.out.println("No existe la carpeta necesarias");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;

    }

    public BufferedImage obtenerUnaSeleccion(String nombre){
        return imagenSeleccionDict.get(nombre);
    }

    public Set<String> setNombres(){
        return imagenSeleccionDict.keySet();
    }
}
