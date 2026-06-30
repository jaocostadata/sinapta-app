package br.com.sinapta.ecossistema.relatorios;

import br.com.sinapta.ecossistema.financeiro.FinancialBalance;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/relatorios")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/trafego-pago")
    public List<ChannelPerformance> paidTraffic() {
        return reportService.paidTrafficByChannel();
    }

    @GetMapping("/financeiro")
    public FinancialBalance financial(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return reportService.financialReport(inicio, fim);
    }

    @GetMapping("/trafego-pago/exportar")
    public ResponseEntity<byte[]> exportPaidTrafficCsv() {
        String header = "canal;investido;leads;cpl\n";
        String rows = reportService.paidTrafficByChannel().stream()
                .map(c -> "%s;%s;%d;%s".formatted(c.channel(), c.invested(), c.leads(), c.costPerLead()))
                .collect(Collectors.joining("\n"));

        byte[] csvBytes = (header + rows).getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"relatorio-trafego-pago.csv\"")
                .body(csvBytes);
    }
}
