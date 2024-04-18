package repositoriesimpl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import Entities.User;
import repositories.UserRepository;
import static com.mongodb.client.model.Filters.eq;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class UserRepositoryImpl implements UserRepository {
    private final MongoCollection<Document> collection;

    public UserRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection("users");
    }

    @Override
    public User findByUsername(String username) {
        Document doc = collection.find(eq("username", username)).first();
        if (doc != null) {
            User user = new User(doc.getString("username"), doc.getString("email"), doc.getString("password"));
            user.setId(doc.getObjectId("_id").toString());
            return user;
        }
        return null;
    }

    @Override
    public void save(User user) {
        Document doc = new Document("username", user.getUsername())
                .append("email", user.getEmail())
                .append("password", user.getPassword());
        collection.insertOne(doc);
        user.setId(doc.getObjectId("_id").toString());  // Setzt die von MongoDB generierte ID
    }

    @Override
    public boolean authenticate(String username, String password) {
        User user = findByUsername(username);
        return user != null && user.getPassword().equals(hashPassword(password));
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }
}