package br.com.aprendizagem.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpsodio(@JsonAlias("Title")String Titulo,
@JsonAlias("Episode")Integer numero,
@JsonAlias("imdbRating")String avaliacao,
@JsonAlias("Released")String dataLançamento) {
    
}
