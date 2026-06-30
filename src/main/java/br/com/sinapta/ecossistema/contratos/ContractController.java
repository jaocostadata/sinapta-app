package br.com.sinapta.ecossistema.contratos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContractController {

    private final ContractRepository repository;

    public ContractController(ContractRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ContractView> list(@RequestParam(defaultValue = "30") int janelaAlertaDias) {
        return repository.findAll().stream()
                .map(c -> new ContractView(c.getId(), c.getTitle(), c.getCounterparty(),
                        c.getExpirationDate(), c.effectiveStatus(janelaAlertaDias)))
                .toList();
    }

    @PostMapping
    public Contract create(@RequestBody Contract contract) {
        return repository.save(contract);
    }

    public record ContractView(java.util.UUID id, String title, String counterparty,
                                java.time.LocalDate expirationDate, ContractStatus status) {
    }
}
