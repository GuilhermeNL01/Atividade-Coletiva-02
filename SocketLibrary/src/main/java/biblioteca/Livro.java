package biblioteca;

import java.io.Serializable;


public class Livro implements Serializable {
    // carach do livro
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

    
}
