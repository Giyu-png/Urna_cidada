# Requisitos funcionais — Urna Cidadã

## Objetivo e escopo

Simulador educativo de votação para deputado federal e presidente, desenvolvido em Java Swing e POO para um projeto de extensão sobre cidadania. Não é um produto eleitoral oficial. As candidaturas e os partidos são paródias criadas para a simulação; algumas imagens fazem referência a pessoas e personagens conhecidos.

## Funcionalidades implementadas

- **RF01:** iniciar e reiniciar sessões fictícias.
- **RF02:** votar sequencialmente para deputado federal (4 dígitos) e presidente (2 dígitos).
- **RF03:** oferecer teclas numéricas e BRANCO, CORRIGE e CONFIRMA.
- **RF04:** identificar candidatos pelo cadastro compartilhado, incluindo foto local e partido.
- **RF05:** classificar voto nominal, de legenda (deputado federal), branco e nulo.
- **RF06:** impedir confirmação de escolha incompleta.
- **RF07:** exibir fim da sessão após os dois cargos.
- **RF08:** registrar a sessão em memória e sincronizar os votos agregados com Firestore por transação idempotente.
- **RF09:** apresentar estado de sincronização, reenvio e opção de terminar sem confirmação de gravação.
- **RF10:** abrir aplicação separada e somente leitura para consultar totais, percentuais e a candidatura mais votada.

## Não funcionais e limites

- JDK 21, Maven, Swing, testes JUnit, Firestore Emulator e CI.
- Dados fictícios, sem identificação pessoal dos eleitores.
- O acesso ao Firestore usa credenciais locais ADC e permissões IAM, sem distribuir segredos.
- Persistência executada fora da thread de interface (`SwingWorker`).
- A indicação do candidato mais votado não equivale a declarar eleito; não há cálculo de cadeiras proporcionais.
- O programa não reproduz a infraestrutura e as garantias de uma urna oficial.

## Fora do código atual

Não há IA local, minigame, verificação automática de notícias ou questionários. Esses recursos podem existir como parte da oficina ou de outros materiais, mas não são entregues neste repositório.
