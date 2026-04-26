package calculoHeuristicaTetris;

import java.util.ArrayList;
import java.util.List;

public class resultadoHeuristica {
    Integer numeroIteracionMaxima;
    List<String> listaDePiezasSacadas;
    List<Integer> movimientoLs;
    List<Integer> girosLs;
    String tipoDePartida;
    String tipoFinal;
    List<Integer> numeroPiezas;
    List<Double> tiempos;


    public resultadoHeuristica(String tipoDePartida){
        this.tipoDePartida = tipoDePartida;
        this.numeroIteracionMaxima = 0;
        this.listaDePiezasSacadas = new ArrayList<>();
        this.movimientoLs = new ArrayList<>();
        this.girosLs = new ArrayList<>();
        this.numeroPiezas = new ArrayList<>();
        this.tiempos = new ArrayList<>();
        tipoFinal = "SinFinalizar";
    }

    public void addAccion(Integer movimiento, Integer giro, String pieza , Integer numeroPieza, Double duracionTiempo){
        this.movimientoLs.add(movimiento);
        this.girosLs.add(giro);
        this.listaDePiezasSacadas.add(pieza);
        this.numeroPiezas.add(numeroPieza);
        this.tiempos.add(duracionTiempo);
        this.numeroIteracionMaxima++;
    }

    public Integer getNumeroIteracionMaxima() {
        return numeroIteracionMaxima;
    }

    public void setNumeroIteracionMaxima(Integer numeroIteracionMaxima) {
        this.numeroIteracionMaxima = numeroIteracionMaxima;
    }

    public List<String> getListaDePiezasSacadas() {
        return listaDePiezasSacadas;
    }

    public void setListaDePiezasSacadas(List<String> listaDePiezasSacadas) {
        this.listaDePiezasSacadas = listaDePiezasSacadas;
    }

    public List<Integer> getMovimientoLs() {
        return movimientoLs;
    }

    public void setMovimientoLs(List<Integer> movimientoLs) {
        this.movimientoLs = movimientoLs;
    }

    public List<Integer> getGirosLs() {
        return girosLs;
    }

    public void setGirosLs(List<Integer> girosLs) {
        this.girosLs = girosLs;
    }

    public String getTipoDePartida() {
        return tipoDePartida;
    }

    public void setTipoDePartida(String tipoDePartida) {
        this.tipoDePartida = tipoDePartida;
    }

    public String getTipoFinal() {
        return tipoFinal;
    }

    public void setTipoFinal(String tipoFinal) {
        this.tipoFinal = tipoFinal;
    }

    @Override
    public String toString() {
        return "resultadoHeuristica{" +
                "\nnumeroIteracionMaxima=" + numeroIteracionMaxima +
                ",\nlistaDePiezasSacadas=" + listaDePiezasSacadas +
                ",\nmovimientoLs=" + movimientoLs +
                ",\ngirosLs=" + girosLs +
                ",\ntipoDePartida='" + tipoDePartida + '\'' +
                ",\ntipoFinal='" + tipoFinal + '\'' +
                '}';
    }
    public String titulo(){
        return "numeroIteracionMaxima;tipoDePartida;tipoFinal;tiempos;numeroPiezas;listaDePiezasSacadas;movimientoLs;girosLs";
    }
    public String fila(){
        String res = "";
        res += numeroIteracionMaxima+";";
        res +=tipoDePartida+";";
        res+=tipoFinal+";";
        res += tiempos.toString().replace("[","").replace("]","")+";";
        res += numeroPiezas.toString().replace("[","").replace("]","")+";";
        res += listaDePiezasSacadas.toString().replace("[","").replace("]","")+";";
        res +=movimientoLs.toString().replace("[","").replace("]","")+";";
        res += girosLs.toString().replace("[","").replace("]","");
        return res;
    }

    public List<Integer> getNumeroPiezas() {
        return numeroPiezas;
    }

    public void setNumeroPiezas(List<Integer> numeroPiezas) {
        this.numeroPiezas = numeroPiezas;
    }

    public List<Double> getTiempos() {
        return tiempos;
    }

    public void setTiempos(List<Double> tiempos) {
        this.tiempos = tiempos;
    }
}
