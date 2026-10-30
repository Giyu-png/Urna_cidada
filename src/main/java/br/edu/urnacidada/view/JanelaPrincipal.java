package br.edu.urnacidada.view;

import br.edu.urnacidada.controller.UrnaController;
import br.edu.urnacidada.repository.MemoriaVotoRepository;
import br.edu.urnacidada.service.CatalogoFicticio;
import br.edu.urnacidada.service.VotacaoService;
import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public final class JanelaPrincipal extends JFrame {
    private final CardLayout layout = new CardLayout();
    private final JPanel telas = new JPanel(layout);
    private final VotacaoService service = CatalogoFicticio.criarServico();
    private final MemoriaVotoRepository repository = new MemoriaVotoRepository();
    private TelaUrna telaUrna;
    private TelaFim telaFim;

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
        telaUrna = new TelaUrna(new UrnaController(service, repository), this::exibirFim);
        telas.add(telaUrna, "urna");
        layout.show(telas, "urna");
    }

    private void exibirFim() {
        if (telaFim != null) telas.remove(telaFim);
        telaFim = new TelaFim(this::iniciarVotacao);
        telas.add(telaFim, "fim");
        layout.show(telas, "fim");
    }
}

