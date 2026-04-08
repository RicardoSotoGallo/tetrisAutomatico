package algoritmoAprendizajePorRefuerzo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SalidaRefuerzo {
    public List<Float> listaIteraciones;
    private float factorAprendizaje, factorRecuerdo;
    private Integer numeroDeJuegos;
    public SalidaRefuerzo(float factorAprendizaje , float factorRecuerdo,
                          Integer numeroDeJuegos){
        this.listaIteraciones = new ArrayList<>();
        this.factorAprendizaje = factorAprendizaje;
        this.factorRecuerdo = factorRecuerdo;
        this.numeroDeJuegos = numeroDeJuegos;

    }
    public void addListaIteracion(Float iteracion){
        this.listaIteraciones.add(iteracion);
    }
    public String nombreDato(){
        return factorAprendizaje+"|"+factorRecuerdo;
    }
    public void escribirFichero(String nombre){
        String dire = "tetrisJava/salidaTestFactores/" +nombre;
        String texto = factorAprendizaje +";"+factorRecuerdo+";"+numeroDeJuegos;
        for(int i = 0 ; i < listaIteraciones.size();i++){
            texto+="\n"+i+";"+listaIteraciones.get(i);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dire))) {
            writer.write(texto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
