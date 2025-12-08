// EstatisticaDAO.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EstatisticaDAO {

    // URL de conexão com o seu placarbet.db
    private static final String DB_URL = "jdbc:sqlite:placarbet.db";
    // Total de jogos a serem analisados (últimos 10 jogos)
    private static final int TOTAL_JOGOS = 10; 

    /**
     * Busca os dados e calcula a probabilidade e o minuto estimado para o clube.
     * @param clube Nome do clube (ex: "Grêmio" ou "Sport").
     * @return Objeto Estatistica com os resultados ou null se houver erro.
     */
    public Estatistica calcularEstatisticas(String clube) {
        // Consulta 1: Contar jogos com gol no 1º tempo para a probabilidade
        String sqlProbabilidade = 
            "SELECT COUNT(id) FROM jogos WHERE clube = '" + clube + 
            "' AND gols_marcados_45 > 0";
        
        // Consulta 2: Somar os minutos dos gols no 1º tempo para a média
        String sqlMinutos = 
            "SELECT minuto_gol FROM jogos WHERE clube = '" + clube + 
            "' AND minuto_gol IS NOT NULL AND minuto_gol > 0"; // minuto_gol é a chave para os gols no 1º tempo
        
        int jogosComGol = 0;
        List<Integer> minutosGols = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // --- Execução da Probabilidade ---
            ResultSet rsProb = stmt.executeQuery(sqlProbabilidade);
            if (rsProb.next()) {
                jogosComGol = rsProb.getInt(1);
            }
            rsProb.close();

            // --- Execução da Média dos Minutos ---
            ResultSet rsMin = stmt.executeQuery(sqlMinutos);
            while (rsMin.next()) {
                minutosGols.add(rsMin.getInt("minuto_gol"));
            }
            rsMin.close();

        } catch (Exception e) {
            System.err.println("Erro de acesso ao banco de dados: " + e.getMessage());
            return null; // Retorna nulo em caso de falha
        }

        // --- CÁLCULOS ESTATÍSTICOS ---
        
        // 1. Probabilidade de Marcar Gol [cite: 155]
        double probabilidadeGol = (double) jogosComGol / TOTAL_JOGOS; 
        
        // 2. Minuto Estimado (Média) [cite: 156]
        double minutoEstimado = 0.0;
        int totalGols = minutosGols.size();

        // Só calcula a média se houver gols (probabilidade > 0)
        if (totalGols > 0) {
            int somaMinutos = 0;
            for (int minuto : minutosGols) {
                somaMinutos += minuto;
            }
            minutoEstimado = (double) somaMinutos / totalGols;
        }

        // Retorna o objeto com os resultados
        return new Estatistica(probabilidadeGol, minutoEstimado);
    }
}