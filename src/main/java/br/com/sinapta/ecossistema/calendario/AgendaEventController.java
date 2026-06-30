package br.com.sinapta.ecossistema.calendario;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/calendario")
public class AgendaEventController {

    private final AgendaEventService eventService;
    private final NotificationRepository notificationRepository;

    public AgendaEventController(AgendaEventService eventService, NotificationRepository notificationRepository) {
        this.eventService = eventService;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/eventos")
    public List<AgendaEvent> list() {
        return eventService.findAll();
    }

    @GetMapping("/eventos/proximos")
    public List<AgendaEvent> upcoming(@RequestParam(defaultValue = "30") int dias) {
        return eventService.findUpcoming(dias);
    }

    @PostMapping("/eventos")
    public AgendaEvent create(@RequestBody AgendaEvent event) {
        return eventService.save(event);
    }

    @GetMapping("/notificacoes")
    public List<Notification> notifications() {
        return notificationRepository.findByReadFalseOrderByCreatedAtDesc();
    }

    @PatchMapping("/notificacoes/{id}/lida")
    public void markRead(@PathVariable UUID id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }
}
