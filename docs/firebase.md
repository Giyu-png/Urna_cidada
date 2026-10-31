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

O ID público do projeto já vem configurado como `urna-cidada-pjde-3`. **Não é necessário definir uma variável no IntelliJ para testar esse projeto.** Se precisar usar outro projeto, configure opcionalmente em Run > Edit Configurations > Environment variables:

```text
FIRESTORE_PROJECT_ID=outro-id-de-projeto
```

Depois de `gcloud auth application-default login`, execute a classe `br.edu.urnacidada.config.TesteConexaoFirestore` para validar apenas a leitura. A classe `FirebaseConfig.criarCliente()` carrega a identidade autenticada pelo ambiente. Se a sua configuração exigir um arquivo JSON de conta de serviço, aponte `GOOGLE_APPLICATION_CREDENTIALS` para um arquivo **fora do repositório**. Nunca publique esse JSON nem o envie para colegas. Credenciais privilegiadas podem contornar as regras comuns de segurança do Firestore.

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

## 8. Integração com a interface Swing

Na branch `feature/firebase-firestore`, a interface envia a sessão concluída ao
Firestore usando `SwingWorker` para não bloquear a thread de eventos do Swing.

1. Execute `br.edu.urnacidada.Main` pelo IntelliJ com suas credenciais ADC locais.
2. Conclua a votação fictícia para os dois cargos.
3. A tela **SALVANDO VOTAÇÃO** aguarda a confirmação da nuvem.
4. Só depois da gravação confirmada a tela **FIM** informa que os votos foram contabilizados no Firestore.
5. Se a nuvem falhar, escolha **TENTAR NOVAMENTE** (mesmo ID de sessão, sem duplicação), ou **CONTINUAR SEM SALVAR NA NUVEM**, que informa claramente que a confirmação remota não ocorreu.

As gravações da interface ficam em
`simulacoes/demonstracao-interface-2026/resultados`, separadas dos testes manuais
`simulacoes/diagnostico-integracao-2026`. Cada execução bem-sucedida representa
uma sessão fictícia completa, com dois votos.

**Atenção:** credenciais ADC permitem acesso privilegiado e **não devem ser
distribuídas aos participantes ou empacotadas no desktop**. A comunicação
direta com o Firestore é apropriada apenas para uso supervisionado em uma
máquina administrada pelos desenvolvedores. Para distribuição a estudantes,
implante um backend intermediário com autenticação e privilégios mínimos.

Os avisos de SLF4J ou métodos nativos vistos com Java 25 não impedem a
persistência, mas a versão recomendada para o projeto continua Java 21.

## 9. Aplicação de apuração separada (somente leitura)

A urna e o painel administrativo são duas janelas **independentes** no mesmo
projeto Maven. A urna permanece em `br.edu.urnacidada.Main`. Para abrir somente
o painel de apuração, execute `br.edu.urnacidada.apuracao.ApuracaoMain`
no IntelliJ (ou uma configuração Application com essa Main class).

O painel lê `simulacoes/demonstracao-interface-2026/resultados`, exatamente a
mesma simulação usada pela interface de votação, mas **não altera documentos**.
Ele mostra uma aba por cargo, opções e candidatos fictícios, voto em legenda,
brancos, nulos, totais e percentuais. Clique em **ATUALIZAR RESULTADOS** para
consultar novamente o Firestore; o processo acontece em segundo plano.

Os percentuais são calculados em relação ao total de votos do respectivo
cargo (incluindo brancos, nulos e legenda). Nomes conhecidos são retirados do
catálogo fictício; categorias novas também aparecem para não ocultar contagens.
Em caso de falha de rede, o painel preserva a última consulta bem-sucedida e
mostra erro, em vez de substituir resultados válidos por zeros.

**Importante:** a interface da urna não ganha um botão de apuração.
A leitura por SDK Java usa as credenciais ADC do administrador e não deve ser
distribuída a alunos ou público. A aplicação não oferece votação nem faz
gravações no Firebase.

### Melhorias visuais do painel

O painel apresenta cartões separados de total, brancos e nulos, com colunas
numéricas alinhadas à direita e espaçamento interno para evitar valores grudados.
As colunas "Votos" (inteiros) e "Percentual" (números decimais) continuam
numéricas internamente para permitir ordenação correta pelos cabeçalhos.
A interface usa linhas alternadas e preserva os valores anteriores se a
atualização do Firestore falhar.

Para conferir a apresentação visual, execute `ApuracaoMain`, alterne entre
as abas, clique em **ATUALIZAR RESULTADOS** e ordene por **Votos** ou
**Percentual**. Teste também uma janela redimensionada para verificar se há
espaçamento adequado.

## 10. Cadastro centralizado e fotos dos candidatos

O arquivo `src/main/java/br/edu/urnacidada/service/CatalogoFicticio.java`
define **uma lista única de fichas** (candidato, número, partido e PNG).
Para adicionar uma nova candidatura fictícia, edite a lista `FICHAS`;
para adicionar um partido fictício, inclua-o na lista `PARTIDOS` também.
Escolha um número ainda livre no mesmo cargo.

As fotos ficam em `src/main/resources/candidatos/`, com os nomes
documentados no `LEIA-ME.md` dessa pasta. Na ausência de um PNG, é
desenhada uma **silhueta neutra**: a aplicação continua executável sem
imagens adicionais. As fotos são carregadas do classpath, não do Firestore.

A urna agora tem visor com quatro ou dois campos de número, dados do
candidato e fotografia, além de teclado escuro e teclas BRANCO, CORRIGE
e CONFIRMA. A ordem dos cargos e a persistência no Firebase não mudaram.
A apuração passou a gerar a lista de candidatos e partidos automaticamente
a partir do mesmo cadastro (sem chaves estáticas duplicadas). O painel exibe
miniaturas nas linhas nominais e destaca **o mais votado até o momento**.
Isso não equivale a declarar um vencedor, pois não há encerramento formal
do pleito ou apuração de cadeiras proporcionais.

Teste: votação nominal, legenda, branco, nulo, transição de cargo,
carregamento sem PNG, fotos PNG opcionais e consulta em `ApuracaoMain`.
