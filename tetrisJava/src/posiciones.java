
import java.util.ArrayList;
import java.util.List;

public class posiciones {
    private List<Integer> x;
    private List<Integer> y;
    public Integer XMin,XMax;
    public Integer YMin,YMax;
    private Integer divisionX,divisionY;
    public posiciones(){
        x = new ArrayList<>();
        y = new ArrayList<>();
    }
    public void cacularDivisiones(Integer margenX , Integer margenY , Integer divisionX, Integer divisionY){
        if(margenX > 0 && margenY > 0 && divisionX > 2 && divisionY >2){
            int inicioX = XMin + margenX;
            int tamanoX = XMax - (inicioX + margenX);
            int inicioY = YMin + margenY;
            int tamanoY = YMax - (inicioY + margenY);
            if (tamanoX > 0 && tamanoY > 0) {
                divisionX = divisionX -1;
                divisionY = divisionY -1;
                for(int i = 0 ; i < divisionX+1; i++){
                    x.add(inicioX + Math.round( tamanoX * i/divisionX ));
                }
                for(int i = 0 ; i < divisionY+1; i++){
                    y.add(inicioY + Math.round( tamanoY * i/divisionY ));
                }
            }
        }
        
    }
    public Integer getpixelX(int a){
        return x.get(a);
    }
    public Integer getpixelY(int a){
        return y.get(a);
    }
    @Override
    public String toString(){
        return "["+XMin+","+XMax+"] -> "+x+" | ["+YMin+","+YMax+","+"] -> "+y;
    }
}
