package com.loncoto.firstMongo;


import java.util.Arrays;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class App 
{
    public static void main( String[] args )
    {
    	// connection au serveur mongo
        MongoClient mongo = new MongoClient("localhost", 27017);
        
        // recuperation base de donnée
        MongoDatabase database = mongo.getDatabase("alloquoicine");
        MongoCollection collection = database.getCollection("films");
        
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("titre", "kung fury");
        // { "titre": "kung fury"}
        FindIterable<Document> result = collection.find(searchQuery);
        
        for (Document elem : result) {
        	System.out.println(elem);
        	System.out.println("annee = " + elem.getInteger("annee")); 
        }
        System.out.println("-----------------------");
        
        
        /*
        Document newFilm = new Document();
        newFilm.put("titre", "la grande vadrouille");
        newFilm.put("annee", 1966);
        newFilm.put("duree", 180);
        Document realisateur = new Document();
        
        realisateur.put("nom", "gerard oury");
        realisateur.put("nationalite", "francais");
        newFilm.put("realisateur", realisateur);
        
        newFilm.put("genre", Arrays.asList("comedie", "film de guerre"));
        
        Document deFunes = new Document();
        deFunes.put("nom", "de funes");
        deFunes.put("age", 45);
        Document bourvil = new Document();
        bourvil.put("nom", "bourvil");
        bourvil.put("age", 42);
        
        newFilm.put("acteurs", Arrays.asList(deFunes, bourvil));
        
        collection.insertOne(newFilm);
        */
        
        // insertOne -> insertion
        // replaceOne --> save
        // updateIne --> update
        // deleteone --> remove
        
        
    }
}
