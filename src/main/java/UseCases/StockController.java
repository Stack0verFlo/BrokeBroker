package UseCases;

import Entities.Stock;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class StockController {
    private final List<String> availableStocks;
    private final Map<String, Stock> stocks = new HashMap<>();
    private MongoCollection<Document> stocksCollection;
    private Stock stock;

    public StockController() {
        this.availableStocks = Arrays.asList("AAPL", "MSFT", "GOOGL", "AMZN", "TSLA", "FB", "BRK.A", "V",
                "JNJ", "WMT", "JPM", "MA", "PG", "UNH", "DIS", "NVDIA", "HD",
                "PYPL", "BAC", "VZ", "SAP"); // Beispielaktien
        initializeDatabase();
        initializePrices();
    }

    private void initializeDatabase() {
        String uri = "mongodb://root:example@127.0.0.1:27018";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("BrokeBroker");
        this.stocksCollection = database.getCollection("Stocks");
    }

    private void initializePrices() {
        Random random = new Random();
        for (String stockSymbol : availableStocks) {
            int currentPrice = getCurrentPrice(stockSymbol) + random.nextInt(71) - 35; // Jetzt von -50 bis +50
            List<Integer> prices = new ArrayList<>();
            prices.add(currentPrice); // Aktuellen Preis als ersten Eintrag hinzufügen
            for (int i = 1; i < 30; i++) { // Letzte 30 Tage
                prices.add(currentPrice + random.nextInt(21) - 10); // Kleine Schwankungen von -10 bis +10
            }

            Stock stock = new Stock(stockSymbol, currentPrice);
            stock.getHistoricalPrices().addAll(prices); // Fügen die generierten historischen Preise hinzu
            stocks.put(stockSymbol, stock);

            // Aktualisiere die Daten in MongoDB
            Document stockDoc = new Document("symbol", stockSymbol)
                    .append("currentPrice", currentPrice)
                    .append("historicalPrices", prices);
            stocksCollection.findOneAndReplace(new Document("symbol", stockSymbol), stockDoc);
        }
    }

    public int getCurrentPrice(String stockSymbol) {
        Document stockDoc = stocksCollection.find(new Document("symbol", stockSymbol)).first();
        if (stockDoc != null) {
            return stockDoc.getInteger("currentPrice");
        }
        return 0;
    }

    public List<Integer> getHistoricalPrices(String stockSymbol) {
        Document stockDoc = stocksCollection.find(new Document("symbol", stockSymbol)).first();
        if (stockDoc != null) {
            return (List<Integer>) stockDoc.get("historicalPrices");
        }
        return new ArrayList<>();
    }

    public List<String> getAvailableStocks() {
        return availableStocks;
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }
}
