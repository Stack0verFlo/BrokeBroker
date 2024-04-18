package repositoriesimpl;

import Entities.Stock;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import repositories.StockRepository;

import static com.mongodb.client.model.Filters.eq;


public class StockRepositoryImpl implements StockRepository{
    private final MongoCollection<Document> collection;

    public StockRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection("stocks");
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
