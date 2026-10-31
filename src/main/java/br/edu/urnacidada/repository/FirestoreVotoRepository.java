package br.edu.urnacidada.repository;

import br.edu.urnacidada.model.SessaoVotacao;
import br.edu.urnacidada.model.TipoVoto;
import br.edu.urnacidada.model.Voto;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Persistência do simulador no Cloud Firestore.
 *
 * Armazena somente resultados agregados e marcadores sem dados pessoais.
 * Dois votos da mesma sessão são contabilizados em uma transação.
 */
public final class FirestoreVotoRepository implements VotoRepository {
    private final Firestore banco;
    private final String simulacaoId;

    public FirestoreVotoRepository(Firestore banco, String simulacaoId) {
        this.banco = Objects.requireNonNull(banco, "banco");
        if (simulacaoId == null || !simulacaoId.matches("[A-Za-z0-9_-]{1,64}")) {
            throw new IllegalArgumentException(
                "O identificador da simulação deve conter até 64 letras, dígitos, _ ou -.");
        }
        this.simulacaoId = simulacaoId;
    }

    private DocumentReference documentoSimulacao() {
        return banco.collection("simulacoes").document(simulacaoId);
    }

    @Override
    public void registrarSessao(SessaoVotacao sessao) {
        Objects.requireNonNull(sessao, "sessao");
        if (!sessao.estaConcluida() || sessao.getVotos().size() != 2) {
            throw new IllegalArgumentException("A sessão deve conter os dois votos confirmados.");
        }

        DocumentReference processada = documentoSimulacao()
                .collection("sessoesProcessadas").document(sessao.getId());

        try {
            banco.runTransaction(transacao -> {
                // Toda leitura deve ocorrer antes de qualquer escrita na transação.
                if (transacao.get(processada).get().exists()) {
                    return false; // Repetição: nunca contar a mesma sessão duas vezes.
                }
                for (Voto voto : sessao.getVotos()) {
                    DocumentReference contador = documentoSimulacao()
                            .collection("resultados").document(ChaveResultado.doVoto(voto));
                    Map<String, Object> campos = new HashMap<>();
                    campos.put("cargo", voto.getCargo().name());
                    campos.put("tipo", voto.getTipo().name());
                    campos.put("quantidade", FieldValue.increment(1L));
                    // A referência só existe para votos nominais ou de legenda.
                    if (voto.getTipo() == TipoVoto.NOMINAL || voto.getTipo() == TipoVoto.LEGENDA) {
                        campos.put("referencia", voto.getReferencia());
                    }
                    transacao.set(contador, campos, SetOptions.merge());
                }
                // Apenas um identificador aleatório, sem armazenar a combinação dos votos.
                transacao.set(processada, Map.of(
                    "processadaEm", FieldValue.serverTimestamp(),
                    "versaoEsquema", 1
                ));
                return true;
            }).get();
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Registro interrompido antes da confirmação.", erro);
        } catch (ExecutionException erro) {
            throw new IllegalStateException("Erro ao registrar a simulação no Firestore.", erro.getCause());
        }
    }

    /** Retorna uma fotografia das contagens por categoria, sem dados de participantes. */
    public Map<String, Long> consultarResultados() {
        try {
            Map<String, Long> totais = new LinkedHashMap<>();
            for (QueryDocumentSnapshot documento : documentoSimulacao()
                    .collection("resultados").get().get().getDocuments()) {
                Long quantidade = documento.getLong("quantidade");
                if (quantidade != null) totais.put(documento.getId(), quantidade);
            }
            return Map.copyOf(totais);
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Consulta interrompida.", erro);
        } catch (ExecutionException erro) {
            throw new IllegalStateException("Erro ao consultar resultados no Firestore.", erro.getCause());
        }
    }
}
