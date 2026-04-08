
import algoritmoAprendizajePorRefuerzo.SalidaRefuerzo;
import algoritmoAprendizajePorRefuerzo.refuerzo;
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
        refuerzo i = new refuerzo();
        SalidaRefuerzo res;
        int apreMax = 20;
        int recuerMax = 3;
        List<SalidaRefuerzo> listaRes = new ArrayList<>();
        for(int apren = 1 ; apren < apreMax ; apren++){
            for(int recurd = 1; recurd < recuerMax ; recurd++){
                float aprender = (float)apren/ apreMax;
                float recuerdo = (float) recurd/recuerMax * 0.8f + 0.2f;
                System.out.println("Empezamos -> FactorAprendizaje: "+aprender+" FactorRecuerdo: "+recuerdo);

                res = i.entrenar( true  , 6 , 10 , 50,
                        100 , -1,
                        aprender , recuerdo,
                        false);

                listaRes.add(res);


            }
        }
        escribirTodo(listaRes,"Datos2-2");





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