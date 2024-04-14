package Frameworks;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
//DIESE KLASSE IST EIN SINGLETON
/**
 Einmalige Initialisierung: DatabaseManager wird als Singleton implementiert, was bedeutet, dass die Datenbankverbindung und die Sammlungen genau einmal initialisiert werden.
 Zentrale Datenbankverwaltung: Alle Datenbankoperationen werden durch eine zentrale Klasse gehandhabt, was die Wartung und das Debugging erleichtert.
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private MongoClient mongoClient;
    private MongoDatabase database;

    private DatabaseManager() {
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        String uri = "mongodb://root:example@127.0.0.1:27018";
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("BrokeBroker");
        ensureStocksCollection();
    }

    private void ensureStocksCollection() {
        MongoCollection<Document> stocksCollection = database.getCollection("Stocks");
        long count = stocksCollection.countDocuments();
        if (count == 0) { // Nur initialisieren, wenn die Sammlung leer ist
            initializeStocks(stocksCollection);
        }
    }
    //test
    private void initializeStocks(MongoCollection<Document> stocksCollection) {
        String[] stockSymbols = new String[]{
                "AAPL", "MSFT", "GOOGL", "AMZN", "TSLA", "FB", "BRK.A", "V",
                "JNJ", "WMT", "JPM", "MA", "PG", "UNH", "DIS", "NVDIA", "HD",
                "PYPL", "BAC", "VZ", "SAP"
        };
        for (String symbol : stockSymbols) {
            Document stock = new Document("symbol", symbol)
                    .append("currentPrice", 0.00); // Initialpreis auf 0 setzen, kann später aktualisiert werden
            stocksCollection.insertOne(stock);
        }
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}