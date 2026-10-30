package br.edu.urnacidada.service;

import br.edu.urnacidada.model.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Regras simplificadas da simulação, independentes da interface Swing. */
public final class VotacaoService {
    private final Map<Cargo, Map<String, Candidato>> candidatosPorCargo = new HashMap<>();
    private final Map<String, Partido> partidos = new HashMap<>();

    public VotacaoService(List<Candidato> candidatos, List<Partido> partidos) {
        Objects.requireNonNull(candidatos, "candidatos");
        Objects.requireNonNull(partidos, "partidos");
        for (Partido partido : partidos) {
            if (this.partidos.putIfAbsent(partido.getNumero(), partido) != null) {
                throw new IllegalArgumentException("Legenda duplicada.");
            }
        }
        for (Candidato candidato : candidatos) {
            if (!this.partidos.containsKey(candidato.getPartido().getNumero())) {
                throw new IllegalArgumentException("Candidato com legenda não cadastrada.");
            }
            Map<String, Candidato> porNumero =
                candidatosPorCargo.computeIfAbsent(candidato.getCargo(), k -> new HashMap<>());
            if (porNumero.putIfAbsent(candidato.getNumero(), candidato) != null) {
                throw new IllegalArgumentException("Candidato duplicado no mesmo cargo.");
            }
        }
    }

    public Optional<Candidato> buscarCandidato(Cargo cargo, String numero) {
        Objects.requireNonNull(cargo, "cargo");
        return Optional.ofNullable(candidatosPorCargo.getOrDefault(cargo, Map.of()).get(numero));
    }

    /**
     * Retorna null se a digitação ainda estiver incompleta.
     * BRANCO tem prioridade quando explicitamente selecionado.
     */
    public Voto classificarVoto(Cargo cargo, String digitos, boolean branco) {
        Objects.requireNonNull(cargo, "cargo");
        if (branco) return Voto.branco(cargo);
        if (digitos == null || !digitos.matches("\\d+")) return null;
        if (cargo == Cargo.DEPUTADO_FEDERAL && digitos.length() == 2
                && partidos.containsKey(digitos)) {
            return Voto.legenda(cargo, digitos);
        }
        if (digitos.length() != cargo.getQuantidadeDigitos()) return null;
        return buscarCandidato(cargo, digitos).isPresent()
                ? Voto.nominal(cargo, digitos)
                : Voto.nulo(cargo);
    }

    public String descreverEscolha(Cargo cargo, String digitos, boolean branco) {
        if (branco) return "VOTO EM BRANCO — pressione CONFIRMA.";
        if (digitos == null || digitos.isEmpty()) {
            return "Digite o número do candidato ou selecione BRANCO.";
        }
        Voto voto = classificarVoto(cargo, digitos, false);
        if (voto == null) return "Continue digitando para completar o número.";
        return switch (voto.getTipo()) {
            case NOMINAL -> buscarCandidato(cargo, digitos).orElseThrow().getDescricao();
            case LEGENDA -> "VOTO DE LEGENDA — " + partidos.get(digitos).getNome();
            case NULO -> "NÚMERO NÃO CADASTRADO — voto nulo ao confirmar.";
            case BRANCO -> "VOTO EM BRANCO.";
        };
    }
}

