package br.edu.urnacidada.view;

import br.edu.urnacidada.controller.UrnaController;
import br.edu.urnacidada.model.Cargo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public final class TelaUrna extends JPanel {
    private final UrnaController controller;
    private final Runnable aoConcluir;
    private final JLabel tituloCargo = Tema.texto("", 28, true);
    private final JLabel digitos = Tema.texto("", 48, true);
    private final JLabel descricao = Tema.texto("", 17, false);

    public TelaUrna(UrnaController controller, Runnable aoConcluir) {
        this.controller = controller;
        this.aoConcluir = aoConcluir;
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(28, 32, 28, 32));
        setBackground(Tema.FUNDO);

        JLabel cabecalho = Tema.texto("URNA CIDADÃ   /   SIMULAÇÃO EDUCATIVA", 16, true);
        cabecalho.setForeground(Tema.AZUL);
        add(cabecalho, BorderLayout.NORTH);

        JPanel corpo = new JPanel(new GridLayout(1, 2, 24, 0));
        corpo.setOpaque(false);
        corpo.add(criarPainelVoto());
        corpo.add(criarPainelTeclado());
        add(corpo, BorderLayout.CENTER);

        JLabel rodape = Tema.texto("Para aprender: BRANCO escolhe voto em branco, CORRIGE limpa e CONFIRMA registra.", 14, false);
        rodape.setForeground(Tema.CINZA);
        add(rodape, BorderLayout.SOUTH);

        configurarAtalhos();
        atualizar();
    }

    private JPanel criarPainelVoto() {
        JPanel painel = new JPanel(new BorderLayout(12, 12));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDDE3E7)),
                new EmptyBorder(26, 24, 26, 24)));

        JLabel legenda = Tema.texto("SEU VOTO PARA", 16, false);
        legenda.setForeground(Tema.CINZA);

        JPanel acima = new JPanel(new GridLayout(2, 1, 0, 7));
        acima.setOpaque(false);
        acima.add(legenda);
        acima.add(tituloCargo);
        painel.add(acima, BorderLayout.NORTH);

        digitos.setHorizontalAlignment(SwingConstants.CENTER);
        digitos.setFont(new Font(Font.MONOSPACED, Font.BOLD, 44));
        digitos.setForeground(Tema.AZUL);
        painel.add(digitos, BorderLayout.CENTER);

        descricao.setVerticalAlignment(SwingConstants.TOP);
        painel.add(descricao, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarPainelTeclado() {
        JPanel painel = new JPanel(new BorderLayout(0, 14));
        painel.setOpaque(false);

        JPanel numeros = new JPanel(new GridLayout(4, 3, 10, 10));
        numeros.setOpaque(false);
        for (String numero : new String[]{"1","2","3","4","5","6","7","8","9","","0",""}) {
            if (numero.isEmpty()) {
                numeros.add(new JLabel(""));
            } else {
                JButton botao = Tema.botao(numero, Tema.TEXTO, Color.WHITE);
                botao.setFont(new Font("SansSerif", Font.BOLD, 28));
                botao.addActionListener(evento -> digitar(numero.charAt(0)));
                numeros.add(botao);
            }
        }
        painel.add(numeros, BorderLayout.CENTER);

        JPanel comandos = new JPanel(new GridLayout(1, 3, 8, 0));
        comandos.setOpaque(false);
        JButton branco = Tema.botao("BRANCO", Color.WHITE, Tema.TEXTO);
        JButton corrige = Tema.botao("CORRIGE", new Color(0xEBA54B), Tema.TEXTO);
        JButton confirma = Tema.botao("CONFIRMA", new Color(0x25815A), Color.WHITE);
        for (JButton b : new JButton[]{branco, corrige, confirma}) {
            b.setFont(new Font("SansSerif", Font.BOLD, 14));
            b.setPreferredSize(new Dimension(110, 58));
        }
        branco.addActionListener(evento -> { controller.votarBranco(); atualizar(); });
        corrige.addActionListener(evento -> { controller.corrigir(); atualizar(); });
        confirma.addActionListener(evento -> confirmar());
        comandos.add(branco);
        comandos.add(corrige);
        comandos.add(confirma);
        painel.add(comandos, BorderLayout.SOUTH);
        return painel;
    }

    private void digitar(char numero) {
        controller.digitar(numero);
        atualizar();
    }

    private void confirmar() {
        try {
            if (!controller.confirmar()) {
                JOptionPane.showMessageDialog(this,
                        "Complete o número do candidato ou escolha BRANCO.",
                        "Voto incompleto", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (controller.estaConcluida()) {
                aoConcluir.run();
            } else {
                atualizar();
            }
        } catch (RuntimeException erro) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível registrar esta simulação: " + erro.getMessage(),
                    "Erro de registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizar() {
        Cargo cargo = controller.getCargoAtual();
        if (cargo == null) return;
        tituloCargo.setText(cargo.getNome());
        String digitado = controller.getDigitos();
        StringBuilder exibicao = new StringBuilder();
        for (int i = 0; i < cargo.getQuantidadeDigitos(); i++) {
            exibicao.append(i < digitado.length() ? digitado.charAt(i) : '_').append(' ');
        }
        digitos.setText(controller.isBrancoSelecionado() ? "BRANCO" : exibicao.toString());
        descricao.setText("<html><div style='width:290px'>"
                + controller.getDescricaoEscolha() + "</div></html>");
    }

    private void configurarAtalhos() {
        var mapa = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        var acoes = getActionMap();
        for (int n = 0; n <= 9; n++) {
            final char numero = (char) ('0' + n);
            mapa.put(KeyStroke.getKeyStroke(KeyEvent.VK_0 + n, 0), "numero" + n);
            mapa.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0 + n, 0), "numero" + n);
            acoes.put("numero" + n, new AbstractAction() {
                @Override public void actionPerformed(ActionEvent e) { digitar(numero); }
            });
        }
        mapa.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "corrigir");
        acoes.put("corrigir", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                controller.corrigir(); atualizar();
            }
        });
        mapa.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "confirmar");
        acoes.put("confirmar", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { confirmar(); }
        });
    }
}

