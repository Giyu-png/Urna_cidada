package br.edu.urnacidada.model;

import java.util.Objects;

public final class Voto {
    private final Cargo cargo;
    private final TipoVoto tipo;
    private final String referencia;

    private Voto(Cargo cargo, TipoVoto tipo, String referencia) {
        this.cargo = Objects.requireNonNull(cargo, "cargo");
        this.tipo = Objects.requireNonNull(tipo, "tipo");
        this.referencia = referencia;
    }

    public static Voto nominal(Cargo cargo, String numero) {
        if (numero == null || numero.isBlank()) throw new IllegalArgumentException("Número obrigatório.");
        return new Voto(cargo, TipoVoto.NOMINAL, numero);
    }

    public static Voto legenda(Cargo cargo, String numero) {
        if (cargo != Cargo.DEPUTADO_FEDERAL || numero == null || !numero.matches("\\d{2}")) {
            throw new IllegalArgumentException("Voto de legenda somente para cargo proporcional.");
        }
        return new Voto(cargo, TipoVoto.LEGENDA, numero);
    }

    public static Voto branco(Cargo cargo) { return new Voto(cargo, TipoVoto.BRANCO, null); }
    public static Voto nulo(Cargo cargo) { return new Voto(cargo, TipoVoto.NULO, null); }

    public Cargo getCargo() { return cargo; }
    public TipoVoto getTipo() { return tipo; }
    public String getReferencia() { return referencia; }
}

