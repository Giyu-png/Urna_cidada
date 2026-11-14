
package br.edu.urnacidada.service;

import br.edu.urnacidada.model.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Cadastro único de candidaturas satíricas e legendas fictícias.
 *
 * Para adicionar uma candidatura: crie o partido, adicione uma Ficha
 * à lista FICHAS e coloque seu PNG em src/main/resources/candidatos.
 */
public final class CatalogoFicticio {

    // PARTIDOS

    private static final Partido REZA =
            new Partido("14", "Partido da Reza e do Caos");

    private static final Partido ROCK =
            new Partido("40", "Partido do Rock e da Revolta");

    private static final Partido INJUSTICADOS =
            new Partido("67", "Partido dos Injustiçados");

    private static final Partido CANCELAMENTO =
            new Partido("68", "Partido do Cancelamento");

    private static final Partido GATOS =
            new Partido("10", "Partido dos Gatos Revolucionários");

    private static final Partido ANIMAIS =
            new Partido("11", "Partido dos Animais de Elite");

    private static final Partido BILIONARIOS =
            new Partido("91", "Partido dos Bilionários Sem Limites");

    private static final Partido CAICAI =
            new Partido("92", "Partido do Cai-Cai");

    private static final Partido INFANCIA =
            new Partido("93", "Partido da Infância Eterna");

    private static final Partido RUAS =
            new Partido("94", "Partido da Voz das Ruas");

    private static final Partido RAP =
            new Partido("95", "Partido do Rap Nacional");

    private static final Partido BAU =
            new Partido("96", "Partido do Baú da Felicidade");

    private static final Partido SIRI =
            new Partido("97", "Partido do Siri Cascudo");

    private static final Partido VIZINHANCA =
            new Partido("98", "Partido da Vizinhança");

    private static final Partido SUPERIORES =
            new Partido("99", "Partido dos Superiores");

    private static final Partido PALHACO =
            new Partido("20", "Partido do Palhaço Nacional");

    private static final Partido CIENCIA =
            new Partido("21", "Partido da Ciência e da Loucura");


    private static final List<Partido> PARTIDOS = List.of(
            REZA,
            ROCK,
            INJUSTICADOS,
            CANCELAMENTO,
            GATOS,
            ANIMAIS,
            BILIONARIOS,
            CAICAI,
            INFANCIA,
            RUAS,
            RAP,
            BAU,
            SIRI,
            VIZINHANCA,
            SUPERIORES,
            PALHACO,
            CIENCIA
    );


    // FICHA DO CANDIDATO

    public record Ficha(Candidato candidato, String fotoRecurso) {

        public Ficha {
            Objects.requireNonNull(candidato, "candidato");

            if (fotoRecurso == null
                    || !fotoRecurso.startsWith("/candidatos/")
                    || !fotoRecurso.endsWith(".png")) {

                throw new IllegalArgumentException(
                        "Foto deve ser um PNG em /candidatos/."
                );
            }
        }
    }


    // CANDIDATOS

    private static final List<Ficha> FICHAS = List.of(

            // =========================
            // CANDIDATOS MAJORITÁRIOS
            // =========================

            new Ficha(
                    new CandidatoMajoritario(
                            "14", "Padre Kelmon", "Padre Kelmon", REZA
                    ),
                    "/candidatos/padre_kelmon.png"
            ),

            new Ficha(
                    new CandidatoMajoritario(
                            "67", "Coloridinho", "Hulk BR", INJUSTICADOS
                    ),
                    "/candidatos/coloridinho.png"
            ),

            new Ficha(
                    new CandidatoMajoritario(
                            "10", "Gato Nihao", "Lobo Pidao", GATOS
                    ),
                    "/candidatos/gato_nihao.png"
            ),

            new Ficha(
                    new CandidatoMajoritario(
                            "68", "Neymar Jr", "Neymar", CANCELAMENTO
                    ),
                    "/candidatos/neymar.png"
            ),

            new Ficha(
                    new CandidatoMajoritario(
                            "94", "Mano Brown", "Mano Brown", RUAS
                    ),
                    "/candidatos/mano_brown.png"
            ),

            new Ficha(
                    new CandidatoMajoritario(
                            "96", "Silvio Santos", "Silvio Santos", BAU
                    ),
                    "/candidatos/silvio_santos.png"
            ),

            new Ficha(
                    new CandidatoMajoritario(
                            "98", "Chaves", "Chaves", VIZINHANCA
                    ),
                    "/candidatos/chaves.png"
            ),

            new Ficha(
                    new CandidatoMajoritario(
                            "20", "Tiririca", "Tiririca", PALHACO
                    ),
                    "/candidatos/tiririca.png"
            ),


            // =========================
            // CANDIDATOS PROPORCIONAIS
            // =========================

            new Ficha(
                    new CandidatoProporcional(
                            "4066", "Lobao", ROCK
                    ),
                    "/candidatos/lobao.png"
            ),

            new Ficha(
                    new CandidatoProporcional(
                            "9267", "Emily Vik", CAICAI
                    ),
                    "/candidatos/emily_vik.png"
            ),

            new Ficha(
                    new CandidatoProporcional(
                            "1169", "Gato Babao", ANIMAIS
                    ),
                    "/candidatos/Gato_Babao.png"
            ),

            new Ficha(
                    new CandidatoProporcional(
                            "9122", "Elon Musk", BILIONARIOS
                    ),
                    "/candidatos/elon_musk.png"
            ),

            new Ficha(
                    new CandidatoProporcional(
                            "9389", "Luccas Neto", INFANCIA
                    ),
                    "/candidatos/luccas_neto.png"
            ),

            new Ficha(
                    new CandidatoProporcional(
                            "9510", "CJ", RAP
                    ),
                    "/candidatos/Cj.png"
            ),

            new Ficha(
                    new CandidatoProporcional(
                            "9790", "Bob Esponja", SIRI
                    ),
                    "/candidatos/bob_esponja.png"
            ),

            new Ficha(
                    new CandidatoProporcional(
                            "9965", "Kiko", SUPERIORES
                    ),
                    "/candidatos/kiko.png"
            ),

            new Ficha(
                    new CandidatoProporcional(
                            "2155", "Eneas Carneiro", CIENCIA
                    ),
                    "/candidatos/eneas.png"
            )

    );


    private CatalogoFicticio() {}


    // RETORNA OS PARTIDOS

    public static List<Partido> partidos() {
        return PARTIDOS;
    }


    // RETORNA AS FICHAS

    public static List<Ficha> fichas() {
        return FICHAS;
    }


    // RETORNA TODOS OS CANDIDATOS

    public static List<Candidato> candidatos() {
        return FICHAS.stream()
                .map(Ficha::candidato)
                .toList();
    }


    // BUSCA CANDIDATO PELO CARGO E NÚMERO

    public static Optional<Ficha> buscarFicha(
            Cargo cargo,
            String numero
    ) {

        if (cargo == null || numero == null) {
            return Optional.empty();
        }

        return FICHAS.stream()
                .filter(f ->
                        f.candidato().getCargo() == cargo
                                && f.candidato().getNumero().equals(numero)
                )
                .findFirst();
    }


    // CRIA O SERVIÇO DE VOTAÇÃO

    public static VotacaoService criarServico() {
        return new VotacaoService(candidatos(), PARTIDOS);
    }
}
