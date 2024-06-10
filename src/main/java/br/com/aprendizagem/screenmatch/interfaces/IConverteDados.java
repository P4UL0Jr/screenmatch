package br.com.aprendizagem.screenmatch.interfaces;

public interface IConverteDados {
 <T> T obterDados(String json, Class<T> classe);   
}
