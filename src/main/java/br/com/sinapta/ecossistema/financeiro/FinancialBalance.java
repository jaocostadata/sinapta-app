package br.com.sinapta.ecossistema.financeiro;

import java.math.BigDecimal;

public record FinancialBalance(BigDecimal totalReceitas, BigDecimal totalDespesas, BigDecimal saldo) {
}
