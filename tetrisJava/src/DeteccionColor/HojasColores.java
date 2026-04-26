package DeteccionColor;

import java.util.*;

public class HojasColores {
    HojasColores hijoMenor;
    HojasColores hijoMayor;
    HojasColores hijoCentro;
    Intervalo pregunta = null;
    String capa;
    String tipoRama;
    Intervalo rojoInter;
    Intervalo azulInter;
    Intervalo verdeInter;
    public HojasColores(List<Intervalo> lsRojo, List<Intervalo> lsAzul , List<Intervalo> lsVerde){
        /*
        Casas que puede entrar en cada capa
        1.- la lista esta vacia. Esto es un error del padre No deberia de suceder (ERROR)
        2.- Un solo elemento Entonces esque esa es una franja ya escogida (Ni se mira)
        3.- Varios. Hay que escogerla
         */

        Intervalo interAux;
        if(lsRojo.size() > 1){
            capa = "Rojo";
            pregunta = lsRojo.stream().max( Comparator.comparing(Intervalo::getPesos) ).get();
        }
        if(lsAzul.size() > 1){
            interAux = lsAzul.stream().max( Comparator.comparing(Intervalo::getPesos) ).get();
            if(pregunta == null){
                pregunta = interAux;
                capa = "Azul";
            }else if(interAux.getPesos() > pregunta.getPesos() ){
                pregunta = interAux;
                capa = "Azul";
            }

        }

        if(lsVerde.size() > 1){
            interAux = lsVerde.stream().max( Comparator.comparing(Intervalo::getPesos) ).get();
            if(pregunta == null){
                pregunta = interAux;
                capa = "Verde";
            }else if(interAux.getPesos() > pregunta.getPesos()){
                pregunta = interAux;
                capa = "Verde";
            }

        }

        if(pregunta == null){
            tipoRama = "Hoja";
            rojoInter = lsRojo.getFirst();
            azulInter = lsAzul.getFirst();
            verdeInter = lsVerde.getFirst();
        }else{
            tipoRama = "Rama";
            /*
            Aqui vamos a crear los hijos
            1.- los menores si al sacar los menor la lista sale vacia NO SE CREA
             */
            List<List<Intervalo>> nuevaHijo = lsHijos(lsRojo , lsAzul ,lsVerde , -1);
            if(nuevaHijo == null){
                hijoMenor = null;
            }else{
                hijoMenor = new HojasColores( nuevaHijo.get(0) , nuevaHijo.get(1), nuevaHijo.get(2) );
            }
            /*
            Ahora el hijo del centro. Esta va a depender del tipo
             */
            if(capa == "Rojo"){
                hijoCentro = new HojasColores( new ArrayList<>(List.of(pregunta)) , lsAzul , lsVerde );
            } else if (capa == "Azul") {
                hijoCentro = new HojasColores( lsRojo , new ArrayList<>(List.of(pregunta)) , lsVerde  );
            } else if (capa == "Verde") {
                hijoCentro = new HojasColores( lsRojo , lsAzul , new ArrayList<>(List.of(pregunta))  );
            }
            /*
            Aqui creamos la mayot
             */
            nuevaHijo = lsHijos(lsRojo , lsAzul ,lsVerde , 1);
            if(nuevaHijo == null){
                hijoMayor = null;
            }else{
                hijoMayor = new HojasColores( nuevaHijo.get(0) , nuevaHijo.get(1), nuevaHijo.get(2) );
            }
        }

    }

