fichero = "JuegoBase/piezas.txt"
alturaMaxima = 4
class conjuntoDePiezas():
    diccionarioPiezas:'dict[str,list[(int,int)]]'
    def __init__(self):
        contarLinea = 0
        nombreFichaActual = ""
        self.diccionarioPiezas = {}
        with open(fichero,"r",encoding="utf-8") as file:
            for linea in file:
                if contarLinea == 0:
                    nombreFichaActual = linea.replace('\n','')
                    self.diccionarioPiezas[nombreFichaActual] = []
                else:
                    spliteo = linea.replace('\n','').split("|")
                    spliteoNumero = list( map(lambda i : pasarNumero(i), spliteo) )
                    self.diccionarioPiezas[nombreFichaActual].append(pasarAPosicion(spliteoNumero))
                contarLinea = (contarLinea + 1)%5
    def devolverPiezaPosiciones(self,tipo:"str",giro:'int',x:'int',y:'int'):
        return list(
            map(lambda i : (i[0]+x,i[1]+y) , self.diccionarioPiezas[tipo][giro])
        )


def pasarNumero(texto):
    res = []
    for i in list(texto):
        res.append(int(i))
    return res

def pasarAPosicion(datos:'(list[int],list[int])'): # type: ignore
    res = []
    maximo = len(datos[0])
    for i in range(0,maximo):
        for j in range(0,maximo):
            if datos[0][j] > i and datos[1][j] <= i:
                res.append((j,i)) 
                #print(f"1 -> {datos[0][j]}  2 -> {datos[1][j]}")
    return res

#c = conjuntoDePiezas()

#print(c.diccionarioPiezas)

#print(c.devolverPiezaPosiciones('palo',0,1,1))
