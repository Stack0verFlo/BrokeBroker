package UseCases;

import Entities.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

public class UserController {
    private MongoCollection<Document> collection;

    public UserController() {
        // Initialisierung der Datenbankverbindung und Kollektion
        String uri = "mongodb://root:example@127.0.0.1:27018";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("BrokeBroker");
        this.collection = database.getCollection("Users"); // Stelle sicher, dass es "Users" ist
    }

    public User createUser(String username, String password, double initialBalance) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username, null, hashedPassword, initialBalance);
        Document userDoc = new Document("username", user.getName())
                .append("password", user.getPassword())
                .append("balance", user.getBalance());
        if(collection.insertOne(userDoc).wasAcknowledged()) {
            user.setId(userDoc.get("_id").toString());
            return user;
        } else {
            throw new RuntimeException("Failed to create user");
        }
    }

    public void updateUserBalance(String userId, double newBalance) {
        Document update = new Document("$set", new Document("balance", newBalance));
        if(!collection.updateOne(new Document("_id", new ObjectId(userId)), update).wasAcknowledged()) {
            throw new RuntimeException("Failed to update user balance");
        }
    }
}
