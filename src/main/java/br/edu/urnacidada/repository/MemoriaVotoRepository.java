package br.edu.urnacidada.repository;

import br.edu.urnacidada.model.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Persistência provisória, não sobrevivendo ao encerramento do programa. */
public final class MemoriaVotoRepository implements VotoRepository {
    private final Map<String, List<Voto>> sessoes = new LinkedHashMap<>();

    @Override
    public synchronized void registrarSessao(SessaoVotacao sessao) {
        Objects.requireNonNull(sessao, "sessao");
        if (!sessao.estaConcluida()) throw new IllegalArgumentException("Sessão incompleta.");
        sessoes.putIfAbsent(sessao.getId(), sessao.getVotos());
    }

    public synchronized int getQuantidadeSessoes() { return sessoes.size(); }

    public synchronized long contarVotos(Cargo cargo, TipoVoto tipo) {
        return sessoes.values().stream().flatMap(List::stream)
                .filter(v -> v.getCargo() == cargo && v.getTipo() == tipo)
                .count();
    }
}

