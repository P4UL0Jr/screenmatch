package br.com.aprendizagem.screenmatch.gui;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.aprendizagem.screenmatch.model.DadosSerie;
import br.com.aprendizagem.screenmatch.model.DadosTemporada;
import br.com.aprendizagem.screenmatch.model.Episodio;
import br.com.aprendizagem.screenmatch.service.ConsumoApi;
import br.com.aprendizagem.screenmatch.service.ConverteDados;

public class MenuPrincipal {
    Scanner sc = new Scanner(System.in);

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String APIKEY = "&apikey=94881192";

    private ConsumoApi consumoApi = new ConsumoApi();
	private ConverteDados conversor = new ConverteDados();

    List<DadosTemporada> listaTemporada = new ArrayList<>();

    public void main(){
        int loop = 1;
        
        System.out.print("Digite o nome da série que deseja ver ");
        try{
            String nomeSerie = sc.nextLine();
            var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + APIKEY);
            DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
               
                
            for(int i = 1; i<= dados.totalTemporada(); i++){
                json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+")+"&season="+i + APIKEY);
                DadosTemporada dadosTemporada  = conversor.obterDados(json,DadosTemporada.class);
                listaTemporada.add(dadosTemporada);
            }
            listaTemporada.stream().forEach(t -> t.Episodios().forEach(e-> System.out.println("Temporada - "+t.numero()+" Ep - "+e)));
            List<Episodio> listaEpisodios = listaTemporada.stream()
            .flatMap(t -> t.Episodios().stream())
                .map(d -> new Episodio(d.numero(), d))
            .collect(Collectors.toList());
            do {
                System.out.println("Você deseja?");
                System.out.println("[1] - Procurar algum episódo");
                System.out.println("[2] - Ver dados da temporada");
                System.err.println("[3] - Buscar nova série");
                System.out.println("[4] - Sair");
                int escolha = sc.nextInt();
                sc.nextLine();

                switch (escolha) {
                    case 1:
                        System.out.print("Qual o titulo você procura: ");
                        String trechoTitulo = sc.nextLine();
            
                        Optional<Episodio> epsodioBuscado = listaEpisodios.stream().filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase())).findFirst();
            
                        if(epsodioBuscado.isPresent()){
                            System.out.println("Epsisodio encontrado");
                            System.out.println("Temporada - " +epsodioBuscado.get().getTemporada());
                        }else{
                            System.out.println("Epsisodio não encontrado");
                        }    
                        break;
                    case 2:
                    Map<Integer, Double> avaliacaoPorTemporada = listaEpisodios.stream()
                    .filter(e -> e.getAvaliacao()>0.0)
                    .collect(Collectors.groupingBy(Episodio::getTemporada,
                    Collectors.averagingDouble(Episodio::getAvaliacao)));

                    System.out.println(avaliacaoPorTemporada);

                    DoubleSummaryStatistics est = listaEpisodios.stream()
                    .filter(e -> e.getAvaliacao()>0.0)
                    .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

                    System.out.println("Media - "+est.getAverage()+
                    "\nMelhor episodio - "+ est.getMax()+
                    "\nPior episodio - "+ est.getMin()+
                    "\nQuantidade de Ep"+ est.getCount());
                    break;
                    case 3:
                        main();
                        break;
                    case 4:
                        System.out.println("Volte sempre");
                        System.out.println("Saindo...");
                        System.exit(0);
                    default:
                        System.out.println("Tente novamente");
                        break;
                }
            } while (loop==1);

        }catch(Exception e){
            System.out.println("Tente novamente");
        }

        
    }
}
