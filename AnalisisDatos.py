import matplotlib.pyplot as plt
import math

class FilaDatosHeuristica:
    def __init__(self, numeroIteracionMaxima, tipoDePartida, tipoFinal,
                 tiempos, numeroPiezas, listaDePiezasSacadas,
                 movimientoLs, girosLs):

        self.numeroIteracionMaxima = numeroIteracionMaxima
        self.tipoDePartida = tipoDePartida
        self.tipoFinal = tipoFinal
        self.tiempos = tiempos
        self.numeroPiezas = numeroPiezas
        self.listaDePiezasSacadas = listaDePiezasSacadas
        self.movimientoLs = movimientoLs
        self.girosLs = girosLs

    def __repr__(self):
        return f"FilaDatos(iter={self.numeroIteracionMaxima}, piezas={len(self.listaDePiezasSacadas)})"

class FilaDatosRefuerzo:
    def __init__(self, numeroIteraciones, numeroJuegosEntrenamiento,
                 tipoDePartida, movimientoLs, girosLs,
                 numeroPiezas, tiempos):

        self.numeroIteraciones = numeroIteraciones
        self.numeroJuegosEntrenamiento = numeroJuegosEntrenamiento
        self.tipoDePartida = tipoDePartida
        self.movimientoLs = movimientoLs
        self.girosLs = girosLs
        self.numeroPiezas = numeroPiezas
        self.tiempos = tiempos

    def __repr__(self):
        return f"FilaEntrenamiento(iter={self.numeroIteraciones}, juegos={self.numeroJuegosEntrenamiento})"
    
class tiemposHiperParametros:
    pasos: "list[float]"
    constate:str = ""
    def __init__(self , constate:str):
        self.pasos = []
        self.constate = constate
    def addPaso(self,valor:float):
        self.pasos.append(valor)


def leer_fichero_Heuristica(ruta:str):
    datos = []

    with open(ruta, 'r', encoding='utf-8') as f:
        lineas = f.readlines()

    # Saltamos la cabecera
    for linea in lineas[1:]:
        partes = linea.strip().split(';')

        numeroIteracionMaxima = int(partes[0])
        tipoDePartida = partes[1]
        tipoFinal = partes[2]

        # Función auxiliar para listas
        def parse_lista(cadena, tipo):
            return [tipo(x.strip()) for x in cadena.split(',')]

        tiempos = parse_lista(partes[3], float)
        numeroPiezas = parse_lista(partes[4], int)
        listaDePiezasSacadas = [x.strip() for x in partes[5].split(',')]
        movimientoLs = parse_lista(partes[6], int)
        girosLs = parse_lista(partes[7], int)

        fila = FilaDatosHeuristica(
            numeroIteracionMaxima,
            tipoDePartida,
            tipoFinal,
            tiempos,
            numeroPiezas,
            listaDePiezasSacadas,
            movimientoLs,
            girosLs
        )

        datos.append(fila)

    return datos

def leer_fichero_Refuerzo(ruta:str):
    datos:"list[FilaDatosRefuerzo]" = []

    with open(ruta, 'r', encoding='utf-8') as f:
        lineas = f.readlines()

    # Saltar cabecera
    for linea in lineas[1:]:
        partes = linea.strip().split(';')

        numeroIteraciones = float(partes[0])
        numeroJuegosEntrenamiento = int(partes[1])
        tipoDePartida = partes[2]

        def parse_lista(cadena, tipo):
            return [tipo(x.strip()) for x in cadena.split(',')]

        movimientoLs = parse_lista(partes[3], int)
        girosLs = parse_lista(partes[4], int)
        numeroPiezas = parse_lista(partes[5], int)
        tiempos = parse_lista(partes[6], float)

        fila:"FilaDatosRefuerzo" = FilaDatosRefuerzo(
            numeroIteraciones,
            numeroJuegosEntrenamiento,
            tipoDePartida,
            movimientoLs,
            girosLs,
            numeroPiezas,
            tiempos
        )

        datos.append(fila)

    return datos

