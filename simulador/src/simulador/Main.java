package simulador;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Arrays;
import java.util.List;

public class Main extends JFrame {

    // ── Paleta ──────────────────────────────────────────────────────────────
    private static final Color BG         = new Color(18, 18, 28);
    private static final Color PANEL_BG   = new Color(28, 28, 42);
    private static final Color CARD_BG    = new Color(36, 36, 54);
    private static final Color ACCENT     = new Color(99, 102, 241);  // indigo
    private static final Color SUCCESS    = new Color(52, 211, 153);  // green
    private static final Color WARNING    = new Color(251, 191, 36);  // yellow
    private static final Color DANGER     = new Color(248, 113, 113); // red
    private static final Color TEXT_MAIN  = new Color(241, 245, 249);
    private static final Color TEXT_SUB   = new Color(148, 163, 184);
    private static final Color BORDER_COL = new Color(55, 65, 81);

    private static final Color[] ALG_COLORS = {
        new Color(248, 113, 113),  // FIFO   — vermelho
        new Color(96,  165, 250),  // LRU    — azul
        new Color(52,  211, 153),  // Clock  — verde
        new Color(251, 191, 36),   // Ótimo  — amarelo
    };

    // ── Componentes ─────────────────────────────────────────────────────────
    private JTextField campoReferencias;
    private JSpinner   spinnerQuadros;
    private JPanel     painelResultados;
    private GraficoBarras grafico;
    private JLabel[]   lblFaltas;
    private JPanel     painelTabelaContainer;

    public Main() {
        super("Simulador de Substituição de Páginas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 780);
        setMinimumSize(new Dimension(900, 680));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(0, 0));

        add(criarHeader(), BorderLayout.NORTH);
        add(criarPainelCentral(), BorderLayout.CENTER);

        setVisible(true);
    }

