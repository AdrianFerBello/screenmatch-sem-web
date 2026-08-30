package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    
    private SerieRepository repositorio;

    List<Serie> series = new ArrayList<>();

    ConverteDados conversor = new ConverteDados();
    ConsumoApi consumoApi = new ConsumoApi();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6fedcde0";

    private Scanner leitor = new Scanner(System.in);
    boolean rodarMenu = true;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

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

        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSerieWeb() {

        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        series.add(serie);
        repositorio.save(serie);
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

        listarSeriesBuscadas();

        System.out.println("Digite uma serie pelo nome: ");
        var nomeSerie = leitor.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase()
                        .contains(nomeSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();

            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {

                var json = consumoApi.obterDados(
                        ENDERECO
                                + serieEncontrada.getTitulo().replace(" ", "+")
                                + "&season="
                                + i
                                + API_KEY
                );

                DadosTemporada dadosTemporada =
                        conversor.obterDados(json, DadosTemporada.class);

                temporadas.add(dadosTemporada);
            }

            temporadas.forEach(System.out::println);

            // Criando e adicionando os episódios à série
            temporadas.forEach(temporada -> {

                temporada.episodios().forEach(dadosEpisodio -> {

                    Episodio episodio = new Episodio(
                            temporada.numero(),
                            dadosEpisodio
                    );

                    serieEncontrada.adicionarEpisodio(episodio);
                });
            });

            // Salva a série e, por causa do CascadeType.ALL,
            // salva os episódios também
            repositorio.save(serieEncontrada);

        } else {
            System.out.println("Serie não encontrada no banco de dados!!");
        }
    }
}