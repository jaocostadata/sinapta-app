package br.com.sinapta.ecossistema.integracoes.connector;

import br.com.sinapta.ecossistema.integracoes.ApiIntegration;
import br.com.sinapta.ecossistema.integracoes.ApiProvider;

/**
 * Estrategia de importacao por provedor. Cada API suportada (Google Ads,
 * Meta, Sheets...) implementa esta interface; o IntegrationService escolhe
 * o connector certo a partir de ApiIntegration.getProvider(). Implementacoes
 * reais devem chamar o SDK/REST da API externa usando o token armazenado em
 * ApiIntegration.getApiToken() e gravar os dados no servico do modulo alvo.
 */
public interface ApiConnector {

    ApiProvider supportedProvider();

    ImportResult sync(ApiIntegration integration);
}
