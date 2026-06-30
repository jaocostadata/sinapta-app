package br.com.sinapta.ecossistema.integracoes;

import br.com.sinapta.ecossistema.common.exception.ResourceNotFoundException;
import br.com.sinapta.ecossistema.integracoes.connector.ApiConnector;
import br.com.sinapta.ecossistema.integracoes.connector.ImportResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class IntegrationService {

    private final ApiIntegrationRepository integrationRepository;
    private final ImportLogRepository importLogRepository;
    private final Map<ApiProvider, ApiConnector> connectorsByProvider;

    public IntegrationService(ApiIntegrationRepository integrationRepository,
                               ImportLogRepository importLogRepository,
                               List<ApiConnector> connectors) {
        this.integrationRepository = integrationRepository;
        this.importLogRepository = importLogRepository;
        this.connectorsByProvider = connectors.stream()
                .collect(Collectors.toMap(ApiConnector::supportedProvider, Function.identity()));
    }

    public List<ApiIntegration> findAll() {
        return integrationRepository.findAll();
    }

    public ApiIntegration connect(ApiIntegration integration) {
        integration.setStatus(IntegrationStatus.CONECTADO);
        return integrationRepository.save(integration);
    }

    public ImportLog sync(UUID integrationId) {
        ApiIntegration integration = integrationRepository.findById(integrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Integração não encontrada: " + integrationId));

        ApiConnector connector = connectorsByProvider.get(integration.getProvider());
        ImportResult result = connector != null
                ? connector.sync(integration)
                : new ImportResult(0, "Conector para " + integration.getProvider()
                        + " ainda não implementado. Cadastre a chave de API e aguarde a próxima atualização.");

        integration.setLastSyncAt(Instant.now());
        if (connector != null) {
            integration.setStatus(IntegrationStatus.CONECTADO);
        }
        integrationRepository.save(integration);

        ImportLog log = new ImportLog(integration.getId(), integration.getProvider().name(),
                result.recordsImported(), result.message());
        return importLogRepository.save(log);
    }

    public List<ImportLog> recentLogs() {
        return importLogRepository.findAllByOrderByCreatedAtDesc();
    }
}
