package br.edu.urnacidada.controller;

import br.edu.urnacidada.model.*;
import br.edu.urnacidada.repository.VotoRepository;
import br.edu.urnacidada.service.VotacaoService;
import java.util.Objects;

/** Controla uma sessão independente dos componentes visuais. */
public final class UrnaController {
    private final VotacaoService service;
    private final VotoRepository repository;
    private final SessaoVotacao sessao = new SessaoVotacao();
    private final StringBuilder digitos = new StringBuilder();
    private boolean brancoSelecionado;

    public UrnaController(VotacaoService service, VotoRepository repository) {
        this.service = Objects.requireNonNull(service, "service");
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    public Cargo getCargoAtual() { return sessao.getCargoAtual(); }
    public String getDigitos() { return digitos.toString(); }
    public boolean isBrancoSelecionado() { return brancoSelecionado; }
    public boolean estaConcluida() { return sessao.estaConcluida(); }
    public String getDescricaoEscolha() {
        if (estaConcluida()) return "FIM DA SIMULAÇÃO";
        return service.descreverEscolha(getCargoAtual(), getDigitos(), brancoSelecionado);
    }

    public boolean digitar(char digito) {
        if (estaConcluida() || brancoSelecionado || !Character.isDigit(digito)
                || digitos.length() >= getCargoAtual().getQuantidadeDigitos()) {
            return false;
        }
        digitos.append(digito);
        return true;
    }

    public void votarBranco() {
        if (estaConcluida()) throw new IllegalStateException("Sessão encerrada.");
        digitos.setLength(0);
        brancoSelecionado = true;
    }

    public void corrigir() {
        if (estaConcluida()) throw new IllegalStateException("Sessão encerrada.");
        digitos.setLength(0);
        brancoSelecionado = false;
    }

    /**
     * Retorna false quando a escolha ainda estiver incompleta.
     * Ao finalizar os dois cargos, salva a sessão no repositório configurado.
     */
    public boolean confirmar() {
        if (estaConcluida()) throw new IllegalStateException("Sessão encerrada.");
        Voto voto = service.classificarVoto(getCargoAtual(), getDigitos(), brancoSelecionado);
        if (voto == null) return false;
        sessao.registrarVoto(voto);
        digitos.setLength(0);
        brancoSelecionado = false;
        if (sessao.estaConcluida()) repository.registrarSessao(sessao);
        return true;
    }
}

