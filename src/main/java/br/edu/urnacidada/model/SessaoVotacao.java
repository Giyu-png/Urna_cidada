package br.edu.urnacidada.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class SessaoVotacao {
    private final String id = UUID.randomUUID().toString();
    private final List<Voto> votos = new ArrayList<>();

    public String getId() { return id; }
    public List<Voto> getVotos() { return List.copyOf(votos); }
    public boolean estaConcluida() { return votos.size() == 2; }

    public Cargo getCargoAtual() {
        if (votos.isEmpty()) return Cargo.DEPUTADO_FEDERAL;
        if (votos.size() == 1) return Cargo.PRESIDENTE;
        return null;
    }

    public void registrarVoto(Voto voto) {
        Objects.requireNonNull(voto, "voto");
        if (estaConcluida()) throw new IllegalStateException("Sessão encerrada.");
        if (voto.getCargo() != getCargoAtual()) {
            throw new IllegalArgumentException("Voto fora da sequência de cargos.");
        }
        votos.add(voto);
    }
}

