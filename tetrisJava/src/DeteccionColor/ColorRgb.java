package DeteccionColor;

import java.util.Objects;

public class ColorRgb {
    private Integer rojo;
    private Integer verde;
    private Integer azul;

    public ColorRgb(int rojo, int verde,int azul ){
        this.rojo = rojo;
        this.verde = verde;
        this.azul = azul;
    }

    public ColorRgb rgbNuevo(){
        return new ColorRgb(this.rojo , this.verde, this.azul);
    }

    public void setAzul(Integer azul) {
        this.azul = azul;
    }

    public void setVerde(Integer verde) {
        this.verde = verde;
    }

    public void setRojo(Integer rojo) {
        this.rojo = rojo;
    }

    public Integer getAzul() {
        return azul;
    }

    public Integer getVerde() {
        return verde;
    }

    public Integer getRojo() {
        return rojo;
    }


    @Override
    public String toString() {
        return "R="+rojo+" G="+verde+" B="+azul;
    }
    public String toHexa(){
        return String.format("#%02X%02X%02X", rojo, verde, azul);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ColorRgb colorRgb = (ColorRgb) o;
        return Objects.equals(rojo, colorRgb.rojo) && Objects.equals(verde, colorRgb.verde) && Objects.equals(azul, colorRgb.azul);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rojo, verde, azul);
    }
}
