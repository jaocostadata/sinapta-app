package br.com.sinapta.ecossistema.calendario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Job periodico que verifica eventos da agenda perto do prazo e gera
 * notificacoes (sino da interface). Em producao, este e o ponto de
 * extensao para disparar e-mail/push/WhatsApp.
 */
@Component
public class ReminderScheduler {

    private static final Logger log = LoggerFactory.getLogger(ReminderScheduler.class);

    private final AgendaEventRepository agendaEventRepository;
    private final NotificationRepository notificationRepository;

    public ReminderScheduler(AgendaEventRepository agendaEventRepository,
                              NotificationRepository notificationRepository) {
        this.agendaEventRepository = agendaEventRepository;
        this.notificationRepository = notificationRepository;
    }

    @Scheduled(cron = "${app.reminders.check-cron}")
    public void checkUpcomingDeadlines() {
        Instant now = Instant.now();
        List<AgendaEvent> pendingEvents = agendaEventRepository.findByReminderSentFalseAndEventDateTimeAfter(now);

        for (AgendaEvent event : pendingEvents) {
            long daysUntilEvent = ChronoUnit.DAYS.between(now, event.getEventDateTime());
            if (daysUntilEvent <= event.getNotifyDaysBefore()) {
                String message = "\"" + event.getTitle() + "\" vence em " + daysUntilEvent + " dia(s).";
                notificationRepository.save(new Notification(event.getId(), message));
                event.setReminderSent(true);
                agendaEventRepository.save(event);
                log.info("Lembrete disparado: {}", message);
            }
        }
    }
}
