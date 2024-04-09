package UseCases;

import Entities.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

public class UserController {
    private MongoCollection<Document> collection;

    public UserController() {
        // Initialisierung der Datenbankverbindung und Kollektion
        String uri = "mongodb://root:example@127.0.0.1:27018";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("BrokeBroker");
        this.collection = database.getCollection("Users"); // Stelle sicher, dass es "Users" ist
    }

    public User createUser(User user) {
        Document userDoc = new Document("name", user.getName())
                .append("email", user.getEmail())
                .append("password", user.getPassword()); // Beachte: Passwort sollte gehasht sein
        collection.insertOne(userDoc);

        // Nach dem Einfügen hat das userDoc ein Feld _id, das von MongoDB generiert wurde
        user.setId(userDoc.get("_id").toString()); // Setze die generierte ID im User-Objekt

        return user;
    }
}
