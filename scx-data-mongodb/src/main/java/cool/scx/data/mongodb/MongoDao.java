package cool.scx.data.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import cool.scx.data.Dao;
import cool.scx.data.Query;
import org.bson.Document;

import java.util.Collection;
import java.util.List;

public class MongoDao<Entity> implements Dao<Entity, String> {


    public static void main(String[] args) {
        String uri = "mongodb://127.0.0.1:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");
            collection.insertOne(Document.parse("{}"));
//            Document doc = collection.find(eq("title", "Back to the Future")).first();
            Document doc = collection.find().first();
            if (doc != null) {
                System.out.println(doc.toJson());
            } else {
                System.out.println("No matching documents found.");
            }
        }
    }

    @Override
    public String insert(Object o) {
        return null;
    }

    @Override
    public List<String> insertBatch(Collection<Entity> entityList) {
        return null;
    }

    @Override
    public List<Entity> select(Query query) {
        return null;
    }

    @Override
    public long update(Object o, Query query) {
        return 0;
    }

    @Override
    public long delete(Query query) {
        return 0;
    }

    @Override
    public long count(Query query) {
        return 0;
    }

    @Override
    public void _clear() {

    }

    @Override
    public Class<Entity> _entityClass() {
        return null;
    }

}
