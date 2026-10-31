package br.edu.urnacidada.apuracao;

import br.edu.urnacidada.config.FirebaseConfig;
import br.edu.urnacidada.model.Cargo;
import br.edu.urnacidada.repository.FirestoreVotoRepository;
import br.edu.urnacidada.view.Tema;
import com.google.cloud.firestore.Firestore;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Painel de apuração independente da urna. Faz SOMENTE LEITURA no Firestore.
 * O botão atualizar consulta a nuvem por SwingWorker, sem congelar a interface.
 */
public final class JanelaApuracao extends JFrame {
    private static final String SIMULACAO_ID = "demonstracao-interface-2026";
    private static final DateTimeFormatter RELOGIO = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final JButton atualizar = Tema.botao("ATUALIZAR RESULTADOS", Tema.AZUL, Tema.BRANCO);
    private final JLabel estado = Tema.texto("Nenhuma consulta realizada.", 14, false);
    private final Map<Cargo, DefaultTableModel> modelos = new EnumMap<>(Cargo.class);
    private final Map<Cargo, JLabel> totais = new EnumMap<>(Cargo.class);

    public JanelaApuracao() {
        super("Urna Cidadã — Painel de Apuração");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1060, 670);
        setMinimumSize(new Dimension(850, 550));
        setLocationRelativeTo(null);

        JPanel raiz = new JPanel(new BorderLayout(18, 18));
        raiz.setBorder(new EmptyBorder(22, 28, 22, 28));
        raiz.setBackground(Tema.FUNDO);

        JPanel cabecalho = new JPanel(new BorderLayout(10, 12));
        cabecalho.setOpaque(false);
        JPanel titulos = new JPanel(new BorderLayout(0, 5));
        titulos.setOpaque(false);
        JLabel titulo = Tema.texto("PAINEL DE APURAÇÃO", 29, true);
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
        JPanel painel = new JPanel(new BorderLayout(0, 16));
        painel.setBackground(Tema.BRANCO);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(0xDDE3E7)),
                new EmptyBorder(18, 18, 18, 18)));

        JLabel resumo = Tema.texto("Total: —   |   Brancos: —   |   Nulos: —", 17, true);
        totais.put(cargo, resumo);
        painel.add(resumo, BorderLayout.NORTH);

        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Opção", "Tipo", "Número", "Votos", "Percentual"}, 0) {
            @Override public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
            @Override public Class<?> getColumnClass(int coluna) {
                return coluna == 3 ? Long.class : String.class;
            }
        };
        modelos.put(cargo, modelo);

        JTable tabela = new JTable(modelo);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 15));
        tabela.setRowHeight(38);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tabela.setAutoCreateRowSorter(true);
        tabela.setFillsViewportHeight(true);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(300);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
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
                            modelo.addRow(new Object[]{
                                    linha.opcao(), linha.tipo(), linha.numero(), linha.votos(),
                                    String.format(Locale.forLanguageTag("pt-BR"), "%.1f%%", linha.percentual())
                            });
                        }
                        totais.get(cargo).setText("Total: " + dados.total()
                                + "   |   Brancos: " + dados.brancos()
                                + "   |   Nulos: " + dados.nulos());
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
        estado.setForeground(new java.awt.Color(0xB3261E));
    }
}
