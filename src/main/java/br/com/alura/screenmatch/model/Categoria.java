package br.com.alura.screenmatch.model;

public enum Categoria {
    AVENTURA("Adventure"),
    DRAMA("Drama"),
    FANTASIA("Fantasy"),
    TERROR("Terror"),
    COMEDIA("Comedy"),
    ROMANCE("Romance"),
    CRIME("Crime"),
    ACAO("Action");

    public String categoriaOmdb;

    Categoria(String categoriaOmdb){
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
