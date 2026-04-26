package algoritmoAprendizajePorRefuerzo;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import tetrissimulador.Estado;
import tetrissimulador.juego;
/*
 * 
 * Normalizar la de la matriz entera en toda la matriz
 */
public class refuerzo {
    /*
     * qsa es esactamente Q(s,a)
     */
    private HashMap<String,Float> qsa = new HashMap<>();    // La lista de puntos
    private float aleatorio;                                //Una variable aleatoria
    private int aletarioInt;                                //Una variable aleatoria entera
    private Random random;                                  //La funcion rando
    private int anchoTablero,altoTablero;                   //Anchura y altura de tablero
    private String estadoSalida = "";

    List<Integer> movimientoLs;
    List<Integer> girosLs;
    List<Integer> numeroPiezas;
    List<Double> tiempos;

    public List<ResultadosRefuerzo> resultadosRefuerzoList;

    /*
     * La idea es primero se inicia el juego eliguiendo altura y anchura
     * se usa la funcion dibujar para enseñar por consola el estado del juego
     * iniciar desde cubo es para colocar el juego en un estado concreto
     * splitear es para recibir el estado y sacar en estadoActual y acciones el estado del juego y el conjunto de acciones disponibles
     */

    /*
    Cosas por realizar
    -> splitearAccionesCubo(String t)
    -> entrenar hay que reacerlo
    -> crear una funcion para entrenar independiente de la entrada
     */
    public SalidaRefuerzo entrenar(boolean reentrenar , int anchura , int altura  , int etapas , int numeroJuegos, Integer piezaEleguida,
                                   float factorAprendizaje, float factorRecuerdo,
                                   boolean juegarAlFinal){
        anchoTablero = anchura;
        altoTablero = altura;
        for(int i = 0; i < anchoTablero;i++) estadoSalida += "0";
        resultadosRefuerzoList = new ArrayList<>();
        ResultadosRefuerzo resultadoAux;

        int numeroEntrenamiento = 0;



        Integer proximaAccionX ,proximaAccionGiro;
        float auxFloat,auxFloat2;
        int maximaAcciones;
        float valorMaximoAccion;
        Estado estadoAnterior , estadoSiguiente = null;
        //List<String> listaAcciones;
        juego j = new juego(altoTablero,anchoTablero);
        float puntuacion;
        List<Integer> lsNoVisitidos;
        String accionElegida;
        String estadoMensaje;
        parEstado salidasJuegos = null;
        resultadoMovimiento resultadoMovimiento = null;
        Integer iteracionesMedios = 0;

        SalidaRefuerzo res = new SalidaRefuerzo(factorAprendizaje,factorRecuerdo,numeroJuegos);

        if( !reentrenar ) leerResultados(anchoTablero , altoTablero , 7);

        for(int ii = 0 ; ii < etapas; ii++){
            random = new Random(); //iniciamos numero aleatorio
            j = new juego(altoTablero,anchoTablero); //iniciamos juego
            iteracionesMedios = 0;
            movimientoLs = new ArrayList<>();
            girosLs = new ArrayList<>();
            numeroPiezas = new ArrayList<>();
            tiempos = new ArrayList<>();
            for( int juegosTest = 0 ; juegosTest < 10 ; juegosTest++ ){
                                iteracionesMedios += jugarUna( j , false , 50, altoTablero - 2, altoTablero - 2 , estadoSalida , piezaEleguida);
            }
            res.addListaIteracion((float) iteracionesMedios / 10);
            System.out.println( "E/J -> "+ii+" sobre "+etapas +" iteraciones -> "+(float) iteracionesMedios / 10);
            //Aqui vamos a meter los resultados
            resultadoAux = new ResultadosRefuerzo();
            if(piezaEleguida < 0) resultadoAux.tipoDePartida = "piezasAzar";
            else resultadoAux.tipoDePartida = j.getNombrePieza().get( piezaEleguida );
            resultadoAux.numeroJuegosEntrenamiento = numeroEntrenamiento;
            resultadoAux.numeroIteraciones = (float) iteracionesMedios / 10;
            resultadoAux.numeroPiezas = this.numeroPiezas;
            resultadoAux.girosLs = this.girosLs;
            resultadoAux.movimientoLs = this.movimientoLs;
            resultadoAux.tiempos = this.tiempos;
            resultadosRefuerzoList.add(resultadoAux);



            if( iteracionesMedios/10 == 50.0 ){
                break;
            }


            // ========== eleguimos realizar 50 partidas =======================
            for(int i = 0; i < numeroJuegos ; i++){
                numeroEntrenamiento++;
                salidasJuegos = inicarJuego( j  , piezaEleguida);
                estadoMensaje = salidasJuegos.mensaje();
                estadoSiguiente = salidasJuegos.estado();

                if(estadoMensaje.equals("Correcto")){

                    while(estadoMensaje.equals("Correcto")){ //Tranquilo esta condicion se cambia luego
                        //iteracionesMedios += 1;
                        /*
                         * Aqui dibujamos el estado y adquirmos el esado
                         */
                        //j.dibujar(); //aqui

                        resultadoMovimiento = siguienteEstado( j , estadoSiguiente , piezaEleguida);
                        estadoMensaje = resultadoMovimiento.mensaje();
                        accionElegida = resultadoMovimiento.movimientoElegido();
                        estadoAnterior = resultadoMovimiento.estadoAnterior();
                        estadoSiguiente = resultadoMovimiento.estadoActual();
                        if(estadoMensaje.equals("Correcto")){
                            //Vamos a empezar a calcular el refuerzo o puntucion. Este sera segun las alturas
                            puntuacion = calcularPuntucaion(estadoSiguiente);

                            addNuevoEventos(estadoSiguiente);
                            /*
                             * ya tenemos r^t que es puntuacion
                             * tenemos s^t que es estadoAnteriorAct
                             * tenemos s^(t+1) que es estadoActual
                             * a^t es accion anterior
                             * a' es acciones que es una lista
                             */
                            auxFloat = 0;
                            Estado estadoAux;
                            //listaAcciones = new ArrayList<>();
                            String act;
                            /* creo que hay que porbar todas las variaciones de pieza*/
                            for( int pi = 0 ; pi < j.getNombrePieza().size() ; pi++ ){
                                estadoAux = estadoSiguiente.copia(pi);

                                auxFloat = 0;
                                for( int accionFor = 0 ; accionFor < estadoAux.accionPosicion().size() ; accionFor ++ ){
                                    act = crearEtiquetaEstado(estadoAux , accionFor);
                                    auxFloat2 = qsa.getOrDefault(act, 0.0f);
                                    if (auxFloat2 > auxFloat) {
                                        auxFloat = auxFloat2;
                                    }
                                }
                            }
                           qsa.put(accionElegida,
                                    qsa.get(accionElegida)
                                            + factorAprendizaje
                                            * (puntuacion
                                            + factorRecuerdo*(auxFloat - qsa.get(accionElegida))
                                    )
                           );

                        }

                    }

                }else{
                    System.out.println("Error -> "+estadoMensaje);
                }
            }

            guardarResultados( anchoTablero , altoTablero , j.getNombrePieza().size() );

        }
        
        //j.dibujar();   aqui
        
        if(juegarAlFinal){

            System.out.println("=============================== Ahora vamos a intentar jugar ===============================");

            int numeroDePaso = 0;

            if(piezaEleguida >= 0){
                estadoMensaje = j.iniciarDesdePieza(estadoSalida,
                        j.getNombrePieza().get(piezaEleguida));
            }
            else{
                estadoMensaje = j.iniciarDesdePieza(estadoSalida,
                        j.getNombrePieza().get(random.nextInt(j.getNombrePieza().size())));
            }


            //es obligatorio usar esta funcion cada vez que empieza una nueva partida para que reinicie el contador de iteraciones
            j.configurarLimites(true, 50, altoTablero - 2, altoTablero -2);
            //Se hace al principio para arrancar

            //splitearAccionesCubo(textoDeEstado);
            //addNuevasAcciones();

            while(estadoMensaje.equals("Correcto")) {

                estadoAnterior = j.devolverEstadoClase();
                j.dibujar();

                valorMaximoAccion = -100;
                maximaAcciones = 0;
                lsNoVisitidos = new ArrayList<>();

                for (int k = 0; k < estadoAnterior.accionPosicion().size(); k++) {
                    auxFloat = qsa.getOrDefault(crearEtiquetaEstado(estadoAnterior, k), 0.0f);


                    lsNoVisitidos.add(k);
                    if (valorMaximoAccion < auxFloat) {
                        valorMaximoAccion = auxFloat;
                        maximaAcciones = k;
                    }

                }

                proximaAccionX = estadoAnterior.accionPosicion().get(maximaAcciones);
                proximaAccionGiro = estadoAnterior.accionGiro().get(maximaAcciones);

                System.out.println("avance");
                numeroDePaso += 1;

                estadoMensaje = j.realizarMovimientoDevClase(proximaAccionX, proximaAccionGiro, piezaEleguida);

                //addNuevasAcciones();
            }



            System.out.println("============= fin del juego =============");
            System.out.println(estadoMensaje +" numero de iteraciones -> "+numeroDePaso);
        }
        return res;
    }

