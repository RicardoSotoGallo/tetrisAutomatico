package algoritmoAprendizajePorRefuerzo;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import tetrissimulador.Estado;
import tetrissimulador.juego;

import static java.util.stream.Collectors.toList;

/*
 * 
 * Normalizar la de la matriz entera en toda la matriz
 */
public class refuerzo {
    /*
     * qsa es esactamente Q(s,a)
     */
    private HashMap<String,Integer> qsap = new HashMap<>();
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
    String tipoElecciones;

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
                                   boolean juegarAlFinal, String tipoDeElecciones){
        /*
        tipoDeElecciones:
        azar : las elecciones se realiza al azar
        primeroZeros: Se usa primero las que son zero
        EGreede : e-greede con contante de 20%
        modificarCte: e-greede pero modificamos constante segun si la media 1/2 tamaño o 3*tamaño
        porVistas: Ahora lo miramos por una media entre puntuacion y veces que se ve
         */
        this.tipoElecciones = tipoDeElecciones;
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
        //Integer iteracionesMedios = 0;

        SalidaRefuerzo res = new SalidaRefuerzo(factorAprendizaje,factorRecuerdo,numeroJuegos);

        if( !reentrenar ) leerResultados(anchoTablero , altoTablero , 7);

        for(int ii = 0 ; ii < etapas; ii++){
            random = new Random(); //iniciamos numero aleatorio
            j = new juego(altoTablero,anchoTablero); //iniciamos juego
            float mediasIteraciones;
            movimientoLs = new ArrayList<>();
            girosLs = new ArrayList<>();
            numeroPiezas = new ArrayList<>();
            tiempos = new ArrayList<>();
            List<Integer> lsIteraciones = new ArrayList<>();
            Integer iterAux;
            //===================
            if(piezaEleguida < 0){
                for( int juegosTest = 0 ; juegosTest < 50 ; juegosTest++ ){
                    iterAux = jugarUna( j , false , 50, altoTablero - 2, altoTablero - 2 , estadoSalida , piezaEleguida);
                    lsIteraciones.add(iterAux);
                    //iteracionesMedios += jugarUna( j , false , 50, altoTablero - 2, altoTablero - 2 , estadoSalida , piezaEleguida);
                }
                mediasIteraciones = (float) lsIteraciones.stream()
                        .sorted()
                        .skip(5)
                        .limit(lsIteraciones.size()-10)
                        .mapToInt(Integer::intValue)
                        .average().orElse(0.0);
            }else{
                mediasIteraciones = (float)jugarUna( j , false , 50, altoTablero - 2, altoTablero - 2 , estadoSalida , piezaEleguida);
                //lsIteraciones.add(iterAux);
            }


            //=============================
            res.addListaIteracion( mediasIteraciones);
            System.out.println( "E/J -> "+ii+" sobre "+etapas +" iteraciones -> "+(float) mediasIteraciones);
            //Aqui vamos a meter los resultados
            resultadoAux = new ResultadosRefuerzo();
            if(piezaEleguida < 0) resultadoAux.tipoDePartida = "piezasAzar";
            else resultadoAux.tipoDePartida = j.getNombrePieza().get( piezaEleguida );
            resultadoAux.numeroJuegosEntrenamiento = numeroEntrenamiento;
            resultadoAux.numeroIteraciones = mediasIteraciones;
            resultadoAux.numeroPiezas = this.numeroPiezas;
            resultadoAux.girosLs = this.girosLs;
            resultadoAux.movimientoLs = this.movimientoLs;
            resultadoAux.tiempos = this.tiempos;
            resultadosRefuerzoList.add(resultadoAux);



            if( mediasIteraciones >= 50.0 ){
                break;
            }
            int tiempoDePartida = 0;
            // ========== eleguimos realizar 50 partidas =======================
            for(int i = 0; i < numeroJuegos ; i++){
                numeroEntrenamiento++;
                salidasJuegos = inicarJuego( j  , piezaEleguida);
                estadoMensaje = salidasJuegos.mensaje();
                estadoSiguiente = salidasJuegos.estado();
                tiempoDePartida = 0;

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

                        if(estadoMensaje.equals("Correcto")){
                            tiempoDePartida += 1;
                            estadoSiguiente = resultadoMovimiento.estadoActual();
                            //Vamos a empezar a calcular el refuerzo o puntucion. Este sera segun las alturas
                            puntuacion = calcularPuntucaion(estadoSiguiente,tiempoDePartida); //Esto es una prueba

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
                                    auxFloat2 = qsa.getOrDefault(act, 0.0f); //esto es para la siguiente En plan maxa(Q[s',a])
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
                        else {
                            //System.out.println(estadoSiguiente);
                            puntuacion = calcularPuntucaion(estadoSiguiente,tiempoDePartida);
                            addNuevoEventos(estadoSiguiente);
                            qsa.put(accionElegida,
                                    qsa.get(accionElegida)
                                            + factorAprendizaje
                                            * (puntuacion-(anchoTablero*altoTablero)
                                            + factorRecuerdo*( -( anchoTablero*altoTablero) - qsa.get(accionElegida))
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
                    auxFloat = qsa.getOrDefault(crearEtiquetaEstado(estadoAnterior, k), 0.0f); //Esto es de jugar una


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
                auxFloat = qsa.getOrDefault(crearEtiquetaEstado(estadoAnterior, k), 0.0f); //Tambien es de jugar Una


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


        Estado estadoAnterior = null;
        Estado estadoActual = null;
        AccionEscogida accionElegida;
        String resultado;
        //Eleguimos acciones ================
        if(this.tipoElecciones.equals("primeroZeros")){
            accionElegida = eleguirAleatorioPrimeroZeros(estado);
        } else if (this.tipoElecciones.equals( "EGreede" )) {
            accionElegida = eleguirEGreedyPrimeroZeros(estado);
        } else if (this.tipoElecciones.equals("modificarCte")) {
            accionElegida = EGreedyModificarCte(estado);
        }else if(this.tipoElecciones.equals("porVistas")){
            accionElegida = elegcionPorVisitas(estado);

        } else{
            accionElegida = eleguirAleatorio(estado);
        }

        String textoAccionElegida = crearEtiquetaEstado(accionElegida);

        //Aqui sumamos las acciones
        qsap.put(textoAccionElegida, qsap.get(textoAccionElegida)+1);

        //Realizamos Accion ================
        resultado = j.realizarMovimientoDevClase(accionElegida.posicion() , accionElegida.giro() , piezaElegida);

        if( resultado.equals("Correcto") ){
            estadoActual = j.devolverEstadoClase();
        }

        return new resultadoMovimiento( resultado , estadoAnterior , estadoActual , textoAccionElegida);


    }
    private AccionEscogida eleguirAleatorio(Estado estado){
        int aletarioInt,proximaAccionX,proximaAccionGiro;
        String accionEleguida;
        aletarioInt = random.nextInt( estado.accionPosicion().size() );
        accionEleguida = crearEtiquetaEstado(estado,aletarioInt);
        return new AccionEscogida( estado.pieza() , estado.alturas() , estado.accionPosicion().get(aletarioInt),estado.accionGiro().get(aletarioInt) );


    }
    private AccionEscogida eleguirAleatorioPrimeroZeros(Estado estado){
        int aletarioInt,accionPosicionEscogida;
        String accionEleguida;
        //Aqui creamos la lista a eleguir
        List<Integer> accionesAMirar = new ArrayList<>();   //Aqui van a estar las posiciones de los estados a eleguir
                                                            //Si al final no hay zeros esto sera una lista de 0 a estado.accionPosicion().size()
        List<Integer> accionesAux = new ArrayList<>();      //Este va a ser una lista de todas las acciones [0, sice]

        for(int i = 0 ; i < estado.accionPosicion().size() ; i++){
            accionesAux.add(i);
            if( qsap.get(crearEtiquetaEstado(estado,i)) == 0 ){
                accionesAMirar.add(i);
            }
        }
        if(accionesAMirar.isEmpty()){
            accionesAMirar = accionesAux;
        }



        //El objetivo es siempre preguntar y modificar aletarioInt y accionPosicionEscogida
        aletarioInt = random.nextInt( accionesAMirar.size() );
        accionPosicionEscogida = accionesAMirar.get(aletarioInt);
        //accionEleguida = crearEtiquetaEstado(estado,accionesAMirar.get(aletarioInt));
        return new AccionEscogida( estado.pieza() , estado.alturas() ,
                estado.accionPosicion().get(
                        accionPosicionEscogida
                ),
                estado.accionGiro().get(
                        accionPosicionEscogida
                ) );


    }

    private AccionEscogida eleguirEGreedyPrimeroZeros(Estado estado){
        float probabilidadExplotar = 0.2f;
        float azarFloat;
        int aletarioInt,accionPosicionEscogida;
        String accionEleguida;
        boolean hayZeros = true;
        //Aqui creamos la lista a eleguir
        List<Integer> accionesAMirar = new ArrayList<>();   //Aqui van a estar las posiciones de los estados a eleguir
        //Si al final no hay zeros esto sera una lista de 0 a estado.accionPosicion().size()
        List<Integer> accionesAux = new ArrayList<>();      //Este va a ser una lista de todas las acciones [0, sice]

        for(int i = 0 ; i < estado.accionPosicion().size() ; i++){
            accionesAux.add(i);
            if( qsap.get(crearEtiquetaEstado(estado,i)) == 0 ){
                accionesAMirar.add(i);
            }
        }
        if(accionesAMirar.isEmpty()){
            accionesAMirar = accionesAux;
            hayZeros = false;
        }
        //accionesAMirar = accionesAux;


        //El objetivo es siempre preguntar y modificar aletarioInt y accionPosicionEscogida
        if(hayZeros){
            aletarioInt = random.nextInt( accionesAMirar.size() );
            accionPosicionEscogida = accionesAMirar.get(aletarioInt);
        }else{
            azarFloat = random.nextFloat();
            if(azarFloat <= probabilidadExplotar){ //El mejor
                accionPosicionEscogida = accionesAMirar.stream()
                        .max( Comparator.comparing(c ->
                                qsa.get(
                                        crearEtiquetaEstado(estado , c)
                                )
                        )  ).orElse(0);
            }else {
                aletarioInt = random.nextInt( accionesAMirar.size() );
                accionPosicionEscogida = accionesAMirar.get(aletarioInt);
            }
        }



        //accionEleguida = crearEtiquetaEstado(estado,accionesAMirar.get(aletarioInt));
        return new AccionEscogida( estado.pieza() , estado.alturas() ,
                estado.accionPosicion().get(
                        accionPosicionEscogida
                ),
                estado.accionGiro().get(
                        accionPosicionEscogida
                ) );


    }

    private AccionEscogida EGreedyModificarCte(Estado estado){
        float probabilidadExplotar = 0.2f;
        float azarFloat;
        int aletarioInt,accionPosicionEscogida;
        String accionEleguida;
        boolean hayZeros = true;
        //=================================================//=================================================//
        //Aqui creamos la lista a eleguir
        List<Integer> accionesAMirar = new ArrayList<>();   //Aqui van a estar las posiciones de los estados a eleguir
        //Si al final no hay zeros esto sera una lista de 0 a estado.accionPosicion().size()
        List<Integer> accionesAux = new ArrayList<>();      //Este va a ser una lista de todas las acciones [0, sice]

        for(int i = 0 ; i < estado.accionPosicion().size() ; i++){
            accionesAux.add(i);
            if( qsap.get(crearEtiquetaEstado(estado,i)) == 0 ){
                accionesAMirar.add(i);
            }
        }
        if(accionesAMirar.isEmpty()){
            accionesAMirar = accionesAux;
            hayZeros = false;
        }

        //El objetivo es siempre preguntar y modificar aletarioInt y accionPosicionEscogida
        if(hayZeros){
            aletarioInt = random.nextInt( accionesAMirar.size() );
            accionPosicionEscogida = accionesAMirar.get(aletarioInt);
        }else{
            //=================================================//=================================================//
            //Aqui modificamos la constante
            int numeroAcciones = estado.accionPosicion().size();
            double mediaVistas = accionesAMirar.stream()
                    .mapToInt( c -> qsap.get(crearEtiquetaEstado(estado,c)) )
                    .average().orElse(0);
            if(mediaVistas < (double)(numeroAcciones/2) ){
                probabilidadExplotar = 0.1f;
            } else if (mediaVistas > (double) numeroAcciones*3) {
                probabilidadExplotar = 0.9f;
            }else{
                probabilidadExplotar = (float) (0.32/numeroAcciones) -0.06f;
            }


            azarFloat = random.nextFloat();
            if(azarFloat <= probabilidadExplotar){ //El mejor
                accionPosicionEscogida = accionesAMirar.stream()
                        .max( Comparator.comparing(c ->
                                qsa.get(
                                        crearEtiquetaEstado(estado , c)
                                )
                        )  ).orElse(0);
            }else {
                aletarioInt = random.nextInt( accionesAMirar.size() );
                accionPosicionEscogida = accionesAMirar.get(aletarioInt);
            }
        }



        //accionEleguida = crearEtiquetaEstado(estado,accionesAMirar.get(aletarioInt));
        return new AccionEscogida( estado.pieza() , estado.alturas() ,
                estado.accionPosicion().get(
                        accionPosicionEscogida
                ),
                estado.accionGiro().get(
                        accionPosicionEscogida
                ) );


    }

    private AccionEscogida elegcionPorVisitas(Estado estado){
        int aletarioInt,accionPosicionEscogida;
        accionPosicionEscogida = 0;
        String accionEleguida;
        boolean hayZeros = true;
        //=================================================//=================================================//
        //Aqui creamos la lista a eleguir
        List<Integer> accionesAMirar = new ArrayList<>();   //Aqui van a estar las posiciones de los estados a eleguir
        //Si al final no hay zeros esto sera una lista de 0 a estado.accionPosicion().size()
        List<Integer> accionesAux = new ArrayList<>();      //Este va a ser una lista de todas las acciones [0, sice]

        for(int i = 0 ; i < estado.accionPosicion().size() ; i++){
            accionesAux.add(i);
            if( qsap.get(crearEtiquetaEstado(estado,i)) == 0 ){
                accionesAMirar.add(i);
            }
        }
        if(accionesAMirar.isEmpty()){
            accionesAMirar = accionesAux;
            hayZeros = false;
        }

        //El objetivo es siempre preguntar y modificar aletarioInt y accionPosicionEscogida
        if(hayZeros){
            aletarioInt = random.nextInt( accionesAMirar.size() );
            accionPosicionEscogida = accionesAMirar.get(aletarioInt);
        }else{

            // accionesAMirar Aqui estan todas las acciones
            //Vamos a escoger una en accionPosicionEscogida
            Float puntuacionInferior = (float)accionesAMirar.stream()
                    .mapToDouble(c -> qsa.get(crearEtiquetaEstado(estado,c)))
                    .min().orElse(0.0f);

            List<Float> puntuacionLs = accionesAMirar.stream()
                    .map( c -> qsa.get( crearEtiquetaEstado(estado,c) )+puntuacionInferior)
                    .toList();

            Float total = (float) puntuacionLs.stream().mapToDouble(c -> c)
                    .sum();
            puntuacionLs = puntuacionLs.stream()
                    .map( c -> c/ total)
                    .toList();

            Integer vistas = accionesAMirar.stream()
                    .mapToInt(c -> qsap.get( crearEtiquetaEstado(estado,c) ))
                    .sum();
            float mediaVistas = (float) vistas /puntuacionLs.size();
            Integer numeroAcciones = puntuacionLs.size();
            List<Float> vistaMirar = accionesAMirar.stream()
                    .map(c ->(
                            (1 - (qsap.get( crearEtiquetaEstado(estado,c) ) /(float)vistas) )
                    ) )
                    .toList();
            float vistaTotal = (float) vistaMirar.stream().mapToDouble(c->c).sum();
            vistaMirar = vistaMirar.stream().map(c -> c/vistaTotal).toList();

            Float finalTotal = (float)vistaMirar.stream().mapToDouble(c -> c).sum();

            vistaMirar = vistaMirar.stream()
                    .map(c -> c/finalTotal)
                    .toList();


            float azarFloat = random.nextFloat()*2;
            float acumulador = 0.0f;
            float pesoPuntuacion;
            float pesoVistas;
            if(mediaVistas < (double)(numeroAcciones/2) ){
                pesoPuntuacion = 0.1f;
            } else if (mediaVistas > (double) numeroAcciones*3) {
                pesoPuntuacion = 0.9f;
            }else{
                pesoPuntuacion = (float) (0.32/numeroAcciones) -0.06f;
            }
            pesoVistas = 1 - pesoPuntuacion;

            for( int posi = 0 ; posi <  accionesAMirar.size() ; posi++){
                //Aqui es donde se realiza la suma ponderada
                acumulador += puntuacionLs.get(posi)*pesoPuntuacion+vistaMirar.get(posi)*pesoVistas;
                if(azarFloat < acumulador){
                    accionPosicionEscogida = accionesAMirar.get(posi);
                    break;
                }

            }



        }
        return new AccionEscogida( estado.pieza() , estado.alturas() ,
                estado.accionPosicion().get(
                        accionPosicionEscogida
                ),
                estado.accionGiro().get(
                        accionPosicionEscogida
                ) );
    }

    // Coreguir y pedir variables
    //==========
        /*
        for( int k = 0 ; k < estado.accionPosicion().size();k++ ){
            auxFloat = qsa.get( crearEtiquetaEstado( estado , k ) ); //Esta es la de eleguir accion

            if (valorMaximoAccion < auxFloat) {
                valorMaximoAccion = auxFloat;
                maximaAcciones = k;
            }
        }

        //======
        auxFloat = 2.0f; //(float) ( - ( numeroAccionesPuntuadas/acciones.size() - 1)*0.9 + 0.1);
        //Ahora mismo esta en full aleatorio
        aletarioInt = random.nextInt( estado.accionPosicion().size() );
        aleatorio = random.nextFloat();

        //Continuar programando Linea 107

        if (aleatorio < auxFloat) {
            proximaAccionX = estado.accionPosicion().get(aletarioInt);
            proximaAccionGiro = estado.accionGiro().get(aletarioInt);

            qsap.put( crearEtiquetaEstado(estado,aletarioInt) ,  qsap.get(crearEtiquetaEstado(estado,aletarioInt))+1); //encrementamos 1
            //System.out.println("Exploracion");
        }else{
            proximaAccionX = estado.accionPosicion().get(maximaAcciones);
            proximaAccionGiro = estado.accionGiro().get(maximaAcciones);
            qsap.put( crearEtiquetaEstado(estado,maximaAcciones) ,  qsap.get(crearEtiquetaEstado(estado,maximaAcciones))+1); //incrementamos 1
            //System.out.println("avance");
        }

        //Sumamos para la accion eleguida
        estadoAnterior = estado;
        accionElegida = estadoAnterior.pieza()+"/"+ //este si se queda porque depende de proximo
                estadoAnterior.alturas()+"A"+
                proximaAccionX+"-"+proximaAccionGiro;
        */
    //=========

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
                qsap.put( crearEtiquetaEstado(entrada,i) ,
                        0);
            }
        }
        return res;
    }



    private String crearEtiquetaEstado(Estado estado , int n){
        return  estado.pieza()+"/"+estado.alturas()+"A"+estado.accionPosicion().get(n)+"-"+estado.accionGiro().get(n);
    }
    private String crearEtiquetaEstado(AccionEscogida accion){
        return  accion.pieza()+"/"+accion.alturas()+"A"+accion.posicion()+"-"+accion.giro();
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
        puntuacion += estadoSiguiente.filasQuitadas() - (float)(estadoSiguiente.huecosNuevos()*anchoTablero/2);

        return puntuacion;
    }
    private float calcularPuntucaion( Estado estadoSiguiente , Integer tiempoDePartida){
        float puntuacion = tiempoDePartida;

        //Ahora hay que realizar una cosa muy bonita
        // Si se genera un hueco nuevo se le castiga con ( - numero de huecos )
        // Si se borra una linea se le premia con        ( + el numero de fila quitadas )
        puntuacion += estadoSiguiente.filasQuitadas();

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
                        bw.write( j+"="+qsa.get(j)+"="+qsap.get(j) );
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
                        qsap.put( separacion[0] , Integer.parseInt(separacion[2]) );
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

    private record AccionEscogida(Integer pieza , List<Integer> alturas, Integer posicion, Integer giro ){}

}
