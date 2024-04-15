package UseCases;

import Entities.User;
import Frameworks.DatabaseManager;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import UseCases.UserController;
import org.bson.Document;
import org.bson.types.ObjectId;

public class AuthenticationService {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private UserController userController;
    private MongoCollection<Document> userCollection;
    private MongoCollection<Document> portfolioCollection;

    public AuthenticationService() {
        MongoDatabase database = DatabaseManager.getInstance().getDatabase();
        userCollection = database.getCollection("Users");
        portfolioCollection = database.getCollection("Portfolio");
        userController = new UserController();
    }

    public boolean authenticateUser(String username, String password) {
        Document user = userCollection.find(new Document("username", username)).first();
        return user != null && user.getString("password").equals(password);
    }

    public void registerUser(String username, String password) {
        if (!userExists(username)) {
            User newUser = userController.createUser(username, password);
            createPortfolioForUser(newUser.getId(), username);
        }
    }

    private boolean userExists(String username) {
        Document user = userCollection.find(new Document("username", username)).first();
        return user != null;
    }

    //test
    private void createPortfolioForUser(String userId, String username) {
        Document newPortfolio = new Document("userId", userId)
                .append("username", username) // Initial leeres Portfolio
                .append("stocks", new Document());
        portfolioCollection.insertOne(newPortfolio);
    }
}
