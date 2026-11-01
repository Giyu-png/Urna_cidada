package br.edu.urnacidada;

import br.edu.urnacidada.view.JanelaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorado) {
                // Mantém o visual padrão do Swing caso o tema do sistema falhe.
            }
            new JanelaPrincipal().setVisible(true);
        });
    }
}


