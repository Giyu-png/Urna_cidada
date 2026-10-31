package br.edu.urnacidada.config;

import com.google.cloud.firestore.Firestore;

/**
 * Diagnóstico manual de conexão: testa leitura, sem criar ou alterar documentos.
 *
 * Configure FIRESTORE_PROJECT_ID e Application Default Credentials (ADC)
 * no ambiente antes de executar esta classe pelo IntelliJ IDEA.
 */
public final class TesteConexaoFirestore {
    private TesteConexaoFirestore() {}

    public static void main(String[] args) {
        String projectId = System.getenv("FIRESTORE_PROJECT_ID");
        try (Firestore db = FirebaseConfig.criarCliente()) {
            System.out.println("Verificando acesso ao projeto: " + projectId);
            db.collection("simulacoes").limit(1).get().get();
            System.out.println("SUCESSO: conexão e leitura do Cloud Firestore confirmadas.");
            System.out.println("Nenhum documento foi criado ou alterado.");
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            System.err.println("Teste interrompido.");
            System.exit(1);
        } catch (Exception erro) {
            System.err.println("FALHA na verificação de acesso ao Firestore.");
            System.err.println(erro.getClass().getSimpleName() + ": " + erro.getMessage());
            System.exit(1);
        }
    }
}
