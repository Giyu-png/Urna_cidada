package br.edu.urnacidada.view;

import br.edu.urnacidada.config.FirebaseConfig;
import br.edu.urnacidada.controller.UrnaController;
import br.edu.urnacidada.model.SessaoVotacao;
import br.edu.urnacidada.repository.FirestoreVotoRepository;
import br.edu.urnacidada.repository.MemoriaVotoRepository;
import br.edu.urnacidada.service.CatalogoFicticio;
import br.edu.urnacidada.service.VotacaoService;
import com.google.cloud.firestore.Firestore;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.concurrent.ExecutionException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

/**
 * Simulação Swing: confirma os votos em memória e sincroniza uma sessão completa
 * com o Firestore sem bloquear a thread de eventos da interface.
 */
public final class JanelaPrincipal extends JFrame {
    private static final String SIMULACAO_NUVEM = "demonstracao-interface-2026";

    private final CardLayout layout = new CardLayout();
    private final JPanel telas = new JPanel(layout);
    private final VotacaoService service = CatalogoFicticio.criarServico();
    private final MemoriaVotoRepository repository = new MemoriaVotoRepository();
    private TelaUrna telaUrna;
    private TelaFim telaFim;
    private TelaSincronizacao telaSincronizacao;
    private UrnaController controladorAtual;

    public JanelaPrincipal() {
        super("Urna Cidadã — Simulação Educativa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 580));
        setSize(1024, 650);
        setLocationRelativeTo(null);
        telas.add(new TelaInicial(this::iniciarVotacao), "inicio");
        setContentPane(telas);
        layout.show(telas, "inicio");
    }

    public void iniciarVotacao() {
        if (telaUrna != null) telas.remove(telaUrna);
        controladorAtual = new UrnaController(service, repository);
        telaUrna = new TelaUrna(controladorAtual, this::iniciarSincronizacao);
        telas.add(telaUrna, "urna");
        layout.show(telas, "urna");
    }

    private void iniciarSincronizacao() {
        // Manter o mesmo ID de sessão é essencial para uma tentativa segura de reenvio.
        SessaoVotacao sessao = controladorAtual.getSessaoConcluida();
        if (telaSincronizacao != null) telas.remove(telaSincronizacao);
        telaSincronizacao = new TelaSincronizacao(
                () -> salvarNaNuvem(sessao),
                () -> exibirFim(false));
        telas.add(telaSincronizacao, "sincronizacao");
        layout.show(telas, "sincronizacao");
        salvarNaNuvem(sessao);
    }

    private void salvarNaNuvem(SessaoVotacao sessao) {
        TelaSincronizacao painel = telaSincronizacao;
        painel.mostrarSalvando();
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // A autenticação e todas as chamadas de rede acontecem fora da EDT.
                try (Firestore banco = FirebaseConfig.criarCliente()) {
                    new FirestoreVotoRepository(banco, SIMULACAO_NUVEM)
                            .registrarSessao(sessao);
                }
                return null;
            }

            @Override
            protected void done() {
                if (telaSincronizacao != painel) return;
                try {
                    get();
                    exibirFim(true);
                } catch (InterruptedException erro) {
                    Thread.currentThread().interrupt();
                    painel.mostrarFalha();
                } catch (ExecutionException erro) {
                    // Não imprimir os votos, a identidade do participante ou credenciais.
                    System.err.println("Falha na sincronização: " + erro.getCause());
                    painel.mostrarFalha();
                }
            }
        }.execute();
    }

    private void exibirFim(boolean confirmadoNaNuvem) {
        if (telaFim != null) telas.remove(telaFim);
        telaFim = new TelaFim(this::iniciarVotacao, confirmadoNaNuvem);
        telas.add(telaFim, "fim");
        layout.show(telas, "fim");
    }
}
