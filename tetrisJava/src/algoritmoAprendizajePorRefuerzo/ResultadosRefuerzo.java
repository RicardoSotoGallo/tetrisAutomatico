package algoritmoAprendizajePorRefuerzo;

import java.util.List;

public class ResultadosRefuerzo {
    public float numeroIteraciones;
    public Integer numeroJuegosEntrenamiento;
    public List<String> listaDePiezasSacadas;
    public List<Integer> movimientoLs;
    public List<Integer> girosLs;
    public String tipoDePartida;
    public String tipoFinal;
    public List<Integer> numeroPiezas;
    public List<Double> tiempos;

    public String titulo(){
        return "numeroIteraciones;numeroJuegosEntrenamiento;tipoDePartida;movimientoLs;girosLs;numeroPiezas;tiempos";
    }
    public String fila(){
        return numeroIteraciones+";"+
                numeroJuegosEntrenamiento+";"+
                tipoDePartida+";"+
                movimientoLs.toString().replace("[","").replace("]","")+";"+
                girosLs.toString().replace("[","").replace("]","")+";"+
                numeroPiezas.toString().replace("[","").replace("]","")+";"+
                tiempos.toString().replace("[","").replace("]","");
    }
}

