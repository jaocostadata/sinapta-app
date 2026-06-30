package br.com.sinapta.ecossistema.calendario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AgendaEventRepository extends JpaRepository<AgendaEvent, UUID> {

    List<AgendaEvent> findByReminderSentFalseAndEventDateTimeAfter(Instant now);

    List<AgendaEvent> findByEventDateTimeBetweenOrderByEventDateTimeAsc(Instant start, Instant end);
}
