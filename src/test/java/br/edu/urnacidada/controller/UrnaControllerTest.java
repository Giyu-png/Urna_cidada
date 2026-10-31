package br.edu.urnacidada.controller;

import br.edu.urnacidada.model.*;
import br.edu.urnacidada.repository.MemoriaVotoRepository;
import br.edu.urnacidada.service.CatalogoFicticio;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UrnaControllerTest {
    @Test void fluxoCompletoComDoisCargos() {
        MemoriaVotoRepository repository = new MemoriaVotoRepository();
        UrnaController urna = new UrnaController(CatalogoFicticio.criarServico(), repository);
        for (char c : "8101".toCharArray()) urna.digitar(c);
        assertTrue(urna.confirmar());
        assertEquals(Cargo.PRESIDENTE, urna.getCargoAtual());
        urna.votarBranco();
        assertTrue(urna.confirmar());
        assertTrue(urna.estaConcluida());
        assertEquals(1, repository.getQuantidadeSessoes());
        assertEquals(1, repository.contarVotos(Cargo.DEPUTADO_FEDERAL, TipoVoto.NOMINAL));
        assertEquals(1, repository.contarVotos(Cargo.PRESIDENTE, TipoVoto.BRANCO));
    }

    @Test void sessaoConcluidaPodeSerReenviadaComMesmoId() {
        UrnaController urna = new UrnaController(
                CatalogoFicticio.criarServico(), new MemoriaVotoRepository());
        assertThrows(IllegalStateException.class, urna::getSessaoConcluida);
        urna.votarBranco();
        assertTrue(urna.confirmar());
        urna.votarBranco();
        assertTrue(urna.confirmar());
        SessaoVotacao sessao = urna.getSessaoConcluida();
        assertEquals(2, sessao.getVotos().size());
        assertSame(sessao, urna.getSessaoConcluida());
    }

    @Test void corrigirApagaNumeroMasNaoVotoJaConfirmado() {
        UrnaController urna = new UrnaController(
                CatalogoFicticio.criarServico(), new MemoriaVotoRepository());
        urna.digitar('9');
        urna.corrigir();
        assertEquals("", urna.getDigitos());
        urna.votarBranco();
        urna.corrigir();
        assertFalse(urna.isBrancoSelecionado());
        for (char c : "8202".toCharArray()) urna.digitar(c);
        assertTrue(urna.confirmar());
        urna.corrigir();
        assertEquals(Cargo.PRESIDENTE, urna.getCargoAtual());
    }

    @Test void votoIncompletoNaoAvanca() {
        UrnaController urna = new UrnaController(
                CatalogoFicticio.criarServico(), new MemoriaVotoRepository());
        urna.digitar('8');
        assertFalse(urna.confirmar());
        assertEquals(Cargo.DEPUTADO_FEDERAL, urna.getCargoAtual());
    }
}

