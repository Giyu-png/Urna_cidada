# Urna Cidadã

**Simulador educativo de votação eletrônica e material de apoio sobre desinformação digital**

Projeto acadêmico de extensão do curso de **Ciência da Computação**, desenvolvido com conceitos de **Programação Orientada a Objetos (POO)**. O repositório reúne uma urna simulada em Java Swing, um painel independente de apuração e um site educativo sobre verificação de informações.

> **Uso exclusivamente didático:** este software não é uma urna oficial, não integra os sistemas da Justiça Eleitoral e não reproduz todas as regras, garantias de segurança e procedimentos de uma eleição real. As candidaturas e legendas cadastradas são recursos de simulação e devem ser revisadas antes de sua utilização em atividades externas.

## Componentes

| Componente | Descrição | Localização |
| --- | --- | --- |
| Urna Cidadã | Interface de votação simulada para deputado federal e presidente, com votos nominais, de legenda, brancos e nulos | `br.edu.urnacidada.Main` |
| Painel de apuração | Consulta de resultados agregados, percentuais e candidato mais votado até o momento | `br.edu.urnacidada.apuracao.ApuracaoMain` |
| Site educativo | Conteúdo sobre desinformação, deepfakes, verificação de fontes e quiz interativo | [`site/index.html`](site/index.html) |

A urna e a apuração são aplicações **Java Swing** distintas. O site é uma página **HTML, CSS e JavaScript**, independente do Firebase e do programa Java. O quiz é uma atividade educativa e não substitui os questionários de avaliação de impacto previstos no projeto de extensão.

## Funcionalidades do simulador

- Votação sequencial para um cargo proporcional e outro majoritário.
- Teclado numérico, botões **BRANCO**, **CORRIGE** e **CONFIRMA**.
- Cadastro centralizado de candidaturas, partidos e imagens locais.
- Classificação de votos nominais, de legenda, brancos e nulos.
- Registro local da sessão e sincronização assíncrona com **Cloud Firestore**.
- Transações para evitar dupla contabilização da mesma sessão.
- Tratamento de falha de sincronização com possibilidade de nova tentativa ou continuação sem confirmação remota.
- Painel independente de consulta, com totais, percentuais e ordenação.
- Testes unitários e de integração com o **Firestore Emulator**, executados também no GitHub Actions.

## Tecnologias

- **Java 21**, Java Swing e Maven.
- **JUnit 5** para testes automatizados.
- **Google Cloud Firestore** para persistência de contagens de votos fictícios.
- **Firebase Emulator** para testes locais de integração.
- **HTML, JavaScript e Tailwind CSS** no material educativo web.

## Estrutura do repositório

```text
.github/workflows/      Automação de testes
docs/                   Requisitos, arquitetura, persistência e testes
site/index.html         Site educativo sobre desinformação
src/main/java/          Código Java do simulador e da apuração
src/main/resources/     Fotografias e outros recursos locais
src/test/java/          Testes JUnit
pom.xml                 Configuração do projeto Maven
firebase.json           Configuração do emulador Firebase
```

## Como executar

### Pré-requisitos

- JDK **21**.
- Maven ou um ambiente de desenvolvimento com suporte ao projeto Maven, como o IntelliJ IDEA.
- Para sincronizar com o Firestore real: conexão com a internet e credenciais locais **ADC** de uma conta autorizada no Google Cloud IAM.

### Compilação e execução da urna

Na pasta raiz do repositório:

```bash
mvn clean verify
mvn package
java -jar target/urna-cidada-0.1.0.jar
```

O Maven Shade empacota as bibliotecas necessárias no JAR. A autenticação com o Firebase **não** é embutida nesse arquivo.

No IntelliJ IDEA, importe o projeto Maven e execute a classe `br.edu.urnacidada.Main`.

### Executar o painel de apuração

Após gerar o JAR:

```bash
java -cp target/urna-cidada-0.1.0.jar br.edu.urnacidada.apuracao.ApuracaoMain
```

Ou execute diretamente a classe `br.edu.urnacidada.apuracao.ApuracaoMain` no IntelliJ IDEA. O painel é independente da urna, mas usa as mesmas permissões locais para consultar o Firestore.

### Abrir o site educativo

Abra o arquivo [`site/index.html`](site/index.html) em um navegador. O site oferece quatro seções: **Entenda**, **Deepfakes**, **Checagem** e **Quiz**.

A página carrega **Tailwind CSS e fontes externas**; portanto, sua apresentação visual pode depender de internet. Teste no navegador e na conexão que serão utilizados na atividade.

## Firebase e controle de acesso

A conexão Java utiliza **Application Default Credentials (ADC)**. Cada desenvolvedor autorizado deve usar sua própria identidade Google e receber apenas as permissões necessárias no IAM do projeto.

Uma forma de configurar as credenciais na própria máquina é:

```bash
gcloud auth application-default login
```

O login **não concede permissão automaticamente**: a conta deve estar autorizada no projeto. Nunca coloque arquivos de credenciais, tokens ou chaves privadas no GitHub, no JAR ou em computadores compartilhados.

O simulador atual utiliza `simulacoes/demonstracao-interface-2026` no Firestore. Registros antigos permanecem armazenados e podem aparecer na apuração quando o catálogo de candidaturas for alterado. A atualização de candidatos **não altera nem apaga os votos anteriores**.

A arquitetura atual foi planejada para execução supervisionada por integrantes autorizados. Não se destina à distribuição irrestrita a usuários sem credenciais.

Mais detalhes: [`docs/firebase.md`](docs/firebase.md).

## Cadastro de candidaturas

Os cadastros ficam centralizados em:

`src/main/java/br/edu/urnacidada/service/CatalogoFicticio.java`

As fotografias são recursos locais armazenados em:

`src/main/resources/candidatos/`

Para substituir ou adicionar candidaturas, atualize a lista de fichas no catálogo e os arquivos de imagem correspondentes. Verifique a unicidade dos números por cargo, os partidos cadastrados e os **testes automatizados que dependem dos números de exemplo**. A urna e o painel utilizam o mesmo cadastro, evitando listas duplicadas.

Este README não fixa uma lista de candidatos porque o catálogo pode ser revisado para diferentes ações educativas.

## Testes e integração contínua

Para os testes unitários:

```bash
mvn clean test
```

Para testar também a persistência em um **emulador local**, sem escrever no banco real:

```bash
firebase emulators:exec --only firestore --project demo-urna-cidada "mvn clean verify"
```

A execução com emulador requer a **Firebase CLI** instalada. O workflow [`Java CI`](.github/workflows/java-ci.yml) executa as verificações automatizadas. Consulte [`docs/testes.md`](docs/testes.md) para o roteiro de validação manual.

## Documentação

- [Requisitos funcionais](docs/requisitos.md)
- [Arquitetura e conceitos de POO](docs/arquitetura.md)
- [Integração com Cloud Firestore](docs/firebase.md)
- [Plano de testes](docs/testes.md)

## Privacidade, limitações e uso acadêmico

O simulador contabiliza votos fictícios por categoria e utiliza identificadores aleatórios de sessão para evitar duplicidade. Não solicita nome ou CPF dos participantes nem armazena a associação entre os dois votos de uma pessoa.

O painel mostra dados agregados. A indicação de quem tem mais votos é **parcial e ilustrativa**, não uma declaração oficial de vencedor nem um cálculo de vagas de representação proporcional.

Para a documentação do projeto de extensão, preservem evidências do desenvolvimento, dos testes e das atividades educativas, observando as autorizações aplicáveis ao uso de imagens dos participantes.

**Projeto desenvolvido para fins de educação, cidadania e alfabetização digital.**
