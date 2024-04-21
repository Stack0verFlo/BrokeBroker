package repositoriesimpl;

import Entities.Portfolio;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import repositories.PortfolioRepository;
import Entities.StockEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class PortfolioRepositoryImpl implements PortfolioRepository {

    private final MongoCollection<Document> collection;
    private StockEntry stockEntry;

    public PortfolioRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection("portfolios");
    }

    @Override
    public Portfolio findByUserId(String userId) {
        Document doc = collection.find(eq("userId", userId)).first();
        if (doc != null) {
            Portfolio portfolio = new Portfolio(userId, 5000);
            portfolio.setId(doc.getObjectId("_id").toString()); // Setzt die ID des Portfolios
            portfolio.setBalance(doc.getDouble("balance"));
            doc.get("stocks", ArrayList.class).forEach(stock -> {
                String[] stockData = ((String) stock).split(",");
                portfolio.addStock(stockData[0], Integer.parseInt(stockData[1]), Double.parseDouble(stockData[2]));
            });
            return portfolio;
        }
        return null;
    }

    @Override
    public void createForUserId(String userId) {
        Document newPortfolio = new Document("userId", userId)
                .append("balance", 5000.0)
                .append("stocks", new ArrayList<>());


        collection.insertOne(newPortfolio);
    }

    @Override
    public void update(Portfolio portfolio) {
        Document doc = new Document("_id", portfolio.getId())
                .append("userId", portfolio.getUserId())
                .append("balance", portfolio.getBalance())
                .append("stocks", portfolio.getStocks())
                .append("quantity",stockEntry.getQuantity());
        collection.replaceOne(eq("_id", portfolio.getId()), doc);
    }

    @Override
    public Portfolio findById(String portfolioId) {
        if (portfolioId == null /*|| portfolioId.length() != 24*/) {
            throw new IllegalArgumentException("Could not get PortfolioId");
        }
        Document doc = collection.find(eq("_id", new ObjectId(portfolioId))).first();
        if (doc != null) {

            double balance = doc.get("balance") instanceof Double
                    ? doc.getDouble("balance")
                    : (double) doc.getInteger("balance", 0);

            Portfolio portfolio = new Portfolio(doc.getString("userId"), balance);
            portfolio.setId(doc.getObjectId("_id").toString());
            doc.get("stocks", ArrayList.class).forEach(stock -> {
                String[] stockData = ((String) stock).split(",");
                portfolio.addStock(stockData[0], Integer.parseInt(stockData[1]), Double.parseDouble(stockData[2]));
            });
            return portfolio;
        }
        return null;
    }

    @Override
    public void save(Portfolio portfolio) {
        Document doc = new Document()
                .append("userId", portfolio.getUserId())
                .append("balance", portfolio.getBalance())
                .append("stocks", portfolio.getStocks().stream().map(StockEntry::toString).collect(Collectors.toList()));
        collection.replaceOne(eq("_id", new ObjectId(portfolio.getId())), doc, new ReplaceOptions().upsert(true));
    }
}
