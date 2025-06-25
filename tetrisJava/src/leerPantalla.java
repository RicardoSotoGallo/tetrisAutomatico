import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class leerPantalla{
    public static void main(String[] args){
        System.out.println("Buenas");
        leerPantalla lector = new leerPantalla();
        HashMap<String,gradiente> mapaGradiente = lector.iniciarGradiente();

        System.out.println(mapaGradiente);
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