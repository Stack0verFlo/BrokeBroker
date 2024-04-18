package repositoriesimpl;

import Entities.Portfolio;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import repositories.PortfolioRepository;

import static com.mongodb.client.model.Filters.eq;

public class PortfolioRepositoryImpl implements PortfolioRepository {

    private final MongoCollection<Document> collection;

    public PortfolioRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection("portfolios");
    }

    @Override
    public Portfolio findById(String id) {
        Document doc = collection.find(eq("_id", id)).first();
        if (doc != null) {
            return new Portfolio(doc.getString("_id"), null); // Anpassung nötig für das User-Objekt
        }
        return null;
    }

    @Override
    public void save(Portfolio portfolio) {
        Document doc = new Document("_id", portfolio.getId())
                .append("owner", portfolio.getOwner().getId()); // Annahme, dass User bereits konvertiert ist
        collection.replaceOne(eq("_id", portfolio.getId()), doc, new ReplaceOptions().upsert(true));
    }
}
