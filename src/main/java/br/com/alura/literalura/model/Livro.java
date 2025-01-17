package br.com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String autor;
    private String titulo;
    private String linguagem;
    private Integer totalDownloads;

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores;

    public Livro() {}

    public Livro(DadosLivro dadosLivro) {
        this.autor = dadosLivro.autores().getFirst().nomeAutor();
        this.titulo = dadosLivro.titulo();
        this.linguagem = dadosLivro.idioma().toString();
        this.totalDownloads = dadosLivro.totalDownloads();
        this.autores = new ArrayList<>();

        for (DadosAutor dadosAutor : dadosLivro.autores()) {
            Autor autor = new Autor(dadosAutor);
            autor.setLivro(this);
            this.autores.add(autor);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLinguagem() {
        return linguagem;
    }

    public void setLinguagem(String linguagem) {
        this.linguagem = linguagem;
    }

    public Integer getTotalDownloads() {
        return totalDownloads;
    }

    public void setTotalDownloads(Integer totalDownloads) {
        this.totalDownloads = totalDownloads;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "Livro" + titulo + '\'' +
                ", linguagem='" + linguagem + '\'' +
                ", totalDownloads=" + totalDownloads +
                ", autores=" + autores;
    }
}
