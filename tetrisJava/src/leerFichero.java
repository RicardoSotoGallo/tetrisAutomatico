
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class leerFichero {
    String localizacion;
    public leerFichero(){
        localizacion = "ficherosComunos/estadoJuego.richi";
        
    }
    public void leerFichero(){
        try(BufferedReader br = new BufferedReader(new FileReader(localizacion))){
            String linea = br.readLine();
            System.out.println(linea);

        }catch(IOException io){
            System.out.println("No luedo leer el fichero");
        }
    }
}
