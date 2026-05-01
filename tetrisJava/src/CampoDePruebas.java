import java.util.*;
import java.util.stream.Collectors;

public class CampoDePruebas {
    public static void main(String[] args) {
        HashMap<String,Integer> diccionario = new HashMap<>();
        diccionario.put("a",0);
        diccionario.put("b",1);
        diccionario.put("c",1);
        diccionario.put("d",2);
        diccionario.put("e",3);
        diccionario.put("f",0);
        List<String> lista = new ArrayList<>();
        lista.add("a");
        lista.add("b");
        lista.add("c");
        lista.add("d");
        lista.add("f");
        List<Integer> lsStream = new ArrayList<>();



        System.out.println(diccionario);
        System.out.println(lista);
        String eleguido = lista.stream()
                        .max(  Comparator.comparing(c -> diccionario.get(c)) )
                                .orElse("No hay nada");

        System.out.println(eleguido);
    }
}
