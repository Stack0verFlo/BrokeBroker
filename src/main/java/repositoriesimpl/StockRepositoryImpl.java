package repositoriesimpl;

import Entities.Stock;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import repositories.StockRepository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;


public class StockRepositoryImpl implements StockRepository{
    private final MongoCollection<Document> collection;

    public StockRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection("stocks");
    }

    @Override
    public List<Stock> findAll() {
        List<Stock> stocks = new ArrayList<>();
        for (Document doc : collection.find()) {
            stocks.add(documentToStock(doc));
        }
        return stocks;
    }

    private Stock documentToStock(Document doc) {
        // Methode zum Umwandeln eines MongoDB-Dokuments in ein Stock-Objekt
        Stock stock = new Stock(doc.getString("symbol"), doc.getDouble("currentPrice"));
        // Setzen der historischen Preise, wenn vorhanden
        List<Double> historicalPrices = doc.getList("historicalPrices", Double.class);
        for (Double price : historicalPrices) {
            stock.addHistoricalPrice(price);
        }
        return stock;
    }

    @Override
    public boolean collectionExists() {
        // Überprüfen, ob Dokumente in der Collection vorhanden sind
        return collection.countDocuments() > 0;
    }

    @Override
    public Stock findBySymbol(String symbol) {
        Document doc = collection.find(eq("symbol", symbol)).first();
        if (doc != null) {
            return new Stock(doc.getString("symbol"), doc.getDouble("currentPrice"));
        }
        return null;
    }

    @Override
    public void save(Stock stock) {
        Document doc = new Document("symbol", stock.getSymbol())
                .append("currentPrice", stock.getCurrentPrice())
                .append("historicalPrices", stock.getHistoricalPrices());
        collection.replaceOne(eq("symbol", stock.getSymbol()), doc, new ReplaceOptions().upsert(true));
    }
}
