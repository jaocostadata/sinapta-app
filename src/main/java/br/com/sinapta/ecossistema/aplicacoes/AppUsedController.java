package br.com.sinapta.ecossistema.aplicacoes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/aplicacoes")
public class AppUsedController {

    private final AppUsedRepository repository;

    public AppUsedController(AppUsedRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<AppUsed> list() {
        return repository.findAll();
    }

    @PostMapping
    public AppUsed create(@RequestBody AppUsed appUsed) {
        return repository.save(appUsed);
    }
}
