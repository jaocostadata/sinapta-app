package br.com.sinapta.ecossistema.marketing;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CampaignService {

    private final CampaignRepository repository;

    public CampaignService(CampaignRepository repository) {
        this.repository = repository;
    }

    public List<Campaign> findAll() {
        return repository.findAll();
    }

    public List<Campaign> findPaidTraffic() {
        return repository.findByType(CampaignType.TRAFEGO_PAGO);
    }

    public PaidTrafficSummary paidTrafficSummary() {
        List<Campaign> campaigns = findPaidTraffic();
        BigDecimal totalInvested = campaigns.stream()
                .map(Campaign::getAmountSpent)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalLeads = campaigns.stream().mapToInt(Campaign::getLeadsGenerated).sum();
        BigDecimal averageCpl = totalLeads == 0 ? BigDecimal.ZERO
                : totalInvested.divide(BigDecimal.valueOf(totalLeads), 2, RoundingMode.HALF_UP);
        return new PaidTrafficSummary(totalInvested, averageCpl, totalLeads);
    }

    public Campaign save(Campaign campaign) {
        return repository.save(campaign);
    }
}
