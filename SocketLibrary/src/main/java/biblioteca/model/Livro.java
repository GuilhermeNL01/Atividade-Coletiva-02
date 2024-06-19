package biblioteca.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

/**
 * A classe Livro representa um livro em um sistema de biblioteca.
 * Implementa a interface Serializable para serialização de objetos.
 * A classe é anotada com @JsonPropertyOrder para especificar a ordem das propriedades na serialização JSON.
 */
@JsonPropertyOrder({"titulo", "autor", "genero", "exemplares"})
public class Livro implements Serializable {
    private String autor;
    private String titulo;
    private String genero;
    private int exemplares;

    /**
     * Construtor padrão para Livro.
     * Necessário para processos de serialização e desserialização.
     */
    public Livro() {}

    /**
     * Construtor parametrizado para Livro.
     *
     * @param autor      o autor do livro
     * @param titulo     o título do livro
     * @param genero     o gênero do livro
     * @param exemplares o número de exemplares do livro
     */
    public Livro(String autor, String titulo, String genero, int exemplares) {
        this.autor = autor;
        this.titulo = titulo;
        this.genero = genero;
        this.exemplares = exemplares;
    }

    // Getter para autor
    public String getAutor() {
        return autor;
    }

    // Setter para autor
    public void setAutor(String autor) {
        this.autor = autor;
    }

    // Getter para titulo
    public String getTitulo() {
        return titulo;
    }

    // Setter para titulo
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Getter para genero
    public String getGenero() {
        return genero;
    }

    // Setter para genero
    public void setGenero(String genero) {
        this.genero = genero;
    }

    // Getter para exemplares
    public int getExemplares() {
        return exemplares;
    }

    // Setter para exemplares
    public void setExemplares(int exemplares) {
        this.exemplares = exemplares;
    }

    /**
     * Retorna uma representação em string do objeto Livro.
     *
     * @return uma representação em string do objeto Livro
     */
    @Override
    public String toString() {
        return "Livro{" +
                "autor='" + autor + '\'' +
                ", titulo='" + titulo + '\'' +
                ", genero='" + genero + '\'' +
                ", exemplares=" + exemplares +
                '}';
    }
}
