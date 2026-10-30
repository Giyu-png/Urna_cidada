# Urna Cidadã

Simulador **educativo e apartidário** de votação para dois cargos (deputado federal e presidente), desenvolvido como projeto de extensão de Programação Orientada a Objetos.

> **Atenção:** este é um protótipo acadêmico com candidaturas e legendas fictícias. Não é um sistema eleitoral oficial nem se conecta à Justiça Eleitoral.

## Tecnologias

- Java 21, Java Swing e Maven.
- JUnit 5 para testes das regras de negócio.
- Persistência **em memória** nesta primeira versão; integração com Cloud Firestore será implementada posteriormente.

## Como executar

Pré-requisitos: JDK 21 e Maven 3.9 ou superior.

```bash
mvn clean test
mvn package
java -jar target/urna-cidada-0.1.0.jar
```

## Como testar a simulação

- **Deputado federal:** digite `8101` ou `8202` para votos nominais, ou `81` / `82` para votos de legenda.
- **Presidente:** digite `81` ou `82`.
- Use **BRANCO**, **CORRIGE** e **CONFIRMA**. Números completos não cadastrados podem ser confirmados como voto nulo.
- Após o segundo cargo, aparece a tela de encerramento.

Os números e nomes são **exemplos fictícios**, sem vínculo com partidos ou pessoas reais. Nenhum voto real é registrado.

## Arquitetura

O código separa `model` (entidades), `service` (regras eleitorais simplificadas), `controller` (fluxo de votação), `view` (Swing) e `repository` (armazenamento). Mais detalhes em [docs/arquitetura.md](docs/arquitetura.md) e [docs/requisitos.md](docs/requisitos.md).

## Segurança e privacidade

Esta versão não solicita dados pessoais. **Nunca publique credenciais do Firebase no GitHub**, nem as distribua em aplicativos desktop. A persistência em nuvem e sua configuração segura são trabalho de uma etapa posterior.

## Próximos passos

- Integrar o Cloud Firestore com uma estratégia segura para demonstração.
- Adicionar materiais educativos sobre desinformação e deepfakes.
- Organizar a oficina, questionários antes/depois e relatório extensionista.

Projeto desenvolvido para fins educacionais
