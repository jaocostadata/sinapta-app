package br.com.sinapta.ecossistema.modulos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/modulos")
public class CustomModuleController {

    private final CustomModuleRepository repository;

    public CustomModuleController(CustomModuleRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<CustomModule> list() {
        return repository.findAllByOrderByDisplayOrderAsc();
    }

    @PostMapping
    public CustomModule create(@RequestBody CustomModule customModule) {
        customModule.setDisplayOrder((int) repository.count());
        return repository.save(customModule);
    }
}