    public HojasColores(String tipo , String pregunta, HashMap<Integer,Intervalo> diccIntervalo){
       this.tipoRama = tipo;
       if(tipoRama.equals("Rama")){
           this.pregunta = diccIntervalo.get( Integer.valueOf(pregunta) );
           this.capa = this.pregunta.getCapa();
       }else{
           if(pregunta.equals("null")){
               for(Intervalo inter : diccIntervalo.values()){
                   if(inter.getCapa().equals("Rojo")) {
                       this.rojoInter = inter;
                   } else if (inter.getCapa().equals("Azul")) {
                       this.azulInter = inter;
                   } else if (inter.getCapa().equals("Verde")) {
                       this.verdeInter = inter;
                   }
               }
           }else{
               pregunta = pregunta.replace("[","").replace("]","");
               String[] tresCInter = pregunta.split(",");
               this.rojoInter = diccIntervalo.get( Integer.valueOf(tresCInter[0]) );
               this.azulInter = diccIntervalo.get( Integer.valueOf(tresCInter[1]) );
               this.verdeInter = diccIntervalo.get( Integer.valueOf(tresCInter[2]) );
           }

       }

    }

    private List<List<Intervalo>> lsHijos(List<Intervalo> lsRojo, List<Intervalo> lsAzul , List<Intervalo> lsVerde , Integer menorMayor){
        /*
        Si la  resuesta es null
        No se crea Hijos
        menor = -1
        mayor = 1
         */
        List<List<Intervalo>> res = null;
        if(pregunta != null){
            List<Intervalo> mirar = new ArrayList<>();
            if(capa == "Rojo"){
                mirar = new ArrayList<>(lsRojo);
            } else if (capa == "Azul") {
                mirar = new ArrayList<>(lsAzul);
            } else if (capa == "Verde") {
                mirar = new ArrayList<>(lsVerde);
            }
            if(menorMayor < 0){
                mirar = mirar.stream().filter( t -> t.getFin() <= pregunta.getInicio() && !t.equals(pregunta)).toList();
            }else{
                mirar = mirar.stream().filter( t -> t.getInicio() >= pregunta.getFin() && !t.equals(pregunta)).toList();
            }

            if(!mirar.isEmpty()){
                res = new ArrayList<>();
                if(capa == "Rojo"){
                    res.add(mirar);
                    res.add(lsAzul);
                    res.add(lsVerde);
                } else if (capa == "Azul") {
                    res.add(lsRojo);
                    res.add(mirar);
                    res.add(lsVerde);
                } else if (capa == "Verde") {
                    res.add(lsRojo);
                    res.add(lsAzul);
                    res.add(mirar);
                }
            }
        }


        return res;
    }

    public void showArbol(String cadenaSucesion){
        /*
        M -> Hijo Mayor
        N -> Hijo Menor
        C -> Hijo Centro
         */
        System.out.println("===========");
        System.out.println("Cadena="+cadenaSucesion);
        System.out.println("Tipo="+tipoRama);
        System.out.println("Pregunta="+pregunta+" Capa="+capa);
        if(hijoMenor!=null) hijoMenor.showArbol(cadenaSucesion+"N");
        if(hijoCentro != null) hijoCentro.showArbol(cadenaSucesion+"C");
        if(hijoMayor != null) hijoMayor.showArbol(cadenaSucesion+"M");
        if(tipoRama.equals("Hoja")){
            System.out.println("rojo->"+rojoInter);
            System.out.println("azul->"+azulInter);
            System.out.println("verde->"+verdeInter);
        }


    }

    public void contruirEnlaces(HashMap<String,HojasColores> diccHojas, String cadenaSucesion){
        String textoMirar = cadenaSucesion+"n";
        if( diccHojas.containsKey(textoMirar) ){
            this.hijoMenor = diccHojas.get(textoMirar);
            this.hijoMenor.contruirEnlaces( diccHojas , textoMirar );
        }

        textoMirar = cadenaSucesion+"c";
        if( diccHojas.containsKey(textoMirar) ){
            this.hijoCentro = diccHojas.get(textoMirar);
            this.hijoCentro.contruirEnlaces( diccHojas , textoMirar );
        }

        textoMirar = cadenaSucesion+"m";
        if( diccHojas.containsKey(textoMirar) ){
            this.hijoMayor = diccHojas.get(textoMirar);
            this.hijoMayor.contruirEnlaces( diccHojas , textoMirar );
        }

    }

