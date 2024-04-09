package Frameworks;

import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class Database {
    public static void main(String[] args) {
        String uri = "mongodb://root:example@127.0.0.1:27018";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("BrokeBroker");
            MongoCollection<Document> collection = database.getCollection("Entities.User");
            collection.insertOne(new Document("name", "TestUser").append("password", "TestPassword"));
            Document doc = collection.find(eq("name", "TestUser")).first();
            if (doc != null) {
                System.out.println(doc.toJson());
            }
        }
    }
}