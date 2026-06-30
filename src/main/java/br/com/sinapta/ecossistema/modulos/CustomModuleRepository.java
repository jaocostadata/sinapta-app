package br.com.sinapta.ecossistema.modulos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomModuleRepository extends JpaRepository<CustomModule, UUID> {

    List<CustomModule> findAllByOrderByDisplayOrderAsc();
}
