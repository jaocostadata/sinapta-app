package br.com.sinapta.ecossistema.contatos;

import br.com.sinapta.ecossistema.common.BaseEntity;
import br.com.sinapta.ecossistema.common.crypto.AttributeEncryptor;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "credentials")
public class Credential extends BaseEntity {

    private String systemName;
    private String url;

    @Convert(converter = AttributeEncryptor.class)
    private String login;

    @Convert(converter = AttributeEncryptor.class)
    private String password;

    private String notes;
}
