package br.com.aprendizagem.interfaces;

public interface IConverteDados {
 <T> T obterDados(String json, Class<T> classe);   
}
