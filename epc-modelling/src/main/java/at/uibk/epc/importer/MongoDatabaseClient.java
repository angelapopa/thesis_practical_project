package at.uibk.epc.importer;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import at.uibk.epc.model.EPC;

public class MongoDatabaseClient {
	
	public static MongoDatabase getDatabase(String dbName, String connectionString) {
		
		MongoClient mongoClient = MongoClients
				.create(connectionString);

		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		MongoDatabase database = mongoClient.getDatabase(dbName);
		database = database.withCodecRegistry(pojoCodecRegistry);

		return database;
	}
	
	public  static void dropAndCreateDB(String connectionString, String databaseName, String collectionName) {
		MongoDatabase database = MongoDatabaseClient.getDatabase(databaseName, connectionString);
		MongoCollection<EPC> epcCollection = database.getCollection(collectionName, EPC.class);
		epcCollection.drop();
		database.createCollection(collectionName);
	}
}
