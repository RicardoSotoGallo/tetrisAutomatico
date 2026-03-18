
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class calcularGrafoVirtual {
    //usamos stack o pilas para las nodos
    public static void main(String[] args) throws Exception {
        datosEstado d = new datosEstado();
        d.leerFichero();
        d.enseñar();
        List<String> accion = calcularGrafoLista(d);
        try {
            FileWriter writer = new FileWriter("ficherosComunos/resultadoHeuristico.richi"); // sobrescribe el archivo
            writer.write(accion.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<String> calcularGrafoLista(datosEstado datos){
        List<String> resultado = new ArrayList<>();
        casilla mejor = new casilla(null, 0, 0);
        Integer mejorHue = 0;
        Integer tamañoPieza = datos.zeroGrados.size();
        Integer sumaAux = 0;
        for(int i = 0; i < datos.posiblesHuecos.size() ; i++){
            datos.posiblesHuecos.get(i).calcularHeuristica(datos , false);
            //System.out.println( "hueco-> "+datos.posiblesHuecos.get(i) +"Heurostica-> "+ datos.posiblesHuecos.get(i).heuristica);
            if(mejorHue < datos.posiblesHuecos.get(i).heuristica || mejor.tipo == null){
                mejor = datos.posiblesHuecos.get(i);
                mejorHue = mejor.heuristica;
            }

            
        }
        //System.out.println("mejor es -> "+mejor);
        //System.out.println("Accion -> "+mejor.obtenerAccion());
        return mejor.obtenerAccion();
        
    }
}
