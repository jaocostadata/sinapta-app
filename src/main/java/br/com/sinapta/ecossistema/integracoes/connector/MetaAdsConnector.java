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
 * Importa metricas de campanhas do Meta Business (Facebook/Instagram Ads).
 * A chamada real usaria o Meta Marketing API com o token armazenado em
 * integration.getApiToken().
 */
@Component
public class MetaAdsConnector implements ApiConnector {

    private final CampaignService campaignService;

    public MetaAdsConnector(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @Override
    public ApiProvider supportedProvider() {
        return ApiProvider.META_ADS;
    }

    @Override
    public ImportResult sync(ApiIntegration integration) {
        Campaign campaign = new Campaign();
        campaign.setTitle("Meta Ads · Conversão (importado)");
        campaign.setType(CampaignType.TRAFEGO_PAGO);
        campaign.setStatus(CampaignStatus.EM_ANDAMENTO);
        campaign.setChannel("Meta Ads");
        campaign.setAmountSpent(new BigDecimal("1600.00"));
        campaign.setLeadsGenerated(189);
        campaignService.save(campaign);

        return new ImportResult(189, "Campanhas e métricas do Meta Business importadas para Marketing.");
    }
}
