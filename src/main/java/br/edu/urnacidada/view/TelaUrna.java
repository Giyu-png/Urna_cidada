package br.edu.urnacidada.view;

import br.edu.urnacidada.controller.UrnaController;
import br.edu.urnacidada.model.Candidato;
import br.edu.urnacidada.model.CandidatoMajoritario;
import br.edu.urnacidada.model.Cargo;
import br.edu.urnacidada.service.CatalogoFicticio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

/**
 * Tela minimalista da urna educativa, inspirada no visor e teclado da urna.
 * Mantém os comandos e o fluxo existentes, com informações da ficha central.
 */
public final class TelaUrna extends JPanel {
    private static final Color CHASSI = new Color(0xE7E9E8);
    private static final Color TECLADO = new Color(0x282D33);
    private static final Color NUMEROS = new Color(0x171B20);
    private static final Color BORDA_VISOR = new Color(0x9BA5A8);
    private static final Color CORRIGE = new Color(0xF4A643);
    private static final Color CONFIRMA = new Color(0x31946B);

    private final UrnaController controller;
    private final Runnable aoConcluir;
    private final JLabel tituloCargo = Tema.texto("", 28, true);
    private final JLabel nome = Tema.texto("", 22, true);
    private final JLabel partido = Tema.texto("", 16, false);
    private final JLabel vice = Tema.texto("", 15, false);
    private final JLabel situacao = Tema.texto("", 16, false);
    private final JLabel foto = new JLabel();
    private final JPanel fotoMoldura = new JPanel(new BorderLayout());
    private final JLabel[] caixasNumero = new JLabel[4];

    public TelaUrna(UrnaController controller, Runnable aoConcluir) {
        this.controller = controller;
        this.aoConcluir = aoConcluir;
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(17, 21, 17, 21));
        setBackground(CHASSI);

        JLabel marca = Tema.texto("URNA CIDADÃ", 17, true);
        marca.setForeground(Tema.TEXTO);
        add(marca, BorderLayout.NORTH);

        JPanel corpo = new JPanel(new BorderLayout(16, 0));
        corpo.setOpaque(false);
        corpo.add(criarVisor(), BorderLayout.CENTER);
        corpo.add(criarTeclado(), BorderLayout.EAST);
        add(corpo, BorderLayout.CENTER);

        JLabel aviso = Tema.texto("SIMULAÇÃO EDUCATIVA  •  CANDIDATURAS FICTÍCIAS", 12, true);
        aviso.setForeground(Tema.CINZA);
        aviso.setHorizontalAlignment(SwingConstants.CENTER);
        add(aviso, BorderLayout.SOUTH);

