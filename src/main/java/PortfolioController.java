import java.io.*;
import java.util.*;

public class PortfolioController {
    private Map<String, Map<String, Integer>> userPortfolios = new HashMap<>();

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
                userPortfolios.computeIfAbsent(data[0], k -> new HashMap<>()).put(data[1], Integer.parseInt(data[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void buyStock(String username, String stockSymbol, int quantity) {
        userPortfolios.get(username).merge(stockSymbol,quantity,Integer::sum);
        saveUserPortfolios();
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

    // Weitere Methoden zum Hinzufügen/Entfernen von Aktien im Portfolio
}
