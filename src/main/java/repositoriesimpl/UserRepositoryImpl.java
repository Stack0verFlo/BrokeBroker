package repositoriesimpl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import Entities.User;
import repositories.UserRepository;
import static com.mongodb.client.model.Filters.eq;

public class UserRepositoryImpl implements UserRepository {
    private final MongoCollection<Document> collection;
    private User user;

    public UserRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection("users");
    }

    @Override
    public User findByUsername(String username) {
        Document doc = collection.find(eq("username", username)).first();
        if (doc != null) {
            // Hier wird angenommen, dass das Passwort in der Datenbank bereits gehasht ist
            User user = new User(
                    doc.getString("username"),
                    doc.getString("email"),
                    doc.getString("password")
            );
            user.setId(doc.get("_id").toString());
            return user;

        }


        return null;
    }

    @Override
    public String save(User user) {
        Document doc = new Document("username", user.getUsername())
                .append("email", user.getEmail())
                .append("password", user.getPassword()); // Passwort ist hier bereits gehasht
        collection.insertOne(doc);
        return doc.getObjectId("_id").toString(); // Gibt die generierte ID zurück
    }

    @Override
    public boolean authenticate(String username, String password) {
        return false;
    }

    @Override
    public void updatePortfolioId(String userId, String portfolioId) {

    }
}