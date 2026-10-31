# Urna Cidadã

Simulador **educativo e apartidário** de votação para dois cargos (deputado federal e presidente), desenvolvido como projeto de extensão de Programação Orientada a Objetos.

> **Atenção:** este é um protótipo acadêmico com candidaturas e legendas fictícias. Não é um sistema eleitoral oficial nem se conecta à Justiça Eleitoral.

## Tecnologias

- Java 21, Java Swing e Maven.
- JUnit 5 para testes das regras de negócio.
- Persistência local em memória e sincronização com Cloud Firestore na branch de integração, via `SwingWorker` e transações idempotentes.

## Como executar

Pré-requisitos: JDK 21 e Maven 3.9 ou superior.

```bash
mvn clean test
mvn package
java -jar target/urna-cidada-0.1.0.jar
```

## Cadastro de candidatos e fotos

Os candidatos fictícios são cadastrados **somente** em
`src/main/java/br/edu/urnacidada/service/CatalogoFicticio.java`. O mesmo
catálogo alimenta a urna e a apuração. Os PNGs opcionais ficam em
`src/main/resources/candidatos/`; sem arquivos de imagem, aparece uma
silhueta neutra. Leia o `LEIA-ME.md` da pasta de fotos.

Para abrir a urna execute `br.edu.urnacidada.Main`; para abrir a
apuração separada, execute `br.edu.urnacidada.apuracao.ApuracaoMain`.

## Como testar a simulação

- **Deputado federal:** digite `8101` ou `8202` para votos nominais, ou `81` / `82` para votos de legenda.
- **Presidente:** digite `81` ou `82`.
- Use **BRANCO**, **CORRIGE** e **CONFIRMA**. Números completos não cadastrados podem ser confirmados como voto nulo.
- Após o segundo cargo, a interface tenta sincronizar a sessão com o Firestore sem travar o Swing. Se falhar, permite reenvio ou continuação local com aviso explícito.

Os números e nomes são **exemplos fictícios**, sem vínculo com partidos ou pessoas reais. Nenhum voto real é registrado.

## Arquitetura

O código separa `model` (entidades), `service` (regras eleitorais simplificadas), `controller` (fluxo de votação), `view` (Swing) e `repository` (armazenamento). Mais detalhes em [docs/arquitetura.md](docs/arquitetura.md) e [docs/requisitos.md](docs/requisitos.md).

## Segurança e privacidade

Esta versão não solicita dados pessoais. **Nunca publique credenciais do Firebase no GitHub**, nem as distribua nos computadores dos participantes. O acesso direto ao Firestore usa ADC exclusivamente na máquina supervisionada do desenvolvedor; para distribuir o sistema, utilize um backend com autenticação. A gravação na interface é feita na simulação de demonstração `demonstracao-interface-2026`.

## Próximos passos

- Validar a interface conectada ao Firestore em uma máquina de demonstração e planejar o backend para distribuição.
- Adicionar materiais educativos sobre desinformação e deepfakes.
- Organizar a oficina, questionários antes/depois e relatório extensionista.

Projeto desenvolvido para fins educacionais
