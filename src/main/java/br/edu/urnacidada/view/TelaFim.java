package br.edu.urnacidada.view;

import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public final class TelaFim extends JPanel {
    public TelaFim(Runnable novaSimulacao) {
        setBackground(Tema.FUNDO);
        setLayout(new GridBagLayout());
        JPanel painel = new JPanel();
        painel.setBackground(Tema.FUNDO);
        painel.setBorder(new EmptyBorder(32, 40, 32, 40));
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        JLabel titulo = Tema.texto("FIM", 72, true);
        titulo.setForeground(Tema.AZUL);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(10));

        JLabel mensagem = Tema.texto("Você concluiu a votação simulada!", 24, true);
        mensagem.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(mensagem);
        painel.add(Box.createVerticalStrut(12));

        JLabel aviso = Tema.texto("Seus votos fictícios foram contabilizados apenas nesta execução.", 15, false);
        aviso.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(aviso);
        painel.add(Box.createVerticalStrut(32));

        var botao = Tema.botao("NOVA SIMULAÇÃO", Tema.AZUL, Tema.BRANCO);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.addActionListener(evento -> novaSimulacao.run());
        painel.add(botao);
        add(painel);
    }
}

