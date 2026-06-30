package br.com.sinapta.ecossistema.integracoes.connector;

import br.com.sinapta.ecossistema.integracoes.ApiIntegration;
import br.com.sinapta.ecossistema.integracoes.ApiProvider;
import br.com.sinapta.ecossistema.marketing.Campaign;
import br.com.sinapta.ecossistema.marketing.CampaignService;
import br.com.sinapta.ecossistema.marketing.CampaignStatus;
import br.com.sinapta.ecossistema.marketing.CampaignType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Importa metricas de campanhas do Google Ads para o sub-modulo de trafego
 * pago dentro de Marketing. A chamada real a Google Ads API (via
 * google-ads-java) entraria no lugar dos valores simulados abaixo, usando
 * integration.getApiToken() para autenticar.
 */
@Component
public class GoogleAdsConnector implements ApiConnector {

    private final CampaignService campaignService;

    public GoogleAdsConnector(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @Override
    public ApiProvider supportedProvider() {
        return ApiProvider.GOOGLE_ADS;
    }

    @Override
    public ImportResult sync(ApiIntegration integration) {
        Campaign campaign = new Campaign();
        campaign.setTitle("Google Ads · Busca (importado)");
        campaign.setType(CampaignType.TRAFEGO_PAGO);
        campaign.setStatus(CampaignStatus.EM_ANDAMENTO);
        campaign.setChannel("Google Ads");
        campaign.setAmountSpent(new BigDecimal("4900.00"));
        campaign.setLeadsGenerated(234);
        campaignService.save(campaign);

        return new ImportResult(234, "Campanhas e métricas do Google Ads importadas para Marketing.");
    }
}
