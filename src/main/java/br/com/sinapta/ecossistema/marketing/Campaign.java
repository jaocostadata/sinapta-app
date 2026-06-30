package br.com.sinapta.ecossistema.marketing;

import br.com.sinapta.ecossistema.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Cobre marketing em geral; quando type = TRAFEGO_PAGO os campos de
 * orcamento/investido/leads sao usados para alimentar o sub-modulo de
 * trafego pago dentro da aba de marketing.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "campaigns")
public class Campaign extends BaseEntity {

    private String title;

    @Enumerated(EnumType.STRING)
    private CampaignType type;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status = CampaignStatus.PLANEJADA;

    private String channel;
    private BigDecimal budget = BigDecimal.ZERO;
    private BigDecimal amountSpent = BigDecimal.ZERO;
    private int leadsGenerated;
    private LocalDate startDate;
    private LocalDate endDate;

    public BigDecimal costPerLead() {
        if (leadsGenerated == 0) {
            return BigDecimal.ZERO;
        }
        return amountSpent.divide(BigDecimal.valueOf(leadsGenerated), 2, java.math.RoundingMode.HALF_UP);
    }
}
