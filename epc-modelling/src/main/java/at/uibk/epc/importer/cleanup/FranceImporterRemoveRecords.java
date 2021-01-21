package at.uibk.epc.importer.cleanup;

import static com.mongodb.client.model.Filters.eq;

import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import at.uibk.epc.importer.MongoDatabaseClient;
import at.uibk.epc.model.EPC;

//remove records with finalEnergyConsumption 0
public class FranceImporterRemoveRecords {

	private static final String database_name = "EPC";
	private static final String collection_name="EPC_France";
	private static final String FRANCE_CONNECTION_STRING = "mongodb+srv://fra_1:Zg8RMRof0PiGOILE@cluster0.o03xt.mongodb.net/EPC?retryWrites=true&w=majority";
	
	public static void main(String[] args) {
		MongoDatabase database = MongoDatabaseClient.getDatabase(database_name, FRANCE_CONNECTION_STRING);
		MongoCollection<EPC> epcCollection = database.getCollection(collection_name, EPC.class);
			
		Bson filter = eq("ratedDwelling.thermalData.finalEnergyConsumption.value", 0);
		DeleteResult result = epcCollection.deleteMany(filter);
        System.out.println(result);
		
		System.out.println("finished");
	}
	
	
}
