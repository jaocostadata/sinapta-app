package br.com.sinapta.ecossistema.config;

import br.com.sinapta.ecossistema.aplicacoes.AppUsed;
import br.com.sinapta.ecossistema.aplicacoes.AppUsedRepository;
import br.com.sinapta.ecossistema.auth.Role;
import br.com.sinapta.ecossistema.auth.User;
import br.com.sinapta.ecossistema.auth.UserRepository;
import br.com.sinapta.ecossistema.calendario.AgendaEvent;
import br.com.sinapta.ecossistema.calendario.AgendaEventRepository;
import br.com.sinapta.ecossistema.contratos.Contract;
import br.com.sinapta.ecossistema.contratos.ContractRepository;
import br.com.sinapta.ecossistema.contratos.ContractStatus;
import br.com.sinapta.ecossistema.crm.ProspectRequest;
import br.com.sinapta.ecossistema.crm.ProspectService;
import br.com.sinapta.ecossistema.marketing.Campaign;
import br.com.sinapta.ecossistema.marketing.CampaignRepository;
import br.com.sinapta.ecossistema.marketing.CampaignStatus;
import br.com.sinapta.ecossistema.marketing.CampaignType;
import br.com.sinapta.ecossistema.site.SiteAccess;
import br.com.sinapta.ecossistema.site.SiteAccessRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Popula dados de exemplo em ambiente de desenvolvimento, espelhando o
 * protótipo de telas aprovado (site, campanhas, prospects, contrato).
 */
@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final SiteAccessRepository siteAccessRepository;
    private final CampaignRepository campaignRepository;
    private final ProspectService prospectService;
    private final ContractRepository contractRepository;
    private final AppUsedRepository appUsedRepository;
    private final AgendaEventRepository agendaEventRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String siteAdminLogin;
    private final String siteAdminPassword;

    public DataSeeder(UserRepository userRepository, SiteAccessRepository siteAccessRepository,
                       CampaignRepository campaignRepository, ProspectService prospectService,
                       ContractRepository contractRepository, AppUsedRepository appUsedRepository,
                       AgendaEventRepository agendaEventRepository, BCryptPasswordEncoder passwordEncoder,
                       @Value("${app.site-seed.admin-login}") String siteAdminLogin,
                       @Value("${app.site-seed.admin-password}") String siteAdminPassword) {
        this.userRepository = userRepository;
        this.siteAccessRepository = siteAccessRepository;
        this.campaignRepository = campaignRepository;
        this.prospectService = prospectService;
        this.contractRepository = contractRepository;
        this.appUsedRepository = appUsedRepository;
        this.agendaEventRepository = agendaEventRepository;
        this.passwordEncoder = passwordEncoder;
        this.siteAdminLogin = siteAdminLogin;
        this.siteAdminPassword = siteAdminPassword;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User admin = new User("Administrador Sinapta", "admin@sinapta.dev.br",
                passwordEncoder.encode("TrocarSenha123!"), Role.ADMIN);
        userRepository.save(admin);
        log.info("Usuário admin de desenvolvimento criado: admin@sinapta.dev.br / TrocarSenha123! (troque após o primeiro login)");

        siteAccessRepository.save(new SiteAccess(
                "Site institucional Sinapta",
                "https://sinapta.dev.br/",
                "https://sinapta.dev.br/admin",
                siteAdminLogin,
                siteAdminPassword));

        Campaign google = new Campaign();
        google.setTitle("Google Ads · Busca");
        google.setType(CampaignType.TRAFEGO_PAGO);
        google.setStatus(CampaignStatus.EM_ANDAMENTO);
        google.setChannel("Google Ads");
        google.setBudget(new BigDecimal("5000.00"));
        google.setAmountSpent(new BigDecimal("4900.00"));
        google.setLeadsGenerated(266);
        campaignRepository.save(google);

        Campaign meta = new Campaign();
        meta.setTitle("Meta Ads · Conversão");
        meta.setType(CampaignType.TRAFEGO_PAGO);
        meta.setStatus(CampaignStatus.EM_ANDAMENTO);
        meta.setChannel("Meta Ads");
        meta.setBudget(new BigDecimal("2000.00"));
        meta.setAmountSpent(new BigDecimal("1600.00"));
        meta.setLeadsGenerated(87);
        campaignRepository.save(meta);

        Campaign linkedin = new Campaign();
        linkedin.setTitle("LinkedIn Ads · ABM");
        linkedin.setType(CampaignType.TRAFEGO_PAGO);
        linkedin.setStatus(CampaignStatus.PAUSADA);
        linkedin.setChannel("LinkedIn Ads");
        linkedin.setBudget(new BigDecimal("500.00"));
        linkedin.setAmountSpent(new BigDecimal("300.00"));
        linkedin.setLeadsGenerated(16);
        campaignRepository.save(linkedin);

        prospectService.create(new ProspectRequest("Padaria Bom Pão", "11.111.111/0001-11",
                "João Silva", "(11) 90000-0000", "joao@bompao.com.br", "Primeiro contato via tráfego pago"));
        prospectService.create(new ProspectRequest("Clínica Vitalis", "33.333.333/0001-33",
                "Pedro Alves", "(11) 90000-0002", "pedro@vitalis.com.br", "Contrato fechado"));

        contractRepository.save(contractEntity("Contrato social — Sinapta", "Sinapta Tecnologia",
                ContractStatus.VIGENTE, LocalDate.now().plusYears(2)));
        contractRepository.save(contractEntity("Fornecedor de hospedagem", "Hospedagem do site",
                ContractStatus.VIGENTE, LocalDate.now().plusDays(25)));

        appUsedRepository.save(appUsed("Google Ads", "Tráfego pago"));
        appUsedRepository.save(appUsed("Meta Business", "Tráfego pago"));
        appUsedRepository.save(appUsed("Google Sheets", "Planilhas"));
        appUsedRepository.save(appUsed("Notion", "Organização"));

        AgendaEvent hostingPayment = new AgendaEvent();
        hostingPayment.setTitle("Pagamento hospedagem do site");
        hostingPayment.setEventDateTime(Instant.now().plus(2, ChronoUnit.DAYS));
        hostingPayment.setNotifyDaysBefore(3);
        hostingPayment.setSourceModule("FINANCEIRO");
        agendaEventRepository.save(hostingPayment);

        log.info("Dados de exemplo carregados com sucesso.");
    }

    private Contract contractEntity(String title, String counterparty, ContractStatus status, LocalDate expiration) {
        Contract contract = new Contract();
        contract.setTitle(title);
        contract.setCounterparty(counterparty);
        contract.setStatus(status);
        contract.setExpirationDate(expiration);
        return contract;
    }

    private AppUsed appUsed(String name, String category) {
        AppUsed appUsed = new AppUsed();
        appUsed.setName(name);
        appUsed.setCategory(category);
        return appUsed;
    }
}
