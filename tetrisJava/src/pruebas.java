
import algoritmoAprendizajePorRefuerzo.refuerzo;
import tetrissimulador.Estado;
import tetrissimulador.juego;

import java.util.List;
import java.util.Random;


public class pruebas {
    public static void main(String[] args) {
        Random azar = new Random();
        String estadoMensaje;
        Estado estado;
        Integer randonInt;
        //juego j = new juego(5,4);
        //j.iniciar();

        refuerzo i = new refuerzo();
        i.entrenar( false  , 6 , 20 , 50, 1000);

        //i.leerResultados( 4 , 6 , 1 );


        /*
        juego j = new juego( 20 , 5 );
        j.configurarLimites(true, 1000, 10, 8);


        /*
            Aqui esta simulando el entrenamiento

        List<String> nombrePiezasAElegir = j.getNombrePieza();

        nombrePiezasAElegir = nombrePiezasAElegir.stream()
                                .filter(c -> !c.equals("punto"))
                                .toList();

        estadoMensaje = j.iniciarDesdePieza(
                "00000",
                        //"punto"
                        nombrePiezasAElegir.get( azar.nextInt( nombrePiezasAElegir.size() ))
                );

        if( estadoMensaje.equals("Correcto") ){
            System.out.println(estadoMensaje);
            estado = j.devolverEstadoClase();
            System.out.println(estado.toString());

            for(int jugadas = 0 ; jugadas < 100 ;jugadas++){
                System.out.println("== accion elegido al azar ==");
                randonInt = azar.nextInt(  estado.accionPosicion().size() );
                System.out.println(estado.accionPosicion().get(randonInt) + " , " + estado.accionGiro().get(randonInt));
                estadoMensaje = j.realizarMovimientoDevClase(
                        estado.accionPosicion().get(randonInt),
                        estado.accionGiro().get(randonInt));

                if(estadoMensaje.equals("Correcto")){
                    j.setPiezas(
                            //"punto"
                            nombrePiezasAElegir.get( azar.nextInt( nombrePiezasAElegir.size() ))
                    );
                    estado = j.devolverEstadoClase();
                    System.out.println( estado.toString() );

                    j.dibujar();
                    //System.out.println(estado.toString());
                }else{
                    System.out.println("No se puede jugar mas por -> "+estadoMensaje);
                    jugadas = 110;
                }
            }


        }else{
            System.out.println(estadoMensaje);
        }

        */

    }
}