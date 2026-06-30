package br.com.sinapta.ecossistema.auth;

import br.com.sinapta.ecossistema.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String name;

    @Column(unique = true, nullable = false)
    private String email;
    private String passwordHash;
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private Role role = Role.COLABORADOR;

    public User(String name, String email, String passwordHash, Role role) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}
