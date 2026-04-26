import calculoHeuristicaTetris.CalcularHeuristicaTablero;
import calculoHeuristicaTetris.resultadoHeuristica;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class pruebasHeuristica {
    public static void main(String[] args){
        Integer altura = 10;
        Integer anchura = 6;
        /*Integer pieza = -1;
        CalcularHeuristicaTablero heo = new CalcularHeuristicaTablero(altura , anchura,
                50, altura-2,altura-2,
                true, pieza);

        System.out.println(
                heo.resultadoFinal.titulo()
        );

        System.out.println(
                heo.resultadoFinal.fila()
        );*/

        for(int p = -1 ; p < 7 ; p++){
            testAutomatico(altura,anchura,p);
        }





    }
    public static void testAutomatico( Integer altura , Integer anchura ,  Integer pieza){
        List<resultadoHeuristica> resultadoHeuristicasLs = new ArrayList<>();
        for(int i = 0; i < 50; i++){
            System.out.println("Partida-> "+i);
            CalcularHeuristicaTablero heo = new CalcularHeuristicaTablero(altura , anchura,
                    50, altura-2,altura-2,
                    false, pieza);

            resultadoHeuristicasLs.add(heo.resultadoFinal);
        }
        String tipo = "Heuristica_"+altura+"_"+anchura+"_"+pieza;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                "tetrisJava/salidaTestFactores/"+tipo+".txt"
        ))) {
            writer.write( resultadoHeuristicasLs.getFirst().titulo() );
            for(resultadoHeuristica i : resultadoHeuristicasLs){
                writer.newLine();
                writer.write(i.fila());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
