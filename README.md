# Sinapta · Ecossistema digital

Backend em Java (Spring Boot) do aplicativo de gestão do ecossistema digital da Sinapta — site, marketing/tráfego pago, comercial (CRM), financeiro, relatórios, calendário, contatos e senhas, aplicações utilizadas, contratos, integrações de API e módulos customizados, tudo em um único lugar.

## Stack

- Java 21 + Spring Boot 3 (Web, Data JPA, Security, Validation)
- Banco: H2 em arquivo no perfil `dev` (zero setup), PostgreSQL no perfil `prod`
- Autenticação: JWT (login em `POST /api/auth/login`)
- Campos sensíveis (senhas de sites, credenciais, tokens de API) são criptografados em banco com AES-256-GCM antes de persistir

## Como rodar

```bash
mvn spring-boot:run
```

Sobe em `http://localhost:8080` com o perfil `dev` (padrão), banco H2 em `./data/sinapta`, console em `/h2-console`.

No primeiro start, o `DataSeeder` cria:
- usuário admin: `admin@sinapta.dev.br` / `TrocarSenha123!` (troque após o primeiro login)
- acesso do site institucional (`sinapta.dev.br` / `sinapta.dev.br/admin`) — o login e a senha reais **não ficam no código**; defina `SINAPTA_SITE_ADMIN_LOGIN` e `SINAPTA_SITE_ADMIN_PASSWORD` no seu `.env` local antes de rodar (sem isso, entra um valor de exemplo)
- campanhas de tráfego pago, prospects, contrato e app de exemplo, para refletir o protótipo de telas aprovado

Para produção, defina as variáveis de ambiente antes de subir com `--spring.profiles.active=prod`:

| Variável | Uso |
|---|---|
| `SINAPTA_CRYPTO_KEY` | chave de criptografia AES dos campos sensíveis |
| `SINAPTA_JWT_SECRET` | segredo de assinatura dos tokens JWT |
| `SINAPTA_DB_URL`, `SINAPTA_DB_USER`, `SINAPTA_DB_PASSWORD` | conexão PostgreSQL (no Render, o `docker-entrypoint.sh` monta `SINAPTA_DB_URL` a partir de `DB_HOST`/`DB_PORT`/`DB_NAME`) |
| `SINAPTA_STORAGE_DIR` | diretório onde os anexos enviados (contratos, comprovantes) são salvos |
| `SINAPTA_ADMIN_EMAIL`, `SINAPTA_ADMIN_PASSWORD` | credenciais do primeiro usuário admin, criado pelo `ProdAdminSeeder` no primeiro start em produção |
| `PORT` | porta em que o servidor escuta (o Render injeta automaticamente; local não precisa definir) |

## Deploy no Render

O repositório já tem tudo que o Render precisa: `Dockerfile` (build multi-stage com Maven + runtime JRE), `docker-entrypoint.sh` (monta a URL JDBC a partir das variáveis do Postgres do Render) e `render.yaml` (Blueprint — cria o web service e o banco PostgreSQL de uma vez).

Passo a passo:

1. Suba este repositório para o GitHub (o Render faz deploy a partir de um repositório Git).
2. No painel do Render, escolha **New > Blueprint** e aponte para o repositório — ele vai ler o `render.yaml` automaticamente e propor criar:
   - o serviço web `sinapta-ecossistema` (Docker, plano free)
   - o banco `sinapta-db` (PostgreSQL, plano free)
   - um disco persistente de 1 GB em `/data/storage` para os anexos (contratos, comprovantes)
3. Antes de aplicar, preencha as duas variáveis marcadas como `sync: false` no blueprint (não ficam no `render.yaml` por serem credenciais):
   - `SINAPTA_ADMIN_EMAIL` — e-mail do primeiro usuário administrador
   - `SINAPTA_ADMIN_PASSWORD` — senha desse usuário (troque após o primeiro login)
4. `SINAPTA_CRYPTO_KEY` e `SINAPTA_JWT_SECRET` são gerados automaticamente pelo Render (`generateValue: true`) — não precisa definir.
5. Aplique o blueprint. O Render builda a imagem Docker, sobe o serviço e expõe uma URL pública `https://sinapta-ecossistema.onrender.com` (ou o nome que você escolher).
6. Teste o login: `POST https://<sua-url>.onrender.com/api/auth/login` com o e-mail/senha definidos no passo 3.

Observações importantes:
- O plano free do Postgres do Render expira em 30 dias — para uso real, migre para um plano pago antes do vencimento.
- O serviço web no plano free "dorme" após 15 min sem tráfego (primeira requisição depois disso demora alguns segundos).
- O healthcheck usa `/actuator/health` (Spring Boot Actuator), já liberado publicamente na `SecurityConfig`.
- Sem Blueprint, o deploy manual também funciona: crie um Web Service do tipo Docker apontando para este repo, crie um Postgres separado, e defina manualmente as variáveis de ambiente da tabela abaixo.

## Estrutura de pacotes (`br.com.sinapta.ecossistema`)

| Pacote | Tela correspondente | Conteúdo |
|---|---|---|
| `auth` | login | usuário, JWT, filtro de autenticação |
| `site` | Site | URL pública, painel admin, credenciais criptografadas |
| `marketing` | Marketing (+ sub-módulo Tráfego pago) | campanhas por tipo/canal, resumo de CPL |
| `crm` | Comercial / CRM | prospecção com regra de não duplicidade (ver abaixo) |
| `financeiro` | Financeiro | lançamentos, saldo do período |
| `relatorios` | Relatórios | agregações de tráfego pago e financeiro, exportação CSV |
| `calendario` | Calendário | agenda geral + `ReminderScheduler` (lembretes automáticos) |
| `contatos` | Contatos e senhas | credenciais de sistemas, sempre criptografadas |
| `aplicacoes` | Aplicações utilizadas | catálogo de apps usados pela empresa |
| `contratos` | Contratos | contratos essenciais, status calculado por proximidade do vencimento |
| `integracoes` | Importar dados (API) | conexões com APIs externas e importação automática de dados |
| `modulos` | Novo módulo | módulos simples cadastrados dinamicamente pelo usuário |
| `common` | — | entidade base, criptografia, anexos de arquivo, tratamento de erros |

## Regra de prospecção (planilha sem duplicidade)

`ProspectService.create` normaliza o documento (ou nome da empresa) e bloqueia um novo cadastro se já existir um prospect com a mesma chave **a menos que** algum registro existente já tenha sido marcado como `hotLead = true` — replicando a regra pedida: nunca duplicar uma prospecção inicial, exceto quando o lead é quente e vale remarketing.

## Importação de dados via API

`integracoes/connector/ApiConnector` é o ponto de extensão: cada API externa (Google Ads, Meta Ads, Google Sheets já implementados como exemplo; Analytics/Open Finance/RD Station prontos para receber implementação) tem sua própria classe, escolhida automaticamente pelo `IntegrationService` a partir do provedor cadastrado. Os conectores existentes gravam diretamente nos módulos de Marketing e CRM, reaproveitando as mesmas regras de negócio (inclusive a anti-duplicidade do CRM).

## Próximos passos sugeridos

- Conectar o frontend (o protótipo de telas já validado) a esta API REST
- Implementar as chamadas reais às APIs externas nos conectores (hoje simulam a importação)
- Adicionar exportação em PDF de relatórios (CSV já funcional)
- Restringir endpoints sensíveis (`/revelar` de senhas) por role, hoje exigem apenas usuário autenticado