    private Integer jugarUna(juego j , boolean dibujar , int iterMax , int altMax , int medAltMax , String estado , Integer piezaEleguida){
        int numeroDePaso = 0;
        String estadoMensaje;
        Estado estadoAnterior;

        if(piezaEleguida >= 0){
            estadoMensaje = j.iniciarDesdePieza(estadoSalida,
                    j.getNombrePieza().get(piezaEleguida));
        }else{
            estadoMensaje = j.iniciarDesdePieza(estadoSalida,
                    j.getNombrePieza().get(random.nextInt(j.getNombrePieza().size())));
        }
        j.configurarLimites(true, iterMax,altoTablero - 2, altoTablero - 2);
        long inicio,fin,duracion;
        while(estadoMensaje.equals("Correcto")){
            estadoAnterior = j.devolverEstadoClase();
            if(dibujar) {
                j.dibujar();
            }
            float valorMaximoAccion = -100;
            int maximaAcciones = 0;
            float auxFloat;
            //List<Integer> lsNoVisitidos = new ArrayList<>();
            inicio = System.nanoTime(); // 🔵 línea A
            for(int k = 0 ; k  < estadoAnterior.accionPosicion().size() ; k++){
                auxFloat = qsa.getOrDefault(crearEtiquetaEstado(estadoAnterior, k), 0.0f);


                //lsNoVisitidos.add(k);
                if (valorMaximoAccion < auxFloat) {
                    valorMaximoAccion = auxFloat;
                    maximaAcciones = k;
                }

            }


            //proximaAccionX = estadoAnterior.accionPosicion().get( maximaAcciones );
            //proximaAccionGiro = estadoAnterior.accionGiro().get( maximaAcciones );

            numeroDePaso += 1;

            estadoMensaje = j.realizarMovimientoDevClase(
                    //proximaAccionX
                    estadoAnterior.accionPosicion().get( maximaAcciones ),
                    //proximaAccionGiro
                    estadoAnterior.accionGiro().get( maximaAcciones ), piezaEleguida  );
            fin = System.nanoTime(); // 🔴
            duracion = fin - inicio;

            this.tiempos.add(duracion / 1_000_000.0);
            this.movimientoLs.add( estadoAnterior.accionPosicion().get( maximaAcciones ) );
            this.girosLs.add(estadoAnterior.accionGiro().get( maximaAcciones ));
            this.numeroPiezas.add(estadoAnterior.pieza());


        }

        return numeroDePaso;
    }

