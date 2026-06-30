package br.com.sinapta.ecossistema.integracoes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ImportLogRepository extends JpaRepository<ImportLog, UUID> {

    List<ImportLog> findAllByOrderByCreatedAtDesc();
}
