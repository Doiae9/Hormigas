# Optimización de Sensores en Sistemas de Eventos Discretos (Redes de Petri)

> **Proyecto de Investigación - Verano Delfín**
> **Asesor:** Luis Isidro Aguirre Salas (Universidad de Guadalajara) 
> **Desarrollador:** Leonardo Daniel García Hernández (Instituto Tecnológico de Morelia)

## 📖 Descripción General

Este proyecto aborda el problema del monitoreo eficiente en sistemas de automatización y robótica mediante el uso de **Redes de Petri**. El objetivo principal fue desarrollar un software en **Java** capaz de reducir la cantidad de sensores físicos necesarios para monitorear un sistema, manteniendo su observabilidad completa y reduciendo costos de implementación.

El sistema compara dos enfoques algorítmicos para resolver la optimización de sensores:
1.  **Fuerza Bruta:** Para validación exacta en redes pequeñas.
2.  **Algoritmo de Colonia de Hormigas (ACO):** Una metaheurística bio-inspirada para resolver el problema en redes complejas donde la complejidad computacional es inmanejable.

## ⚙️ El Problema Técnico

El monitoreo de estados en sistemas de eventos discretos se vuelve exponencialmente complejo a medida que el sistema crece.
* **Representación:** Se utilizó una matriz de incidencia de estados (0, 1, -1) para modelar lugares y transiciones, asegurando el control de estados lógicos (ej. un sistema no puede estar en estado "vivo" y "curándose" sin relación lógica).
* **Complejidad Computacional:** El problema de optimización se clasificó como **No Polinomial Duro (NP-Hard)**. [cite_start]La complejidad es de $2^{n+m}$ (donde $n$ son lugares y $m$ transiciones).
* **Escalabilidad:** En pruebas con una matriz de 34 lugares y 23 transiciones ($2^{57}$ combinaciones), un enfoque de fuerza bruta tardaría siglos en completarse, haciendo necesaria una solución heurística.

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java.
* *Entorno de Desarrollo (IDE):** IntelliJ IDEA.
* **Conceptos Clave:** Algoritmos Heurísticos, Redes de Petri, Matrices de Incidencia, Programación Concurrente (Hilos).

## Algoritmos Implementados

### 1. Comprobación por Fuerza Bruta
Se implementó un algoritmo que evalúa cada combinación posible de sensores convirtiendo números decimales a binarios y comprobando la observabilidad de la matriz resultante.
* Se descartan matrices donde columnas repetidas o filas de ceros generan ambigüedad en el estado del sistema.
* **Limitación:** Funcional pero ineficiente para sistemas grandes debido a la explosión combinatoria.

### 2. Metaheurística de Colonia de Hormigas (ACO)
Se adaptó el algoritmo clásico del "Problema del Viajante" (TSP) para recorrer "sensores" en lugar de ciudades.
* **Funcionamiento:** Simula el comportamiento de hormigas depositando feromonas en las soluciones (configuraciones de sensores) más eficientes.
* **Parámetros Ajustables:**
    * **Alfa:** Determina la visibilidad de la solución.
    * **Beta:** Determina la influencia de la feromona.
    * **Evaporación:** Reduce el rastro de feromona para evitar estancamiento en óptimos locales.
    * **Q (Peso):** Cantidad de feromona depositada.

## Resultados y Conclusiones

La implementación del algoritmo de hormigas permitió encontrar configuraciones de sensores óptimas y de bajo costo en tiempos razonables, superando la limitación de la invarianza temporal de la fuerza bruta.

Se determinó que la eficiencia del algoritmo depende del ajuste fino (tuning) de los parámetros Alfa, Beta y Evaporación, requiriendo múltiples ejecuciones para validar la desviación estándar y la fiabilidad de la solución encontrada.

## 📚 Referencias

1.  *Problema del viajante de Comercio TSP (VI). Método Colonia de Hormigas (ACO).* 
2.  Murata, T. *Petri Nets: Properties, Analysis and Applications.* IEEE. 
3.  Dorigo, M. *The Ant Colony Optimization Meta-Heuristic.* 
