package br.com.sinapta.ecossistema.integracoes;

import br.com.sinapta.ecossistema.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "import_logs")
public class ImportLog extends BaseEntity {

    private UUID integrationId;
    private String providerLabel;
    private int recordsImported;
    private String message;

    public ImportLog(UUID integrationId, String providerLabel, int recordsImported, String message) {
        this.integrationId = integrationId;
        this.providerLabel = providerLabel;
        this.recordsImported = recordsImported;
        this.message = message;
    }
}
