package br.com.sinapta.ecossistema.relatorios;

import java.math.BigDecimal;

public record ChannelPerformance(String channel, BigDecimal invested, int leads, BigDecimal costPerLead) {
}
