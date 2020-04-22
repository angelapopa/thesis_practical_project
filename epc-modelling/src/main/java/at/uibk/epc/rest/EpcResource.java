package at.uibk.epc.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.conversions.Bson;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import at.uibk.epc.importer.MongoDatabaseClient;
import at.uibk.epc.model.EPC;

@Path("/resource")
public class EpcResource {

	static final byte PAGE_SIZE = 5;

	//TODO: use property files
	//static final String ALLOWED_ACCESS_FROM = "http://localhost:3000";

	static final String ALLOWED_ACCESS_FROM = "https://epc-modelling-app.herokuapp.com/";
	
	static final String ALLOWED_REST_METHODS = "GET";

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEpc() {
		// https://crunchify.com/what-is-cross-origin-resource-sharing-cors-how-to-add-it-to-your-java-jersey-web-server/
		return Response.ok("{\"info\":\"On air!\"}").header("Access-Control-Allow-Origin", ALLOWED_ACCESS_FROM)
				.header("Access-Control-Allow-Methods", ALLOWED_REST_METHODS).build();
	}

	@GET
	@Path("/{country}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEpcByCountry(@PathParam("country") String country, @QueryParam("page") int pageNumber) {
		MongoDatabase database = MongoDatabaseClient.getDatabase();
	
	    int skip_documents = PAGE_SIZE * (pageNumber - 1);
		FindIterable<EPC> iterable = database.getCollection("EPC_Collection", EPC.class).find(getSearchQuery(country)).skip(skip_documents).limit(PAGE_SIZE);

		List<EPC> epcList = new ArrayList<EPC>();
		for (MongoCursor<EPC> it = iterable.iterator(); it.hasNext();) {
			epcList.add(it.next());
		}

		EpcListWrapper epcListWrapper = new EpcListWrapper();
		epcListWrapper.setWrappedEpcs(epcList);
		
		return Response.ok(epcListWrapper).header("Access-Control-Allow-Origin", ALLOWED_ACCESS_FROM)
				.header("Access-Control-Allow-Methods", ALLOWED_REST_METHODS).build();
	}
	
	private BasicDBObject getSearchQuery(String country) {
		BasicDBObject searchQuery = new BasicDBObject();

		if (country.equals("other")) {
			BasicDBList orList =  new BasicDBList();
			orList.add(new BasicDBObject("ratedDwelling.buildingAddress.country", "Deutschland"));
			orList.add(new BasicDBObject("ratedDwelling.buildingAddress.country", "Romania"));
			orList.add(new BasicDBObject("ratedDwelling.buildingAddress.country", "Denmark"));
			orList.add(new BasicDBObject("ratedDwelling.buildingAddress.country", "Austria"));
			return new BasicDBObject("$or", orList);
		}
		
		searchQuery.put("ratedDwelling.buildingAddress.country", country);
		return searchQuery;
	}

}
