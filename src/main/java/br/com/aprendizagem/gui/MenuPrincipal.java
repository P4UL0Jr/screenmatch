package br.com.aprendizagem.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.aprendizagem.screenmatch.model.DadosEpsodio;
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
    

    public void main(){
        System.out.print("Digite o nome da série que deseja ver ");
        String nomeSerie = sc.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + APIKEY);
		//System.out.println(json);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		//System.out.println(dados);

        List<DadosTemporada> listaTemporada = new ArrayList<>();
        
        for(int i = 1; i<= dados.totalTemporada(); i++){
            json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+")+"&season="+i + APIKEY);
            DadosTemporada dadosTemporada  = conversor.obterDados(json,DadosTemporada.class);
            listaTemporada.add(dadosTemporada);
        }
        listaTemporada.forEach(System.out::println);
        listaTemporada.forEach(t -> t.Episodios().forEach(e-> System.out.println(e.numero()+" - "+ e.Titulo())));

        List<DadosEpsodio> dadosEpsodios = listaTemporada.stream()
                .flatMap(t -> t.Episodios().stream())
                .collect(Collectors.toList());

        List<Episodio> listaEpisodios = listaTemporada.stream()
        .flatMap(t -> t.Episodios().stream())
            .map(d -> new Episodio(d.numero(), d))
        .collect(Collectors.toList());

        listaEpisodios.forEach(System.out::println);
 }
}
