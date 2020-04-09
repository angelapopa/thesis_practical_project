package at.uibk.epc.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import at.uibk.epc.importer.MongoDatabaseClient;
import at.uibk.epc.model.EPC;

@Path("/resource")
public class EpcResource {

	static final byte BATCH_SIZE = 10;
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEpc() {
		return Response.ok("On air!").build();
	}

	@GET
	@Path("/{country}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEpcByCountry(@PathParam("country") String country) {
		MongoDatabase database = MongoDatabaseClient.getDatabase();
		BasicDBObject searchQuery = new BasicDBObject();

		searchQuery.put("ratedDwelling.buildingAddress.country", country);

		FindIterable<EPC> iterable = database.getCollection("EPC_Collection", EPC.class).find(searchQuery);
		
		FindIterable<EPC> result = iterable.batchSize(BATCH_SIZE);
		if (!result.cursor().hasNext()) {
			return Response.ok("No result was found!").build();
		}
		//TODO return a list of epcs
		return Response.ok(iterable.cursor().next()).build();
	}

}
