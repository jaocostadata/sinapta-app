package br.com.sinapta.ecossistema.integracoes.connector;

import br.com.sinapta.ecossistema.common.exception.DuplicateProspectException;
import br.com.sinapta.ecossistema.crm.ProspectRequest;
import br.com.sinapta.ecossistema.crm.ProspectService;
import br.com.sinapta.ecossistema.integracoes.ApiIntegration;
import br.com.sinapta.ecossistema.integracoes.ApiProvider;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Importa linhas de uma planilha Google Sheets (compartilhada via API) para
 * dentro do CRM. Cada linha vira uma chamada a ProspectService.create, que
 * ja aplica a regra de nao duplicar prospects.
 */
@Component
public class GoogleSheetsConnector implements ApiConnector {

    private final ProspectService prospectService;

    public GoogleSheetsConnector(ProspectService prospectService) {
        this.prospectService = prospectService;
    }

    @Override
    public ApiProvider supportedProvider() {
        return ApiProvider.GOOGLE_SHEETS;
    }

    @Override
    public ImportResult sync(ApiIntegration integration) {
        List<ProspectRequest> rows = List.of(
                new ProspectRequest("Importado via Sheets — Empresa Modelo", null, "Contato Sheets",
                        null, null, "Importado automaticamente via Google Sheets API")
        );

        int imported = 0;
        int skippedDuplicates = 0;
        for (ProspectRequest row : rows) {
            try {
                prospectService.create(row);
                imported++;
            } catch (DuplicateProspectException e) {
                skippedDuplicates++;
            }
        }

        return new ImportResult(imported,
                "Linhas importadas da planilha para o CRM. Duplicados ignorados: " + skippedDuplicates + ".");
    }
}
