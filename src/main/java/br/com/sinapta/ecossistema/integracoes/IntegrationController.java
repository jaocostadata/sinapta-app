package br.com.sinapta.ecossistema.integracoes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/integracoes")
public class IntegrationController {

    private final IntegrationService service;

    public IntegrationController(IntegrationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ApiIntegration> list() {
        return service.findAll();
    }

    @PostMapping
    public ApiIntegration connect(@RequestBody ApiIntegration integration) {
        return service.connect(integration);
    }

    @PostMapping("/{id}/sincronizar")
    public ImportLog sync(@PathVariable UUID id) {
        return service.sync(id);
    }

    @GetMapping("/importacoes")
    public List<ImportLog> recentImports() {
        return service.recentLogs();
    }
}
