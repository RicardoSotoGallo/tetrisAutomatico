package calculoHeuristicaTetris;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
Vamos a explicar lo que hay que hacer
Toca hacer un rework de todo_

 */


public class CalcularHeuristicaTablero {
    //usamos stack o pilas para las nodos
    public static void main(String[] args) throws Exception {
        /*
        Por este lado ya no usamos el codigo python. Ahora usamos el simulador de java
        Algo mas simples y con todos los estados.
        Tambien esto dejara de ser un mein a una funcion de iniciacion
         */
        /*
        datosEstado d = new datosEstado();
        d.leerFichero();

        -- Aqui cargariamos los datos --

        -- Y hariamos el bucle jugable --


        d.enseñar();
        List<String> accion = calcularGrafoLista(d);
        try {
            FileWriter writer = new FileWriter("ficherosComunos/resultadoHeuristico.richi"); // sobrescribe el archivo
            writer.write(accion.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

         */
    }

    public CalcularHeuristicaTablero(){
        // -- Esta funcion tiene que sustituir a la anterior.
        // -- Una vez iniciada se incia el juego y se puntua
        // Se escoje la mejorAccion() y se juega
        // -- para cuando el juego se lo pide
    }

    // -- hay que crear una funcion de dando un tablero que devuelva en una lista los posibles huecos --

    public static List<String> mejorAccion(){

        // -- Aqui se recorreria los huecos de forma ordenada y se pararia cuando se encuentre uno bueno --
        // -- al final esto va a devolver una accion --

        //datosEstado datos
        /*
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
         */

        return new ArrayList<>();
    }


}
