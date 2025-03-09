
import java.util.Objects;



public class Coordenada {
    private  Integer x;
    private  Integer y;
    public  Coordenada(int x , int y){
        this.x = x;
        this.y = y;
    }
    public Integer getX(){
        return this.x;
    }
    public Integer getY(){
        return this.y;
    }
    public void putX(Integer x){
        this.x = x;
    }
    public void putY(Integer y){
        this.y = y;
    }
    public String aTexto(){
        return "["+x+","+y+"]";
    }
    @Override
    public boolean equals(Object obj) {
        boolean res = false;
        if(obj.getClass() == Coordenada.class){
            if(Objects.equals(this.x, ((Coordenada) obj).getX()) && Objects.equals(this.y, ((Coordenada) obj).getY())){
                res = true;
            }
        }
        return res;
    }
}
