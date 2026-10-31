package br.edu.urnacidada.service;

import br.edu.urnacidada.model.Cargo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CatalogoFicticioTest {
    @Test
    void servicoEApuracaoUsamMesmoCadastro() {
        var cadastro = CatalogoFicticio.fichas();
        assertEquals(4, cadastro.size());
        assertEquals(4, CatalogoFicticio.candidatos().size());
        assertEquals("Lia Inventora",
                CatalogoFicticio.buscarFicha(Cargo.DEPUTADO_FEDERAL, "8101")
                    .orElseThrow().candidato().getNome());
        assertEquals("/candidatos/deputado_8101.png",
                CatalogoFicticio.buscarFicha(Cargo.DEPUTADO_FEDERAL, "8101")
                    .orElseThrow().fotoRecurso());
        assertTrue(CatalogoFicticio.buscarFicha(Cargo.PRESIDENTE, "8101").isEmpty());
    }
}
