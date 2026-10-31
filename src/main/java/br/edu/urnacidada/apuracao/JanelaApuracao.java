package br.edu.urnacidada.apuracao;

import br.edu.urnacidada.config.FirebaseConfig;
import br.edu.urnacidada.model.Cargo;
import br.edu.urnacidada.repository.FirestoreVotoRepository;
import br.edu.urnacidada.view.Tema;
import com.google.cloud.firestore.Firestore;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 * Aplicação administrativa independente da urna: somente leitura do Firestore.
 * Consultas em segundo plano e números formatados para facilitar a conferência.
 */
public final class JanelaApuracao extends JFrame {
    private static final String SIMULACAO_ID = "demonstracao-interface-2026";
    private static final DateTimeFormatter RELOGIO = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final Locale PORTUGUES = Locale.forLanguageTag("pt-BR");
    private static final Color BORDA = new Color(0xDEE6ED);
    private static final Color LINHA_ALTERNADA = new Color(0xF5F8FB);
    private static final Color FUNDO_CARTAO = new Color(0xF8FAFC);
    private static final Color FUNDO_CABECALHO = new Color(0xEAF0F5);

    private record Indicadores(JLabel total, JLabel brancos, JLabel nulos) {}

    private final JButton atualizar = Tema.botao("ATUALIZAR RESULTADOS", Tema.AZUL, Tema.BRANCO);
    private final JLabel estado = Tema.texto("Nenhuma consulta realizada.", 14, false);
    private final Map<Cargo, DefaultTableModel> modelos = new EnumMap<>(Cargo.class);
    private final Map<Cargo, Indicadores> indicadores = new EnumMap<>(Cargo.class);

