package br.com.aprendizagem.screenmatch.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
       
        Optional<Serie> serieFiltrada =  series.stream().filter(s -> s.getTitulo().toLowerCase().contains(nomeDaSerie.toLowerCase())).findFirst();
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
}