    private parEstado inicarJuego(juego j , Integer piezaEleguida){

        String res = "";
        String estadoMensaje;
        Estado estado = null;


        if(piezaEleguida >= 0){
            estadoMensaje = j.iniciarDesdePieza(estadoSalida,
                    j.getNombrePieza().get(piezaEleguida));
        }else{
            estadoMensaje = j.iniciarDesdePieza(estadoSalida,
                    j.getNombrePieza().get(random.nextInt(j.getNombrePieza().size())));
        }
        if(estadoMensaje.equals("Correcto")){
            //es obligatorio usar esta funcion cada vez que empieza una nueva partida para que reinicie el contador de iteraciones
            j.configurarLimites(true, 50, altoTablero - 2, altoTablero - 2);
            //Se hace al principio para arrancar
            //splitearAccionesCubo(textoDeEstado);
            estado = j.devolverEstadoClase();
            addNuevoEventos(estado); //Es obligatorio despues de realizar un juego
            res = estadoMensaje;
        }else{
            res = "Problema al iniciar";
        }
        return new parEstado(res,estado);

    }

    private resultadoMovimiento siguienteEstado( juego j, Estado estado , Integer piezaElegida){
        int proximaAccionX , proximaAccionGiro;
        float valorMaximoAccion = -100;
        int maximaAcciones = 0;
        //List<Integer> lsNoVisitidos = new ArrayList<>();
        float auxFloat;
        Estado estadoAnterior = null;
        Estado estadoActual = null;
        String accionElegida;
        String resultado;

        for( int k = 0 ; k < estado.accionPosicion().size();k++ ){
            auxFloat = qsa.get( crearEtiquetaEstado( estado , k ) );

            if (valorMaximoAccion < auxFloat) {
                valorMaximoAccion = auxFloat;
                maximaAcciones = k;
            }
        }

        //======
        auxFloat = 2.0f; //(float) ( - ( numeroAccionesPuntuadas/acciones.size() - 1)*0.9 + 0.1);

        aletarioInt = random.nextInt( estado.accionPosicion().size() );
        aleatorio = random.nextFloat();

        //Continuar programando Linea 107

        if (aleatorio < auxFloat) {
            proximaAccionX = estado.accionPosicion().get(aletarioInt);
            proximaAccionGiro = estado.accionGiro().get(aletarioInt);


            //System.out.println("Exploracion");
        }else{
            proximaAccionX = estado.accionPosicion().get(maximaAcciones);
            proximaAccionGiro = estado.accionGiro().get(maximaAcciones);
            //System.out.println("avance");
        }

        estadoAnterior = estado;
        accionElegida = estadoAnterior.pieza()+"/"+ //este si se queda porque depende de proximo
                estadoAnterior.alturas()+"A"+
                proximaAccionX+"-"+proximaAccionGiro;


        /*
         * Aqui realizamos el moviemiento
         */
        //System.out.println( estado.accionPosicion() );
        //System.out.println( estado.accionGiro());
        resultado = j.realizarMovimientoDevClase(proximaAccionX, proximaAccionGiro , piezaElegida);

        if( resultado.equals("Correcto") ){
            estadoActual = j.devolverEstadoClase();
        }

        return new resultadoMovimiento( resultado , estadoAnterior , estadoActual , accionElegida);


    }

