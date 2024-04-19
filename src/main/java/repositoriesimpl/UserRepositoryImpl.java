package repositoriesimpl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import Entities.User;
import repositories.UserRepository;
import static com.mongodb.client.model.Filters.eq;
import org.mindrot.jbcrypt.BCrypt;

public class UserRepositoryImpl implements UserRepository {
    private final MongoCollection<Document> collection;

    public UserRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection("users");
    }

    @Override
    public User findByUsername(String username) {
        Document doc = collection.find(eq("username", username)).first();
        if (doc != null) {
            // Hier wird angenommen, dass das Passwort in der Datenbank bereits gehasht ist
            return new User(
                    doc.getString("username"),
                    doc.getString("email"),
                    doc.getString("password") // Das sollte der gehashte Passwortwert sein
            );
        }
        return null;
    }

    @Override
    public void save(User user) {
        Document doc = new Document("username", user.getUsername())
                .append("email", user.getEmail())
                .append("password", user.getPassword()); // Passwort ist hier bereits gehasht
        collection.insertOne(doc);
    }

    @Override
    public boolean authenticate(String username, String password) {
        return false;
    }
}