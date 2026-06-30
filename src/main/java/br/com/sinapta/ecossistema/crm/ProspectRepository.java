package br.com.sinapta.ecossistema.crm;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProspectRepository extends JpaRepository<Prospect, UUID> {

    List<Prospect> findByNormalizedKeyOrderByCreatedAtDesc(String normalizedKey);
}