def recopilarPasos(ruta:str):
    primero:bool = True
    hiperLs:list[tiemposHiperParametros] = []
    with open(ruta, 'r', encoding='utf-8') as f:
        lineas = f.readlines()
        for linea in lineas:
            if primero:
                primero = False
                partes = linea.strip().split(";")
                for titulo in partes[1:]:
                    hiperLs.append( tiemposHiperParametros(titulo) )
                
            else:
                partes = linea.strip().split(";")
                indice = 0
                for valor in partes[1:]:
                    hiperLs[indice].addPaso( float(valor) )
                    indice+=1
        #print([c.constate for c in hiperLs])
        #print( hiperLs[0].pasos )
    return hiperLs

def iteracionesSegunPiezaHeuristica():
    diccHeu = {}
    for i in range(-1,7):
        
        dato = leer_fichero_Heuristica(f"tetrisJava\\salidaTestFactores\\Heuristica_10_6_{i}.txt")
        diccHeu[i] = sum( s.numeroIteracionMaxima for s in dato ) / len(dato) if dato else 0
    
    print(diccHeu)
    x = [traducirNumeroPieza(s) for s in diccHeu.keys()]
    y = list(diccHeu.values())
    plt.bar(x,y)
    plt.xlabel("Piezas")
    plt.ylabel("Numero de iteracuines")
    plt.title("Numero de iteraciones por piezas")
    plt.show()

def tiemposSegunPiezaHeuristica():
    diccHeu = {}
    for i in range(-1,7):
        
        dato = leer_fichero_Heuristica(f"tetrisJava\\salidaTestFactores\\Heuristica_10_6_{i}.txt")
        diccHeu[i] = sum( sum(s.tiempos)/len(s.tiempos) for s in dato ) / len(dato) if dato else 0
    
    print(diccHeu)
    x = [traducirNumeroPieza(s) for s in diccHeu.keys()]
    y = list(diccHeu.values())
    plt.bar(x,y)
    plt.xlabel("Piezas")
    plt.ylabel("Tiempo Mili Segundos")
    plt.title("Tiempo de decision en Mili segundos")
    plt.show()

def refuerzoEntrenamientoPorIteracionUnica(pieza:"int", titulo:"str",color:str):
    datos:"list[FilaDatosRefuerzo]" = leer_fichero_Refuerzo(f"tetrisJava\\salidaTestFactores\\Refuerzo10_6_{pieza}.txt")
    y = [s.numeroIteraciones for s in datos]
    x = [ s.numeroJuegosEntrenamiento/1000 for s in datos]
    
    # Crear la gráfica
    plt.plot(x, y, marker='o', label=titulo,color=color)  # 'o' para mostrar los puntos

def refuerzoEntrenamientoPorIteracion():
    colors = [
    'red',
    'blue',
    'green',
    'orange',
    'purple',
    'brown',
    'black',
    'cyan'
    ]
    for i in range(0,7):
        refuerzoEntrenamientoPorIteracionUnica(i,traducirNumeroPieza(i),colors[i])
    plt.xlabel("numero de juegos/1000")
    plt.ylabel("Numero de acciones hasta perder")
    plt.title("Grafica de iteraciones")
    plt.legend()
    plt.grid(True)
    plt.show()

def refuerzoEntrenamientoPorIteracionUnicaDecision(pieza:"int", titulo:"str",color:str):
    
    print(f"tetrisJava\\salidaTestFactores\\RefuerzoDecision_{titulo}_10_6_{pieza}.txt")
    datos:"list[FilaDatosRefuerzo]" = leer_fichero_Refuerzo(f"tetrisJava\\salidaTestFactores\\RefuerzoDecision_{titulo}_10_6_{pieza}.txt")
    y = [s.numeroIteraciones for s in datos]
    x = [ s.numeroJuegosEntrenamiento/1000 for s in datos]
    
    # Crear la gráfica
    plt.plot(x, y, marker='o', label=titulo,color=color)  # 'o' para mostrar los puntos


