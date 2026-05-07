import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class pruebasGrande {
    public static void main(String[] args) {
        //System.out.println("hola");
        List<ab> ls = new ArrayList<>();
        ls.add( new ab("hola", 5) );
        ls.add( new ab("adios", 10) );
        ls.add( new ab("buenas", 3) );

        System.out.println( ls.stream().max( Comparator.comparing(ab::numero) ).get().texto );

    }
    public record ab(String texto,Integer numero){}
}
