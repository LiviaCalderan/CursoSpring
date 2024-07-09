package br.com.alura.screenMatch.model;

import jakarta.persistence.*;

import java.time.DateTimeException;
import java.time.LocalDate;

@Entity
@Table(name = "episodios")
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private Double avaliacao;
    private LocalDate dataDeLancamento;

    @ManyToOne
    private Serie serie;

    public Episodio() {}

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodio.titulo();
        this.numeroEpisodio = dadosEpisodio.numero();

        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        } catch (NumberFormatException ex) {
            this.avaliacao = 0.0;
        }

        try {
            this.dataDeLancamento = LocalDate.parse(dadosEpisodio.dataDeLancamento());
        } catch (DateTimeException ex) {
            this.dataDeLancamento = null;
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
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

    public void setNumero(Integer numero) {
        this.numeroEpisodio = numero;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataDeLancamento() {
        return dataDeLancamento;
    }

    public void setDataDeLancamento(LocalDate dataDeLancamento) {
        this.dataDeLancamento = dataDeLancamento;
    }

    @Override
    public String toString() {
        return
                "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", numeroEpisodio=" + numeroEpisodio +
                ", avaliacao=" + avaliacao +
                ", dataDeLancamento=" + dataDeLancamento;
    }
}
