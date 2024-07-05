package br.com.alura.screenMatch.repository;

import br.com.alura.screenMatch.model.Categoria;
import br.com.alura.screenMatch.model.Episodio;
import br.com.alura.screenMatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);
    //verbo introdutório + palavra-chave “By” + critérios de busca

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();
    //Top, para limitar o número de dados

    List<Serie> findByGenero(Categoria categoria);

    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer totalTemporadas, Double avaliacao);

    //JPQL(Java Persistence Query Language)
    //: -> indicam o parametro
    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAvalicao(int totalTemporadas, Double avaliacao);

    @Query("select e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trecho%")
    List<Episodio> episodiosPorTrecho(String trecho);

    @Query("select e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("select s FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataDeLancamento) >= :anoLancamento ")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);
}
