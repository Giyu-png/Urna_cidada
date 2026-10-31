package br.edu.urnacidada.service;

import br.edu.urnacidada.model.SessaoVotacao;
import br.edu.urnacidada.model.Voto;
import br.edu.urnacidada.repository.VotoRepository;

import java.util.Objects;

public final class ApuracaoService {

    private final VotoRepository votoRepository;

    public ApuracaoService(VotoRepository votoRepository) {
        this.votoRepository = Objects.requireNonNull(votoRepository, "votoRepository");
    }

    /**
     * Processa a sessão concluída, extrai os votos e envia para a base de dados via repositório.
     */
    public void processarEEinviarContagem(SessaoVotacao sessao) {
        Objects.requireNonNull(sessao, "sessao");

        if (!sessao.estaConcluida()) {
            throw new IllegalStateException("A sessão de votação ainda não está concluída.");
        }

        System.out.println("=== PROCESSANDO E ENVIANDO VOTOS PARA A BASE DE DADOS ===");
        System.out.println("ID da Sessão: " + sessao.getId());

        // 1. Envia a sessão inteira para o repositório configurado (ex: Memória ou Firestore)
        votoRepository.registrarSessao(sessao);

        // 2. Processa individualmente cada voto computado nesta urna
        for (Voto voto : sessao.getVotos()) {
            System.out.println("Cargo: " + voto.getCargo() +
                    " | Tipo: " + voto.getTipo() +
                    " | Referência/Número: " + (voto.getReferencia() != null ? voto.getReferencia() : "N/A"));

            // Aqui podes adicionar lógica adicional de envio individual se necessário
        }

        System.out.println("=== VOTOS ENVIADOS COM SUCESSO! ===");
    }
}