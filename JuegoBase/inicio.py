from random import random,choice
import subprocess
from pieza import conjuntoDePiezas
class hueco():
    tipo = None
    def __init__(self,tipo):
        self.tipo = tipo

class juego():
    reset    = "\033[0m"
    blanco   = "\033[37m"
    verde    = "\033[32m"
    margenta = "\033[35m"
    rojo     = "\033[31m"
    pared   = f"{verde}M{reset}"   # ; de margen
    vacio   = f" "                 # _ de vacio
    casilla = f"{margenta}C{reset}"# C de casilla
    pieza   = f"{rojo}P{reset}"    # P de pieza
    listafichas:'conjuntoDePiezas' = None
    casillasDict:'dict[(int,int),hueco]'
    alto = 0
    ancho = 0
    posicionXPieza = 0
    posicionYPieza = 0
    rotacionPieza  = 0
    tipoPieza      = ""
    activo = True
    def __init__(self,ancho:'int' , alto:'int'):
        
        self.casillasDict = {}
        self.ancho = ancho
        self.alto = alto
        self.listafichas = conjuntoDePiezas()
        self.cambiarPieza()
        self.activo         = True
        for i in range(0,ancho):
            for j in range(0,alto):
                self.casillasDict[(i,j)] = hueco("Vacio")
                #if j > 10:
                #    if (i+j)%2 == 0:
                #        self.casillasDict[(i,j)] = hueco("Casilla")
                #    else:
                #        self.casillasDict[(i,j)] = hueco("Vacio")
                #else:
                #    self.casillasDict[(i,j)] = hueco("Vacio")
        self.actualizarPieza()
        self.bucleJugableConFichero(40)
    
    def bucleJugableConFichero(self,iteraciones):
        anteriorValor = 0
        accion        = True
        for i in range(0,iteraciones):
            accion        = True
            self.dibujarJuego()
            self.dibujarJuegoFichero()
            subprocess.run(["java","--enable-preview","tetrisJava/src/calcularGrafoVirtual.java"])
            accion = self.recibirHeuristica()+"s"
            a = input()
            print(f"accion -> {accion}")
            for texto in accion:
                self.dibujarJuego()
            #texto = input("").replace("\n","")
                if texto == "f":
                    print("fin del juego")
                    self.activo = False
                elif texto == "d":
                    anteriorValor      = self.rotacionPieza
                    self.rotacionPieza = (self.rotacionPieza+1)%4
                    if not self.esValida():
                        self.rotacionPieza = anteriorValor
                        accion             = False
                elif texto == "a":
                    anteriorValor      = self.rotacionPieza
                    self.rotacionPieza = (self.rotacionPieza-1)%4
                    if not self.esValida():
                        self.rotacionPieza = anteriorValor
                        accion             = False
                elif texto == "w":
                    anteriorValor      = self.rotacionPieza
                    self.rotacionPieza = (self.rotacionPieza+2)%4
                    if not self.esValida():
                        self.rotacionPieza = anteriorValor
                        accion             = False
                elif texto == "s":
                    self.bajarPieza()
                elif texto.isdigit():
                    anteriorValor       = self.posicionXPieza
                    self.posicionXPieza = int(texto)
                    if not self.esValida():
                        self.posicionXPieza = anteriorValor
                        accion             = False
                elif texto == "e":
                    anteriorValor       = self.posicionXPieza
                    self.posicionXPieza = anteriorValor + 1
                    if not self.esValida():
                        self.posicionXPieza = anteriorValor
                        accion             = False
                        print("maximo alcanzado")
                elif texto == "q":
                    anteriorValor       = self.posicionXPieza
                    self.posicionXPieza = anteriorValor - 1
                    if not self.esValida():
                        self.posicionXPieza = anteriorValor
                        accion             = False
                        print("mino alcanzado")
                else:
                    print("Operacion no valida")
                if accion:
                    self.actualizarPieza()
                    self.comprobarCompleto()



    def bucleJugable(self):
        anteriorValor = 0
        accion        = True
        while(self.activo):
            accion        = True
            self.dibujarJuego()
            self.dibujarJuegoFichero()
            subprocess.run(["java","--enable-preview","tetrisJava/src/calcularGrafoVirtual.java"])
            #accion = self.recibirHeuristica()
            #print(f"accion -> {accion}")

            print("f (final) a (giro derecha) d (giro izquierda) w (vuleta) s (bajar) numero (posicion)")
            texto = input("").replace("\n","")
            if texto == "f":
                print("fin del juego")
                self.activo = False
            elif texto == "d":
                anteriorValor      = self.rotacionPieza
                self.rotacionPieza = (self.rotacionPieza+1)%4
                if not self.esValida():
                    self.rotacionPieza = anteriorValor
                    accion             = False
            elif texto == "a":
                anteriorValor      = self.rotacionPieza
                self.rotacionPieza = (self.rotacionPieza-1)%4
                if not self.esValida():
                    self.rotacionPieza = anteriorValor
                    accion             = False
            elif texto == "w":
                anteriorValor      = self.rotacionPieza
                self.rotacionPieza = (self.rotacionPieza+2)%4
                if not self.esValida():
                    self.rotacionPieza = anteriorValor
                    accion             = False
            elif texto == "s":
                self.bajarPieza()
            elif texto.isdigit():
                anteriorValor       = self.posicionXPieza
                self.posicionXPieza = int(texto)
                if not self.esValida():
                    self.posicionXPieza = anteriorValor
                    accion             = False
            elif texto == "e":
                anteriorValor       = self.posicionXPieza
                self.posicionXPieza = anteriorValor + 1
                if not self.esValida():
                    self.posicionXPieza = anteriorValor
                    accion             = False
                    print("maximo alcanzado")
            elif texto == "q":
                anteriorValor       = self.posicionXPieza
                self.posicionXPieza = anteriorValor - 1
                if not self.esValida():
                    self.posicionXPieza = anteriorValor
                    accion             = False
                    print("mino alcanzado")
            else:
                print("Operacion no valida")
            if accion:
                self.actualizarPieza()
                self.comprobarCompleto()

    def cambiarPieza(self):
        self.posicionXPieza = choice(list(range(2,7)))
        self.posicionYPieza = 0
        self.rotacionPieza  = choice(list(range(0,4)))
        self.tipoPieza      = choice(list( self.listafichas.diccionarioPiezas.keys())) #"te"
    
    def bajarLinea(self,altura):
        for i in range(altura , 0 , -1):
            for j in range(0,self.ancho):
                if self.casillasDict[(j,i)].tipo != "Pieza":
                    if self.casillasDict[(j,i - 1)].tipo == "Pieza":
                        self.casillasDict[(j,i)].tipo = "Vacio"
                    else:
                        self.casillasDict[(j,i)].tipo = self.casillasDict[(j,i-1)].tipo

    def comprobarCompleto(self):
        alturasQuitar = []
        for py in range(0,self.alto):
            sumaCasilla = 0
            for px in range(0,self.ancho):
                if self.casillasDict[(px,py)].tipo == "Casilla":
                    sumaCasilla += 1
            if sumaCasilla >= self.ancho:
                alturasQuitar.append(py)
        for py in alturasQuitar:
            self.bajarLinea(py)

    def bajarPieza(self):
        piezaTablero    = list( map( lambda i : 0         , range(0,self.ancho) ) )
        ocupadasTablero = list( map( lambda i : self.alto , range(0,self.ancho) ) )
        cantidadQuebajo = 100
        piezaPosi = [] #para no tener que buscarla de nuevo
        for posiciones in self.casillasDict.keys():
            if self.casillasDict[posiciones].tipo == "Pieza":
                piezaPosi.append( posiciones)
                if  piezaTablero[posiciones[0]] < posiciones[1] + 1:
                    piezaTablero[posiciones[0]] = posiciones[1] + 1
            elif self.casillasDict[posiciones].tipo == "Casilla":
                if  ocupadasTablero[posiciones[0]] > posiciones[1]:
                    ocupadasTablero[posiciones[0]] = posiciones[1]
        for piezaY,ocupadoY in zip(piezaTablero,ocupadasTablero):
            aux = abs(piezaY - ocupadoY)
            if aux < cantidadQuebajo and piezaY != 0:
                cantidadQuebajo = aux
        for nuevaOcu in piezaPosi:
            self.casillasDict[(nuevaOcu[0], nuevaOcu[1] + cantidadQuebajo) ].tipo = "Casilla"
        self.cambiarPieza()
         
    def actualizarPieza(self):
        for i in self.casillasDict.keys():
            if self.casillasDict[i].tipo == "Pieza":
                self.casillasDict[i].tipo = "Vacio"
        for i in self.listafichas.devolverPiezaPosiciones(
            self.tipoPieza,
            self.rotacionPieza,
            self.posicionXPieza,
            self.posicionYPieza
            ):
            self.casillasDict[i].tipo = "Pieza"

    def esValida(self):
        res = True
        for i in self.listafichas.devolverPiezaPosiciones(
            self.tipoPieza,
            self.rotacionPieza,
            self.posicionXPieza,
            self.posicionYPieza
            ):
            if i in self.casillasDict.keys():
                if self.casillasDict[i] == "Casilla":
                    res = False
            else:
                res = False
        return res
    
    def dibujarJuego(self):
        texto:str = ""
        for j in range(-1 , self.alto + 1):
            for i in range(-1 , self.ancho + 1):
                #texto += self.pared
                if(i < 0 or i >= self.ancho):
                    texto += self.pared
                elif(j < 0 or j >= self.alto):
                    texto += self.pared
                else:
                    match self.casillasDict[(i,j)].tipo:
                        case "Vacio":
                            texto += self.vacio
                        case "Casilla":
                            texto += self.casilla
                        case "Pieza":
                            texto += self.pieza
                        case _:
                            texto += "E" #esto es error

            print(texto,end='\n')
            texto = ""
        print(" 0123456789")
    
    def dibujarJuegoFichero(self):
        texto:str = ""
        textoAFichero = ["x\n"]
        for j in range(-1 , self.alto + 1):
            for i in range(-1 , self.ancho + 1):
                #texto += self.pared
                if(i < 0 or i >= self.ancho):
                    texto += "M"
                elif(j < 0 or j >= self.alto):
                    texto += "M"
                else:
                    match self.casillasDict[(i,j)].tipo:
                        case "Vacio":
                            texto += "V"
                        case "Casilla":
                            texto += "C"
                        case "Pieza":
                            texto += "P"
                        case _:
                            texto += "E" #esto es error

            #print(texto,end='\n')
            textoAFichero.append(texto + "\n")
            texto = ""
        with open("ficherosComunos/estadoJuego.richi","w") as archivo:
            for i in textoAFichero:
                archivo.write(i)
        ##print(" 0123456789")

    def recibirHeuristica(self):
        res = ""
        with open("ficherosComunos/resultadoHeuristico.richi","r") as archivo:
            res += archivo.readline()
        res = res.replace("[","").replace("]","").replace(",","")
        return res.lower()
            

    


juegoActual = juego(10,20)
