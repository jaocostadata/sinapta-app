package br.com.sinapta.ecossistema.crm;

import br.com.sinapta.ecossistema.common.exception.DuplicateProspectException;
import br.com.sinapta.ecossistema.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Regra de negocio central da prospeccao: a mesma empresa (planilha de
 * prospeccao) nao pode ser cadastrada de novo enquanto nao houver sido
 * marcada como lead quente. Uma vez marcada como lead quente, ela pode ser
 * recadastrada para uma nova rodada de remarketing.
 */
@Service
public class ProspectService {

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9]");

    private final ProspectRepository repository;

    public ProspectService(ProspectRepository repository) {
        this.repository = repository;
    }

    public List<Prospect> findAll() {
        return repository.findAll();
    }

    public Prospect create(ProspectRequest request) {
        String normalizedKey = normalize(request.document() != null && !request.document().isBlank()
                ? request.document()
                : request.companyName());

        List<Prospect> existing = repository.findByNormalizedKeyOrderByCreatedAtDesc(normalizedKey);
        if (!existing.isEmpty()) {
            boolean anyHotLead = existing.stream().anyMatch(Prospect::isHotLead);
            if (!anyHotLead) {
                throw new DuplicateProspectException(
                        "Este prospect já está cadastrado e ainda não foi marcado como lead quente. "
                                + "Marque como lead quente para permitir um novo registro de remarketing.");
            }
        }

        Prospect prospect = new Prospect();
        prospect.setCompanyName(request.companyName());
        prospect.setDocument(request.document());
        prospect.setContactName(request.contactName());
        prospect.setContactPhone(request.contactPhone());
        prospect.setContactEmail(request.contactEmail());
        prospect.setNotes(request.notes());
        prospect.setNormalizedKey(normalizedKey);
        prospect.setLastActionAt(Instant.now());
        return repository.save(prospect);
    }

    public Prospect markHotLead(UUID id, boolean hotLead) {
        Prospect prospect = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prospect não encontrado: " + id));
        prospect.setHotLead(hotLead);
        prospect.setLastActionAt(Instant.now());
        return repository.save(prospect);
    }

    public Prospect updateStatus(UUID id, ProspectStatus status) {
        Prospect prospect = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prospect não encontrado: " + id));
        prospect.setStatus(status);
        prospect.setLastActionAt(Instant.now());
        return repository.save(prospect);
    }

    private String normalize(String value) {
        String unaccented = Normalizer.normalize(value.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return NON_ALPHANUMERIC.matcher(unaccented).replaceAll("");
    }
}
