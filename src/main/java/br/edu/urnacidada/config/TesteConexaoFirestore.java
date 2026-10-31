package br.edu.urnacidada.config;

import com.google.cloud.firestore.Firestore;

/**
 * Diagnóstico manual de leitura no Firestore (sem criar ou alterar documentos).
 *
 * É necessário autenticar localmente com:
 * gcloud auth application-default login
 *
 * A variável FIRESTORE_PROJECT_ID é opcional; o projeto padrão é
 * urna-cidada-pjde-3. Nenhuma credencial é salva no repositório.
 */
public final class TesteConexaoFirestore {
    private TesteConexaoFirestore() {}

    public static void main(String[] args) {
        try (Firestore db = FirebaseConfig.criarCliente()) {
            System.out.println("Verificando acesso ao projeto: " + FirebaseConfig.obterIdProjeto());
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
