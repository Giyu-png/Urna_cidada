package br.edu.urnacidada.repository;

import br.edu.urnacidada.model.SessaoVotacao;

/** Contrato de persistência: será implementado também por Cloud Firestore. */
public interface VotoRepository {
    void registrarSessao(SessaoVotacao sessao);
}

