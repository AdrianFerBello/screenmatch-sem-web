package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    List<Serie> series = new ArrayList<>();

    ConverteDados conversor = new ConverteDados();
    ConsumoApi consumoApi = new ConsumoApi();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6fedcde0";

    private Scanner leitor = new Scanner(System.in);
    boolean rodarMenu = true;

    public void exibeMenu() {

        while (rodarMenu) {

            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar series buscadas
                    
                    0 - Sair
                    """;

            System.out.println(menu);

            var opcao = leitor.nextInt();
            leitor.nextLine();

            switch (opcao) {

                case 1:
                    buscarSerieWeb();
                    break;

                case 2:
                    buscarEpisodioPorSerie();
                    break;

                case 3:
                    listarSeriesBuscadas();
                    break;

                case 0:
                    System.out.println("Saindo...");
                    rodarMenu = false;
                    break;

                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void listarSeriesBuscadas() {

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSerieWeb() {

        DadosSerie dados = getDadosSerie();

        Serie serie = new Serie(dados);

        series.add(serie);

        System.out.println(serie);
    }

    private DadosSerie getDadosSerie() {

        System.out.println("Digite o nome da série para busca");

        var nomeSerie = leitor.nextLine();

        var json = consumoApi.obterDados(
                ENDERECO + nomeSerie.replace(" ", "+") + API_KEY
        );

        return conversor.obterDados(json, DadosSerie.class);
    }

    private void buscarEpisodioPorSerie() {

        DadosSerie dadosSerie = getDadosSerie();

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {

            var json = consumoApi.obterDados(
                    ENDERECO
                            + dadosSerie.titulo().replace(" ", "+")
                            + "&season="
                            + i
                            + API_KEY
            );

            DadosTemporada dadosTemporada =
                    conversor.obterDados(json, DadosTemporada.class);

            temporadas.add(dadosTemporada);
        }

        temporadas.forEach(System.out::println);
    }
}