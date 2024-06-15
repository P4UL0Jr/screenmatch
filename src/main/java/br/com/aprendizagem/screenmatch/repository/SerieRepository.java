package br.com.aprendizagem.screenmatch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.aprendizagem.screenmatch.model.Categoria;
import br.com.aprendizagem.screenmatch.model.Episodio;
import br.com.aprendizagem.screenmatch.model.Serie;

public interface  SerieRepository extends  JpaRepository<Serie, Long>{
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeDaserie);

    public List<Serie> findByAtoresContainingIgnoreCase(String nomeAtor);

    public List<Serie> findTop5ByOrderByAvaliacaoDesc();

    public List<Serie> findByGenero(Categoria genero);

    @Query("select s from Serie s WHERE s.totalTemporada <= :totalTemporada AND s.avaliacao >= :avaliacao")
    public List<Serie> seriePorTemporadaEAvaliacao(int totalTemporada, Double avaliacao);

    @Query("select e from Serie s join s.episodios e where e.titulo ilike %:nomeEpsisodio%")
    public List<Episodio> episodioPorTrecho(String nomeEpsisodio);
    
}
