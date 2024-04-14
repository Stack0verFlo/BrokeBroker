package UseCases;

import Frameworks.DatabaseManager;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

public class AuthenticationService {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> userCollection;
    private MongoCollection<Document> portfolioCollection;

    public AuthenticationService() {
        MongoDatabase database = DatabaseManager.getInstance().getDatabase();
        userCollection = database.getCollection("User");
        portfolioCollection = database.getCollection("Portfolio");
    }

    public boolean authenticateUser(String username, String password) {
        Document user = userCollection.find(new Document("username", username)).first();
        return user != null && user.getString("password").equals(password);
    }

    public void registerUser(String username, String password) {
        if (!userExists(username)) {
            createUser(username, password);
            createPortfolioForUser(username);
        }
    }

    private boolean userExists(String username) {
        Document user = userCollection.find(new Document("username", username)).first();
        return user != null;
    }

    private void createUser(String username, String password) {
        // In einem echten Szenario sollte das Passwort gehasht und gesalzen werden
        Document newUser = new Document("username", username)
                .append("password", password);
        userCollection.insertOne(newUser);
    }
    //test
    private void createPortfolioForUser(String username) {
        Document user = userCollection.find(new Document("username", username)).first();
        if (user != null) {
            ObjectId userId = user.getObjectId("_id");
            Document newPortfolio = new Document("userId", userId.toHexString())
                    .append("username", username) // Initial leeres Portfolio
                    .append("stocks", new Document());
            portfolioCollection.insertOne(newPortfolio);
        }
    }
}
