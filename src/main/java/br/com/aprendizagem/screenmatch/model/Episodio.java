package br.com.aprendizagem.screenmatch.model;

import java.time.LocalDate;

public class Episodio {
    private  Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private double avaliacao;
    private LocalDate dataLancamento;


    
    public Episodio(Integer temporada, DadosEpsodio dadosEpsodio) {
        this.temporada = temporada;
        this.titulo = dadosEpsodio.Titulo();
        this.numeroEpisodio = dadosEpsodio.numero();
        try{
            this.avaliacao = Double.valueOf(dadosEpsodio.avaliacao());
        }catch(NumberFormatException e){
            this.avaliacao=0.0;
        }
        this.dataLancamento = LocalDate.parse(dadosEpsodio.dataLançamento());
    }
    public Integer getTemporada() {
        return temporada;
    }
    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }
    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }
    public double getAvaliacao() {
        return avaliacao;
    }
    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }
    public LocalDate getDataLancamento() {
        return dataLancamento;
    }
    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("---------");
        sb.append("temporada=").append(temporada);
        sb.append(", titulo=").append(titulo);
        sb.append(", numeroEpisodio=").append(numeroEpisodio);
        sb.append(", avaliacao=").append(avaliacao);
        sb.append(", dataLancamento=").append(dataLancamento);
        sb.append("--------");
        return sb.toString();
    }

    
}
