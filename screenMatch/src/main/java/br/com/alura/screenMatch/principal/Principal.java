package br.com.alura.screenMatch.principal;

import br.com.alura.screenMatch.model.*;
import br.com.alura.screenMatch.repository.SerieRepository;
import br.com.alura.screenMatch.service.ConsumoApi;
import br.com.alura.screenMatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=546e4c2f";
    List<DadosSerie> dadosSeries = new ArrayList<>();


    private SerieRepository repositorio;

    List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por titulo
                    5 - Buscar series por ator
                    6 - Top 5 séries
                    7 - Buscar série por categoria;
                    8 - Filtrar pelo número de temporadas;
                    9 - Buscar episódios por trecho;
                                        
                    0 - Sair
                    """;

            System.out.println(menu);


            opcao = leitura.nextInt();
            leitura.nextLine();

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
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Seires();
                    break;
                case 7:
                    buscarSeriePorGenero();
                    break;
                case 8:
                    buscarSeriePorTotalDeTemporadas();
                    break;
                case 9:
                    buscarEpisodioporTrecho();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {

        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.next();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }

    }

    private void listarSeriesBuscadas() {

        series = series = repositorio.findAll(); //mostra tudo q foi cadastrado
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = leitura.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("Dados da série: " + serieBuscada.get());

        } else {
            System.out.println("Serie não encontrada!");
        }

    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do ator para buscar: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de qual valor? ");
        var avaliacao = leitura.nextDouble();

        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Series em que " + nomeAtor + " trabalhou: ");
        seriesEncontradas.forEach(s ->
                System.out.println(s.getTitulo() + " | avaliação: " + s.getAvaliacao()));

    }

    private void buscarTop5Seires() {
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();

        seriesTop.forEach(s ->
                System.out.println(s.getTitulo()
                        + " | avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorGenero() {
        System.out.println("Qual gênero você deseja buscar: ");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Series da categoria: " + nomeGenero);
        seriesPorCategoria.forEach(s ->
                System.out.println(s.getTitulo() + " | avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorTotalDeTemporadas() {
        System.out.println("Você deseja ver séries com no máximo quantas temporadas? ");
        var numeroDeTemporadas = leitura.nextInt();
        System.out.println("Avaliações a partir de qual valor? ");
        var avaliacao = leitura.nextDouble();

        List<Serie> seriesPorTemporada = repositorio
                .seriesPorTemporadaEAvalicao(numeroDeTemporadas, avaliacao);
        seriesPorTemporada.forEach(s -> System.out.println(s.getTitulo() + " | avaliação: " + s.getAvaliacao()));
    }

    private void buscarEpisodioporTrecho() {
        System.out.println("Digite o trecho do episódio que deseja buscar: ");
        var trecho = leitura.nextLine();

        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trecho);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s | Temporada %s | Episódio %s - %s\n",
                        e.getSerie().getTitulo(),
                        e.getTemporada(),
                        e.getNumero(),
                        e.getTitulo()));
    }
}
