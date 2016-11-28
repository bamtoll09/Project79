package moe.bamtoll;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Vector;

/**
 * Created by Jaehyun on 2016-11-28.
 */
public class Rank {

    public static Rank Instance;

    private static MongoClient mongoClient;
    private static MongoDatabase mongoDB;
    private static MongoCollection<Document> coll;

    public static void Init(String host, String dbName) {

        mongoClient = new MongoClient(host, 27017);

        mongoClient.setWriteConcern(WriteConcern.JOURNALED);

        mongoDB = mongoClient.getDatabase(dbName);
        coll = mongoDB.getCollection("pick_pick");

    }

    public static void Insert(String name, int score) {
        Document doc =
                new Document("name", name)
                        .append("score", score);
        coll.insertOne(doc);
    }

    public static Vector<RankItem> FindAll() {
        FindIterable<Document> documents = coll.find();
        documents.sort(new Document("score", -1));

        Vector<RankItem> rankItems = new Vector<>();
        documents.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                rankItems.add(new RankItem(
                        document.getString("name"),
                        document.getInteger("score")
                ));
            }
        });
        return rankItems;
    }

}
