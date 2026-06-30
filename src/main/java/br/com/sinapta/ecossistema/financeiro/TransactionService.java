package br.com.sinapta.ecossistema.financeiro;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> findAll() {
        return repository.findAll();
    }

    public Transaction save(Transaction transaction) {
        return repository.save(transaction);
    }

    public FinancialBalance balanceForMonth(LocalDate referenceDate) {
        return balanceForRange(referenceDate.withDayOfMonth(1), referenceDate.withDayOfMonth(referenceDate.lengthOfMonth()));
    }

    public FinancialBalance balanceForRange(LocalDate start, LocalDate end) {
        List<Transaction> transactions = repository.findByDateBetween(start, end);
        BigDecimal receitas = sumByType(transactions, TransactionType.RECEITA);
        BigDecimal despesas = sumByType(transactions, TransactionType.DESPESA);
        return new FinancialBalance(receitas, despesas, receitas.subtract(despesas));
    }

    private BigDecimal sumByType(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
