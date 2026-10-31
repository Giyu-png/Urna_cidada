package br.edu.urnacidada.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import java.io.IOException;

/**
 * Cria o cliente Firestore com ADC, ou sem credenciais reais quando o emulador está ativo.
 * O ID público do projeto tem um valor padrão para facilitar a execução local.
 * Credenciais privadas nunca devem ser colocadas no código ou repositório.
 */
public final class FirebaseConfig {
    private static final String ID_PROJETO_PADRAO = "urna-cidada-pjde-3";

    private FirebaseConfig() {}

    public static String obterIdProjeto() {
        String variavel = System.getenv("FIRESTORE_PROJECT_ID");
        return variavel == null || variavel.isBlank()
                ? ID_PROJETO_PADRAO
                : variavel.trim();
    }

    public static Firestore criarCliente() throws IOException {
        FirestoreOptions.Builder opcoes = FirestoreOptions.getDefaultInstance()
                .toBuilder()
                .setProjectId(obterIdProjeto());

        String hostEmulador = System.getenv("FIRESTORE_EMULATOR_HOST");
        if (hostEmulador != null && !hostEmulador.isBlank()) {
            opcoes.setEmulatorHost(hostEmulador.trim())
                    .setCredentials(new FirestoreOptions.EmulatorCredentials());
        } else {
            opcoes.setCredentials(GoogleCredentials.getApplicationDefault());
        }

        return opcoes.build().getService();
    }
}
