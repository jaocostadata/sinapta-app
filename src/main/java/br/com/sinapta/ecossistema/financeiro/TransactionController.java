package br.com.sinapta.ecossistema.financeiro;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/financeiro")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/lancamentos")
    public List<Transaction> list() {
        return service.findAll();
    }

    @PostMapping("/lancamentos")
    public Transaction create(@RequestBody Transaction transaction) {
        return service.save(transaction);
    }

    @GetMapping("/saldo")
    public FinancialBalance balance(@RequestParam(required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate referencia) {
        return service.balanceForMonth(referencia != null ? referencia : LocalDate.now());
    }
}
