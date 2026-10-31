package br.edu.urnacidada.view;

import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Informa o resultado da persistência sem confundir confirmação local e nuvem.
 * O usuário pode reenviar a mesma sessão ou seguir conscientemente sem nuvem.
 */
public final class TelaSincronizacao extends JPanel {
    private final JLabel titulo = Tema.texto("", 32, true);
    private final JLabel mensagem = Tema.texto("", 16, false);
    private final JButton tentarNovamente = Tema.botao("TENTAR NOVAMENTE", Tema.AZUL, Tema.BRANCO);
    private final JButton semNuvem = Tema.botao("CONTINUAR SEM SALVAR NA NUVEM", Tema.TEXTO, Tema.BRANCO);

    public TelaSincronizacao(Runnable aoTentarNovamente, Runnable aoContinuarSemNuvem) {
        setBackground(Tema.FUNDO);
        setLayout(new GridBagLayout());
        JPanel painel = new JPanel();
        painel.setBackground(Tema.FUNDO);
        painel.setBorder(new EmptyBorder(32, 40, 32, 40));
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mensagem.setAlignmentX(Component.CENTER_ALIGNMENT);
        tentarNovamente.setAlignmentX(Component.CENTER_ALIGNMENT);
        semNuvem.setAlignmentX(Component.CENTER_ALIGNMENT);

        tentarNovamente.addActionListener(evento -> aoTentarNovamente.run());
        semNuvem.addActionListener(evento -> aoContinuarSemNuvem.run());

        painel.add(titulo);
        painel.add(Box.createVerticalStrut(20));
        painel.add(mensagem);
        painel.add(Box.createVerticalStrut(28));
        painel.add(tentarNovamente);
        painel.add(Box.createVerticalStrut(10));
        painel.add(semNuvem);
        add(painel);
        mostrarSalvando();
    }

    public void mostrarSalvando() {
        titulo.setText("SALVANDO VOTAÇÃO...");
        mensagem.setText("Aguarde a confirmação do banco de dados Firestore.");
        tentarNovamente.setVisible(false);
        semNuvem.setVisible(false);
    }

    public void mostrarFalha() {
        titulo.setText("SALVAMENTO NÃO CONFIRMADO");
        mensagem.setText("<html><div style='width:520px;text-align:center'>"
                + "Não foi possível confirmar a gravação no Firestore. "
                + "Confira a conexão e as permissões antes de tentar novamente. "
                + "Reenviar esta mesma sessão não contabiliza os votos duas vezes."
                + "</div></html>");
        tentarNovamente.setVisible(true);
        semNuvem.setVisible(true);
    }
}
