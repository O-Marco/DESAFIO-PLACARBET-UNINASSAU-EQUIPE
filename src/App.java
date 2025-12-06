PASTA APP.JAVA
Foi dividido em 3 partes, a sua é a 1

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
        labelMinutoEstimado = new JLabel(" "); // Valor do Minuto