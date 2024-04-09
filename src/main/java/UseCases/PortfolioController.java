package UseCases;

import java.io.*;
import java.util.*;

public class PortfolioController {
    private final Map<String, Map<String, Integer>> userPortfolios = new HashMap<>();

    public PortfolioController() {
        loadUserPortfolios();
    }

    public Object[][] getPortfolioData(String username) {
        Map<String, Integer> portfolio = userPortfolios.getOrDefault(username, new HashMap<>());
        Object[][] data = new Object[portfolio.size()][2];
        int i = 0;
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            data[i][0] = entry.getKey();
            data[i][1] = entry.getValue();
            i++;
        }
        return data;
    }

    private void loadUserPortfolios() {
        try (BufferedReader reader = new BufferedReader(new FileReader("portfolios.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) { // Stellen Sie sicher, dass jede Zeile mindestens 3 Elemente hat
                    userPortfolios.computeIfAbsent(data[0], k -> new HashMap<>()).put(data[1], Integer.parseInt(data[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buyStock(String username, String stockSymbol, int quantity) {
        // Entfernen des Preises aus dem stockSymbol, wenn vorhanden
        String cleanStockSymbol = stockSymbol.split(" - ")[0]; // Nimmt an, dass der StockSymbol im Format "SYMBOL - PREIS" kommt
        userPortfolios.computeIfAbsent(username, k -> new HashMap<>()).merge(cleanStockSymbol, quantity, Integer::sum);
        saveUserPortfolios();
    }

    public void sellStock(String username, String stockSymbol, int quantity) {
        String cleanStockSymbol = stockSymbol.split(" - ")[0];
        Map<String, Integer> portfolio = userPortfolios.get(username);
        if (portfolio != null) {
            int currentQuantity = portfolio.getOrDefault(cleanStockSymbol, 0);
            if (currentQuantity >= quantity) {
                portfolio.put(cleanStockSymbol, currentQuantity - quantity);
                saveUserPortfolios();
            } else {
                throw new IllegalArgumentException("Verkaufsfehler: Sie können nicht mehr Aktien verkaufen, als Sie besitzen.");
            }
        }
    }


    public void saveUserPortfolios() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("portfolios.csv"))) {
            for (Map.Entry<String, Map<String, Integer>> userEntry : userPortfolios.entrySet()) {
                for (Map.Entry<String, Integer> stockEntry : userEntry.getValue().entrySet()) {
                    writer.write(userEntry.getKey() + "," + stockEntry.getKey() + "," + stockEntry.getValue() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
