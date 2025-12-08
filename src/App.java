import javax.swing.*;
import java.awt.*; // Necessário para classes de layout (BorderLayout, FlowLayout, etc.) e cores (Color)
import java.awt.event.ActionEvent; // Classe de evento para o botão
import java.awt.event.ActionListener; // Interface para tratamento de eventos do botão
import java.text.DecimalFormat; // Para formatar a porcentagem e os minutos
import java.util.Locale; // Para garantir a formatação decimal correta
import javax.swing.border.EmptyBorder; // Para criar margens/espaçamentos internos
import javax.swing.border.TitledBorder; // Para criar bordas com título

public class App extends JFrame {

    // --- Componentes da Interface (Declaração de Membros) ---
    private JTextField clubeInput; // Campo de texto para o nome do clube
    private JButton processarButton; // Botão para iniciar o cálculo
    
    // Rótulos (JLabels) de Saída, que serão atualizados dinamicamente
    private JLabel labelStatus;
    // JLabels para manter a formatação de "título + valor" sem HTML
    private JLabel labelProbabilidadeTitle; // Exibe o texto "Probabilidade..."
    private JLabel labelProbabilidade;      // Exibe o valor da probabilidade
    private JLabel labelMinutoEstimadoTitle; // Exibe o texto "Minuto Estimado:"
    private JLabel labelMinutoEstimado;      // Exibe o valor do minuto

    private EstatisticaDAO dao; // Objeto de acesso a dados (DAO) para o banco SQLite

