package at.uibk.epc.importer;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDatabaseClient {
	
	public static MongoDatabase getDatabase() {
		//TODO extract this to a properties file
		MongoClient mongoClient = MongoClients.create(
				"mongodb+srv://epc_user:1user01@clusterepc-typif.mongodb.net/test?retryWrites=true&w=majority");

    	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		
		MongoDatabase database = mongoClient.getDatabase("EPC_DB");
		database = database.withCodecRegistry(pojoCodecRegistry);
		
		return database;
	}
}
