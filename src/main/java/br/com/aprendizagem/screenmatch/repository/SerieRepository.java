package br.com.aprendizagem.screenmatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.aprendizagem.screenmatch.model.Serie;

public interface  SerieRepository extends  JpaRepository<Serie, Long>{

    
}
