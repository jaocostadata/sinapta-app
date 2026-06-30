package br.com.sinapta.ecossistema.integracoes;

import br.com.sinapta.ecossistema.common.BaseEntity;
import br.com.sinapta.ecossistema.common.crypto.AttributeEncryptor;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Conexao com a API de um aplicativo externo (Google Ads, Meta, etc.),
 * usada para importar dados automaticamente para dentro do ecossistema,
 * evitando digitacao manual.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "api_integrations")
public class ApiIntegration extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private ApiProvider provider;

    @Enumerated(EnumType.STRING)
    private TargetModule targetModule;

    @Enumerated(EnumType.STRING)
    private IntegrationStatus status = IntegrationStatus.NAO_CONECTADO;

    @Convert(converter = AttributeEncryptor.class)
    private String apiToken;

    private Instant lastSyncAt;
}
