package at.uibk.epc.importer.cleanup;

import static com.mongodb.client.model.Filters.eq;

import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import at.uibk.epc.importer.MongoDatabaseClient;
import at.uibk.epc.model.EPC;

//remove records with floor area 0
public class IrelandImporterRemoveRecords {

	private static final String database_name = "EPC";
	private static final String collection_name="EPC_Ireland";
	//private static final String IRELAND_CONNECTION_STRING="mongodb+srv://epcuser2:pw12epc559@epcfull-2jvr7.mongodb.net/test?retryWrites=true&w=majority";
	private static final String IRELAND_CONNECTION_STRING = "mongodb+srv://ire_1:t9YjjOigsWGmPTJJ@cluster0.fxx98.mongodb.net/EPC?retryWrites=true&w=majority";
	
	public static void main(String[] args) {
		MongoDatabase database = MongoDatabaseClient.getDatabase(database_name, IRELAND_CONNECTION_STRING);
		MongoCollection<EPC> epcCollection = database.getCollection(collection_name, EPC.class);
			
		Bson filter = eq("ratedDwelling.spatialData.totalFloorArea.value", 0);
		DeleteResult result = epcCollection.deleteMany(filter);
        System.out.println(result);
		
		System.out.println("finished");
	}
	
	
}
