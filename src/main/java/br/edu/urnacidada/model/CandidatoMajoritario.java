package br.edu.urnacidada.model;

public final class CandidatoMajoritario extends Candidato {
    private final String nomeVice;

    public CandidatoMajoritario(String numero, String nome, String nomeVice, Partido partido) {
        super(Cargo.PRESIDENTE, numero, nome, partido);
        if (nomeVice == null || nomeVice.isBlank()) {
            throw new IllegalArgumentException("Nome da vice-candidatura é obrigatório.");
        }
        this.nomeVice = nomeVice;
    }

    public String getNomeVice() { return nomeVice; }

    @Override
    public String getDescricao() {
        return getNome() + " (vice: " + nomeVice + ") — " + getPartido().getNome();
    }
}

