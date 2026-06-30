package br.com.sinapta.ecossistema.contratos;

import br.com.sinapta.ecossistema.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contracts")
public class Contract extends BaseEntity {

    private String title;
    private String counterparty;

    @Enumerated(EnumType.STRING)
    private ContractStatus status = ContractStatus.VIGENTE;

    private LocalDate expirationDate;

    /**
     * Status efetivo considerando a proximidade do vencimento, sem
     * sobrescrever o status manual ja definido como ENCERRADO.
     */
    public ContractStatus effectiveStatus(int alertWindowDays) {
        if (status == ContractStatus.ENCERRADO) {
            return ContractStatus.ENCERRADO;
        }
        if (expirationDate != null && !expirationDate.isAfter(LocalDate.now().plusDays(alertWindowDays))) {
            return ContractStatus.A_RENOVAR;
        }
        return ContractStatus.VIGENTE;
    }
}