    public JanelaApuracao() {
        super("Urna Cidadã — Painel de Apuração");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 740);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);

        JPanel raiz = new JPanel(new BorderLayout(18, 20));
        raiz.setBorder(new EmptyBorder(24, 30, 22, 30));
        raiz.setBackground(Tema.FUNDO);

        JPanel cabecalho = new JPanel(new BorderLayout(24, 12));
        cabecalho.setOpaque(false);
        JPanel titulos = new JPanel(new BorderLayout(0, 5));
        titulos.setOpaque(false);
        JLabel titulo = Tema.texto("PAINEL DE APURAÇÃO", 30, true);
        JLabel legenda = Tema.texto("Simulação educativa  •  Acesso administrativo  •  Somente leitura", 15, false);
        legenda.setForeground(Tema.CINZA);
        titulos.add(titulo, BorderLayout.NORTH);
        titulos.add(legenda, BorderLayout.SOUTH);
        cabecalho.add(titulos, BorderLayout.CENTER);
        cabecalho.add(atualizar, BorderLayout.EAST);
        raiz.add(cabecalho, BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("SansSerif", Font.BOLD, 16));
        for (Cargo cargo : Cargo.values()) {
            abas.addTab(cargo.getNome(), criarAba(cargo));
        }
        raiz.add(abas, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rodape.setOpaque(false);
        estado.setForeground(Tema.CINZA);
        rodape.add(estado);
        raiz.add(rodape, BorderLayout.SOUTH);

        setContentPane(raiz);
        atualizar.addActionListener(evento -> carregar());
        carregar();
    }

    private JPanel criarAba(Cargo cargo) {
        JPanel painel = new JPanel(new BorderLayout(0, 20));
        painel.setBackground(Tema.BRANCO);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                new EmptyBorder(18, 20, 18, 20)));

        JLabel valorTotal = Tema.texto("—", 27, true);
        JLabel valorBrancos = Tema.texto("—", 27, true);
        JLabel valorNulos = Tema.texto("—", 27, true);
        indicadores.put(cargo, new Indicadores(valorTotal, valorBrancos, valorNulos));

        JPanel resumo = new JPanel(new GridLayout(1, 3, 14, 0));
        resumo.setOpaque(false);
        resumo.add(criarCartao("TOTAL DE VOTOS", valorTotal));
        resumo.add(criarCartao("VOTOS EM BRANCO", valorBrancos));
        resumo.add(criarCartao("VOTOS NULOS", valorNulos));
        painel.add(resumo, BorderLayout.NORTH);

        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Opção", "Tipo", "Número", "Votos", "Percentual"}, 0) {
            @Override public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
            @Override public Class<?> getColumnClass(int coluna) {
                return switch (coluna) {
                    case 3 -> Long.class;
                    case 4 -> Double.class;
                    default -> String.class;
                };
            }
        };
        modelos.put(cargo, modelo);

        JTable tabela = new JTable(modelo);
        configurarTabela(tabela);
        JScrollPane rolagem = new JScrollPane(tabela);
        rolagem.setBorder(BorderFactory.createLineBorder(BORDA));
        rolagem.getViewport().setBackground(Tema.BRANCO);
        painel.add(rolagem, BorderLayout.CENTER);

        JLabel explicacao = Tema.texto(
                "Percentuais sobre todos os votos do cargo, incluindo brancos, nulos e legenda.",
                13, false);
        explicacao.setForeground(Tema.CINZA);
        painel.add(explicacao, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarCartao(String rotulo, JLabel valor) {
        JPanel cartao = new JPanel(new BorderLayout(0, 8));
        cartao.setBackground(FUNDO_CARTAO);
        cartao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                new EmptyBorder(16, 20, 15, 20)));

        JLabel titulo = Tema.texto(rotulo, 12, true);
        titulo.setForeground(Tema.CINZA);
        valor.setForeground(Tema.AZUL);
        cartao.add(titulo, BorderLayout.NORTH);
        cartao.add(valor, BorderLayout.CENTER);
        return cartao;
    }

    private void configurarTabela(JTable tabela) {
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 15));
        tabela.setRowHeight(46);
        tabela.setAutoCreateRowSorter(true);
        tabela.setFillsViewportHeight(true);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabela.setShowVerticalLines(false);
        tabela.setShowHorizontalLines(true);
        tabela.setGridColor(BORDA);
        tabela.setIntercellSpacing(new Dimension(1, 1));
        tabela.setSelectionBackground(new Color(0xDCEAF3));
        tabela.setSelectionForeground(Tema.TEXTO);
        tabela.setRowSelectionAllowed(true);
        tabela.setColumnSelectionAllowed(false);

        int[] larguras = {390, 120, 110, 110, 140};
        int[] minimos = {270, 100, 85, 95, 120};
        for (int coluna = 0; coluna < larguras.length; coluna++) {
            tabela.getColumnModel().getColumn(coluna).setPreferredWidth(larguras[coluna]);
            tabela.getColumnModel().getColumn(coluna).setMinWidth(minimos[coluna]);
            tabela.getColumnModel().getColumn(coluna).setCellRenderer(new CelulaApuracao(coluna));
        }

        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setReorderingAllowed(false);
        cabecalho.setPreferredSize(new Dimension(0, 46));
        TableCellRenderer renderizadorOriginal = cabecalho.getDefaultRenderer();
        cabecalho.setDefaultRenderer((t, valor, selecionado, foco, linha, coluna) -> {
            Component componente = renderizadorOriginal.getTableCellRendererComponent(
                    t, valor, selecionado, foco, linha, coluna);
            if (componente instanceof JLabel rotulo) {
                rotulo.setOpaque(true);
                rotulo.setFont(new Font("SansSerif", Font.BOLD, 14));
                rotulo.setBackground(FUNDO_CABECALHO);
                rotulo.setForeground(Tema.TEXTO);
                rotulo.setBorder(new EmptyBorder(0, 14, 0, 14));
                rotulo.setHorizontalAlignment(coluna >= 3 ? SwingConstants.RIGHT
                        : coluna == 0 ? SwingConstants.LEFT : SwingConstants.CENTER);
            }
            return componente;
        });
    }

    private static final class CelulaApuracao extends DefaultTableCellRenderer {
        private final int coluna;

        private CelulaApuracao(int coluna) {
            this.coluna = coluna;
            setBorder(new EmptyBorder(0, 14, 0, 14));
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable tabela, Object valor, boolean selecionado,
                boolean foco, int linha, int colunaVisivel) {
            super.getTableCellRendererComponent(tabela, valor, selecionado,
                    foco, linha, colunaVisivel);

            setHorizontalAlignment(switch (coluna) {
                case 0 -> SwingConstants.LEFT;
                case 3, 4 -> SwingConstants.RIGHT;
                default -> SwingConstants.CENTER;
            });
            setBorder(new EmptyBorder(0, 14, 0, 14));
            if (!selecionado) {
                setBackground(linha % 2 == 0 ? Tema.BRANCO : LINHA_ALTERNADA);
                setForeground(Tema.TEXTO);
            }
            if (valor instanceof Number numero) {
                if (coluna == 3) {
                    setText(formatarInteiro(numero.longValue()));
                } else if (coluna == 4) {
                    setText(String.format(PORTUGUES, "%.1f%%", numero.doubleValue()));
                }
            }
            return this;
        }
    }

    private static String formatarInteiro(long numero) {
        return NumberFormat.getIntegerInstance(PORTUGUES).format(numero);
    }

    private void carregar() {
        atualizar.setEnabled(false);
        estado.setText("Consultando os resultados do Cloud Firestore...");

        new SwingWorker<Map<Cargo, ResumoApuracao.PorCargo>, Void>() {
            @Override
            protected Map<Cargo, ResumoApuracao.PorCargo> doInBackground() throws Exception {
                try (Firestore banco = FirebaseConfig.criarCliente()) {
                    Map<String, Long> contagens =
                            new FirestoreVotoRepository(banco, SIMULACAO_ID)
                                    .consultarResultados();
                    return ResumoApuracao.analisar(contagens);
                }
            }

            @Override
            protected void done() {
                atualizar.setEnabled(true);
                try {
                    Map<Cargo, ResumoApuracao.PorCargo> resultados = get();
                    for (Cargo cargo : Cargo.values()) {
                        ResumoApuracao.PorCargo dados = resultados.get(cargo);
                        DefaultTableModel modelo = modelos.get(cargo);
                        modelo.setRowCount(0);
                        for (ResumoApuracao.Linha linha : dados.linhas()) {
                            // Números permanecem numéricos: ordenação correta na tabela.
                            modelo.addRow(new Object[]{
                                linha.opcao(), linha.tipo(), linha.numero(),
                                linha.votos(), linha.percentual()
                            });
                        }
                        Indicadores cartoes = indicadores.get(cargo);
                        cartoes.total().setText(formatarInteiro(dados.total()));
                        cartoes.brancos().setText(formatarInteiro(dados.brancos()));
                        cartoes.nulos().setText(formatarInteiro(dados.nulos()));
                    }
                    estado.setText("Resultados atualizados às " + LocalTime.now().format(RELOGIO)
                            + "  •  Simulação: " + SIMULACAO_ID);
                    estado.setForeground(Tema.CINZA);
                } catch (InterruptedException erro) {
                    Thread.currentThread().interrupt();
                    mostrarErro();
                } catch (ExecutionException erro) {
                    System.err.println("Falha na consulta do painel: " + erro.getCause());
                    mostrarErro();
                }
            }
        }.execute();
    }

    private void mostrarErro() {
        estado.setText("Falha ao consultar o Firestore. Verifique a conexão e tente atualizar.");
        estado.setForeground(new Color(0xB3261E));
    }
}
