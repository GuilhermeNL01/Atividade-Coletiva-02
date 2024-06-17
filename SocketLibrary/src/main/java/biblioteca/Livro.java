package biblioteca;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonPropertyOrder({"titulo", "autor", "genero", "exemplares"})
public class Livro implements Serializable {
    // Atributos do livro
    private String autor;
    private String titulo;
    private String genero;
    private int exemplares;

    public Livro() {}

    public Livro(String autor, String titulo, String genero, int exemplares) {
        this.autor = autor;
        this.titulo = titulo;
        this.genero = genero;
        this.exemplares = exemplares;
    }

    // Getters e setters
    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getExemplares() {
        return exemplares;
    }

    public void setExemplares(int exemplares) {
        this.exemplares = exemplares;
    }

    // Formatador
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
