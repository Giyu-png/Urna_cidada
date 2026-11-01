# Urna Cidadã

Projeto acadêmico de extensão em **Programação Orientada a Objetos (POO)**: um simulador educativo de votação para **deputado federal** e **presidente**, acompanhado de uma aplicação independente para consultar resultados.

> **Simulação, não eleição real.** A Urna Cidadã não é um sistema da Justiça Eleitoral. As candidaturas e legendas são paródias humorísticas, com referências a memes, personagens e algumas personalidades conhecidas; não representam candidaturas, partidos ou posicionamentos reais.

## Funcionalidades entregues

- **Urna Java Swing:** visor, fotos, números dos candidatos, teclado, BRANCO, CORRIGE e CONFIRMA; votação sequencial dos dois cargos e tela de conclusão.
- **Catálogo centralizado em Java:** candidaturas, partidos e imagens PNG, compartilhados entre a urna e a apuração.
- **Votos simulados:** nominais, de legenda para deputado federal, brancos e nulos.
- **Cloud Firestore:** a urna sincroniza uma sessão concluída em segundo plano, com transação e identificador de sessão para evitar contagens duplicadas. Falhas permitem tentar novamente ou encerrar sem confirmação de gravação.
- **Painel de apuração separado:** totais por cargo, votos, percentuais, miniaturas e indicação da candidatura **mais votada até o momento**; somente leitura do Firestore. Não declara eleitos e não implementa o cálculo de cadeiras.
- **Testes JUnit e Firestore Emulator** executados no pipeline de integração contínua do GitHub.

Este repositório contém a urna e a apuração. **Não inclui um assistente LLM nem um minigame de desinformação.**

## Tecnologias

Java 21, Java Swing, Maven, JUnit 5, Google Cloud Firestore (SDK Java) e Firebase Emulator para testes automatizados.

## Executar

Pré-requisitos para desenvolver: **JDK 21** e **Maven**. Abra o projeto Maven no IntelliJ, ou, pelo terminal:

```bash
mvn clean test
mvn package
java -jar target/urna-cidada-0.1.0.jar
```

O comando `mvn package` gera um JAR com as dependências da aplicação. Para executar apenas o painel de apuração, com o JAR já criado:

```bash
java -cp target/urna-cidada-0.1.0.jar br.edu.urnacidada.apuracao.ApuracaoMain
```

No IntelliJ, as classes principais são:

- `br.edu.urnacidada.Main` — urna de votação;
- `br.edu.urnacidada.apuracao.ApuracaoMain` — resultados da simulação.

### Acesso ao Firebase

A urna usa **Application Default Credentials (ADC)** no computador autorizado. Cada desenvolvedor usa **sua própria conta**, com permissão concedida no projeto `urna-cidada-pjde-3` pelo Google Cloud IAM. Um método de configuração local é:

```bash
gcloud auth application-default login
```

Esse comando **não** concede acesso por si só: a conta precisa estar autorizada no IAM. A integração acessa o Firestore pela internet, mas não precisa de API própria nem de servidor adicional. **Não coloque tokens, JSON de contas de serviço ou outras credenciais no GitHub.** Não execute com conta administrativa em computadores compartilhados.

O código registra votos em `simulacoes/demonstracao-interface-2026`. A urna e o painel consultam a mesma simulação. Dados antigos **não são apagados**: como os candidatos foram substituídos, votos de números anteriores podem aparecer na apuração como históricos de candidatos não cadastrados. Prepare uma simulação separada antes de usar os resultados numa oficina, caso precise evitar essa mistura.

Veja [docs/firebase.md](docs/firebase.md) para detalhes técnicos.

## Candidaturas e números disponíveis

As candidaturas abaixo são **fictícias/satíricas** e existem apenas dentro do simulador.

| Cargo | Número | Nome usado na simulação | Legenda fictícia |
| --- | --- | --- | --- |
| Presidente | `67` | Coloridinho | Partido Teen |
| Presidente | `10` | Gato Nihao | Partido RacionaisMC |
| Presidente | `90` | P Diddy | Partido ILHA |
| Presidente | `66` | IT a Coisa | Partido Demoniaco |
| Deputado federal | `6767` | Emily Vik | Partido Teen |
| Deputado federal | `1069` | Gato Babao | Partido RacionaisMC |
| Deputado federal | `9022` | Elon Musk | Partido ILHA |
| Deputado federal | `6666` | cthulhu | Partido Demoniaco |

**Voto de legenda:** para deputado federal, digite apenas o número de dois dígitos de uma legenda cadastrada (`67`, `10`, `90` ou `66`) e confirme.

Para cadastrar ou modificar candidatos, edite `src/main/java/br/edu/urnacidada/service/CatalogoFicticio.java`. As imagens ficam em `src/main/resources/candidatos/`. O arquivo [LEIA-ME.md](src/main/resources/candidatos/LEIA-ME.md) documenta os nomes dos PNGs. As fotografias e referências culturais precisam ser avaliadas quanto à adequação ao público e direitos de uso antes de uma apresentação externa.

## Testes

```bash
mvn clean test
```

Para rodar também os testes de persistência no **emulador local**, sem escrever no Firestore de produção, instale o Firebase CLI e execute:

```bash
firebase emulators:exec --only firestore --project demo-urna-cidada "mvn clean verify"
```

Os testes do Firestore só executam quando `FIRESTORE_EMULATOR_HOST` está definido pelo emulador. Consulte [docs/testes.md](docs/testes.md) e o workflow [Java CI](.github/workflows/java-ci.yml).

## Organização do código

- `model` — entidades e tipos de voto;
- `service` — catálogo fictício e regras da simulação;
- `controller` — sequência e estado de votação;
- `view` — interface da urna e carregamento de fotos;
- `repository` — persistência local e no Firestore;
- `apuracao` — aplicação administrativa independente;
- `config` — inicialização de clientes do Firestore.

Mais detalhes: [arquitetura](docs/arquitetura.md) e [requisitos](docs/requisitos.md).

## Segurança e limitações

O sistema armazena contagens agregadas e marcadores aleatórios de sessões, sem nome, CPF ou a combinação dos votos do participante. O acesso ao Firestore depende do IAM da identidade local; **não distribua credenciais privilegiadas**. A conexão direta com o SDK de servidor é adequada apenas para execução supervisionada em computadores autorizados, e não para distribuição irrestrita.

A votação e a apuração são **didáticas**; não reproduzem a segurança, auditoria ou todas as regras de uma eleição oficial. A apuração não define vencedores de eleição proporcional.

## Evidências do projeto de extensão

Para o relatório acadêmico, preservem capturas da urna, do painel, dos testes, do esquema do Firestore e da oficina realizada, observando as autorizações necessárias para imagens de estudantes.

Projeto desenvolvido exclusivamente para fins educacionais.
