package at.uibk.epc.importer;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.Calendar;

import at.uibk.epc.model.Address;
import at.uibk.epc.model.Assessor;
import at.uibk.epc.model.Dwelling;
import at.uibk.epc.model.DwellingType;
import at.uibk.epc.model.EPC;
import at.uibk.epc.model.Measure;
import at.uibk.epc.model.MeasuringUnit;
import at.uibk.epc.model.Person;
import at.uibk.epc.model.Rating;
import at.uibk.epc.model.SpatialData;
import at.uibk.epc.model.Temperature;
import at.uibk.epc.model.TemperatureUnit;
import at.uibk.epc.model.ThermalData;

public class ImportEPC {

	public static void main(String args[]) {

		MongoClient mongoClient = MongoClients.create(
				"mongodb+srv://epc_user:1user01@clusterepc-typif.mongodb.net/test?retryWrites=true&w=majority");

    	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		
		MongoDatabase database = mongoClient.getDatabase("EPC_DB");
		database = database.withCodecRegistry(pojoCodecRegistry);
		
		System.out.println("Total: " + database.getCollection("EPC_Collection").countDocuments());
		
		MongoCollection<Document> collection = database.getCollection("EPC_Collection");

        newRomanianEPC(database);
        
        for (Document doc: collection.find()) {
        	System.out.println(doc.toJson());
        }
        
        System.out.println("Total: " + database.getCollection("EPC_Collection").countDocuments());
	}
	
    private static void newRomanianEPC(MongoDatabase database) {
		MongoCollection<EPC> epcCollection = database.getCollection("EPC_Collection", EPC.class);
		
		Dwelling dwelling = new Dwelling(
				new Address("St James Square", "45A", "2", "C", "4321", "London", "UK"), 
				1987, 
				DwellingType.FLAT, 
				"123456A", 
				new SpatialData(new Measure(56, MeasuringUnit.SQUARE_METER), new Measure(234, MeasuringUnit.SQUARE_METER), "S-E"),
				new ThermalData(new Temperature(22, TemperatureUnit.CELCIUS), 250));
		
		
		Calendar creationDate = Calendar.getInstance();
		creationDate.set(2009, 11, 8, 0, 0);
		
		Calendar validUntil = Calendar.getInstance();
		validUntil.set(2019, 11, 8, 0, 0);
		
		EPC epcRomania = new EPC("2019-12-12-12345", 
				creationDate.getTime(), validUntil.getTime(), 
				dwelling, 
				new Rating("B",(byte) 77), 
				new Assessor("surname1", "surname2", "123-AA-B-20090103", "123987-BN", 
						new Address("Wales Str.", "5", "1", "4", "40342", "Liverpool", "UK")), 
				"ÖNORM-2019");
		
		epcCollection.insertOne(epcRomania);
		
		for (EPC epc : epcCollection.find()) {
			System.out.println(epc.toString());
		}
    }
}
