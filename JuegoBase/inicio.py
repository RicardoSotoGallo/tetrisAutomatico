from random import random,choice
from math import cos , sin , pi
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
        self.actualizarPieza()
        #self.bucleJugable()
        self.bucleJugableConFichero(40,False)
    
    def bucleJugableConFichero(self,iteraciones, teclado):
        
            
        anteriorValor = 0
        accion        = True
        iter = 0
        while(self.activo and iteraciones >= iter):
            accion        = True
            #self.escanearJuego()
            self.dibujarJuego()
            self.dibujarJuegoFichero()
            #self.dibujarJuegoFicheroComprimido()
            self.dibujarJuegoFicheroTodo()
            print(f"Iteracion numero {iter}")
            self.dibujarJuegoFicheroTodoNumero(iter)
            iter += 1
            subprocess.run(["java","--enable-preview","tetrisJava/src/calcularGrafoVirtual.java"])


            print("f (final) a (giro derecha) d (giro izquierda) w (vuleta) s (bajar) numero (posicion)")
            with open("ficherosComunos/resultadoHeuristico.richi") as archivo:
                comando = archivo.read()

            texto = comando.replace("\n","").replace("[","").replace("]","").split(",")
            for t in texto:
                t = t.strip()
                if t == "F":
                    print("fin del juego")
                    self.activo = False
                elif t == "D":
                    anteriorValor      = self.listaPosicionesFichas
                    self.listaPosicionesFichas = self.girarPieza(pi/2)
                    if not self.esValida():
                        self.listaPosicionesFichas = anteriorValor
                        accion = False
                    
                elif t == "A":
                    anteriorValor      = self.listaPosicionesFichas
                    self.listaPosicionesFichas = self.girarPieza( - pi/2)
                    if not self.esValida():
                        self.listaPosicionesFichas = anteriorValor
                        accion = False
                    
                elif t == "W":
                    anteriorValor      = self.listaPosicionesFichas
                    self.listaPosicionesFichas = self.girarPieza( pi)
                    if not self.esValida():
                        self.listaPosicionesFichas = anteriorValor
                        accion = False
                
                elif t == "E":
                        anteriorValor       = self.posicionXPieza
                        self.posicionXPieza = anteriorValor + 1
                        if not self.esValida():
                            self.posicionXPieza = anteriorValor
                            accion             = False
                            print("maximo alcanzado")
                elif t == "Q":
                    anteriorValor       = self.posicionXPieza
                    self.posicionXPieza = anteriorValor - 1
                    if not self.esValida():
                        self.posicionXPieza = anteriorValor
                        accion             = False
                        print("mino alcanzado")
                
                elif t == "S":
                        self.bajarPieza()
                        self.actualizarPieza()
                        accion = False
                elif t.isdigit():
                    self.posicionXPieza = int(t)
                    if not self.esValida():
                        self.posicionXPieza = anteriorValor
                        accion             = False
                        print("posicion incorrecta")
                else:
                    print("Operacion no valida")
                if accion:
                    self.actualizarPosicionPieza()
                    self.comprobarCompleto()

    def bucleJugable(self):
        anteriorValor = 0
        accion        = True
        while(self.activo):
            accion        = True
            #self.escanearJuego()
            self.dibujarJuego()
            self.dibujarJuegoFichero()
            #self.dibujarJuegoFicheroComprimido()
            self.dibujarJuegoFicheroTodo()
            print("f (final) a (giro derecha) d (giro izquierda) w (vuleta) s (bajar) numero (posicion)")
            texto = input("").replace("\n","")
            if texto == "f":
                print("fin del juego")
                self.activo = False
            elif texto == "d":
                anteriorValor      = self.listaPosicionesFichas
                self.listaPosicionesFichas = self.girarPieza(pi/2)
                if not self.esValida():
                    self.listaPosicionesFichas = anteriorValor
                    accion = False
                
            elif texto == "a":
                anteriorValor      = self.listaPosicionesFichas
                self.listaPosicionesFichas = self.girarPieza( - pi/2)
                if not self.esValida():
                    self.listaPosicionesFichas = anteriorValor
                    accion = False
                
            elif texto == "w":
                anteriorValor      = self.listaPosicionesFichas
                self.listaPosicionesFichas = self.girarPieza( pi)
                if not self.esValida():
                    self.listaPosicionesFichas = anteriorValor
                    accion = False
            
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
            
            elif texto == "s":
                    self.bajarPieza()
                    self.actualizarPieza()
                    accion = False
            
            else:
                print("Operacion no valida")
            if accion:
                self.actualizarPosicionPieza()
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
        
        self.listaPosicionesFichas = self.listafichas.devolverPiezaSinPosicion(self.tipoPieza)
        
        self.listaPosicionesFichas = self.girarPieza( pi / 2 * self.rotacionPieza )
        for x,y in self.listaPosicionesFichas:
            self.casillasDict[ (x + self.posicionXPieza , y +self.posicionYPieza) ].tipo = "Pieza"
        #print(f"tipo de pieza -> {self.tipoPieza}")
    
    def actualizarPosicionPieza(self):
        for i in self.casillasDict.keys():
            if self.casillasDict[i].tipo == "Pieza":
                self.casillasDict[i].tipo = "Vacio"
        
        for x,y in self.listaPosicionesFichas:
            self.casillasDict[ (x + self.posicionXPieza , y +self.posicionYPieza) ].tipo = "Pieza"

    def esValida(self):
        res = True
        for x , y in self.listaPosicionesFichas:
            if x + self.posicionXPieza >= self.ancho or x + self.posicionXPieza < 0:
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

    def dibujarJuegoFicheroComprimido(self):
        texto = ""
        textoAFichero = ["x\n"]
        self.escanearJuego()
        textoAFichero.append(f"base:{self.base}\n")
        #======================================================
        posAux = self.listaPosicionesFichas
        abajo = [-1 for i in range(0,4)]
        arriba = [-1 for i in range(0,4)]
        for x,y in posAux:
            if abajo[x] == -1 or abajo[x] - 1 < y:
                abajo[x] = y+1
            if arriba[x] == -1 or arriba[x] > y:
                arriba[x] = y
        textoAFichero.append(f"0:{abajo}|{arriba}\n")
        #======================================================
        posAux = self.girarPieza(pi/2)
        abajo = [-1 for i in range(0,4)]
        arriba = [-1 for i in range(0,4)]
        for x,y in posAux:
            if abajo[x] == -1 or abajo[x] - 1 < y:
                abajo[x] = y+1
            if arriba[x] == -1 or arriba[x] > y:
                arriba[x] = y
        textoAFichero.append(f"1:{abajo}|{arriba}\n")
        #======================================================
        posAux = self.girarPieza(pi)
        abajo = [-1 for i in range(0,4)]
        arriba = [-1 for i in range(0,4)]
        for x,y in posAux:
            if abajo[x] == -1 or abajo[x] - 1 < y:
                abajo[x] = y+1
            if arriba[x] == -1 or arriba[x] > y:
                arriba[x] = y
        textoAFichero.append(f"2:{abajo}|{arriba}\n")
        #======================================================
        posAux = self.girarPieza(-pi/2)
        abajo = [-1 for i in range(0,4)]
        arriba = [-1 for i in range(0,4)]
        for x,y in posAux:
            if abajo[x] == -1 or abajo[x] - 1 < y:
                abajo[x] = y+1
            if arriba[x] == -1 or arriba[x] > y:
                arriba[x] = y
        textoAFichero.append(f"3:{abajo}|{arriba}\n")


        textoAFichero.append(f"p:{self.posicionXPieza}|{self.posicionYPieza}\n")
        with open("ficherosComunos/estadoJuegoSimple.richi","w") as archivo:
            for i in textoAFichero:
                archivo.write(i)

    def dibujarJuegoFicheroTodo(self):
        texto = ""
        for x in range(0,self.ancho):
            for y in range(0, self.alto):
                texto += f"{x}:{y}:{self.casillasDict[(x,y)].tipo}\n"
        texto +="=====\n"
        texto += str( self.listaPosicionesFichas)+"\n"
        texto +="=====\n"
        texto += str(self.girarPieza(pi/2))+"\n"
        texto +="=====\n"
        texto += str( self.girarPieza(pi))+"\n"
        texto +="=====\n"
        texto += str(self.girarPieza(-pi/2))+"\n"
        with open("ficherosComunos/estadoJuegoDic.richi","w") as fichero:
            fichero.write(texto)


    def dibujarJuegoFicheroTodoNumero(self , n):
        texto = ""
        for x in range(0,self.ancho):
            for y in range(0, self.alto):
                texto += f"{x}:{y}:{self.casillasDict[(x,y)].tipo}\n"
        texto +="=====\n"
        maxY = 0
        meter = self.listaPosicionesFichas
        
        texto += str(meter)+"\n"

        texto +="=====\n"#self.girarPieza(pi) 
        maxY = 0
        mete = self.girarPieza(pi/2)
        
        texto += str(meter)+"\n"

        texto +="=====\n"
        maxY = 0
        mete = self.girarPieza(pi)
        
        texto += str(meter)+"\n"
        texto +="=====\n"#self.girarPieza(-pi/2)
        maxY = 0
        meter = self.girarPieza(-pi/2)
        
        texto += str(meter)+"\n"
        with open(f"ficherosComunos/estadoJuegoDic{n}.richi","w") as fichero:
            fichero.write(texto)
    
    def escanearJuego(self):
        self.base = [self.alto for i in range(0,self.ancho)] # la idea es que el primer objeto ponemos la altura
        self.listaPosicionesFichas = [] #Aqui vamos a meter todas las posciones
        for j in range(0, self.alto):
            for i in range(0 , self.ancho):
                match self.casillasDict[(i,j)].tipo:
                    case "Vacio":
                        pass
                    case "Casilla":
                        if self.base[i] == self.alto:
                            self.base[i] = j
                    case "Pieza":
                        self.listaPosicionesFichas.append((i - self.posicionXPieza,j - self.posicionYPieza))
                    case _:
                        print("error en escaneo")
        
        #print(f"base -> {self.base}")
        #print(f"lista fichero -> {self.listaPosicionesFichas}")

    def girarPieza(self , angulo):
        nuevaPieza = self.listaPosicionesFichas
        if angulo != 0:
            nuevaPieza = []
            for x,y in self.listaPosicionesFichas:
                nuevaPieza.append( ( int(round(x*cos(angulo) - y *sin (angulo) , 0  )), int(round(x * sin(angulo) + y * cos(angulo) , 0)) ) )
            moverX = 0
            moverY = 0
            for x,y in nuevaPieza:
                if moverX > x:
                    moverX = x
                if moverY > y:
                    moverY = y
            if moverX != 0 or moverY != 0:
                aux = []
                for x,y in nuevaPieza:
                    aux.append((x - moverX , y - moverY))
                nuevaPieza = aux
        #print(f"nuevo posicion -> {nuevaPieza}")
        return nuevaPieza

    def recibirHeuristica(self):
        res = ""
        with open("ficherosComunos/resultadoHeuristico.richi","r") as archivo:
            res += archivo.readline()
        res = res.replace("[","").replace("]","").replace(",","")
        return res.lower()
            
    


juegoActual = juego(10,20)


