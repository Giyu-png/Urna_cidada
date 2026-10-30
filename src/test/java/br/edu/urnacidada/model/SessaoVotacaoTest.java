package br.edu.urnacidada.model;

import br.edu.urnacidada.repository.MemoriaVotoRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SessaoVotacaoTest {
    @Test void respeitaOrdemDosCargos() {
        SessaoVotacao sessao = new SessaoVotacao();
        assertThrows(IllegalArgumentException.class,
                () -> sessao.registrarVoto(Voto.branco(Cargo.PRESIDENTE)));
        sessao.registrarVoto(Voto.branco(Cargo.DEPUTADO_FEDERAL));
        sessao.registrarVoto(Voto.nulo(Cargo.PRESIDENTE));
        assertTrue(sessao.estaConcluida());
        assertThrows(IllegalStateException.class,
                () -> sessao.registrarVoto(Voto.branco(Cargo.PRESIDENTE)));
    }

    @Test void repositorioEvitaDuplicidadeDaMesmaSessao() {
        SessaoVotacao sessao = new SessaoVotacao();
        sessao.registrarVoto(Voto.branco(Cargo.DEPUTADO_FEDERAL));
        sessao.registrarVoto(Voto.branco(Cargo.PRESIDENTE));
        MemoriaVotoRepository repo = new MemoriaVotoRepository();
        repo.registrarSessao(sessao);
        repo.registrarSessao(sessao);
        assertEquals(1, repo.getQuantidadeSessoes());
        assertEquals(1, repo.contarVotos(Cargo.PRESIDENTE, TipoVoto.BRANCO));
    }

    @Test void naoSalvaSessaoIncompleta() {
        assertThrows(IllegalArgumentException.class,
                () -> new MemoriaVotoRepository().registrarSessao(new SessaoVotacao()));
    }
}

