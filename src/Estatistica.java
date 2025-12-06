/**
 * Classe de modelo (POJO - Plain Old Java Object) utilizada para
 * encapsular e transportar os dois resultados estatísticos
 * esta classe atua como um 'container' de dados que será retornado
 * pelo EstatisticaDAO e consumido pela interface Swing (App.java).
 */
public class Estatistica {
    
    // Variável para armazenar a probabilidade de o clube marcar um gol nos primeiros 45 minutos.
    // O valor é armazenado como um decimal (ex: 0.5 para 50%).
    private double probabilidadeGol; 
    
    // Variável para armazenar a média dos minutos em que os gols no 1º tempo
    // foram marcados, conforme exigido pelo requisito do desafio.
    private double minutoEstimado;  

    /**
     * Construtor da classe Estatistica.
     * * @param probabilidadeGol O valor decimal da probabilidade (0.0 a 1.0).
     * @param minutoEstimado A média dos minutos dos gols (ex: 22.8).
     */
    public Estatistica(double probabilidadeGol, double minutoEstimado) {
        this.probabilidadeGol = probabilidadeGol;
        this.minutoEstimado = minutoEstimado;
    }

    // --- Métodos Getters ---

    /**
     * Retorna a probabilidade de o clube marcar um gol no 1º tempo.
     * * @return O valor da probabilidade (decimal).
     */
    public double getProbabilidadeGol() {
        return probabilidadeGol;
    }

    /**
     * Retorna o minuto estimado para o gol (média dos minutos de gols passados).
     * * @return O valor médio em minutos.
     */
    public double getMinutoEstimado() {
        return minutoEstimado;
    }
}