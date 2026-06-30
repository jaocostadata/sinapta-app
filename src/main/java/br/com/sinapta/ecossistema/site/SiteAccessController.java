package br.com.sinapta.ecossistema.site;

import br.com.sinapta.ecossistema.common.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/site")
public class SiteAccessController {

    private final SiteAccessRepository repository;

    public SiteAccessController(SiteAccessRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<SiteAccessSummary> list() {
        return repository.findAll().stream().map(SiteAccessSummary::from).toList();
    }

    @PostMapping
    public SiteAccess create(@RequestBody SiteAccess siteAccess) {
        return repository.save(siteAccess);
    }

    @PutMapping("/{id}")
    public SiteAccess update(@PathVariable UUID id, @RequestBody SiteAccess update) {
        SiteAccess existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acesso de site não encontrado: " + id));
        existing.setName(update.getName());
        existing.setSiteUrl(update.getSiteUrl());
        existing.setAdminUrl(update.getAdminUrl());
        existing.setLoginIdentifier(update.getLoginIdentifier());
        existing.setPassword(update.getPassword());
        return repository.save(existing);
    }

    /**
     * Revela login e senha em texto puro. Requer usuário autenticado
     * (ja garantido pelo filtro /api/** authenticated) e idealmente seria
     * restrito por role em uma evolucao futura (ex.: somente ADMIN).
     */
    @GetMapping("/{id}/revelar")
    public SiteAccessCredentials reveal(@PathVariable UUID id) {
        SiteAccess siteAccess = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acesso de site não encontrado: " + id));
        return new SiteAccessCredentials(siteAccess.getLoginIdentifier(), siteAccess.getPassword());
    }

    public record SiteAccessSummary(UUID id, String name, String siteUrl, String adminUrl) {
        static SiteAccessSummary from(SiteAccess entity) {
            return new SiteAccessSummary(entity.getId(), entity.getName(), entity.getSiteUrl(), entity.getAdminUrl());
        }
    }

    public record SiteAccessCredentials(String loginIdentifier, String password) {
    }
}
