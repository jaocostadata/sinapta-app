package br.com.sinapta.ecossistema.relatorios;

import br.com.sinapta.ecossistema.financeiro.FinancialBalance;
import br.com.sinapta.ecossistema.financeiro.TransactionService;
import br.com.sinapta.ecossistema.marketing.Campaign;
import br.com.sinapta.ecossistema.marketing.CampaignService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final CampaignService campaignService;
    private final TransactionService transactionService;

    public ReportService(CampaignService campaignService, TransactionService transactionService) {
        this.campaignService = campaignService;
        this.transactionService = transactionService;
    }

    public List<ChannelPerformance> paidTrafficByChannel() {
        Map<String, List<Campaign>> byChannel = campaignService.findPaidTraffic().stream()
                .collect(Collectors.groupingBy(c -> c.getChannel() == null ? "Outros" : c.getChannel()));

        return byChannel.entrySet().stream()
                .map(entry -> {
                    BigDecimal invested = entry.getValue().stream()
                            .map(Campaign::getAmountSpent)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    int leads = entry.getValue().stream().mapToInt(Campaign::getLeadsGenerated).sum();
                    BigDecimal cpl = leads == 0 ? BigDecimal.ZERO
                            : invested.divide(BigDecimal.valueOf(leads), 2, RoundingMode.HALF_UP);
                    return new ChannelPerformance(entry.getKey(), invested, leads, cpl);
                })
                .sorted(Comparator.comparing(ChannelPerformance::invested).reversed())
                .toList();
    }

    public FinancialBalance financialReport(LocalDate start, LocalDate end) {
        return transactionService.balanceForRange(start, end);
    }
}
