package br.com.sinapta.ecossistema.site;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SiteAccessRepository extends JpaRepository<SiteAccess, UUID> {
}
