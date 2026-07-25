package br.com.alura.screenmatch.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);  //estamos criando uma variavel de tipo generico que pode me retornar qualquer coisa


}
