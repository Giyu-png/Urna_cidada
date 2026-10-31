package br.edu.urnacidada.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import java.io.IOException;

/**
 * Cria o cliente Firestore usando credenciais locais do ambiente.
 * Nenhuma chave privada deve ser armazenada no código ou no repositório.
 */
public final class FirebaseConfig {
    private FirebaseConfig() {}

    public static Firestore criarCliente() throws IOException {
        String projectId = System.getenv("FIRESTORE_PROJECT_ID");
        if (projectId == null || projectId.isBlank()) {
            throw new IllegalStateException(
                "Defina FIRESTORE_PROJECT_ID com o identificador do projeto Firebase.");
        }
        return FirestoreOptions.getDefaultInstance().toBuilder()
                .setProjectId(projectId.trim())
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build()
                .getService();
    }
}
