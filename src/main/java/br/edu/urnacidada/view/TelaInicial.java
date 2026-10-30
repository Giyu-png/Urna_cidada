package br.edu.urnacidada.view;

import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public final class TelaInicial extends JPanel {
    public TelaInicial(Runnable iniciar) {
        setBackground(Tema.FUNDO);
        setLayout(new GridBagLayout());

        JPanel conteudo = new JPanel();
        conteudo.setBackground(Tema.FUNDO);
        conteudo.setBorder(new EmptyBorder(32, 50, 32, 50));
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));

        JLabel identificacao = Tema.texto("PROJETO DE EXTENSÃO  •  CIÊNCIA DA COMPUTAÇÃO", 14, true);
        identificacao.setForeground(Tema.AZUL);
        identificacao.setAlignmentX(Component.CENTER_ALIGNMENT);
        conteudo.add(identificacao);
        conteudo.add(Box.createVerticalStrut(24));

        JLabel titulo = Tema.texto("URNA CIDADÃ", 44, true);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        conteudo.add(titulo);
        conteudo.add(Box.createVerticalStrut(12));

        JLabel descricao = Tema.texto("Aprenda a votar em uma simulação educativa.", 20, false);
        descricao.setAlignmentX(Component.CENTER_ALIGNMENT);
        conteudo.add(descricao);
        conteudo.add(Box.createVerticalStrut(10));

        JLabel aviso = Tema.texto("Candidaturas fictícias. Sem vínculo com a Justiça Eleitoral.", 14, false);
        aviso.setForeground(Tema.CINZA);
        aviso.setAlignmentX(Component.CENTER_ALIGNMENT);
        conteudo.add(aviso);
        conteudo.add(Box.createVerticalStrut(40));

        var botao = Tema.botao("INICIAR SIMULAÇÃO", Tema.AZUL, Tema.BRANCO);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.addActionListener(evento -> iniciar.run());
        conteudo.add(botao);

        add(conteudo);
    }
}

