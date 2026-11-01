package br.edu.urnacidada.apuracao;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Entrada independente do painel administrativo. A urna continua em
 * br.edu.urnacidada.Main, sem menu de apuração para o participante.
 */
public final class ApuracaoMain {
    private ApuracaoMain() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorado) {
                // O painel permanece funcional no visual Swing padrão.
            }
            new JanelaApuracao().setVisible(true);
        });
    }
}
