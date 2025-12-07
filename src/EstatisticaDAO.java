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