package br.edu.urnacidada.service;

import br.edu.urnacidada.model.Cargo;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CatalogoFicticioTest {
    @Test
    void servicoEApuracaoUsamMesmoCadastro() {
        var cadastro = CatalogoFicticio.fichas();
        assertEquals(8, cadastro.size());
        assertEquals(cadastro.size(), CatalogoFicticio.candidatos().size());
        assertEquals(4, CatalogoFicticio.partidos().size());

        var servico = CatalogoFicticio.criarServico();
        var numeros = new HashSet<String>();
        for (var ficha : cadastro) {
            var candidato = ficha.candidato();
            assertTrue(numeros.add(candidato.getCargo().name() + ":" + candidato.getNumero()),
                    "Número duplicado no mesmo cargo");
            assertSame(candidato, CatalogoFicticio.buscarFicha(
                    candidato.getCargo(), candidato.getNumero()).orElseThrow().candidato());
            assertTrue(servico.buscarCandidato(
                    candidato.getCargo(), candidato.getNumero()).isPresent());
            assertNotNull(CatalogoFicticioTest.class.getResource(ficha.fotoRecurso()),
                    "Foto ausente: " + ficha.fotoRecurso());
        }

        assertEquals("Gato Babao", CatalogoFicticio.buscarFicha(
                Cargo.DEPUTADO_FEDERAL, "1069").orElseThrow().candidato().getNome());
        assertTrue(CatalogoFicticio.buscarFicha(Cargo.PRESIDENTE, "1069").isEmpty());
    }
}
