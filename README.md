# 🕹️ Tetris Automático (AI Bot)

[![Python Version](https://img.shields.io/badge/python-3.8%2B-blue.svg)](https://www.python.org)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![AI & Optimization](https://img.shields.io/badge/Focus-AI%20%26%20Optimization-orange.svg)]()

Este proyecto consiste en el desarrollo de un agente inteligente (Bot) capaz de jugar al Tetris de forma completamente automatizada y óptima en tiempo real. Utiliza algoritmos de optimización para evaluar la matriz del juego en cada ciclo y tomar la mejor decisión de rotación y traslación de las piezas para maximizar la puntuación y evitar el *Game Over*.

---

## 🚀 Características Principales

* **Toma de Decisiones en Tiempo Real:** Simulación veloz de todas las posiciones y rotaciones posibles para la pieza actual.
* **Evaluación de Estado Mediante Heurísticas / IA:** Análisis matemático del tablero basado en múltiples variables críticas.
* **Renderizado Gráfico:** Interfaz visual integrada para observar el comportamiento del agente y las métricas de rendimiento en vivo.
* **Control de Velocidad:** Capacidad para acelerar la velocidad del bot para pruebas de estrés y análisis de rendimiento masivo.

---

## 🧠 Algoritmo y Lógica de Decisión

El agente evalúa el "coste" o "beneficio" de cada posible movimiento final calculando una función de puntuación basada en los siguientes factores del tablero:

1. **Altura Agregada (Aggregate Height):** Suma de las alturas de todas las columnas. El agente prioriza mantener el tablero lo más bajo posible.
2. **Líneas Completadas (Cleared Lines):** Número de líneas que se eliminarán con el movimiento. Se prioriza hacer *Tetris* (4 líneas simultáneas) o limpiar filas críticas.
3. **Huecos/Agujeros (Holes):** Espacios vacíos que tienen bloques encima (inaccesibles). El algoritmo penaliza fuertemente la creación de huecos.
4. **Rugosidad del Tablero (Bumpiness):** Diferencia de altura entre columnas adyacentes. Se busca una superficie lo más lisa y uniforme posible.

> *[Nota técnica opcional: Si implementa Aprendizaje por Refuerzo]*: El agente ha sido entrenado utilizando una red **Deep Q-Network (DQN)** mediante **TensorFlow/Keras**, donde las recompensas se asignan en función de la supervivencia y la eficiencia en la limpieza de líneas.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Python 3.x
* **Librería Gráfica:** Pygame (o la usada en el proyecto, ej: Tkinter / Custom)
* **Librerías de Computo/IA:** NumPy / TensorFlow *(ajustar según dependencias reales)*

---

## 🔧 Instalación y Uso

### 1. Clonar el repositorio
```bash
git clone [https://github.com/RicardoSotoGallo/tetrisAutomatico.git](https://github.com/RicardoSotoGallo/tetrisAutomatico.git)
cd tetrisAutomatico
