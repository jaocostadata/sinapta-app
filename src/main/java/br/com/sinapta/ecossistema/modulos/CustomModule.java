package br.com.sinapta.ecossistema.modulos;

import br.com.sinapta.ecossistema.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Modulo cadastrado dinamicamente pelo usuario (aba "Novo modulo" na
 * interface), para o ecossistema crescer sem precisar de deploy de codigo
 * para cada nova area de gestao simples (link + descricao). Modulos com
 * regras de negocio proprias continuam sendo pacotes Java dedicados, como
 * os demais nesta aplicacao.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "custom_modules")
public class CustomModule extends BaseEntity {

    private String name;
    private String icon;
    private String description;
    private int displayOrder;
}
