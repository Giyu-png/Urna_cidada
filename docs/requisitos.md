# Especificação funcional inicial — v0.1

## Objetivo
Construir um simulador educativo e apartidário de votação com Java Swing, aplicando POO, e apoiar uma oficina sobre cidadania e desinformação. O aplicativo **não é** uma urna oficial.

## Requisitos incluídos no protótipo 0.1
- RF01: Iniciar e reiniciar simulações.
- RF02: Votar sequencialmente para deputado federal (4 dígitos) e presidente (2 dígitos).
- RF03: Teclado numérico, teclas BRANCO, CORRIGE e CONFIRMA.
- RF04: Identificar candidaturas fictícias cadastradas.
- RF05: Classificar votos nominais, de legenda, brancos e nulos.
- RF06: Impedir confirmar escolha incompleta.
- RF07: Exibir mensagem de conclusão após dois cargos.
- RF08: Registrar sessões de forma idempotente em memória, sem identificação pessoal.

## Não funcionais
- Java 21, Maven, Swing, POO com encapsulamento, herança, polimorfismo e abstração.
- Nomes e números fictícios, sem propagandas ou vínculo partidário.
- Sem coleta de nome, CPF ou outros dados pessoais.
- Teclado e botões operáveis localmente.

## A desenvolver
- Persistência em Cloud Firestore com arquitetura segura para demonstração.
- Material educativo sobre deepfakes, atividade e questionários pré/pós-oficina.
- Relatório com evidências reais após a oficina.
- Painel para resultados agregados (opcional).

## Observação de escopo
Esta é uma representação didática **simplificada**, sem reproduzir as garantias de auditoria, segurança e regras completas de uma urna real.

