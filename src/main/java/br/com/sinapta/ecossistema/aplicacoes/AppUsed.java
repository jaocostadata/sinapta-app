package br.com.sinapta.ecossistema.aplicacoes;

import br.com.sinapta.ecossistema.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "apps_used")
public class AppUsed extends BaseEntity {

    private String name;
    private String category;
    private String url;
}
