package br.com.alura.screenmatch.principal;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrincipaisDesafios {
    public static void main(String[] args) {
        //praticando streams

        List<Integer> integerList = List.of(1, 2, 3, 4, 5);

        List<Integer> numerosDobrados = integerList.stream()
                .map(n -> n * 2)
                .collect(Collectors.toList());

        System.out.println(numerosDobrados);

        //exercicio 2

        List<String> nomes = List.of("Adrian", "Daniel", "Murilo", "Yara", "Wellington", "Amanda", "Alisson", "Adrian");

        List<String> nomesMaiusculos = nomes.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println(nomesMaiusculos);

        // exercicio 3

        List<Integer> listaTamanhoNomes = nomes.stream()
                .map(String::length)
                .collect(Collectors.toList());

        System.out.println(listaTamanhoNomes);

        //

        List<String> nomescomA = nomes.stream()
                .filter(n -> n.contains("A"))
                        .collect(Collectors.toList());
        System.out.println(nomescomA);

        //exercicio 4
        List<List<Integer>> numeros = List.of(
                List.of(1, 2, 3),
                List.of(4, 5, 6),
                List.of(7, 8, 9)
        );

        List<Integer> unicaLista = numeros.stream()
                .flatMap(l -> l.stream())
                .collect(Collectors.toList());
        System.out.println(unicaLista);


    }
}

