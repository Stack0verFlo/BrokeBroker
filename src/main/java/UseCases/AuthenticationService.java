package UseCases;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class AuthenticationService {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> userCollection;

    public AuthenticationService() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        String uri = "mongodb://root:example@127.0.0.1:27018";
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("BrokeBroker");
        userCollection = database.getCollection("User");
    }

    public boolean authenticateUser(String username, String password) {
        Document user = userCollection.find(new Document("username", username)).first();
        return user != null && user.getString("password").equals(password);
    }

    public void registerUser(String username, String password) {
        Document existingUser = userCollection.find(new Document("username", username)).first();
        if (existingUser == null) {
            Document newUser = new Document("username", username)
                    .append("password", password); // Passwörter sollten gehasht werden
            userCollection.insertOne(newUser);
        }
    }
}
