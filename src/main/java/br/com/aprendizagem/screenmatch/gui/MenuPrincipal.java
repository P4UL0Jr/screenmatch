package br.com.aprendizagem.screenmatch.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.aprendizagem.screenmatch.model.Categoria;
import br.com.aprendizagem.screenmatch.model.DadosSerie;
import br.com.aprendizagem.screenmatch.model.DadosTemporada;
import br.com.aprendizagem.screenmatch.model.Episodio;
import br.com.aprendizagem.screenmatch.model.Serie;
import br.com.aprendizagem.screenmatch.repository.SerieRepository;
import br.com.aprendizagem.screenmatch.service.ConsumoApi;
import br.com.aprendizagem.screenmatch.service.ConverteDados;


public class MenuPrincipal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?";
    private final String API_KEY = "apikey=6585022c&t=";
// c&&Episode=1
    List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();
    public MenuPrincipal(SerieRepository repositorio){
        this.repositorio = repositorio;
    }
    public void exibeMenu() {
        var opcao = 5;
        while (opcao!=0) {
        
                var menu ="""
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar séries por titulo
                    5 - Buscar séries por ator
                    6 - Buscar top 5 Séries
                    7 - Buscar Séries por categoria
                    8 - Filtrar séries
                    9 - Buscar episódio por trecho 

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
                    buscarSeriesBuscadsa();
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
                    buscarSeriePorCatergoria();
                    break;
                case 8:
                    filtrarSeriePorTemporadaEAvaliacao();
                    break;
                case 9: 
                    buscarEpidodioPorTrecho();
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
        repositorio.save(serie);
    }
    private void buscarSeriesBuscadsa(){
        series = repositorio.findAll();
        series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        buscarSeriesBuscadsa();
        System.out.println("Escolha uma série pelo nome");
        String nomeDaSerie = leitura.nextLine();
       
        Optional<Serie> serieFiltrada =  repositorio.findByTituloContainingIgnoreCase(nomeDaSerie);
        if (serieFiltrada.isPresent()) {
            var serieEncontrada = serieFiltrada.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i<= serieEncontrada.getTotalTemporada();i++) {
                var json = consumo.obterDados(ENDERECO+API_KEY+serieEncontrada.getTitulo().replace(" ","+")+"&type=series&Season="+i);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.stream().forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream().flatMap(d ->d.Episodios().stream().map(e -> new Episodio(e.numero(), e))).collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }else{
            System.out.println("Não foi encontrada");
        }
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série por nome");
        String nomeSerie = leitura.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
        
        if(serieBuscada.isPresent()){
            System.out.println(serieBuscada);
        }else{
            System.out.println("Série não encontrada");
        }

    }

    private void buscarSeriePorAutor() {
        System.out.println("Qual o nome do ator");
        String nomeAtor = leitura.nextLine();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCase(nomeAtor);
        System.out.println(seriesEncontradas);
    }

    private void buscarTop5Series() {
        List<Serie> seriesEncontradas = repositorio.findTop5ByOrderByAvaliacaoDesc();
        for (Serie serie : seriesEncontradas) {
            System.out.println(serie.getTitulo()+ serie.getAvaliacao());
        }
    }

    private void buscarSeriePorCatergoria() {
        System.out.println("Qual o genero você busca? ");
        String nomeGenero = leitura.nextLine();
        Categoria genero = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesEncontradas = repositorio.findByGenero(genero);
        seriesEncontradas.stream().forEach(System.out::println);
    }
    private void filtrarSeriePorTemporadaEAvaliacao(){
        System.out.println("Filtrar série até quantas temporadas");
        int temporadaFiltrada = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Com avaliação a partir de?");
        double avaliacaoFiltrada = leitura.nextDouble();
        leitura.nextLine();
        List<Serie> serieFiltrada = repositorio.seriePorTemporadaEAvaliacao(temporadaFiltrada, avaliacaoFiltrada);
        serieFiltrada.forEach(s -> System.out.println(s.getTitulo()+" Avaliação - "+ s.getAvaliacao()));
    }

    private void buscarEpidodioPorTrecho() {
        System.out.println("Digite o nome do episodio para a busca");
        String nomeEpsisodio = leitura.nextLine();
        List<Episodio> listaDeEpisodios = repositorio.episodioPorTrecho(nomeEpsisodio);     
        listaDeEpisodios.forEach (e -> System.out.println(e.getSerie() +" "+ e.getTemporada() +" "+ e.getTitulo()));   
    }
}
