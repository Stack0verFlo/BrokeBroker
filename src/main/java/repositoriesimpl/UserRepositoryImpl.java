package repositoriesimpl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import Entities.User;
import repositories.UserRepository;
import static com.mongodb.client.model.Filters.eq;

public class UserRepositoryImpl implements UserRepository {
    private final MongoCollection<Document> collection;

    public UserRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection("users");
    }

    @Override
    public User findByUsername(String username) {
        Document doc = collection.find(eq("username", username)).first();
        if (doc != null) {
            User user = new User(
                    doc.getString("username"),
                    doc.getString("email"),
                    doc.getString("password") // Passwort ist hier bereits gehasht
            );
            // Korrekter Umgang mit ObjectId
            ObjectId id = doc.getObjectId("_id");
            if (id != null) {
                user.setId(id.toString());
            }
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
        ObjectId id = (ObjectId) doc.get("_id");
        return id != null ? id.toString() : null; // Gibt die generierte ID zurück, sicherstellen, dass ID existiert
    }
}