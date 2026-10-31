# Banco de dados — Cloud Firestore (responsabilidade de persistência)

Esta branch implementa o armazenamento de **votos fictícios agregados**, não de dados pessoais. O programa utiliza Java 21, Maven e a biblioteca oficial `google-cloud-firestore:3.49.0`.

> **Situação:** a implementação da camada de persistência está disponível, mas a interface Swing continua usando `MemoriaVotoRepository` até a integração ser acordada com o grupo. Ainda é necessário criar um projeto Firebase, configurar credenciais locais e testar a conexão de verdade.

## 1. Criar o banco

1. Abra https://console.firebase.google.com/ com sua conta.
2. Clique em **Adicionar projeto** e crie um projeto específico para a atividade acadêmica.
3. Abra **Firestore Database** e crie o banco em modo nativo/Standard. Escolha uma localização apropriada e verifique condições de uso e cobrança antes de prosseguir.
4. Anote apenas o **ID do projeto** (não é uma senha). Não envie credenciais para o chat ou GitHub.
5. Se houver dados reais de teste, lembre-se: esta é uma simulação, não um sistema eleitoral oficial.

## 2. Autenticação local (Windows/IntelliJ)

Use **Application Default Credentials (ADC)** da conta do Google Cloud configurada com acesso mínimo necessário, de preferência uma conta de serviço para testes isolados. Com o Google Cloud CLI, uma alternativa é executar `gcloud auth application-default login` no próprio computador. Documentação oficial: https://cloud.google.com/docs/authentication/provide-credentials-adc

Configure no IntelliJ (Run > Edit Configurations > Environment variables):

```text
FIRESTORE_PROJECT_ID=seu-projeto-firebase
```

A classe `FirebaseConfig.criarCliente()` carrega a identidade autenticada pelo ambiente. Se a sua configuração exigir um arquivo JSON de conta de serviço, aponte `GOOGLE_APPLICATION_CREDENTIALS` para um arquivo **fora do repositório**. Nunca publique esse JSON nem o envie para colegas. Credenciais privilegiadas podem contornar as regras comuns de segurança do Firestore.

## 3. Estrutura dos documentos

```text
simulacoes/{simulacaoId}
  resultados/{CARGO_TIPO_REFERENCIA}
    cargo: "DEPUTADO_FEDERAL"
    tipo: "NOMINAL"
    referencia: "8101"
    quantidade: 7

  resultados/{CARGO_TIPO}
    cargo: "PRESIDENTE"
    tipo: "BRANCO"
    quantidade: 3

  sessoesProcessadas/{idAleatorioSessao}
    processadaEm: timestamp do servidor
    versaoEsquema: 1
```

**Nenhum marcador contém a escolha do participante**. As contagens de cada cargo são armazenadas em documentos separados, sem CPF, nome ou identificação do votante.

## 4. Como conectar na aplicação Java

O código já possui `VotoRepository` e permite trocar o backend sem reescrever as regras da urna:

```java
Firestore db = FirebaseConfig.criarCliente();
VotoRepository repository = new FirestoreVotoRepository(db, "oficina-2026");
```

A aplicação deve manter o cliente `db` aberto durante seu uso e executar `db.close()` ao encerrar. **Não coloque a gravação síncrona diretamente na thread de eventos do Swing**, pois lentidão de rede congela a tela. A integração com a UI deve usar `SwingWorker` ou outra execução assíncrona com estado de confirmação/erro recuperável.

A versão atual ainda cria `MemoriaVotoRepository` em `JanelaPrincipal`. Ela permanece como padrão até a equipe alinhar a substituição. O JAR simples do Maven não empacota dependências do Firestore; o projeto abre pelo IntelliJ, e a distribuição exigirá empacotamento adequado depois.

## 5. Proteção contra contagem duplicada

Uma transação:
1. Verifica se `sessoesProcessadas/{id}` já existe.
2. Se existir, termina sem modificar resultados.
3. Se não existir, incrementa atomicamente os dois contadores.
4. Registra o identificador aleatório da sessão na mesma transação.

Se a gravação falhar, a transação não deve adicionar resultados parciais. Isso é importante porque não devemos contar duas vezes quando o participante ou a aplicação repetir uma tentativa.

## 6. Como testar

- `mvn clean test`: executa os testes unitários sem depender de credenciais Firebase.
- Para testar a integração, configure o Firestore **Emulator** ou um projeto de testes isolado. Verifique uma sessão completa, duas categorias contabilizadas, repetição idempotente e leitura dos resultados.
- Nunca execute testes destrutivos no banco de uma oficina real.
- Antes de habilitar a persistência na apresentação, verifique conexão, autorização, tratamento de erros e tempo de resposta.

## 7. Próximos passos com o grupo

- Integrar `FirestoreVotoRepository` com `JanelaPrincipal` sem bloquear a UI.
- Criar uma tela de resultados agregados em área reservada ao facilitador.
- Validar a persistência com o emulador ou projeto Firebase de teste.
- Adicionar testes de integração e revisar permissões das contas utilizadas.

## Referências técnicas

- https://docs.cloud.google.com/firestore/native/docs/create-database-server-client-library
- https://firebase.google.com/docs/firestore/manage-data/transactions
- https://docs.cloud.google.com/java/docs/reference/google-cloud-firestore/latest/overview
