package br.edu.urnacidada.apuracao;

import br.edu.urnacidada.model.Cargo;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResumoApuracaoTest {
    @Test
    void calculaResultadosSeparadamentePorCargo() {
        var dados = ResumoApuracao.analisar(Map.of(
            "DEPUTADO_FEDERAL_NOMINAL_6767", 3L,
            "DEPUTADO_FEDERAL_LEGENDA_10", 1L,
            "DEPUTADO_FEDERAL_BRANCO", 1L,
            "PRESIDENTE_NOMINAL_67", 4L,
            "PRESIDENTE_NULO", 1L
        ));
        var deputado = dados.get(Cargo.DEPUTADO_FEDERAL);
        var presidente = dados.get(Cargo.PRESIDENTE);

        assertEquals(5L, deputado.total());
        assertEquals(1L, deputado.brancos());
        assertEquals(0L, deputado.nulos());
        assertEquals(5L, presidente.total());
        assertEquals(0L, presidente.brancos());
        assertEquals(1L, presidente.nulos());
        assertEquals("Emily Vik", deputado.linhas().get(0).opcao());
        assertEquals(3L, deputado.linhas().get(0).votos());
        assertEquals(60.0, deputado.linhas().get(0).percentual(), 0.001);
        assertTrue(deputado.linhas().stream().anyMatch(
                linha -> linha.numero().equals("10")
                        && linha.opcao().contains("Partido RacionaisMC")));
    }

    @Test
    void bancoSemVotosExibeTodosOsCandidatosComZero() {
        var dados = ResumoApuracao.analisar(Map.of());
        for (Cargo cargo : Cargo.values()) {
            assertEquals(0L, dados.get(cargo).total());
            assertTrue(dados.get(cargo).linhas().stream().allMatch(l -> l.votos() == 0L));
            assertTrue(dados.get(cargo).linhas().stream().allMatch(l -> l.percentual() == 0.0));
        }
    }

    @Test
    void candidatoAdicionadoNaoDesapareceDaApuracao() {
        var dados = ResumoApuracao.analisar(Map.of("DEPUTADO_FEDERAL_NOMINAL_8399", 2L));
        var deputado = dados.get(Cargo.DEPUTADO_FEDERAL);
        assertEquals(2L, deputado.total());
        assertTrue(deputado.linhas().stream()
                .anyMatch(l -> l.numero().equals("8399") && l.votos() == 2L));
    }

    @Test
    void rejeitaContagensNegativas() {
        assertThrows(IllegalArgumentException.class,
                () -> ResumoApuracao.analisar(Map.of("PRESIDENTE_BRANCO", -1L)));
    }
}
