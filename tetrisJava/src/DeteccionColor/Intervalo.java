package DeteccionColor;

import java.util.Objects;

public class Intervalo {
    private Integer inicio;
    private Integer fin;
    private String tipo;
    private Integer pesos;

    private String capa;

    public Intervalo(Integer inicio, Integer fin,boolean tipo , Integer pesos) {
        this.inicio = inicio;
        this.fin = fin;
        if(tipo){
            this.tipo = "P";
        }else {
            this.tipo = "N";
        }
        this.pesos = pesos;
    }
    public Intervalo(Integer inicio, Integer fin,boolean tipoIni, boolean tipoFin) {
        this.inicio = inicio;
        this.fin = fin;
        this.pesos = 0;
        if( tipoIni ){
            if(tipoFin){
                this.tipo = "P";
            }else{
                this.tipo = "PN";
            }
        }else{
            if(tipoFin){
                this.tipo = "NP";
            }else {
                this.tipo = "N";
            }
        }
    }
    public Intervalo(String inicio,String fin, String capa, String tipo , String pesos){
        this.inicio = Integer.valueOf(inicio);
        this.fin = Integer.valueOf(fin);
        this.capa = capa;
        this.tipo = tipo;
        this.pesos = Integer.valueOf(pesos);
    }

    public Integer getInicio() {
        return inicio;
    }

    public void setInicio(Integer inicio) {
        this.inicio = inicio;
    }

    public Integer getFin() {
        return fin;
    }

    public void setFin(Integer fin) {
        this.fin = fin;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getPesos() {
        return pesos;
    }

    public void setPesos(Integer pesos) {
        this.pesos = pesos;
    }

    public float devolverValor(int valor){
        float res = 0;
        if(Objects.equals(tipo, "N")){
            res = 0;
        } else if (Objects.equals(tipo, "P")) {
            res = 1;
        } else if (Objects.equals(tipo, "NP")) {
            res = (float) (valor - inicio) /(fin-inicio);
        } else if (Objects.equals(tipo, "PN")) {
            res = 1 - (float) (valor - inicio) /(fin-inicio);
        }
        return res;
    }

    public String getCapa() {
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }

    @Override
    public String toString() {
        return "["+inicio+","+fin+"]=("+tipo+") peso="+pesos+" Capa="+capa+"|";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Intervalo intervalo = (Intervalo) o;
        return Objects.equals(inicio, intervalo.inicio) && Objects.equals(fin, intervalo.fin) &&
                Objects.equals(capa,intervalo.capa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inicio, fin,tipo);
    }
}
