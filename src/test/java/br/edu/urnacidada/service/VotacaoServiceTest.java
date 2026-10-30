package br.edu.urnacidada.service;

import br.edu.urnacidada.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VotacaoServiceTest {
    private final VotacaoService service = CatalogoFicticio.criarServico();

    @Test void identificaVotoNominalProporcional() {
        Voto voto = service.classificarVoto(Cargo.DEPUTADO_FEDERAL, "8101", false);
        assertEquals(TipoVoto.NOMINAL, voto.getTipo());
        assertEquals("8101", voto.getReferencia());
    }

    @Test void identificaVotoDeLegendaProporcional() {
        Voto voto = service.classificarVoto(Cargo.DEPUTADO_FEDERAL, "81", false);
        assertEquals(TipoVoto.LEGENDA, voto.getTipo());
    }

    @Test void identificaVotoNominalMajoritario() {
        assertEquals(TipoVoto.NOMINAL,
                service.classificarVoto(Cargo.PRESIDENTE, "82", false).getTipo());
    }

    @Test void registraVotoBranco() {
        assertEquals(TipoVoto.BRANCO,
                service.classificarVoto(Cargo.PRESIDENTE, "", true).getTipo());
    }

    @Test void classificaNumeroInexistenteComoNulo() {
        assertEquals(TipoVoto.NULO,
                service.classificarVoto(Cargo.DEPUTADO_FEDERAL, "9999", false).getTipo());
    }

    @Test void naoConfirmaNumerosIncompletos() {
        assertNull(service.classificarVoto(Cargo.DEPUTADO_FEDERAL, "8", false));
        assertNull(service.classificarVoto(Cargo.PRESIDENTE, "", false));
    }
}

