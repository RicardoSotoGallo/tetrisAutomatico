package algoritmoAprendizajePorRefuerzo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import tetrissimulador.juego;

public class refuerzo {
    /*
     * qsa es esactamente Q(s,a)
     */
    private HashMap<String,Integer> qsa = new HashMap<>();
    private String estadoActual;
    private Integer posicion;
    private Integer giro;
    List<String> acciones;
    private String textoDeEstado;
    /*
     * La idea es primero se inicia el juego eliguiendo altura y anchura
     * se usa la funcion dibujar para enseñar por consola el estado del juego
     * iniciar desde cubo es para colocar el juego en un estado concreto
     * splitear es para recibir el estado y sacar en estadoActual y acciones el estado del juego y el conjunto de acciones disponibles
     */
    public void entrenar(){
        juego j = new juego(5,4);
        estadoActual = j.iniciarDesdeEstadoCubo("0300");
        j.dibujar();
        System.out.println(estadoActual);
        splitearAccionesCubo(estadoActual);
        System.out.println("estaodo -> "+estadoActual);
        System.out.println("acciones -> "+acciones);
        for (String i : acciones) {
            if (!qsa.containsKey(estadoActual+"-"+i)) {
                qsa.put(estadoActual+"-"+i, 0);
            }
        }
        System.out.println(qsa);
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
