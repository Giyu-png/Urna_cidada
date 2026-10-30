package br.edu.urnacidada.service;

import br.edu.urnacidada.model.*;
import java.util.List;

/** Catálogo temático apenas para fins de ensino, sem vínculo partidário real. */
public final class CatalogoFicticio {
    private CatalogoFicticio() {}

    public static VotacaoService criarServico() {
        Partido aurora = new Partido("81", "Equipe Aurora");
        Partido horizonte = new Partido("82", "Equipe Horizonte");
        List<Candidato> candidatos = List.of(
            new CandidatoProporcional("8101", "Lia Inventora", aurora),
            new CandidatoProporcional("8202", "Caio Explorador", horizonte),
            new CandidatoMajoritario("81", "Nina Aurora", "Teo Amanhecer", aurora),
            new CandidatoMajoritario("82", "Ravi Horizonte", "Bia do Amanhã", horizonte)
        );
        return new VotacaoService(candidatos, List.of(aurora, horizonte));
    }
}

