package algoritmoAprendizajePorRefuerzo;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import tetrissimulador.juego;

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
    /*
     * La idea es primero se inicia el juego eliguiendo altura y anchura
     * se usa la funcion dibujar para enseñar por consola el estado del juego
     * iniciar desde cubo es para colocar el juego en un estado concreto
     * splitear es para recibir el estado y sacar en estadoActual y acciones el estado del juego y el conjunto de acciones disponibles
     */
    public void entrenar(){
        String[] aux;
        Integer proximaAccion;
        float auxFloat;
        Integer maximaAcciones;
        float valorMaximoAccion;
        Integer numeroAccionesPuntuadas;
        String estadoAnterior = "";
        Integer maximoAltura,minimaAltura,auxInt;
        juego j = new juego(6,4);
        float puntuacion;
        List<String> lsNoVisitidos;
        for(int ii = 0 ; ii < 10; ii++){
            random = new Random();
            j = new juego(6,4);
            textoDeEstado = j.iniciarDesdeEstadoCubo("0000");
            for(int i = 0; i < 100000 ; i++){
                
                
                j.dibujar();
                splitearAccionesCubo(textoDeEstado);
                addNuevasAcciones();
                //====== Aqui puntuamos los estados
                maximoAltura = -1;
                minimaAltura = 100;
                for(int o = 0; o < estadoActual.length();o++){
                    auxInt = Character.getNumericValue((estadoActual.charAt(o)));
                    if (maximoAltura < auxInt) {
                        maximoAltura = auxInt;
                    }
                    if (minimaAltura > auxInt) {
                        minimaAltura = auxInt;
                    }

                }
                puntuacion = 0.0f;
                for(int o = 0; o < estadoActual.length();o++){
                    auxInt = Character.getNumericValue((estadoActual.charAt(o)));
                    puntuacion += auxInt;
                }
                puntuacion = puntuacion/estadoActual.length()-maximoAltura;
                qsa.put(estadoAnterior, puntuacion);
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
                auxFloat =  (float) ( - ( numeroAccionesPuntuadas/acciones.size() - 1)*0.9 + 0.1);
                aletarioInt = random.nextInt( acciones.size() );
                aleatorio = random.nextFloat();
                if (aleatorio < auxFloat) {
                    if (numeroAccionesPuntuadas != 0) {
                        aux = acciones.get(aletarioInt).split("-");
                        proximaAccion = Integer.valueOf(aux[0]);
                    }else{
                        aletarioInt = random.nextInt( lsNoVisitidos.size() );
                        aux = lsNoVisitidos.get(aletarioInt).split("-");
                        proximaAccion = Integer.valueOf(aux[0]);
                    }
                    
                    System.out.println("Exploracion");
                }else{
                    proximaAccion = maximaAcciones;
                    System.out.println("avance");
                }
                estadoAnterior = estadoActual+"-"+proximaAccion;
                textoDeEstado = j.realizarMovimiento(proximaAccion, 0);
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
        
        //System.out.println(textoDeEstado);
        splitearAccionesCubo(textoDeEstado);
        //System.out.println("estaodo -> "+estadoActual);
        //System.out.println("acciones -> "+acciones);
        
        //System.out.println(qsa);
        //System.out.println("==================");
        String texto = j.realizarMovimiento(2, 0);
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
