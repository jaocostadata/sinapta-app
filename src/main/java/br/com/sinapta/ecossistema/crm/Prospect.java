package br.com.sinapta.ecossistema.crm;

import br.com.sinapta.ecossistema.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Linha da "planilha" de prospeccao. normalizedKey identifica o mesmo
 * prospect (documento ou nome da empresa, normalizado) e nao tem unique
 * constraint no banco de propósito: leads quentes podem ser recadastrados
 * para remarketing. Quem impede a duplicidade indevida e o ProspectService.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "prospects", indexes = @Index(name = "idx_prospect_normalized_key", columnList = "normalizedKey"))
public class Prospect extends BaseEntity {

    private String companyName;
    private String document;
    private String contactName;
    private String contactPhone;
    private String contactEmail;

    @Enumerated(EnumType.STRING)
    private ProspectStatus status = ProspectStatus.NOVO;

    private boolean hotLead = false;

    @Column(nullable = false)
    private String normalizedKey;

    private Instant lastActionAt = Instant.now();
    private String notes;
}
