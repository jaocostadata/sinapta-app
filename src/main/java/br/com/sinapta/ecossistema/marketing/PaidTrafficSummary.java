package br.com.sinapta.ecossistema.marketing;

import java.math.BigDecimal;

public record PaidTrafficSummary(BigDecimal totalInvested, BigDecimal averageCostPerLead, int totalLeads) {
}
