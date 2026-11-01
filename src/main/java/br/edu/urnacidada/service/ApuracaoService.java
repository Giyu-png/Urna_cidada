package br.edu.urnacidada.service;

import br.edu.urnacidada.model.SessaoVotacao;
import br.edu.urnacidada.repository.VotoRepository;
import java.util.Objects;

public final class ApuracaoService {
    private final VotoRepository votoRepository;

    public ApuracaoService(VotoRepository votoRepository) {
        this.votoRepository = Objects.requireNonNull(votoRepository, "votoRepository");
    }

    /**
     * Registra uma sessão concluída sem expor suas escolhas em logs.
     */
    public void processarEEinviarContagem(SessaoVotacao sessao) {
        Objects.requireNonNull(sessao, "sessao");
        if (!sessao.estaConcluida()) {
            throw new IllegalStateException("A sessão de votação ainda não está concluída.");
        }
        votoRepository.registrarSessao(sessao);
    }
}
