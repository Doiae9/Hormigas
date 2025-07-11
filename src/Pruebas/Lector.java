package Pruebas;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lector{
public static DatosExtraidos leerDesdeArchivo(String archivo) {
    List<String> bloqueEnteros = new ArrayList<>();
    List<String> bloqueDouble1 = new ArrayList<>();
    List<String> bloqueDouble2 = new ArrayList<>();

    String seccionActual = "";

    try (Scanner scanner = new Scanner(new File(archivo))) {
        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();

            if (linea.isEmpty()) continue;

            if (linea.startsWith("#")) {
                seccionActual = linea;
                continue;
            }

            switch (seccionActual) {
                case "#MATRIZC":
                    bloqueEnteros.add(linea);
                    break;
                case "#PRECIOL":
                    bloqueDouble1.add(linea);
                    break;
                case "#PRECIOT":
                    bloqueDouble2.add(linea);
                    break;
            }
        }
    } catch (FileNotFoundException e) {
        System.err.println("Archivo no encontrado: " + e.getMessage());
    }

    int[][] matriz = procesarMatrizEnteros(bloqueEnteros);
    double[] arreglo1 = procesarArregloDoubles(bloqueDouble1);
    double[] arreglo2 = procesarArregloDoubles(bloqueDouble2);

    return new DatosExtraidos(matriz, arreglo1, arreglo2);
}

public static int[][] procesarMatrizEnteros(List<String> lineas) {
    List<List<Integer>> matriz = new ArrayList<>();
    Pattern p = Pattern.compile("-?\\d+");

    for (String linea : lineas) {
        List<Integer> fila = new ArrayList<>();
        Matcher matcher = p.matcher(linea);
        while (matcher.find()) {
            fila.add(Integer.parseInt(matcher.group()));
        }
        matriz.add(fila);
    }

    int filas = matriz.size();
    int columnas = matriz.stream().mapToInt(List::size).max().orElse(0);

    int[][] resultado = new int[filas][columnas];
    for (int i = 0; i < filas; i++) {
        List<Integer> fila = matriz.get(i);
        for (int j = 0; j < fila.size(); j++) {
            resultado[i][j] = fila.get(j);
        }
    }

    return resultado;
}

public static double[] procesarArregloDoubles(List<String> lineas) {
    List<Double> lista = new ArrayList<>();
    Pattern p = Pattern.compile("-?\\d+(\\.\\d+)?");

    for (String linea : lineas) {
        Matcher matcher = p.matcher(linea);
        while (matcher.find()) {
            lista.add(Double.parseDouble(matcher.group()));
        }
    }
    double[] resultado = new double[lista.size()];
    for (int i = 0; i < lista.size(); i++) {
        resultado[i] = lista.get(i);
    }
    return resultado;
}
}
