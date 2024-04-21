package repositoriesimpl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import Entities.Stock;
import repositories.StockRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class StockRepositoryImpl implements StockRepository {
    private final MongoCollection<Document> collection;

    public StockRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection("stocks");
    }

    @Override
    public boolean isEmpty() {
        return collection.countDocuments() == 0;
    }

    @Override
    public Stock findBySymbol(String symbol) {
        Document doc = collection.find(eq("symbol", symbol)).first();
        return doc != null ? documentToStock(doc) : null;
    }

    @Override
    public void save(Stock stock) {
        Document doc = new Document("symbol", stock.getSymbol())
                .append("currentPrice", stock.getCurrentPrice())
                .append("historicalPrices", stock.getHistoricalPrices(40));
        collection.replaceOne(eq("symbol", stock.getSymbol()), doc, new com.mongodb.client.model.ReplaceOptions().upsert(true));
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
        Stock stock = new Stock(doc.getString("symbol"), doc.getDouble("currentPrice"));
        stock.setHistoricalPrices(doc.getList("historicalPrices", Double.class));
        return stock;
    }
}
