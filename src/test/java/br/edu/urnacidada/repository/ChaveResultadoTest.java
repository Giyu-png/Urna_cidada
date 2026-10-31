package br.edu.urnacidada.repository;

import br.edu.urnacidada.model.Cargo;
import br.edu.urnacidada.model.Voto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChaveResultadoTest {
    @Test void criaChaveParaVotoNominal() {
        assertEquals("DEPUTADO_FEDERAL_NOMINAL_8101",
                ChaveResultado.doVoto(Voto.nominal(Cargo.DEPUTADO_FEDERAL, "8101")));
    }

    @Test void criaChaveParaVotoDeLegenda() {
        assertEquals("DEPUTADO_FEDERAL_LEGENDA_81",
                ChaveResultado.doVoto(Voto.legenda(Cargo.DEPUTADO_FEDERAL, "81")));
    }

    @Test void criaChaveParaVotoBranco() {
        assertEquals("PRESIDENTE_BRANCO",
                ChaveResultado.doVoto(Voto.branco(Cargo.PRESIDENTE)));
    }

    @Test void criaChaveParaVotoNulo() {
        assertEquals("DEPUTADO_FEDERAL_NULO",
                ChaveResultado.doVoto(Voto.nulo(Cargo.DEPUTADO_FEDERAL)));
    }

    @Test void exigeReferenciaEmVotoNominal() {
        assertThrows(IllegalArgumentException.class,
                () -> ChaveResultado.doVoto(Voto.nominal(Cargo.PRESIDENTE, "ABC")));
    }
}
