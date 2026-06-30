package br.com.sinapta.ecossistema.crm;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/crm/prospects")
public class ProspectController {

    private final ProspectService service;

    public ProspectController(ProspectService service) {
        this.service = service;
    }

    @GetMapping
    public List<Prospect> list() {
        return service.findAll();
    }

    @PostMapping
    public Prospect create(@Valid @RequestBody ProspectRequest request) {
        return service.create(request);
    }

    @PatchMapping("/{id}/lead-quente")
    public Prospect markHotLead(@PathVariable UUID id, @RequestBody Map<String, Boolean> body) {
        return service.markHotLead(id, Boolean.TRUE.equals(body.get("hotLead")));
    }

    @PatchMapping("/{id}/status")
    public Prospect updateStatus(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        return service.updateStatus(id, ProspectStatus.valueOf(body.get("status")));
    }
}
