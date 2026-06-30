package br.com.sinapta.ecossistema.calendario;

import br.com.sinapta.ecossistema.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Notificacao de aproximacao de prazo, gerada automaticamente pelo
 * ReminderScheduler e consumida pelo sino de notificacoes da interface.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {

    private UUID agendaEventId;
    private String message;
    private boolean read = false;

    public Notification(UUID agendaEventId, String message) {
        this.agendaEventId = agendaEventId;
        this.message = message;
    }
}
