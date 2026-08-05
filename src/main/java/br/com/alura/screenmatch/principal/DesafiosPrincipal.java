package br.com.alura.screenmatch.principal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class DesafiosPrincipal {
    public static void main(String[] args) {

        Desafio multiplicador = new Desafio() {
            @Override
            public int multiplicar(int a, int b) {
                return a * b;
            }
        };
        System.out.println(multiplicador.multiplicar(5, 5));

        //Exercicio 2
        int numero = 4;

        if (ehPrimo(numero)) {
            System.out.println(numero + " é primo.");
        } else {
            System.out.println(numero + " não é primo.");
        }

        //Exercicio 3
        Function<String, String> converteMaiuscula = texto -> texto.toUpperCase();

        System.out.println(converteMaiuscula.apply("Texto qualquer"));

        List<String> listaNomes = Arrays.asList("Adrian, Yara, Murilo, Daniel, Wellington, Marcelo");

        listaNomes.stream()
                .sorted()
                .forEach(System.out::println);
    }

    public static boolean ehPrimo(int numero) {
        if (numero <= 1) return false;
        if (numero == 2) return true;
        if (numero % 2 == 0) return false;

        // Gera números ímpares de 3 até a raiz quadrada do número
        return IntStream.iterate(3, i -> i <= Math.sqrt(numero), i -> i + 2)
                .noneMatch(i -> numero % i == 0);
    }
}

