package br.com.sinapta.ecossistema.calendario;

import br.com.sinapta.ecossistema.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Agenda unica da empresa (estilo Google Agenda): pagamentos, reunioes,
 * prazos de contrato, publicacoes de campanha etc. notifyDaysBefore
 * controla quando o ReminderScheduler dispara o lembrete.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "agenda_events")
public class AgendaEvent extends BaseEntity {

    private String title;
    private String description;
    private Instant eventDateTime;
    private int notifyDaysBefore = 3;
    private boolean reminderSent = false;
    private String sourceModule;
    private String sourceId;
}
