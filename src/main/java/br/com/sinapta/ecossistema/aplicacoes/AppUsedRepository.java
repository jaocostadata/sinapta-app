package br.com.sinapta.ecossistema.aplicacoes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppUsedRepository extends JpaRepository<AppUsed, UUID> {
}
