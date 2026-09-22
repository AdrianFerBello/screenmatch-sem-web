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

    private Optional<Serie> serieBusca;

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
                    ************ SCREENMATCH ************
                    
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar series buscadas
                    4 - Buscar serie por titulo
                    5 - Buscar serie por autor
                    6 - Buscar Top 5 series
                    7 - Buscar por categoria
                    8 - Buscar Series por temporada e avaliação
                    9 - Buscar episódios por trecho
                    10 - Melhores episodios por Serie
                    11 - Buscar episodios depois de um Ano de Lançamento
                    
                    0 - Sair
                    
                    Digite uma opção: 
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

                case 4:
                    buscarSeriePorTitulo();
                    break;

                case 5:
                    buscarSeriePorAutor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarPorCategoria();
                    break;
                case 8:
                    buscarSeriePortemporadaEAvalicao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosDepoisDeUmaData();
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

    private void buscarEpisodiosDepoisDeUmaData() {
        buscarSeriePorTitulo();

        if ((serieBusca.isPresent())){

            System.out.println("Digite o ano limite de lançamento: ");
            var anoEpisodios = leitor.nextInt();
            Serie serie = serieBusca.get();

            List<Episodio> episodiosAno = repositorio.topEpisodiosPorSerieAno(serie, anoEpisodios);

            episodiosAno.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumero(), e.getTitulo(), e.getDataLancamento()));
        }
    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();

        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumero(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite um trecho do episodio: ");
        var trechoBuscado = leitor.nextLine();

        List<Episodio> episodioBuscado = repositorio.episodioPorTrecho(trechoBuscado);

        episodioBuscado.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumero(), e.getTitulo()));
    }

    private void buscarSeriePortemporadaEAvalicao() {
        System.out.println("Digite um total de temporadas para buscar as series: ");
        var totalTemporadas = leitor.nextInt();

        System.out.println("Digite uma avaliação");
        var avaliacao = leitor.nextDouble();

        List<Serie> series = repositorio.findByTotalTemporadasAndAvaliacaoGreaterThanEqual(totalTemporadas, avaliacao);

        System.out.println("Series com " +totalTemporadas+ " temporadas com avaliaçao maior que " +avaliacao);
        series.forEach(s -> System.out.println(s.getTitulo()));
    }

    private void buscarPorCategoria() {
        System.out.println("Digite uma categoria: ");
        var categoriaDigitada = leitor.nextLine();
        Categoria categoria = Categoria.fromPortugues(categoriaDigitada);

        List<Serie> seriesCategoria = repositorio.findByGenero(categoria);

        seriesCategoria.forEach(s -> System.out.println(s.getTitulo() + "Categoria: " +s.getGenero()+ ", Avaliação: " +s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> top5Series = repositorio.findTop5ByOrderByAvaliacaoDesc();
        top5Series.forEach(s -> System.out.println(s.getTitulo()  + ", Avalicao: " + s.getAvaliacao()));
    }

    private void buscarSeriePorAutor() {
        System.out.println("Digite o nome do autor: ");
        var nomeAutor = leitor.nextLine();
        System.out.println("Digite uma avaliação: ");
        var avaliacao = leitor.nextDouble();

        List<Serie> series = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAutor, avaliacao);

        System.out.println("Series em que o autor " +nomeAutor+ " trabalho");
        series.forEach(s -> System.out.println(s.getTitulo() + ", Avaliação: " +s.getAvaliacao()));

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

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

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

    private void buscarSeriePorTitulo() {
        System.out.println("Digite uma serie pelo nome: ");
        var nomeBuscado = leitor.nextLine();

        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeBuscado);

        if(serieBusca.isPresent()){
            System.out.println("Dados series: " + serieBusca.get());
        }else System.out.println("Serie não presente no banco de dados;");
    }
}