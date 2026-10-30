package br.edu.urnacidada.model;

public final class CandidatoProporcional extends Candidato {
    public CandidatoProporcional(String numero, String nome, Partido partido) {
        super(Cargo.DEPUTADO_FEDERAL, numero, nome, partido);
    }

    @Override
    public String getDescricao() {
        return getNome() + " — " + getPartido().getNome();
    }
}

