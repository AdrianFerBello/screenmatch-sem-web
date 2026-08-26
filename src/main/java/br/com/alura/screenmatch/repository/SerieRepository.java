package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

//SerieRepository herda de Serie
public interface SerieRepository extends JpaRepository<Serie, Long> {
}
