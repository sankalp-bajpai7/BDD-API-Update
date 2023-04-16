package Util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Random;

import static com.mongodb.client.model.Filters.eq;

public class mongoDb {

    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> collection;

    Random random = new Random();
    testObj TestObj = new testObj();

    public void establishConnection() {

        // replace "your_database_uri" with the actual URI of your MongoDB instance

        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017/"));

        //mongoClient = new MongoClient("mongodb+srv://sankalpbajpai0701:Jackscarface0701@cluster0.hulk5nt.mongodb.net/?retryWrites=true&w=majority");
        // replace "your_database_name" and "your_collection_name" with the name of your database and collection
       // database = mongoClient.getDatabase("sample_airbnb");
        database = mongoClient.getDatabase("sankalp");
       // collection = database.getCollection("listingsAndReviews");
        collection = database.getCollection("users");

    }

    public void createClusterCon() {
        mongoClient = new MongoClient("mongodb+srv://sankalpbajpai0701:Jackscarface0701@cluster0.hulk5nt.mongodb.net/test");
        database = mongoClient.getDatabase("sample_airbnb");
        collection = database.getCollection("listingsAndReviews");

    }

    public void setResponseDbCon(){
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017/"));
        database = mongoClient.getDatabase("sankalp");
        collection = database.getCollection("Response");
    }

    public void accessRecords() {
        // retrieve data from the collection
        String id = "6433fff112d8294665f07482";
        Document filter = new Document("_id", new ObjectId(id));

        Document result = collection.find(filter).first();
        System.out.println(result.toJson());
    }

    public void addRecords(String username){

        int randomNumber1 = random.nextInt(1000);
        Document document = new Document("id",randomNumber1).append("username",username).append("timestamp",TestObj.getCurrentTime());
        collection.insertOne(document);

    }

    public void deleteRecordfromDB(){
        String id = "6433fff112d8294665f07482";
        Document filter = new Document("_id", new ObjectId(id));
        if(filter!=null) {
            System.out.println("Documents found for the id:" +id);
            collection.deleteOne(filter);
        }
        else {
            System.out.println("Document not found");
        }

    }

    public void deleteManyRecords(String username) {

        Document filter = new Document("username",username);
        if(filter!=null) {
            System.out.println("Documents found for the username:" +username);
            collection.deleteMany(filter);
        }
        else {
            System.out.println("Document not found");
        }
    }

    public void addInvalidResponsetoDb(String responseBody,String status, String result) {
        int genNum = random.nextInt(100,100000);
        Document document = new Document("tokenId", genNum).append("response",responseBody).append("Code", status).append("Message", result);
        collection.insertOne(document);
    }

    public void addUpdatedResponse(String token, String status, String result) {
        int genNum = random.nextInt(100,100000);
        Document document = new Document("tokenId", genNum).append("token",token).append("status", status).append("result", result);
        collection.insertOne(document);
    }



    public void closeConnection() {

        // close the MongoDB connection
        mongoClient.close();
    }


}
