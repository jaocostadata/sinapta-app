package br.com.sinapta.ecossistema.site;

import br.com.sinapta.ecossistema.common.BaseEntity;
import br.com.sinapta.ecossistema.common.crypto.AttributeEncryptor;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Gestao do site: URL publica, URL do painel admin e as credenciais de
 * acesso (login + senha), armazenadas sempre criptografadas em banco.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "site_access")
public class SiteAccess extends BaseEntity {

    private String name;
    private String siteUrl;
    private String adminUrl;

    @Convert(converter = AttributeEncryptor.class)
    private String loginIdentifier;

    @Convert(converter = AttributeEncryptor.class)
    private String password;

    public SiteAccess(String name, String siteUrl, String adminUrl, String loginIdentifier, String password) {
        this.name = name;
        this.siteUrl = siteUrl;
        this.adminUrl = adminUrl;
        this.loginIdentifier = loginIdentifier;
        this.password = password;
    }
}
