public class pareja {
    private Integer x;
    private Integer y;
    public pareja(Integer posicionX , Integer posicionY){
        x = posicionX;
        y = posicionY;
    }
    public Integer getX(){
        return x;
    }
    public Integer getY(){
        return y;
    }
    public void putX(Integer posicionX){
        x = posicionX;
    }
    public void putY(Integer posicionY){
        y = posicionY;
    }
    public String aTexto(){
        return "["+x+","+y+"]";
    }
    @Override
    public String toString(){
        return "["+x+","+y+"]";
    }
}