    public App() {
        // --- Configuração e Estilo Inicial ---
        try {
            // Tenta definir o Look and Feel Nimbus para uma aparência moderna
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Se falhar, usa o padrão do sistema
            System.out.println("Não foi possível carregar o Look and Feel Nimbus.");
        }

        // Configuração da Janela Principal (JFrame)
        super("Placar Bet");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Define o BorderLayout para dividir a janela em Norte (Entrada), Centro (Resultados) e Sul (Botão)
        setLayout(new BorderLayout(15, 15));
        
        setPreferredSize(new Dimension(580, 320)); 
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Inicializa o DAO
        dao = new EstatisticaDAO(); 

        // --- 1. Painel de Entrada (NORTH) ---
        // Usa FlowLayout para centralizar os componentes de entrada horizontalmente
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        
        // Cria uma Borda com Título (TitledBorder) para a área de entrada
        TitledBorder inputBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), // Linha da borda cinza claro
            "Seleção do Clube", // Título
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12),
            Color.BLACK 
        );
        // Adiciona margem externa (EmptyBorder) e a borda com título (CompoundBorder)
        inputPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 0, 5), inputBorder));
        
        JLabel labelClube = new JLabel("Nome do Clube:");
        labelClube.setFont(new Font("SansSerif", Font.BOLD, 14));
        clubeInput = new JTextField(8); // Campo de texto com largura de 8 colunas

        inputPanel.add(labelClube);
        inputPanel.add(clubeInput);
        
        // --- 2. Painel de Resultados (CENTER) ---
        JPanel resultPanel = new JPanel();
        // Usa BoxLayout vertical para empilhar o status e os resultados
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS)); 

        // Cria Borda com Título para a área de resultados
        TitledBorder resultBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Relatório (1º Tempo)", 
            TitledBorder.CENTER, 
            TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 14), 
            Color.DARK_GRAY 
        );
        resultPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 15, 15, 15), resultBorder));
        
        // Inicialização dos JLabels
        labelStatus = new JLabel("Aguardando a entrada do clube...");
        labelStatus.setFont(new Font("SansSerif", Font.BOLD, 16));
        labelStatus.setForeground(new Color(150, 150, 150)); 
        
        labelProbabilidadeTitle = new JLabel(" "); // Título da Probabilidade
        labelProbabilidade = new JLabel(" "); // Valor da Probabilidade
        labelMinutoEstimadoTitle = new JLabel(" "); // Título do Minuto
        labelMinutoEstimado = new JLabel(" "); // Valor do Minuto
        
        // Painel Contêiner para o Status (mantido verticalmente centralizado)
        JPanel statusContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusContainer.add(labelStatus);
        
        // --- NOVO: Contêiner para Probabilidade (Título + Valor lado a lado) ---
        JPanel probContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        probContainer.add(labelProbabilidadeTitle);
        probContainer.add(labelProbabilidade); 
        
        // --- NOVO: Contêiner para Minuto Estimado (Título + Valor lado a lado) ---
        JPanel minutoContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        minutoContainer.add(labelMinutoEstimadoTitle);
        minutoContainer.add(labelMinutoEstimado);
        
        // Empilhando os componentes no BoxLayout (CENTRO)
        resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        resultPanel.add(statusContainer);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        resultPanel.add(probContainer);   // Título e Valor LADO A LADO
        resultPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        resultPanel.add(minutoContainer); // Título e Valor LADO A LADO
        resultPanel.add(Box.createVerticalGlue()); // Para empurrar os componentes para cima no painel central

        // --- 3. Painel do Botão (SOUTH) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(new EmptyBorder(0, 0, 10, 0)); 

        processarButton = new JButton("Processar Estatísticas");
        processarButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        processarButton.setBackground(new Color(230, 230, 230)); 
        processarButton.setForeground(Color.BLACK); 
        // Associa o botão ao Listener (onde está a lógica de processamento)
        processarButton.addActionListener(new ProcessarEstatisticasListener());
        
        buttonPanel.add(processarButton);

        // --- Adicionar Painéis ao JFrame (Janela Principal) ---
        add(inputPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Redimensiona a janela para se ajustar ao tamanho preferencial dos componentes
        pack(); 
        setVisible(true);
    }

    /**
     * Classe interna responsável por ouvir o clique do botão e executar a lógica.
     */
    private class ProcessarEstatisticasListener implements ActionListener {
        
        // Formatadores de números usados para exibir os resultados com formato de porcentagem e decimal
        private final DecimalFormat percentFormat = new DecimalFormat("0.0%", new java.text.DecimalFormatSymbols(Locale.US));
        private final DecimalFormat minutoFormat = new DecimalFormat("0.0", new java.text.DecimalFormatSymbols(Locale.US));

        @Override
        public void actionPerformed(ActionEvent e) {
            String nomeClube = clubeInput.getText().trim();

            // 1. Limpa e Define o Status Inicial
            labelStatus.setText("Processando...");
            
            // Limpa os rótulos de resultado
            labelProbabilidadeTitle.setText(" ");
            labelProbabilidade.setText(" ");
            labelMinutoEstimadoTitle.setText(" ");
            labelMinutoEstimado.setText(" ");
            
            // --- 1. Validação da Entrada ---
            if (nomeClube.isEmpty() || (!nomeClube.equalsIgnoreCase("Sport") && !nomeClube.equalsIgnoreCase("Gremio"))) {
                labelStatus.setText("Entrada Inválida!");
                
                
                labelProbabilidade.setText("Por favor, digite 'Sport' ou 'Gremio'.");
                labelProbabilidade.setFont(new Font("SansSerif", Font.PLAIN, 12));
                
                return; // Encerra a execução após o erro
            }

            // --- 2. Cálculo das Estatísticas (Acesso ao DAO) ---
            Estatistica resultado = dao.calcularEstatisticas(nomeClube);

            if (resultado == null) {
                labelStatus.setText("Erro de Banco de Dados!");
                labelStatus.setForeground(Color.RED);
                
                labelProbabilidade.setText("Falha ao acessar ou calcular os dados. Verifique o banco.");
                labelProbabilidade.setFont(new Font("SansSerif", Font.PLAIN, 12));
                labelProbabilidade.setForeground(Color.BLACK);
                return; // Encerra a execução após o erro
            }

            // --- 3. Exibição dos Resultados (Puro Swing) ---
            double probabilidade = resultado.getProbabilidadeGol();
            
            // Status de Sucesso (Cor agora é PRETO)
            labelStatus.setText(String.format("✅ Análise Concluída para %s", nomeClube));
            labelStatus.setForeground(Color.BLACK); 

            // A) Probabilidade de Marcar Gol (Destaque Principal)
            
            // Título (Pequeno/Cinza)
            labelProbabilidadeTitle.setText("Probabilidade de Gol no 1º Tempo: "); // Espaço no final para separação visual
            labelProbabilidadeTitle.setFont(new Font("SansSerif", Font.PLAIN, 15));

            // Valor (Maior/Negrito/Preto)
            labelProbabilidade.setText(percentFormat.format(probabilidade));
            labelProbabilidade.setFont(new Font("SansSerif", Font.BOLD, 18)); 
            

            // B) Minuto Estimado (Informação Secundária)
            if (probabilidade > 0) {
                double minutoEstimado = resultado.getMinutoEstimado();
                
                // Título (Pequeno/Cinza)
                labelMinutoEstimadoTitle.setText("Minuto Estimado: "); // Espaço no final para separação visual
                labelMinutoEstimadoTitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
            

                // Valor (Médio/Negrito/Preto)
                labelMinutoEstimado.setText(minutoFormat.format(minutoEstimado) + " minutos");
                labelMinutoEstimado.setFont(new Font("SansSerif", Font.BOLD, 18)); 
                

            } else {
                // Mensagem para caso não haja gols registrados
                labelMinutoEstimadoTitle.setText(" ");
                labelMinutoEstimado.setText("Nenhuma média de minuto calculada.");
                labelMinutoEstimado.setFont(new Font("SansSerif", Font.ITALIC, 12)); 
                
            }
        }
    }

    public static void main(String[] args) {
        // Padrão Swing: garante que a criação da interface gráfica ocorra na Thread de Eventos do Swing (EDT), mantendo a segurança.
        SwingUtilities.invokeLater(() -> new App());
    }
}