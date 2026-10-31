package br.edu.urnacidada.config;

import br.edu.urnacidada.model.Cargo;
import br.edu.urnacidada.model.SessaoVotacao;
import br.edu.urnacidada.model.Voto;
import br.edu.urnacidada.repository.ChaveResultado;
import br.edu.urnacidada.repository.FirestoreVotoRepository;
import com.google.cloud.firestore.Firestore;
import java.util.Map;

/**
 * Teste manual de integração com banco real.
 *
 * ATENÇÃO: esta classe ESCREVE documentos de teste no Cloud Firestore.
 * Usa exclusivamente a simulação "diagnostico-integracao-2026", separada
 * da oficina. Cada execução acrescenta uma sessão fictícia completa.
 *
 * Antes de executar, configure Application Default Credentials (ADC):
 * gcloud auth application-default login
 */
public final class TesteGravacaoFirestore {
    private static final String SIMULACAO_TESTE = "diagnostico-integracao-2026";

    private TesteGravacaoFirestore() {}

    public static void main(String[] args) {
        try (Firestore banco = FirebaseConfig.criarCliente()) {
            FirestoreVotoRepository repositorio =
                    new FirestoreVotoRepository(banco, SIMULACAO_TESTE);

            // Valores fictícios, desvinculados de pessoas ou candidatos reais.
            SessaoVotacao sessao = new SessaoVotacao();
            Voto deputado = Voto.nominal(Cargo.DEPUTADO_FEDERAL, "8101");
            Voto presidente = Voto.branco(Cargo.PRESIDENTE);
            sessao.registrarVoto(deputado);
            sessao.registrarVoto(presidente);

            String chaveDeputado = ChaveResultado.doVoto(deputado);
            String chavePresidente = ChaveResultado.doVoto(presidente);

            System.out.println("Projeto: " + FirebaseConfig.obterIdProjeto());
            System.out.println("Área isolada de testes: " + SIMULACAO_TESTE);
            Map<String, Long> antes = repositorio.consultarResultados();
            long antesDeputado = antes.getOrDefault(chaveDeputado, 0L);
            long antesPresidente = antes.getOrDefault(chavePresidente, 0L);

            // A gravação deve adicionar exatamente um voto por cargo.
            repositorio.registrarSessao(sessao);
            Map<String, Long> depois = repositorio.consultarResultados();
            conferir(chaveDeputado, antesDeputado + 1, depois);
            conferir(chavePresidente, antesPresidente + 1, depois);
            System.out.println("GRAVAÇÃO OK: um voto fictício por cargo.");

            // Repetir a MESMA sessão não pode incrementar os contadores.
            repositorio.registrarSessao(sessao);
            Map<String, Long> aposRepeticao = repositorio.consultarResultados();
            conferir(chaveDeputado, depois.get(chaveDeputado), aposRepeticao);
            conferir(chavePresidente, depois.get(chavePresidente), aposRepeticao);

            System.out.println("DUPLICIDADE OK: repetir a sessão não adicionou votos.");
            System.out.println("LEITURA OK: resultados consultados diretamente do Firestore.");
            System.out.println("SUCESSO: integração de escrita e leitura validada.");
            System.out.println("Confira os documentos no Console Firebase em:");
            System.out.println("simulacoes/" + SIMULACAO_TESTE + "/resultados");
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            System.err.println("Teste interrompido.");
            System.exit(1);
        } catch (Exception erro) {
            System.err.println("FALHA no teste real do Firestore: " + erro.getMessage());
            erro.printStackTrace();
            System.exit(1);
        }
    }

    private static void conferir(String chave, long esperado, Map<String, Long> atual) {
        long encontrado = atual.getOrDefault(chave, 0L);
        if (encontrado != esperado) {
            throw new IllegalStateException(
                    "Contador incorreto em " + chave + ". Esperado: " + esperado
                    + ", encontrado: " + encontrado);
        }
    }
}
