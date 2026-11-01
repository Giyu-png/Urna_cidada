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
    private static final Partido TEEN = new Partido("67", "Partido Teen");
    private static final Partido RACIONAISMC = new Partido("10", "Partido RacionaisMC");
    private static final Partido ILHA = new Partido("90", "Partido ILHA");
    private static final Partido DEMONIO = new Partido("66", "Partido Demoniaco");

    private static final List<Partido> PARTIDOS = List.of(TEEN,RACIONAISMC,ILHA,DEMONIO);

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
            new Ficha(new CandidatoMajoritario("67", "Coloridinho", "Hulk BR", TEEN),
                "/candidatos/coloridinho.png"),
            new Ficha(new CandidatoProporcional("6767", "Emily Vik", TEEN),
                    "/candidatos/emily_vik.png"),
            new Ficha(new CandidatoMajoritario("10", "Gato Nihao", "Lobo Pidao", RACIONAISMC),
                    "/candidatos/gato_nihao.png"),
            new Ficha(new CandidatoProporcional("1069", "Gato Babao", RACIONAISMC),
                    "/candidatos/Gato_Babao.png"),
            new Ficha(new CandidatoMajoritario("90", "P Diddy", "Justin Bieber", ILHA),
                    "/candidatos/pdiddy.png"),
            new Ficha(new CandidatoProporcional("9022", "Elon Musk", ILHA),
                    "/candidatos/elon_musk.png"),
            new Ficha(new CandidatoMajoritario("66", "IT a Coisa", "Belzebu", DEMONIO),
                    "/candidatos/IT.png"),
            new Ficha(new CandidatoProporcional("6666", "cthulhu", DEMONIO),
                    "/candidatos/cutulo.png")



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