        configurarAtalhos();
        atualizar();
    }

    private JPanel criarVisor() {
        JPanel visor = new JPanel(new BorderLayout(0, 20));
        visor.setBackground(Color.WHITE);
        visor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA_VISOR, 2),
                new EmptyBorder(22, 25, 20, 25)));

        JPanel cabecalho = new JPanel(new GridLayout(2, 1, 0, 4));
        cabecalho.setOpaque(false);
        JLabel indicacao = Tema.texto("SEU VOTO PARA", 15, false);
        cabecalho.add(indicacao);
        cabecalho.add(tituloCargo);
        visor.add(cabecalho, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, 20));
        centro.setOpaque(false);

        JPanel linhaNumero = new JPanel(new BorderLayout(0, 8));
        linhaNumero.setOpaque(false);
        JLabel rotuloNumero = Tema.texto("NÚMERO", 14, false);
        linhaNumero.add(rotuloNumero, BorderLayout.NORTH);
        JPanel campos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        campos.setOpaque(false);
        for (int i = 0; i < caixasNumero.length; i++) {
            JLabel campo = Tema.texto("", 33, true);
            campo.setFont(new Font(Font.MONOSPACED, Font.BOLD, 34));
            campo.setHorizontalAlignment(SwingConstants.CENTER);
            campo.setVerticalAlignment(SwingConstants.CENTER);
            campo.setPreferredSize(new Dimension(49, 58));
            campo.setBorder(BorderFactory.createLineBorder(new Color(0x283037), 2));
            caixasNumero[i] = campo;
            campos.add(campo);
        }
        linhaNumero.add(campos, BorderLayout.CENTER);
        centro.add(linhaNumero, BorderLayout.NORTH);

        JPanel identificacao = new JPanel(new BorderLayout(12, 0));
        identificacao.setOpaque(false);
        JPanel dados = new JPanel(new GridLayout(3, 1, 0, 5));
        dados.setOpaque(false);
        dados.add(nome);
        dados.add(partido);
        dados.add(vice);
        identificacao.add(dados, BorderLayout.CENTER);

        fotoMoldura.setBackground(new Color(0xEDF0F2));
        fotoMoldura.setBorder(BorderFactory.createLineBorder(new Color(0xBBC4C9)));
        fotoMoldura.setPreferredSize(new Dimension(128, 158));
        foto.setHorizontalAlignment(SwingConstants.CENTER);
        fotoMoldura.add(foto, BorderLayout.CENTER);
        identificacao.add(fotoMoldura, BorderLayout.EAST);
        centro.add(identificacao, BorderLayout.CENTER);
        visor.add(centro, BorderLayout.CENTER);

        JPanel rodapeVisor = new JPanel(new BorderLayout(0, 9));
        rodapeVisor.setOpaque(false);
        rodapeVisor.add(situacao, BorderLayout.CENTER);
        JLabel orientacao = Tema.texto("CORRIGE para alterar     CONFIRMA para registrar", 12, false);
        orientacao.setForeground(Tema.CINZA);
        rodapeVisor.add(orientacao, BorderLayout.SOUTH);
        visor.add(rodapeVisor, BorderLayout.SOUTH);
        return visor;
    }

    private JPanel criarTeclado() {
        JPanel painel = new JPanel(new BorderLayout(0, 15));
        painel.setBackground(TECLADO);
        painel.setPreferredSize(new Dimension(352, 0));
        painel.setBorder(new EmptyBorder(22, 14, 18, 14));

        JPanel numeros = new JPanel(new GridLayout(4, 3, 8, 8));
        numeros.setOpaque(false);
        for (String numero : new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", ""}) {
            if (numero.isEmpty()) {
                JLabel vazio = new JLabel();
                numeros.add(vazio);
            } else {
                JButton tecla = Tema.botao(numero, NUMEROS, Color.WHITE);
                tecla.setFont(new Font("SansSerif", Font.BOLD, 27));
                tecla.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0x4B5055), 1),
                        new EmptyBorder(8, 9, 10, 9)));
                tecla.addActionListener(evento -> digitar(numero.charAt(0)));
                numeros.add(tecla);
            }
        }
        painel.add(numeros, BorderLayout.CENTER);

        JPanel comandos = new JPanel(new GridLayout(1, 3, 7, 0));
        comandos.setOpaque(false);
        JButton branco = teclaComando("BRANCO", Color.WHITE, Tema.TEXTO);
        JButton corrige = teclaComando("CORRIGE", CORRIGE, Tema.TEXTO);
        JButton confirma = teclaComando("CONFIRMA", CONFIRMA, Color.WHITE);
        branco.addActionListener(evento -> { controller.votarBranco(); atualizar(); });
        corrige.addActionListener(evento -> { controller.corrigir(); atualizar(); });
        confirma.addActionListener(evento -> confirmar());
        comandos.add(branco);
        comandos.add(corrige);
        comandos.add(confirma);
        comandos.setPreferredSize(new Dimension(0, 59));
        painel.add(comandos, BorderLayout.SOUTH);
        return painel;
    }

    private JButton teclaComando(String texto, Color fundo, Color cor) {
        JButton tecla = Tema.botao(texto, fundo, cor);
        tecla.setFont(new Font("SansSerif", Font.BOLD, 13));
        tecla.setBorder(BorderFactory.createLineBorder(new Color(0x151B20), 1));
        return tecla;
    }

    private void digitar(char digito) {
        controller.digitar(digito);
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
        for (int i = 0; i < caixasNumero.length; i++) {
            caixasNumero[i].setVisible(i < cargo.getQuantidadeDigitos());
            caixasNumero[i].setText(i < digitado.length()
                    ? Character.toString(digitado.charAt(i)) : " ");
        }

        nome.setText("");
        partido.setText("");
        vice.setText("");
        fotoMoldura.setVisible(false);

        if (controller.isBrancoSelecionado()) {
            situacao.setText("VOTO EM BRANCO");
            return;
        }
        if (digitado.isEmpty()) {
            situacao.setText("Digite o número do candidato.");
            return;
        }

        var ficha = CatalogoFicticio.buscarFicha(cargo, digitado);
        if (ficha.isPresent()) {
            Candidato candidato = ficha.orElseThrow().candidato();
            nome.setText(candidato.getNome());
            partido.setText(candidato.getPartido().getNome());
            if (candidato instanceof CandidatoMajoritario majoritario) {
                vice.setText("Vice: " + majoritario.getNomeVice());
            }
            foto.setIcon(FotoCandidato.icone(candidato, 126, 156));
            fotoMoldura.setVisible(true);
            situacao.setText("Confira os dados e pressione CONFIRMA.");
        } else if (cargo == Cargo.DEPUTADO_FEDERAL && digitado.length() == 2) {
            var partidoEncontrado = CatalogoFicticio.partidos().stream()
                    .filter(p -> p.getNumero().equals(digitado)).findFirst();
            if (partidoEncontrado.isPresent()) {
                nome.setText("VOTO DE LEGENDA");
                partido.setText(partidoEncontrado.orElseThrow().getNome());
                situacao.setText("Confirme a legenda ou continue digitando.");
            } else {
                situacao.setText("Continue digitando.");
            }
        } else if (digitado.length() == cargo.getQuantidadeDigitos()) {
            nome.setText("VOTO NULO");
            situacao.setText("Número não cadastrado. CORRIGE ou CONFIRMA.");
        } else {
            situacao.setText("Continue digitando.");
        }
        revalidate();
        repaint();
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
                controller.corrigir();
                atualizar();
            }
        });
        mapa.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "confirmar");
        acoes.put("confirmar", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { confirmar(); }
        });
        mapa.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "branco");
        acoes.put("branco", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                controller.votarBranco();
                atualizar();
            }
        });
    }
}
