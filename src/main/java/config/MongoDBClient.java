package config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBClient {
    private static MongoClient client;
    private static MongoDatabase database;

    public static MongoDatabase getDatabase() {
        if (client == null || database == null) {
            client = MongoClients.create("mongodb://root:example@127.0.0.1:27018");
            database = client.getDatabase("BrokeBroker");
        }
        return database;
    }
}
