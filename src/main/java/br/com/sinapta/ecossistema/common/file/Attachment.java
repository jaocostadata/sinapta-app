package br.com.sinapta.ecossistema.common.file;

import br.com.sinapta.ecossistema.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A file uploaded and linked to a record from any module (contract,
 * financial entry, etc). ownerType + ownerId let a single table back the
 * "anexar arquivo" feature everywhere in the app without per-module tables.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "attachments")
public class Attachment extends BaseEntity {

    private String ownerType;
    private String ownerId;

    private String originalFileName;
    private String storedFileName;
    private String contentType;
    private long sizeBytes;

    public Attachment(String ownerType, String ownerId, String originalFileName, String storedFileName,
                       String contentType, long sizeBytes) {
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
    }
}
