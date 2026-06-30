package br.com.sinapta.ecossistema.contatos;

import br.com.sinapta.ecossistema.common.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/contatos")
public class CredentialController {

    private final CredentialRepository repository;

    public CredentialController(CredentialRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<CredentialSummary> list() {
        return repository.findAll().stream().map(CredentialSummary::from).toList();
    }

    @PostMapping
    public Credential create(@RequestBody Credential credential) {
        return repository.save(credential);
    }

    @GetMapping("/{id}/revelar")
    public CredentialSecret reveal(@PathVariable UUID id) {
        Credential credential = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credencial não encontrada: " + id));
        return new CredentialSecret(credential.getLogin(), credential.getPassword());
    }

    public record CredentialSummary(UUID id, String systemName, String url, String notes) {
        static CredentialSummary from(Credential entity) {
            return new CredentialSummary(entity.getId(), entity.getSystemName(), entity.getUrl(), entity.getNotes());
        }
    }

    public record CredentialSecret(String login, String password) {
    }
}
