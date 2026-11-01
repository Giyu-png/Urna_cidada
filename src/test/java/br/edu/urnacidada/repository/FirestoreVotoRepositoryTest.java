package br.edu.urnacidada.repository;

import br.edu.urnacidada.config.FirebaseConfig;
import br.edu.urnacidada.model.Cargo;
import br.edu.urnacidada.model.SessaoVotacao;
import br.edu.urnacidada.model.Voto;
import com.google.cloud.firestore.Firestore;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfEnvironmentVariable(named = "FIRESTORE_EMULATOR_HOST", matches = ".+")
class FirestoreVotoRepositoryTest {
    private Firestore banco;
    private FirestoreVotoRepository repositorio;

    @BeforeEach
    void configurar() throws Exception {
        banco = FirebaseConfig.criarCliente();
        repositorio = new FirestoreVotoRepository(
                banco, "teste-ci-" + UUID.randomUUID());
    }

    @AfterEach
    void fecharCliente() throws Exception {
        if (banco != null) banco.close();
    }

    @Test
    void registraOsDoisVotosEReenvioDaMesmaSessaoNaoDuplica() {
        SessaoVotacao sessao = new SessaoVotacao();
        sessao.registrarVoto(Voto.nominal(Cargo.DEPUTADO_FEDERAL, "8101"));
        sessao.registrarVoto(Voto.nominal(Cargo.PRESIDENTE, "81"));

        repositorio.registrarSessao(sessao);
        repositorio.registrarSessao(sessao);

        assertEquals(Map.of(
                "DEPUTADO_FEDERAL_NOMINAL_8101", 1L,
                "PRESIDENTE_NOMINAL_81", 1L
        ), repositorio.consultarResultados());
    }

    @Test
    void rejeitaSessaoIncompletaSemGravarResultados() {
        SessaoVotacao sessao = new SessaoVotacao();
        sessao.registrarVoto(Voto.nominal(Cargo.DEPUTADO_FEDERAL, "8101"));

        assertThrows(IllegalArgumentException.class,
                () -> repositorio.registrarSessao(sessao));
        assertTrue(repositorio.consultarResultados().isEmpty());
    }
}
