package br.com.sinapta.ecossistema.config;

import br.com.sinapta.ecossistema.auth.Role;
import br.com.sinapta.ecossistema.auth.User;
import br.com.sinapta.ecossistema.auth.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Cria o primeiro usuário administrador em produção a partir de variáveis
 * de ambiente, já que o DataSeeder de dados de exemplo só roda no perfil
 * dev. Sem isso ninguém conseguiria fazer login no primeiro deploy.
 */
@Component
@Profile("prod")
public class ProdAdminSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ProdAdminSeeder.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;

    public ProdAdminSeeder(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                            @Value("${app.admin.email:}") String adminEmail,
                            @Value("${app.admin.password:}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }
        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            log.warn("Nenhum usuário cadastrado e SINAPTA_ADMIN_EMAIL/SINAPTA_ADMIN_PASSWORD não foram definidos. "
                    + "Defina essas variáveis de ambiente para criar o primeiro usuário administrador.");
            return;
        }

        userRepository.save(new User("Administrador Sinapta", adminEmail,
                passwordEncoder.encode(adminPassword), Role.ADMIN));
        log.info("Usuário administrador inicial criado: {}", adminEmail);
    }
}
