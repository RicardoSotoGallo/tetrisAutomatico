package DeteccionColor;

public class ConteoColores {
    private Integer negativo;
    private Integer positivo;

    public ConteoColores(){
        negativo = 0;
        positivo = 0;
    }
    public ConteoColores(int posi , int nega){
        negativo = nega;
        positivo = posi;
    }
    public void sumaPosi(int suma){
        positivo += suma;
    }
    public void sumaNega(int suma){
        negativo += suma;
    }

    public Integer getNegativo() {
        return negativo;
    }

    public Integer getPositivo() {
        return positivo;
    }

    public void setNegativo(Integer negativo) {
        this.negativo = negativo;
    }

    public void setPositivo(Integer positivo) {
        this.positivo = positivo;
    }

    @Override
    public String toString() {
        return "P="+positivo+" N="+negativo;
    }
}
