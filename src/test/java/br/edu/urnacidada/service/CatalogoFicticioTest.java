package br.edu.urnacidada.service;

import br.edu.urnacidada.model.Cargo;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CatalogoFicticioTest {
    @Test
    void servicoEApuracaoUsamMesmoCadastro() {
        var cadastro = CatalogoFicticio.fichas();
        assertFalse(cadastro.isEmpty(), "O catálogo não pode ficar vazio");
        assertEquals(cadastro.size(), CatalogoFicticio.candidatos().size());
        assertFalse(CatalogoFicticio.partidos().isEmpty(), "Ao menos um partido é necessário");

        var numerosPartidos = new HashSet<String>();
        for (var partido : CatalogoFicticio.partidos()) {
            assertTrue(numerosPartidos.add(partido.getNumero()), "Legenda de partido duplicada");
        }

        var servico = CatalogoFicticio.criarServico();
        var numeros = new HashSet<String>();
        var cargos = new HashSet<Cargo>();
        for (var ficha : cadastro) {
            var candidato = ficha.candidato();
            cargos.add(candidato.getCargo());
            assertTrue(numerosPartidos.contains(candidato.getPartido().getNumero()),
                    "Candidato vinculado a partido ausente");
            assertTrue(numeros.add(candidato.getCargo().name() + ":" + candidato.getNumero()),
                    "Número duplicado no mesmo cargo");
            assertSame(candidato, CatalogoFicticio.buscarFicha(
                    candidato.getCargo(), candidato.getNumero()).orElseThrow().candidato());
            assertTrue(servico.buscarCandidato(
                    candidato.getCargo(), candidato.getNumero()).isPresent());
            assertNotNull(CatalogoFicticioTest.class.getResource(ficha.fotoRecurso()),
                    "Foto ausente: " + ficha.fotoRecurso());
        }

        assertTrue(cargos.contains(Cargo.DEPUTADO_FEDERAL),
                "É necessário cadastrar ao menos um deputado");
        assertTrue(cargos.contains(Cargo.PRESIDENTE),
                "É necessário cadastrar ao menos um presidente");
        assertTrue(CatalogoFicticio.buscarFicha(Cargo.PRESIDENTE, "9999").isEmpty());
    }
}
