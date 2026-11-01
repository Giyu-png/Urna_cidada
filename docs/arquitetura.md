# Arquitetura do simulador

```text
Aplicação 1: br.edu.urnacidada.Main (Java Swing)
  TelaInicial -> TelaUrna -> TelaSincronizacao -> TelaFim
                     |
               UrnaController
                     |
          VotacaoService / model
                     |
         MemoriaVotoRepository
                     |
          (sessão concluída)
                     |
            SwingWorker (UI)
                     |
       FirestoreVotoRepository
                     |
          Cloud Firestore (IAM/ADC)

Aplicação 2: br.edu.urnacidada.apuracao.ApuracaoMain
       JanelaApuracao (SwingWorker)
                     |
       FirestoreVotoRepository (leitura)
                     |
          Cloud Firestore

Catálogo único: service/CatalogoFicticio
   | candidatos, partidos e caminhos das fotos
   +----> urna e apuração
            imagens locais em src/main/resources/candidatos
```

## Responsabilidades

- `model`: cargos, candidatos, partidos, votos e sessões.
- `service`: catálogo central e regras de votação; `ApuracaoService` encapsula a etapa de registro local.
- `controller`: estado de digitação e avanço entre cargos.
- `repository`: contrato de persistência e implementações em memória/Firestore.
- `view`: interfaces Swing, fotos, sincronização assíncrona.
- `apuracao`: painel independente de consulta de contagens agregadas.
- `config`: cliente Google Cloud Firestore a partir do ambiente.

## Pilares de POO

- Encapsulamento em entidades e controlador.
- Herança: `CandidatoProporcional` e `CandidatoMajoritario`.
- Polimorfismo em `getDescricao()`.
- Abstração pelo contrato `VotoRepository`.

## Segurança e limitações

O código do servidor Firestore executa no desktop **apenas em ambientes supervisionados** com credenciais próprias e IAM. Não existem credenciais administrativas no repositório. Os documentos registram contagens e marcadores aleatórios de sessão, não o par de votos de cada pessoa. A apuração é somente leitura. O jogo não implementa eleição oficial nem regras completas de totalização.
