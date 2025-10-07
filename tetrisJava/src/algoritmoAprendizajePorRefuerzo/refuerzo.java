package algoritmoAprendizajePorRefuerzo;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import tetrissimulador.juego;
/*
 * 
 * Normalizar la de la matriz entera en toda la matriz
 */
public class refuerzo {
    /*
     * qsa es esactamente Q(s,a)
     */
    private HashMap<String,Float> qsa = new HashMap<>();
    private String estadoActual;
    private Integer posicion;
    private Integer giro;
    private List<String> acciones;
    private String textoDeEstado;
    private float aleatorio;
    private int aletarioInt;
    private Random random;
    private int anchoTablero,altoTablero;
    private float escalaValores;
    /*
     * La idea es primero se inicia el juego eliguiendo altura y anchura
     * se usa la funcion dibujar para enseñar por consola el estado del juego
     * iniciar desde cubo es para colocar el juego en un estado concreto
     * splitear es para recibir el estado y sacar en estadoActual y acciones el estado del juego y el conjunto de acciones disponibles
     */
    public void entrenar(){
        anchoTablero = 4;
        altoTablero =6;
        escalaValores = 100;
        float factorAprendizaje = 0.1f, factorRecuerdo = 0.05f;
        String[] aux;
        Integer proximaAccion,accionAnterior;
        float auxFloat,auxFloat2;
        Integer maximaAcciones;
        float valorMaximoAccion;
        Integer numeroAccionesPuntuadas;
        String estadoAnteriorAct = "" , estadoAnterior = "";
        List<String> listaAccionesAnteriores;
        Integer maximoAltura,minimaAltura,auxInt;
        juego j = new juego(altoTablero,anchoTablero);
        float puntuacion;
        List<String> lsNoVisitidos;


        for(int ii = 0 ; ii < 1; ii++){
            random = new Random(); //iniciamos numero aleatorio
            j = new juego(altoTablero,anchoTablero); //iniciamos juego
            
            for(int i = 0; i < 50 ; i++){ //la idea es transformar esto por el numero de juegos jugados
                textoDeEstado = j.iniciarDesdeEstadoCubo("0000");
                //es obligatorio usar esta funcion cada vez que empieza una nueva partida para que reinicie el contador de iteraciones
                j.configurarLimites(true, 50, 5, 4); 
                //Se hace al principio para arrancar
                splitearAccionesCubo(textoDeEstado);
                addNuevasAcciones();
                while(textoDeEstado != "final"){
                    /*
                     * Aqui dibujamos el estado y adquirmos el esado
                     */
                    j.dibujar();
                    
                    
                    

                    //======= Aqui elegimos si es explorar o es avanzar
                    //System.out.println(qsa);
                    valorMaximoAccion = -100;
                    numeroAccionesPuntuadas = 0;
                    maximaAcciones = 0;
                    lsNoVisitidos = new ArrayList<>();
                    
                    for(String k : acciones){
                        auxFloat = qsa.get(estadoActual+"-"+k);
                        if(auxFloat != 0){
                            numeroAccionesPuntuadas ++;
                            
                        }else{
                            lsNoVisitidos.add(k);
                        }
                        if (valorMaximoAccion < auxFloat) {
                            valorMaximoAccion = auxFloat;
                            maximaAcciones = Integer.valueOf(k);
                        }
                        
                    }
                    //======
                    auxFloat = 0.7f; //(float) ( - ( numeroAccionesPuntuadas/acciones.size() - 1)*0.9 + 0.1);
                    aletarioInt = random.nextInt( acciones.size() );
                    aleatorio = random.nextFloat();
                    
                    if (aleatorio < auxFloat) {
                        //if (numeroAccionesPuntuadas != 0) {
                        aux = acciones.get(aletarioInt).split("-");
                        proximaAccion = Integer.valueOf(aux[0]);
                        /*}else{
                            aletarioInt = random.nextInt( lsNoVisitidos.size() );
                            aux = lsNoVisitidos.get(aletarioInt).split("-");
                            proximaAccion = Integer.valueOf(aux[0]);
                        }*/
                        
                        System.out.println("Exploracion");
                    }else{
                        proximaAccion = maximaAcciones;
                        System.out.println("avance");
                    }
                    
                    estadoAnteriorAct = estadoActual+"-"+proximaAccion;
                    estadoAnterior = estadoActual;
                    accionAnterior = proximaAccion;
                    listaAccionesAnteriores = new ArrayList<>(acciones);
                    /*
                     * Aqui realizamos el moviemiento
                     */
                    textoDeEstado = j.realizarMovimiento(proximaAccion, 0);

                    if(textoDeEstado != "final"){
                        //====== Aqui puntuamos los estados
                        //En teoria ya no hace falta calcular el maximo ni el minimo
                        /*maximoAltura = -1;
                        minimaAltura = 100;
                        
                        for(int o = 0; o < estadoActual.length();o++){
                            auxInt = Character.getNumericValue((estadoActual.charAt(o)));
                            if (maximoAltura < auxInt) {
                                maximoAltura = auxInt;
                            }
                            if (minimaAltura > auxInt) {
                                minimaAltura = auxInt;
                            }

                        }*/
                        
                        puntuacion = 0.0f;
                        //aqui optenemos las sumas de las alturas
                        for(int o = 0; o < estadoActual.length();o++){
                            auxInt = Character.getNumericValue((estadoActual.charAt(o)));
                            puntuacion += auxInt;
                        }
                        
                        //puntuacion = (puntuacion/estadoActual.length()-minimaAltura);
                        puntuacion = ( altoTablero*anchoTablero *  altoTablero*anchoTablero * altoTablero*anchoTablero *  altoTablero*anchoTablero - puntuacion*puntuacion* puntuacion*puntuacion) ;//* escalaValores / (altoTablero*anchoTablero* altoTablero*anchoTablero * altoTablero*anchoTablero* altoTablero*anchoTablero );

                        /*
                        * Ahora sacamos las nuevas acciones y actualizamos el estado actual para que sea s^(t+1)
                        */
                        splitearAccionesCubo(textoDeEstado);
                        addNuevasAcciones();
                        /*
                        *  ya tenemos r^t que es puntuacion
                        * tenemos s^t que es estadoAnteriorAct
                        * tenemos s^(t+1) que es estadoActual
                        * a^t es accion anterior
                        * a' es acciones que es una lista
                        */

                        auxFloat = 0;

                        for(String act : acciones){
                            auxFloat2 = qsa.get( estadoActual+"-"+act );
                            if (auxFloat2 > auxFloat) {
                                auxFloat = auxFloat2;
                            }
                        }
                        //auxFloat2 = qsa.get(estadoAnteriorAct);
                        /*
                        * Qsa(s^t , a) <- Qsa(s^t , a) + y * ( r^t + b *( max_a Qsa(s^(t+1) , a' - Qsa(s^t , a )) ) )
                        */
                        auxFloat2 = qsa.get(estadoAnteriorAct);
                        System.out.println("nueva puntuacion de: "+estadoAnteriorAct+" era:"+auxFloat2+" puntuacion: "+puntuacion +" es ->"+
                            (qsa.get(estadoAnteriorAct) 
                                + factorAprendizaje 
                                * (puntuacion 
                                + factorRecuerdo*(auxFloat - qsa.get(estadoAnteriorAct))))
                        );
                        qsa.put(estadoAnteriorAct,
                            qsa.get(estadoAnteriorAct) 
                                + factorAprendizaje 
                                * (puntuacion 
                                + factorRecuerdo*(auxFloat - qsa.get(estadoAnteriorAct))
                                    )
                        );

                        /*puntuacion = 0.0f;
                        for(String act : listaAccionesAnteriores){
                            puntuacion += qsa.get(estadoAnterior+"-"+act);
                        }
                        for(String act : listaAccionesAnteriores){
                            qsa.put(estadoAnterior+"-"+act,  qsa.get(estadoAnterior+"-"+act) * escalaValores/puntuacion );
                        }*/



                        //===============
                    }else{
                        estadoAnteriorAct = "";
                        estadoAnterior = "";
                        accionAnterior = -1;
                        listaAccionesAnteriores = new ArrayList<>();
                    }
                    
                }
                
            }
            try (FileWriter writer = new FileWriter("tetrisJava/ficherosEntrenados/episiodio"+ii+".txt")) {
                for (String o : qsa.keySet()) {
                    if(o != ""){
                        writer.write(o+"="+qsa.get(o)+"\n");
                    }
                    
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        j.dibujar();
        

        System.out.println("=============================== Ahora vamos a intentar jugar ===============================");
        
        
        textoDeEstado = j.iniciarDesdeEstadoCubo("0000");
                //es obligatorio usar esta funcion cada vez que empieza una nueva partida para que reinicie el contador de iteraciones
                j.configurarLimites(true, 50, 5, 4); 
                //Se hace al principio para arrancar
                splitearAccionesCubo(textoDeEstado);
                addNuevasAcciones();
                while(textoDeEstado != "final"){
                    j.dibujar();

                    valorMaximoAccion = -100;
                    numeroAccionesPuntuadas = 0;
                    maximaAcciones = 0;
                    lsNoVisitidos = new ArrayList<>();
                    
                    for(String k : acciones){
                        auxFloat = qsa.get(estadoActual+"-"+k);
                        if(auxFloat != 0){
                            numeroAccionesPuntuadas ++;
                            
                        }else{
                            lsNoVisitidos.add(k);
                        }
                        if (valorMaximoAccion < auxFloat) {
                            valorMaximoAccion = auxFloat;
                            maximaAcciones = Integer.valueOf(k);
                        }
                        
                    }


                    proximaAccion = maximaAcciones;
                    System.out.println("avance");

                    textoDeEstado = j.realizarMovimiento(proximaAccion, 0);
                    if(textoDeEstado != "final") splitearAccionesCubo(textoDeEstado);
                    //addNuevasAcciones();



                }
                System.out.println("============= fin del juego =============");
        
        //System.out.println(textoDeEstado);
        //splitearAccionesCubo(textoDeEstado);
        //System.out.println("estaodo -> "+estadoActual);
        //System.out.println("acciones -> "+acciones);
        
        //System.out.println(qsa);
        //System.out.println("==================");
        //String texto = j.realizarMovimiento(2, 0);
        //System.out.println(texto);
    }

    private void addNuevasAcciones(){
        for (String i : acciones) {
            if (!qsa.containsKey(estadoActual+"-"+i)) {
                qsa.put(estadoActual+"-"+i, 0.0f);
            }
        }
    }

    private void splitearAccionesCubo(String t){
        String estadoObjeto;
        String accioneString;
        String[] aux;
        aux = t.split("a");
        estadoObjeto = aux[0];
        accioneString = aux[1];
        aux = estadoObjeto.split("-");
        estadoActual = aux[0];
        acciones = new ArrayList<>();
        
        for(String i : accioneString.split("-")){
            aux = i.split("/");
            if(aux[1].equals("0")){
                acciones.add(aux[0]);
            }
        }
    }
}
