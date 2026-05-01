
import algoritmoAprendizajePorRefuerzo.ResultadosRefuerzo;
import algoritmoAprendizajePorRefuerzo.SalidaRefuerzo;
import algoritmoAprendizajePorRefuerzo.refuerzo;
import calculoHeuristicaTetris.resultadoHeuristica;
import tetrissimulador.Estado;
import tetrissimulador.juego;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class pruebas {
    public static void main(String[] args) {
        // la mejor es 0,85|0,4666667

        SalidaRefuerzo res;
        int apreMax = 20;
        int recuerMax = 3;
        Integer altura = 10;
        Integer anchura = 6;
        List<SalidaRefuerzo> listaRes = new ArrayList<>();
        List<String> tipoDeDecision = new ArrayList<>( List.of(
                "azar",
                "primeroZeros",
                "EGreede",
                "modificarCte",
                "porVistas") );
//int k = 0 ; k < tipoDeDecision.size();k++
        for(int piezaElegida = 0; piezaElegida < 7 ; piezaElegida++){
            System.out.println("======================= Pieza"+piezaElegida +" ======================= ");
            refuerzo i = new refuerzo();
            res = i.entrenar( true  , anchura , altura , 50,
                    4000 , piezaElegida,
                    0.85f , 0.4666667f,
                    true , tipoDeDecision.get(3));

            String tipo = "Refuerzo"+altura+"_"+anchura+"_"+piezaElegida;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                    "tetrisJava/salidaTestFactores/"+tipo+".txt"
            ))) {
                writer.write( i.resultadosRefuerzoList.getFirst().titulo() );
                for(ResultadosRefuerzo j : i.resultadosRefuerzoList){
                    writer.newLine();
                    writer.write(j.fila());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /*for(int apren = 1 ; apren < apreMax ; apren++){
            for(int recurd = 1; recurd < recuerMax ; recurd++){
                float aprender = (float)apren/ apreMax;
                float recuerdo = (float) recurd/recuerMax * 0.8f + 0.2f;
                System.out.println("Empezamos -> FactorAprendizaje: "+aprender+" FactorRecuerdo: "+recuerdo);



                listaRes.add(res);


            }
        }
        escribirTodo(listaRes,"Datos2-2");*/





        /*int cont = 0;
        juego game = new juego(10,5);
        game.configurarLimites(false);
        game.iniciarDesdePieza("30020","palo");
        game.dibujar();
        Estado estado = game.devolverEstadoClase();
        estado.imprimirCambios();
        game.realizarMovimientoDevClase(0 , 0 , 0);
        game.dibujar();
        estado = game.devolverEstadoClase();
        estado.imprimirCambios();
        game.realizarMovimientoDevClase(2 , 1 , 0);
        game.dibujar();
        estado = game.devolverEstadoClase();
        estado.imprimirCambios();*/

    }

    public static void escribirTodo(List<SalidaRefuerzo> entrada,String nombre){
        String texto = "Contante";
        for( SalidaRefuerzo sal :  entrada){
            texto += ";"+sal.nombreDato();
        }

        for(int i = 0; i < entrada.getFirst().listaIteraciones.size() ; i++){
            texto +="\n"+i;
            for(SalidaRefuerzo sal : entrada){
                texto += ";"+sal.listaIteraciones.get(i).toString().replace(".",",");
            }
        }
        String dire = "tetrisJava/salidaTestFactores/" +nombre;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dire))) {
            writer.write(texto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}