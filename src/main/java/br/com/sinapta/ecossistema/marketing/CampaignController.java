package br.com.sinapta.ecossistema.marketing;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/marketing")
public class CampaignController {

    private final CampaignService service;

    public CampaignController(CampaignService service) {
        this.service = service;
    }

    @GetMapping("/campanhas")
    public List<Campaign> list() {
        return service.findAll();
    }

    @PostMapping("/campanhas")
    public Campaign create(@RequestBody Campaign campaign) {
        return service.save(campaign);
    }

    @GetMapping("/trafego-pago")
    public List<Campaign> paidTraffic() {
        return service.findPaidTraffic();
    }

    @GetMapping("/trafego-pago/resumo")
    public PaidTrafficSummary paidTrafficSummary() {
        return service.paidTrafficSummary();
    }
}
