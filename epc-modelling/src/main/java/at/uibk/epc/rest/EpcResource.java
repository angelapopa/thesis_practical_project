package at.uibk.epc.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import at.uibk.epc.importer.MongoDatabaseClient;

@Path("/resource")
public class EpcResource {

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
		searchQuery.put("country", country);
		FindIterable<Document> iterable = database.getCollection("EPC_Collection").find(searchQuery);

		//TODO: looks like it does not return anything
		return Response.ok(iterable.cursor().next().toString()).build();
	}

}
