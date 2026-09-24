package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService serieService;

    @GetMapping
    public List<SerieDTO> obterDados(){
        return serieService.retornaTodasSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterTop5Series(){
        return serieService.retornaTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamento(){
        return serieService.retornaLancamento();
    }

    @GetMapping("{id}")
    public SerieDTO obterPorId(@PathVariable Long id){
        return serieService.retornaSeriePorId(id);
    }

    @GetMapping("{id}/temporadas/todas")
    public List<EpisodioDTO> obterEpisodios(@PathVariable Long id){
        return serieService.obterTodasTemporadas(id);
    }
}
