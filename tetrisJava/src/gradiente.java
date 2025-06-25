
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class gradiente {
    List<Integer> trapecioRojo = new ArrayList<>(4);
    List<Integer> trapecioAzul = new ArrayList<>(4);
    List<Integer> trapecioVerde = new ArrayList<>(4);
    List<Double> pesos;
    public gradiente(int numeroColores){
        pesos = new ArrayList<>();
        for(int i = 0 ; i < numeroColores+1; i++){
            if(i == 0){
                pesos.add(Math.random());
            }else{
                pesos.add(Math.random());
            }
            
        }
    }
    public static void main(String[] args) {
        List<List<Double>> valores = new ArrayList<>();
        List<Double> valoresValores = new ArrayList<>();
        List<Double> salida = new ArrayList<>();
        for(double i = 0; i < 11; i++){
            for(double j = 0; j < 11; j++){
                for(double k = 0; k < 11 ; k++){
                    valoresValores = new ArrayList<>();
                    valoresValores.add(i/10);
                    valoresValores.add(j/10);
                    valoresValores.add(k/10);
                    if(i+j+k >= 10){
                        salida.add(1.0);
                    }else{
                        salida.add(0.0);
                    }
                    valores.add(valoresValores);
                }
            }
        }
        gradiente grad = new gradiente(3);
        //System.out.println(grad.pesos);
        grad.entrenarPerceptron(valores, salida,20,0.2);
        System.out.println(grad.pesos);
        //System.out.println(valores);
        //System.out.println(salida);
        /*HashMap<Integer,Double> rojo = new HashMap<>();
        HashMap<Integer,Double> verde = new HashMap<>();
        HashMap<Integer,Double> azul = new HashMap<>();
        for(int i = 0 ; i < 20 ; i++){
            rojo.put(i, 0.0);
            verde.put(i, 0.0);
            azul.put(i, 0.0);
        }
        for(int i = 0 ; i < 20 ; i++){
            if(i > 2 && i < 6){
                rojo.put(i, rojo.get(i)+ 1);
            }
            if(i > 10 && i < 15){
                verde.put(i, verde.get(i)+1);
            }
            if(i > 15 && i < 20){
                azul.put(i, azul.get(i)+1);
            }
        }
        gradiente grad = new gradiente();
        grad.contruizFuzzy(rojo, verde, azul,0.05);*/
    }
    public Double salidaPerceptron(double rojo,double azul,double verde){
        return activacionCuadrado( -pesos.get(0)+pesos.get(1)*rojo+pesos.get(2)*azul+pesos.get(3)*verde );
    }
    public Double errorCometido(List<Color> color,List<Double> valor){
        double error = 0;
        int len = color.size();
        
        double v;
        Color c;
        double rojo,azul,verde;
        Double er;

        for(int i = 0; i < len; i++){
            v = valor.get(i);
            c = color.get(i);
            rojo = fuzzyRojo(c.getRed());
            azul = fuzzyAzul(c.getBlue());
            verde = fuzzyVerde(c.getGreen());
            er = Math.abs( v - salidaPerceptron( rojo,azul,verde ) );
            error += er;
        }
        return error/len;
    }
    public void entrenarPerceptron(List<List<Double>> valores , List<Double> salida,int numeroEntrenamiento,double factorAprendizaje){
        List<Double> pesosPorEpoca;
        int longi = salida.size();
        int numeroColores = pesos.size()-1;
        //System.out.println(pesosPorEpoca);
        double salidaEntrenar;
        double error;
        double errorAcumulado = 0.0;
        for(int i = 0; i < numeroEntrenamiento; i++){
            pesosPorEpoca = new ArrayList<>(pesos);
            errorAcumulado = 0.0;
            for(int l = 0; l < longi;l++){
                salidaEntrenar = -pesosPorEpoca.get(0);
                for(int j = 0; j < numeroColores;j++){
                    salidaEntrenar += pesosPorEpoca.get(j+1)*valores.get(l).get(j);
                    
                }
                //System.out.println(salidaEntrenar);
                salidaEntrenar = activacionCuadrado(salidaEntrenar);
                error = salida.get(l) - salidaEntrenar;
                errorAcumulado = errorAcumulado + Math.abs(error);
                for(int j = 0; j < numeroColores+1;j++){
                    if(j == 0){
                        pesosPorEpoca.set(j, 
                        pesosPorEpoca.get(j) - 1 * error*factorAprendizaje
                        );
                    }else{
                        pesosPorEpoca.set(j, 
                        pesosPorEpoca.get(j) + valores.get(l).get(j-1) * error * factorAprendizaje
                        );
                    }
                    
                }
            }
            pesos = pesosPorEpoca;
            //System.out.println(pesos);
            //System.out.println(errorAcumulado +" error");
        }
        
    }
    private double activacionCuadrado(double x){
        double res = 0.0;
        if(x >= 0.5){
            res = 1.0;
        }
        return res;
    }
    public void contruizFuzzy(HashMap<Integer,Double> rojo,HashMap<Integer,Double> verde , HashMap<Integer,Double> azul,Double porcentajeValido){
        /* la funcion de salida es Rojo * Azul * verde -> Salida */
        trapecioRojo = construirTrapecio(rojo, porcentajeValido);
        trapecioAzul = construirTrapecio(azul, porcentajeValido);
        trapecioVerde = construirTrapecio(verde, porcentajeValido);


        System.out.println(trapecioRojo);
        System.out.println(trapecioAzul);
        System.out.println(trapecioVerde);
    }

    private List<Integer> construirTrapecio(HashMap<Integer,Double> color , Double error){
        List<Integer> trapecio = new ArrayList<>();
        List<Integer> zeros = new ArrayList<>();
        Double totalPesos = 0.0;
        Double valido = 0.0;
        Integer menorZero = 0;
        Integer mayorZero = 255;
        Integer menorUno = -1;
        Integer mayorUno = -1;
        for(double i : color.values()){
            totalPesos += i;
        }
        valido = totalPesos*error;
        //System.out.println("total -> "+ totalPesos+" valido -> "+valido);
        for(int llave : color.keySet()){
            if (color.get(llave) > valido) {
                if(menorUno == -1){
                    menorUno = llave;
                    mayorUno = llave;
                }else{
                    if(menorUno > llave){
                        menorUno = llave;
                    }
                    if(mayorUno < llave){
                        mayorUno = llave;
                    }
                }
            }else if (color.get(llave) == 0.0) {
                zeros.add(llave);
            }
        }
        for(int z : zeros){
            if(menorZero < z && menorUno > z){
                menorZero = z;
            }
            if(mayorZero > z && mayorUno < z){
                mayorZero = z;
            }
        }

        //System.out.println("menor Zero -> "+menorZero);
        //System.out.println("menor Uno -> "+menorUno);
        //System.out.println("mayor Uno -> "+mayorUno);
        //System.out.println("mayor Zero -> "+mayorZero);
        //System.out.println("lista Zeros -> "+zeros);
        trapecio.add(menorZero);
        trapecio.add(menorUno);
        trapecio.add(mayorUno);
        trapecio.add(mayorZero);
        return trapecio;

    }

    public double fuzzyRojo(Integer rojo){
        double resRojo = 0.0;
         if(rojo >= trapecioRojo.get(1) && rojo <= trapecioRojo.get(2)){
            resRojo = 1.0;
        }else if (rojo > trapecioRojo.get(0) && rojo < trapecioRojo.get(1)) {
            resRojo =(rojo - trapecioRojo.get(0))/(trapecioRojo.get(1) - trapecioRojo.get(0));
        }else if(rojo > trapecioRojo.get(2) && rojo < trapecioRojo.get(3)){
            resRojo = 1-((rojo - trapecioRojo.get(2))/(trapecioRojo.get(3) - trapecioRojo.get(2)));
        }
        return resRojo;
    }
    public double fuzzyAzul(Integer azul){
        double resAzul = 0.0;
        if(azul >= trapecioAzul.get(1) && azul <= trapecioAzul.get(2)){
            resAzul = 1.0;
        }else if (azul > trapecioAzul.get(0) && azul < trapecioAzul.get(1)) {
            resAzul =(azul - trapecioAzul.get(0))/(trapecioAzul.get(1) - trapecioAzul.get(0));
        }else if(azul > trapecioAzul.get(2) && azul < trapecioAzul.get(3)){
            resAzul = 1-((azul - trapecioAzul.get(2))/(trapecioAzul.get(3) - trapecioAzul.get(2)));
        }
        return resAzul;
    }

    public double fuzzyVerde(Integer verde){
        double resVerde = 0.0;
        if(verde >= trapecioVerde.get(1) && verde <= trapecioVerde.get(2)){
            resVerde = 1.0;
        }else if (verde > trapecioVerde.get(0) && verde < trapecioVerde.get(1)) {
            resVerde =(verde - trapecioVerde.get(0))/(trapecioVerde.get(1) - trapecioVerde.get(0));
        }else if(verde > trapecioVerde.get(2) && verde < trapecioVerde.get(3)){
            resVerde = 1-((verde - trapecioVerde.get(2))/(trapecioVerde.get(3) - trapecioVerde.get(2)));
        }
        return resVerde;
    }


    public double esColorMulti(Integer rojo, Integer azul,Integer verde){
        double resRojo = 0.0;
        double resAzul = 0.0;
        double resVerde = 0.0;
        if(rojo >= trapecioRojo.get(1) && rojo <= trapecioRojo.get(2)){
            resRojo = 1.0;
        }else if (rojo > trapecioRojo.get(0) && rojo < trapecioRojo.get(1)) {
            resRojo =(rojo - trapecioRojo.get(0))/(trapecioRojo.get(1) - trapecioRojo.get(0));
        }else if(rojo > trapecioRojo.get(2) && rojo < trapecioRojo.get(3)){
            resRojo = 1-((rojo - trapecioRojo.get(2))/(trapecioRojo.get(3) - trapecioRojo.get(2)));
        }
        //========================
        if(azul >= trapecioAzul.get(1) && azul <= trapecioAzul.get(2)){
            resAzul = 1.0;
        }else if (azul > trapecioAzul.get(0) && azul < trapecioAzul.get(1)) {
            resAzul =(azul - trapecioAzul.get(0))/(trapecioAzul.get(1) - trapecioAzul.get(0));
        }else if(azul > trapecioAzul.get(2) && azul < trapecioAzul.get(3)){
            resAzul = 1-((azul - trapecioAzul.get(2))/(trapecioAzul.get(3) - trapecioAzul.get(2)));
        }
        //======================================
        if(verde >= trapecioVerde.get(1) && verde <= trapecioVerde.get(2)){
            resVerde = 1.0;
        }else if (verde > trapecioVerde.get(0) && verde < trapecioVerde.get(1)) {
            resVerde =(verde - trapecioVerde.get(0))/(trapecioVerde.get(1) - trapecioVerde.get(0));
        }else if(verde > trapecioVerde.get(2) && verde < trapecioVerde.get(3)){
            resVerde = 1-((verde - trapecioVerde.get(2))/(trapecioVerde.get(3) - trapecioVerde.get(2)));
        }
        return resAzul*resRojo*resVerde;
    }

    public double esColor(Integer rojo, Integer azul,Integer verde){
        double resRojo = 0.0;
        double resAzul = 0.0;
        double resVerde = 0.0;
        if(rojo >= trapecioRojo.get(1) && rojo <= trapecioRojo.get(2)){
            resRojo = 1.0;
        }else if (rojo > trapecioRojo.get(0) && rojo < trapecioRojo.get(1)) {
            resRojo =(rojo - trapecioRojo.get(0))/(trapecioRojo.get(1) - trapecioRojo.get(0));
        }else if(rojo > trapecioRojo.get(2) && rojo < trapecioRojo.get(3)){
            resRojo = 1-((rojo - trapecioRojo.get(2))/(trapecioRojo.get(3) - trapecioRojo.get(2)));
        }
        //========================
        if(azul >= trapecioAzul.get(1) && azul <= trapecioAzul.get(2)){
            resAzul = 1.0;
        }else if (azul > trapecioAzul.get(0) && azul < trapecioAzul.get(1)) {
            resAzul =(azul - trapecioAzul.get(0))/(trapecioAzul.get(1) - trapecioAzul.get(0));
        }else if(azul > trapecioAzul.get(2) && azul < trapecioAzul.get(3)){
            resAzul = 1-((azul - trapecioAzul.get(2))/(trapecioAzul.get(3) - trapecioAzul.get(2)));
        }
        //======================================
        if(verde >= trapecioVerde.get(1) && verde <= trapecioVerde.get(2)){
            resVerde = 1.0;
        }else if (verde > trapecioVerde.get(0) && verde < trapecioVerde.get(1)) {
            resVerde =(verde - trapecioVerde.get(0))/(trapecioVerde.get(1) - trapecioVerde.get(0));
        }else if(verde > trapecioVerde.get(2) && verde < trapecioVerde.get(3)){
            resVerde = 1-((verde - trapecioVerde.get(2))/(trapecioVerde.get(3) - trapecioVerde.get(2)));
        }
        return salidaPerceptron(resRojo, resAzul, resVerde);
    }





    /*
    public void entrenar(List<Double> entradas , List<Double> salida){
        
         * La idea es hacerlo por bach
         
        int longi = entradas.size();
        double sup = 0;
        double inf = 0;
        double p;
        double error;
        double factorAprendizaje = 0.1;
        for(int i = 0; i < longi; i++){
            sup += entradas.get(i)*salida.get(i);
            inf += salida.get(i);
        }
        media = sup/inf;
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < longi; j++){
                p = entradas.get(j) - media;
                error = Math.abs(gaus(p) - salida.get(j));
                error = error - 0.1;
                desviacion = desviacion - error*desviacion;

                

            }
        }
        for(double k: entradas){
            p = k- media;
            System.out.println("entrada -> "+k+" salida -> "+gaus(p));
        }
    }
    public double gaus(double  p){
        return Math.exp( -Math.pow((p), 2)/desviacion );
    }
    public double DGous(double p){
        return -(2*p/desviacion)*gaus(p);
    }*/
}
