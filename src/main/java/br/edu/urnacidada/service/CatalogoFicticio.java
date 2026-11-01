package br.edu.urnacidada.service;

import br.edu.urnacidada.model.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Cadastro único de candidaturas e partidos inteiramente fictícios.
 *
 * Para adicionar uma candidatura: crie o partido (se necessário), adicione uma
 * Ficha à lista FICHAS e coloque seu PNG em src/main/resources/candidatos.
 * A urna e a apuração usarão automaticamente este mesmo catálogo.
 */
public final class CatalogoFicticio {
    private static final Partido AURORA = new Partido("81", "Equipe Aurora");
    private static final Partido HORIZONTE = new Partido("82", "Equipe Horizonte");

    private static final List<Partido> PARTIDOS = List.of(AURORA, HORIZONTE);

    public record Ficha(Candidato candidato, String fotoRecurso) {
        public Ficha {
            Objects.requireNonNull(candidato, "candidato");
            if (fotoRecurso == null || !fotoRecurso.startsWith("/candidatos/")
                    || !fotoRecurso.endsWith(".png")) {
                throw new IllegalArgumentException("Foto deve ser um PNG em /candidatos/.");
            }
        }
    }

    private static final List<Ficha> FICHAS = List.of(
        new Ficha(new CandidatoProporcional("8101", "Lia Inventora", AURORA),
                "/candidatos/deputado_8101.png"),
        new Ficha(new CandidatoProporcional("8202", "Caio Explorador", HORIZONTE),
                "/candidatos/deputado_8202.png"),
        new Ficha(new CandidatoMajoritario("81", "Nina Aurora", "Teo Amanhecer", AURORA),
                "/candidatos/presidente_81.png"),
        new Ficha(new CandidatoMajoritario("82", "Ravi Horizonte", "Bia do Amanhã", HORIZONTE),
                "/candidatos/presidente_82.png")
    );

    private CatalogoFicticio() {}

    public static List<Partido> partidos() { return PARTIDOS; }
    public static List<Ficha> fichas() { return FICHAS; }

    public static List<Candidato> candidatos() {
        return FICHAS.stream().map(Ficha::candidato).toList();
    }

    public static Optional<Ficha> buscarFicha(Cargo cargo, String numero) {
        if (cargo == null || numero == null) return Optional.empty();
        return FICHAS.stream()
                .filter(f -> f.candidato().getCargo() == cargo
                        && f.candidato().getNumero().equals(numero))
                .findFirst();
    }

    public static VotacaoService criarServico() {
        return new VotacaoService(candidatos(), PARTIDOS);
    }
}
