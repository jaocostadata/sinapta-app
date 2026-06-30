package br.com.sinapta.ecossistema.calendario;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AgendaEventService {

    private final AgendaEventRepository repository;

    public AgendaEventService(AgendaEventRepository repository) {
        this.repository = repository;
    }

    public List<AgendaEvent> findAll() {
        return repository.findAll();
    }

    public List<AgendaEvent> findUpcoming(int days) {
        Instant now = Instant.now();
        return repository.findByEventDateTimeBetweenOrderByEventDateTimeAsc(now, now.plus(days, ChronoUnit.DAYS));
    }

    public AgendaEvent save(AgendaEvent event) {
        return repository.save(event);
    }
}
