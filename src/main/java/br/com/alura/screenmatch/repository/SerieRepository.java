package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//SerieRepository herda de Serie
public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAutor, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    List<Serie> findByTotalTemporadasAndAvaliacaoGreaterThanEqual(Integer totalTemporada, Double avaliacao);

    @Query("SELECT e FROM series s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoBuscado%")
    List<Episodio> episodioPorTrecho(String trechoBuscado);

    @Query("SELECT e FROM series s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 10")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("SELECT e FROM series s JOIN s.episodios e WHERE s = :serie AND YEAR (e.dataLancamento) >= :anoEpisodios")
    List<Episodio> topEpisodiosPorSerieAno(Serie serie, int anoEpisodios);

//    @Query("SELECT s FROM serie s " + "LEFT JOIN s.episodios e " + "GROUP BY s " + "ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
//    List<Serie> lancamentosMaisRecentes();

    List<Serie> findTop5ByOrderByEpisodiosDataLancamentoDesc();
}