    // ── Header ───────────────────────────────────────────────────────────────
    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PANEL_BG);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COL),
            new EmptyBorder(16, 24, 16, 24)
        ));

        JLabel titulo = new JLabel("Simulador de Substituição de Páginas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(TEXT_MAIN);

        JLabel subtitulo = new JLabel("Sistemas Operacionais · UNIFOR");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(TEXT_SUB);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);
        header.add(textos, BorderLayout.WEST);
        return header;
    }

    // ── Painel central ───────────────────────────────────────────────────────
    private JPanel criarPainelCentral() {
        JPanel centro = new JPanel(new BorderLayout(0, 0));
        centro.setBackground(BG);

        // Painel de entrada (topo)
        centro.add(criarPainelEntrada(), BorderLayout.NORTH);

        // Painel de resultados (centro)
        painelResultados = new JPanel(new BorderLayout(0, 16));
        painelResultados.setBackground(BG);
        painelResultados.setBorder(new EmptyBorder(0, 20, 20, 20));
        painelResultados.setVisible(false);

        // Cards de faltas
        JPanel cards = new JPanel(new GridLayout(1, 4, 12, 0));
        cards.setOpaque(false);
        lblFaltas = new JLabel[4];
        String[] nomes = {"FIFO", "LRU", "Clock", "Ótimo"};
        for (int i = 0; i < 4; i++) {
            cards.add(criarCard(nomes[i], i));
        }
        painelResultados.add(cards, BorderLayout.NORTH);

        // Gráfico
        grafico = new GraficoBarras();
        grafico.setPreferredSize(new Dimension(0, 280));
        painelResultados.add(grafico, BorderLayout.CENTER);

        // Tabela de passos
        painelTabelaContainer = new JPanel(new BorderLayout());
        painelTabelaContainer.setOpaque(false);
        painelResultados.add(painelTabelaContainer, BorderLayout.SOUTH);

        centro.add(painelResultados, BorderLayout.CENTER);
        return centro;
    }

    // ── Painel de entrada ────────────────────────────────────────────────────
    private JPanel criarPainelEntrada() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 16));
        painel.setBackground(PANEL_BG);
        painel.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COL),
            new EmptyBorder(4, 8, 4, 8)
        ));

        // Label + campo de referências
        painel.add(label("Cadeia de páginas:"));
        campoReferencias = new JTextField("7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1", 30);
        estilizarCampo(campoReferencias);
        painel.add(campoReferencias);

        // Quadros
        painel.add(label("Quadros:"));
        spinnerQuadros = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        spinnerQuadros.setPreferredSize(new Dimension(60, 34));
        estilizarSpinner(spinnerQuadros);
        painel.add(spinnerQuadros);

        // Botão simular
        JButton btnSimular = new JButton("▶  Simular");
        btnSimular.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSimular.setBackground(ACCENT);
        btnSimular.setForeground(Color.WHITE);
        btnSimular.setBorder(new EmptyBorder(8, 20, 8, 20));
        btnSimular.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSimular.setFocusPainted(false);
        btnSimular.addActionListener(e -> simular());
        painel.add(btnSimular);

        return painel;
    }

    // ── Card de resultado ────────────────────────────────────────────────────
    private JPanel criarCard(String nome, int idx) {
        JPanel card = new JPanel(new GridLayout(3, 1, 0, 4));
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
            new LineBorder(ALG_COLORS[idx].darker(), 1),
            new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel lblNome = new JLabel(nome);
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNome.setForeground(ALG_COLORS[idx]);

        lblFaltas[idx] = new JLabel("—");
        lblFaltas[idx].setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblFaltas[idx].setForeground(TEXT_MAIN);

        JLabel lblDesc = new JLabel("faltas de página");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDesc.setForeground(TEXT_SUB);

        card.add(lblNome);
        card.add(lblFaltas[idx]);
        card.add(lblDesc);
        return card;
    }

    // ── Lógica de simulação ──────────────────────────────────────────────────
    private void simular() {
        String texto = campoReferencias.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a cadeia de páginas.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[] refs;
        try {
            refs = Arrays.stream(texto.split("[\\s,;]+"))
                         .mapToInt(Integer::parseInt)
                         .toArray();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Apenas números inteiros separados por espaço.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quadros = (int) spinnerQuadros.getValue();

        ResultadoAlgoritmo[] resultados = {
            FIFO.executar(refs, quadros),
            LRU.executar(refs, quadros),
            Clock.executar(refs, quadros),
            Otimo.executar(refs, quadros),
        };

        // Atualiza cards
        int[] faltas = new int[4];
        for (int i = 0; i < 4; i++) {
            faltas[i] = resultados[i].getFaltasDePagina();
            lblFaltas[i].setText(String.valueOf(faltas[i]));
        }

        // Atualiza gráfico
        String[] nomes = {"FIFO", "LRU", "Clock", "Ótimo"};
        grafico.atualizar(nomes, faltas, ALG_COLORS);

        // Atualiza tabela de passos
        painelTabelaContainer.removeAll();
        painelTabelaContainer.add(criarTabelaPassos(refs, resultados, quadros), BorderLayout.CENTER);

        // Imprime no terminal também
        System.out.println("\n══════════ RESULTADOS ══════════");
        for (ResultadoAlgoritmo r : resultados) {
            System.out.println("  Método " + r.getNomeAlgoritmo() + " - " + r.getFaltasDePagina() + " faltas de página");
        }
        System.out.println("════════════════════════════════\n");

        painelResultados.setVisible(true);
        painelResultados.revalidate();
        painelResultados.repaint();
    }

    // ── Tabela de passos ─────────────────────────────────────────────────────
    private JScrollPane criarTabelaPassos(int[] refs, ResultadoAlgoritmo[] resultados, int quadros) {
        // Colunas: Passo | Página | FIFO (q1..qN) | LRU | Clock | Ótimo
        int qtdAlg = resultados.length;

        // Monta cabeçalho dinâmico
        String[] colunas = new String[2 + qtdAlg * (quadros + 1)];
        colunas[0] = "Passo";
        colunas[1] = "Página";
        int col = 2;
        for (ResultadoAlgoritmo r : resultados) {
            for (int q = 1; q <= quadros; q++) {
                colunas[col++] = r.getNomeAlgoritmo() + " Q" + q;
            }
            colunas[col++] = r.getNomeAlgoritmo() + " ✗";
        }

        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        boolean[] faltaAcumulada = new boolean[qtdAlg];

        for (int i = 0; i < refs.length; i++) {
            Object[] linha = new Object[colunas.length];
            linha[0] = i + 1;
            linha[1] = refs[i];
            int c = 2;

            for (int a = 0; a < qtdAlg; a++) {
                int[] snap = resultados[a].getSnapshots().get(i);
                // detecta falta comparando com snapshot anterior
                boolean falta = (i == 0) || !contains(resultados[a].getSnapshots().get(i - 1), refs[i]);

                for (int q = 0; q < quadros; q++) {
                    linha[c++] = (q < snap.length) ? snap[q] : "";
                }
                linha[c++] = falta ? "✗" : "";
            }

            model.addRow(linha);
        }

        JTable tabela = new JTable(model);
        tabela.setBackground(CARD_BG);
        tabela.setForeground(TEXT_MAIN);
        tabela.setFont(new Font("Consolas", Font.PLAIN, 12));
        tabela.setRowHeight(22);
        tabela.getTableHeader().setBackground(PANEL_BG);
        tabela.getTableHeader().setForeground(TEXT_SUB);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tabela.setGridColor(BORDER_COL);
        tabela.setSelectionBackground(ACCENT.darker());

        // Renderer para colorir faltas de página
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setBackground(sel ? ACCENT.darker() : (row % 2 == 0 ? CARD_BG : PANEL_BG));
                setForeground(TEXT_MAIN);
                setHorizontalAlignment(CENTER);
                setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
                if ("✗".equals(val)) {
                    setForeground(DANGER);
                    setFont(getFont().deriveFont(Font.BOLD));
                }
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(0, 180));
        scroll.setBorder(new LineBorder(BORDER_COL));
        scroll.getViewport().setBackground(CARD_BG);
        return scroll;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private boolean contains(int[] arr, int val) {
        for (int v : arr) if (v == val) return true;
        return false;
    }

    private JLabel label(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(TEXT_MAIN);
        return l;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setBackground(CARD_BG);
        campo.setForeground(TEXT_MAIN);
        campo.setCaretColor(TEXT_MAIN);
        campo.setFont(new Font("Consolas", Font.PLAIN, 13));
        campo.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COL),
            new EmptyBorder(6, 10, 6, 10)
        ));
    }

    private void estilizarSpinner(JSpinner spinner) {
        spinner.setBackground(CARD_BG);
        spinner.setForeground(TEXT_MAIN);
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(CARD_BG);
            tf.setForeground(TEXT_MAIN);
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }
    }

    // ── Componente de gráfico de barras ──────────────────────────────────────
    static class GraficoBarras extends JPanel {

        private String[] nomes;
        private int[]    valores;
        private Color[]  cores;

        GraficoBarras() {
            setBackground(CARD_BG);
            setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL),
                new EmptyBorder(20, 24, 20, 24)
            ));
            setToolTipText(""); // habilita tooltips
        }

        void atualizar(String[] nomes, int[] valores, Color[] cores) {
            this.nomes   = nomes;
            this.valores = valores;
            this.cores   = cores;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (nomes == null) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int W = getWidth() - 48;
            int H = getHeight() - 60;
            int ox = 24, oy = 20;

            int maxVal = Arrays.stream(valores).max().getAsInt();
            maxVal = Math.max(maxVal, 1);

            int n = nomes.length;
            int barW = W / (n * 2);
            int gap  = barW;

            // Grid lines
            g2.setColor(new Color(55, 65, 81, 120));
            g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
                    0, new float[]{4, 4}, 0));
            for (int i = 1; i <= 4; i++) {
                int y = oy + H - (H * i / 4);
                g2.drawLine(ox, y, ox + W, y);
                g2.setColor(TEXT_SUB);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.drawString(String.valueOf(maxVal * i / 4), 2, y + 4);
                g2.setColor(new Color(55, 65, 81, 120));
            }

            // Eixo X
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(BORDER_COL);
            g2.drawLine(ox, oy + H, ox + W, oy + H);

            // Barras
            for (int i = 0; i < n; i++) {
                int bh = (int) ((double) valores[i] / maxVal * H);
                int bx = ox + gap / 2 + i * (barW + gap);
                int by = oy + H - bh;

                // Gradiente na barra
                GradientPaint gp = new GradientPaint(
                    bx, by, cores[i],
                    bx, oy + H, cores[i].darker().darker()
                );
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Float(bx, by, barW, bh, 8, 8));

                // Borda superior highlight
                g2.setColor(cores[i].brighter());
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(bx + 4, by + 2, bx + barW - 4, by + 2);

                // Valor no topo da barra
                g2.setColor(TEXT_MAIN);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                String val = String.valueOf(valores[i]);
                g2.drawString(val, bx + (barW - fm.stringWidth(val)) / 2, by - 6);

                // Nome abaixo
                g2.setColor(cores[i]);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                fm = g2.getFontMetrics();
                g2.drawString(nomes[i], bx + (barW - fm.stringWidth(nomes[i])) / 2,
                        oy + H + 16);
            }

            // Título
            g2.setColor(TEXT_SUB);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.drawString("Comparativo de Faltas de Página", ox, oy - 6);
        }
    }

    // ── Entry point ──────────────────────────────────────────────────────────
    public static void main(String[] args) {
        // Também executa no terminal (modo headless / sem GUI)
        if (args.length > 0 && args[0].equals("--terminal")) {
            executarTerminal();
            return;
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(Main::new);
    }

    private static void executarTerminal() {
        int[] referencias = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1};
        int   quadros     = 3;

        System.out.println("Cadeia: " + Arrays.toString(referencias));
        System.out.println("Quadros: " + quadros);
        System.out.println("─────────────────────────────────");

        ResultadoAlgoritmo[] resultados = {
            FIFO.executar(referencias, quadros),
            LRU.executar(referencias, quadros),
            Clock.executar(referencias, quadros),
            Otimo.executar(referencias, quadros),
        };

        for (int i = 0; i < resultados.length; i++) {
            System.out.printf("  Método %d - %d faltas de página  (%s)%n",
                i + 1, resultados[i].getFaltasDePagina(), resultados[i].getNomeAlgoritmo());
        }
    }
}
