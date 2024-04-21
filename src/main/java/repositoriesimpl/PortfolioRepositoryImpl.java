package repositoriesimpl;

import Entities.Portfolio;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import repositories.PortfolioRepository;

import static com.mongodb.client.model.Filters.eq;

public class PortfolioRepositoryImpl implements PortfolioRepository {

    private final MongoCollection<Document> collection;

    public PortfolioRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection("portfolios");
    }
    @Override
    public Portfolio findByUserId(String userId) {
        Document doc = collection.find(eq("userId", userId)).first();
        if (doc != null) {
            Portfolio portfolio = new Portfolio(userId);
            portfolio.setId(doc.getObjectId("_id").toString()); // Setzt die ID des Portfolios
            // Setzen weiterer Eigenschaften falls notwendig
            return portfolio;
        }
        return null;
    }

    @Override
    public void createForUserId(String userId) {
        Document newPortfolio = new Document("userId", userId);
        collection.insertOne(newPortfolio);
    }

    @Override
    public Portfolio findById(String portfolioId) {
        if (portfolioId == null /*|| portfolioId.length() != 24*/) {
            throw new IllegalArgumentException("Could not get PortfolioId");
        }
        Document doc = collection.find(eq("_id", new ObjectId(portfolioId))).first();
        if (doc != null) {
            // Konvertiere das Dokument in eine Portfolio-Entität
            Portfolio portfolio = new Portfolio(doc.getString("userId"));
            portfolio.setId(doc.getObjectId("_id").toString());
            // Setzen weiterer Eigenschaften falls notwendig
            return portfolio;
        }
        return null;
    }

    @Override
    public void save(Portfolio portfolio) {
        Document doc = new Document("_id", portfolio.getId())
                .append("userId", portfolio.getUserId()); // Nutze die UserID statt des User-Objekts
        collection.replaceOne(eq("_id", portfolio.getId()), doc, new ReplaceOptions().upsert(true));
    }
}
