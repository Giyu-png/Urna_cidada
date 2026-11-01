package br.edu.urnacidada.repository;

import br.edu.urnacidada.model.TipoVoto;
import br.edu.urnacidada.model.Voto;
import java.util.Objects;

/** Converte um voto fictício em uma chave estável para uma contagem agregada. */
public final class ChaveResultado {
    private ChaveResultado() {}

    public static String doVoto(Voto voto) {
        Objects.requireNonNull(voto, "voto");
        String prefixo = voto.getCargo().name() + "_" + voto.getTipo().name();
        if (voto.getTipo() == TipoVoto.NOMINAL || voto.getTipo() == TipoVoto.LEGENDA) {
            if (voto.getReferencia() == null || !voto.getReferencia().matches("\\d{2,4}")) {
                throw new IllegalArgumentException("Referência inválida para voto nominal/legenda.");
            }
            return prefixo + "_" + voto.getReferencia();
        }
        return prefixo;
    }
}