    private List<String> addNuevoEventos(Estado entrada){
        /*
        Si nunca se ha explorado una accion se añade
        Tambien devolvemos una lista de los estados + siguinetes acciones para optimizar el bucle for
         */
        List<String> res = new ArrayList<>();
        for(int i = 0 ; i < entrada.accionPosicion().size();i++){
            res.add( crearEtiquetaEstado(entrada , i) );
            if(!qsa.containsKey( crearEtiquetaEstado(entrada , i) )
            ){
                qsa.put( crearEtiquetaEstado(entrada,i) ,
                        0.0f);
            }
        }
        return res;
    }

    private String crearEtiquetaEstado(Estado estado , int n){
        return  estado.pieza()+"/"+estado.alturas()+"A"+estado.accionPosicion().get(n)+"-"+estado.accionGiro().get(n);
    }

    private float calcularPuntucaion( Estado estadoSiguiente ){
        float puntuacion = 0.0f;

        for(int k = 0 ; k < estadoSiguiente.alturas().size(); k++){
            puntuacion += estadoSiguiente.alturas().get(k);
        }

        //puntuacion = (puntuacion/estadoActual.length()-minimaAltura);
        //Pareze que eleve al 4 la diferencia
        puntuacion = ( altoTablero*anchoTablero
                - puntuacion - estadoSiguiente.huecos()*3 )
                / (altoTablero*anchoTablero );

        //Ahora hay que realizar una cosa muy bonita
        // Si se genera un hueco nuevo se le castiga con ( - numero de huecos )
        // Si se borra una linea se le premia con        ( + el numero de fila quitadas )
        puntuacion += estadoSiguiente.filasQuitadas() - estadoSiguiente.huecosNuevos()*estadoSiguiente.alturas().size();

        return puntuacion;
    }

    private void guardarResultados(int anchura , int altura , int numeroPiezas){
        String[] piezaSet;
        for( int i = 0 ; i < numeroPiezas ; i++){
            try{
                BufferedWriter bw = new BufferedWriter(
                        new FileWriter( "tetrisJava/ficherosEntrenados/"+i+"_"+anchura+"_"+altura+".txt" )
                );
                for( String j : qsa.keySet() ){
                    piezaSet = j.split("/");
                    if( Integer.parseInt(piezaSet[0]) == i ){
                        bw.write( j+"="+qsa.get(j) );
                        bw.newLine();
                    }
                }

                bw.close(); // Cierra el archivo
                //System.out.println("Archivo escrito correctamente.");

            }catch (IOException e) {
                System.out.println("Error escribir el fichero");
            }
        }
    }

    public void leerResultados(int anchura , int altura , int numeroPiezas){
        String linea;
        String[] separacion;
        File fichero;
        for(int i = 0; i <numeroPiezas ; i++){
            fichero = new File("tetrisJava/ficherosEntrenados/"+i+"_"+anchura+"_"+altura+".txt");
            if(fichero.exists()){
                System.out.println("leer -> "+ "tetrisJava/ficherosEntrenados/"+i+"_"+anchura+"_"+altura+".txt");
                try(BufferedReader reader = new BufferedReader(new FileReader("tetrisJava/ficherosEntrenados/"+i+"_"+anchura+"_"+altura+".txt"))){
                    while ( (linea = reader.readLine()) != null ){
                        separacion = linea.split("=");
                        qsa.put( separacion[0] , Float.parseFloat(separacion[1]) );
                        //System.out.println(linea);
                    }
                }catch (IOException e){
                    System.out.println("Error leer fichero");
                }
            }

        }
        //System.out.println(qsa.toString());
    }

    private record parEstado(String mensaje , Estado estado){}

    private record resultadoMovimiento(String mensaje , Estado estadoAnterior , Estado estadoActual, String movimientoElegido){}

}


