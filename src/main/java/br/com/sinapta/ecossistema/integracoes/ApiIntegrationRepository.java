package br.com.sinapta.ecossistema.integracoes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApiIntegrationRepository extends JpaRepository<ApiIntegration, UUID> {
}
