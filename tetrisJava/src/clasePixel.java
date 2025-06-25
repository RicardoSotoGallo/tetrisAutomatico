
import java.awt.Color;

public class clasePixel {
    private int x;
    private int y;
    private Color color;
    private double valor;
    private int captura;
    public clasePixel(int x, int y, Color color , double valor, int numeroCaptura){
        this.x = x;
        this.y = y;
        this.color = color;
        this.valor = valor;
        this.captura = numeroCaptura;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public Color getColor(){
        return color;
    }
    public  double getValor(){
        return valor;
    }
    public int getCaptura(){
        return captura;
    }
    public boolean mismaPosicion(int x, int y){
        return this.x == x && this.y == y;
    }
    public boolean  mismaPosicion(int x,int y,int captura){
        return this.x == x && this.y == y && captura == this.captura;
    }
    public boolean mismoColor(Color color){
        return color.equals(this.color);
    }
    public double cambiarSiEsMayor(double valor){
        if(this.valor < valor){
            this.valor = valor;
        }
        return this.valor;
    }
    @Override
    public String toString(){
        return x+";"+y+";"+captura+";"+color.getRed()+";"+color.getBlue()+";"+color.getGreen()+";"+valor;
    }
    
}