    public float preguntaInter(int rojo , int azul , int verde){
        float res = 0;
        int valorCuestionar = 0;
        if(tipoRama.equals("Rama")){
            if(Objects.equals(capa, "Rojo")) {
                valorCuestionar = rojo;
            }
            else if (Objects.equals(capa, "Azul")) {
                valorCuestionar = azul;
            }
            else if (Objects.equals(capa, "Verde")) {
                valorCuestionar = verde;
            }

            if(valorCuestionar < pregunta.getInicio()){
                //Esta por debajo
                //System.out.println(pregunta+" capa="+capa);
                res = hijoMenor.preguntaInter(rojo,azul,verde);

            } else if (valorCuestionar > pregunta.getFin()) {
                //esta por encima
                //System.out.println(pregunta+" capa="+capa);
                res = hijoMayor.preguntaInter(rojo,azul,verde);

            }else{
                //estamos en el intervalo
                //System.out.println(pregunta+" capa="+capa+" es centro ");
                res = hijoCentro.preguntaInter(rojo,azul,verde);
            }
        }
        else{
            //System.out.println("Hoja \n"+"Rojo->"+this.rojoInter+"\nazul->"+this.azulInter+"\nverde->"+this.verdeInter);
            res = this.rojoInter.devolverValor(rojo) * this.azulInter.devolverValor(azul) * this.verdeInter.devolverValor(verde);
            //estoy es fuzzy mean
            //res = (this.rojoInter.devolverValor(rojo) + this.azulInter.devolverValor(azul) + this.verdeInter.devolverValor(verde))/3;

        }
        return res;
    }

    public void devolverEstructura(HashMap<Intervalo,Integer> diccIntervalo , HashMap<String,String> dicArbol, String id){
        String res = "";
        if(Objects.equals(tipoRama, "Rama")){
            if(!diccIntervalo.containsKey(pregunta)){
                pregunta.setCapa(capa);
                diccIntervalo.put(pregunta , diccIntervalo.size());
            }

        }
        else{
            rojoInter.setCapa("Rojo");
            azulInter.setCapa("Azul");
            verdeInter.setCapa("Verde");

            if(!diccIntervalo.containsKey(rojoInter)){
                diccIntervalo.put(rojoInter, diccIntervalo.size());
            }

            if(!diccIntervalo.containsKey(azulInter)){
                diccIntervalo.put(azulInter,diccIntervalo.size());
            }

            if(!diccIntervalo.containsKey(verdeInter)){
                diccIntervalo.put(verdeInter,diccIntervalo.size());
            }
        }

        /*
        Aqui guardamos el arbol
         */
        if(id.equals("raiz")){

            dicArbol.put(id ,
                    tipoRama+";"+diccIntervalo.get(pregunta)
                    );
            id="";
        }
        else{
            if(tipoRama.equals("Rama")){
                dicArbol.put(id ,
                        tipoRama+";"+diccIntervalo.get(pregunta)
                );
            }
            else {
                dicArbol.put(id ,
                        tipoRama+";["+diccIntervalo.get(rojoInter)+","+
                                diccIntervalo.get(azulInter)+","+
                                diccIntervalo.get(verdeInter)+"]"
                );
            }
        }

        if(Objects.equals(tipoRama, "Rama")) {
            if(hijoMenor != null){
                hijoMenor.devolverEstructura(diccIntervalo ,dicArbol,id+"n");
            }
            if(hijoCentro != null){
                hijoCentro.devolverEstructura(diccIntervalo,dicArbol,id+"c");
            }
            if(hijoMayor != null){
                hijoMayor.devolverEstructura(diccIntervalo,dicArbol,id+"m");
            }
        }
    }

    @Override
    public String toString() {
        return "HojasColores{" +
                "hijoMenor=" + hijoMenor +
                ", hijoMayor=" + hijoMayor +
                ", hijoCentro=" + hijoCentro +
                ", pregunta=" + pregunta +
                ", capa='" + capa + '\'' +
                ", tipoRama='" + tipoRama + '\'' +
                ", rojoInter=" + rojoInter +
                ", azulInter=" + azulInter +
                ", verdeInter=" + verdeInter +
                '}';
    }
}