def refuerzoEntrenamientoPorIteracionDecisionUnico(titulo:"str"):
    
    print(f"tetrisJava\\salidaTestFactores\\{titulo}.txt")
    datos:"list[FilaDatosRefuerzo]" = leer_fichero_Refuerzo(f"tetrisJava\\salidaTestFactores\\{titulo}.txt")
    y = [s.numeroIteraciones for s in datos]
    x = [ s.numeroJuegosEntrenamiento/1000 for s in datos]
    
    # Crear la gráfica
    plt.plot(x, y, marker='o', label=titulo)  # 'o' para mostrar los puntos

def refuerzoEntrenamientoPorIteracionDecision(pieza:"int"):
    colors = [
    'red',
    'blue',
    'green',
    'orange',
    'purple',
    'brown',
    'black',
    'cyan'
    ]
    for i in range(0,5):
        #if( i != 3 and i !=  0 and i != 6):
        print(traducirNumeroBusqueda(i))
        refuerzoEntrenamientoPorIteracionUnicaDecision(pieza,traducirNumeroBusqueda(i),colors[i])
    plt.xlabel("numero de juegos/1000")
    plt.ylabel("Numero de acciones hasta perder")
    plt.title("Grafica de iteraciones")
    plt.legend()
    plt.grid(True)
    plt.show()

def refuerzoEntrenamientoPorPuntuacion(lsNombres:"list[str]"):
    for nombre in lsNombres:
        #if( i != 3 and i !=  0 and i != 6):
        print(nombre)
        refuerzoEntrenamientoPorIteracionDecisionUnico(nombre)
    plt.xlabel("numero de juegos/1000")
    plt.ylabel("Numero de acciones hasta perder")
    plt.title("Grafica de iteraciones")
    plt.legend()
    plt.grid(True)
    plt.show()

def HiperParametroUnico(HiperParametro:"tiemposHiperParametros"):
    #print(f"tetrisJava\\salidaTestFactores\\RefuerzoDecision_{titulo}_10_6_{pieza}.txt")
    #datos:"list[FilaDatosRefuerzo]" = leer_fichero_Refuerzo(f"tetrisJava\\salidaTestFactores\\RefuerzoDecision_{titulo}_10_6_{pieza}.txt")
    y = [s for s in HiperParametro.pasos]
    x = [ s for s in range(0 , len(HiperParametro.pasos))]
    
    # Crear la gráfica
    plt.plot(x, y, marker='o', label=HiperParametro.constate)  # 'o' para mostrar los puntos

def HiperParametroVarios(dire:str):
    hiperLs = recopilarPasos(dire)
    contador = 0
    for h in hiperLs:
        if contador == 0:
            HiperParametroUnico(h)
        contador = (contador+1)%3
    plt.xlabel("Epocas")
    plt.ylabel("Numero de acciones hasta perder")
    plt.title("Grafica de iteraciones")
    plt.legend()
    plt.grid(True)
    plt.show()

def traducirNumeroPieza(numero:"int"):
    res = "No Encontrado"
    if numero == -1:
        res = "Pieza_Azar"
    elif numero == 0:
        res = "palo"
    elif numero == 1:
        res = "eleInvertida"
    elif numero == 2:
        res = "ele"
    elif numero == 3:
        res = "cuadrado"
    elif numero == 4:
        res = "gusano"
    elif numero == 5:
        res = "te"
    elif numero == 6:
        res = "gusanoInvertido"
    return res

def traducirNumeroBusqueda(numero:"int"):
    res = "No Encontrado"
    if numero == 0:
        res = "Azar"
    elif numero == 1:
        res = "PrimeroCeros"
    elif numero == 2:
        res = "EGreedy"
    elif numero == 3:
        res = "ModificarCte"
    elif numero == 4:
        res = "PorVistas"
    return res


lsNombres = [
    "SoloPasos",
    "SoloHuecos",
    "HuecosPremiosYCastigos",
    "SinContarElUltimoPaso"
]

refuerzoEntrenamientoPorPuntuacion(lsNombres)

#refuerzoEntrenamientoPorIteracion()
#refuerzoEntrenamientoPorIteracionDecision(-1)
#iteracionesSegunPiezaHeuristica()
#tiemposSegunPiezaHeuristica()


#HiperParametroVarios(r"tetrisJava\salidaTestFactores\Datos2-2.csv")