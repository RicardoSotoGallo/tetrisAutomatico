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
                    spliteo = linea.replace('\n','').split(";")
                    for posicion in spliteo:
                        x,y = posicion.replace("(","").replace(")","").split(",")
                        x = int(x)
                        y = int(y)
                        self.diccionarioPiezas[nombreFichaActual].append((x,y))
                contarLinea = (contarLinea + 1)%2
    def devolverPiezaPosiciones(self,tipo:"str",x:'int',y:'int'):
        return list(
            map(lambda i : (i[0]+x,i[1]+y) , self.diccionarioPiezas[tipo])
        )
    def devolverPiezaSinPosicion(self,tipo:'str'):
        return self.diccionarioPiezas[tipo]

