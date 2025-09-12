package tetrissimulador;

public record vector2D(Integer x, Integer y){
    
    public vector2D(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object r){
        boolean res = false;
        if (r instanceof  vector2D u) {
            res = u.y.equals(y) && u.x.equals(x);
        }
        return res;
    }
    
    @Override
    public final String toString() {
        return "["+x+","+y+"]";
    }
}
