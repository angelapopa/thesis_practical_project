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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import at.uibk.epc.model.Address;
import at.uibk.epc.model.Assessor;
import at.uibk.epc.model.ClimateData;
import at.uibk.epc.model.Dwelling;
import at.uibk.epc.model.DwellingType;
import at.uibk.epc.model.EPC;
import at.uibk.epc.model.Measure;
import at.uibk.epc.model.MeasuringUnit;
import at.uibk.epc.model.Person;
import at.uibk.epc.model.PurposeType;
import at.uibk.epc.model.Rating;
import at.uibk.epc.model.RatingMethodology;
import at.uibk.epc.model.Software;
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
		
		System.out.println("Before import: " + database.getCollection("EPC_Collection").countDocuments());
		
		MongoCollection<EPC> epcCollection = database.getCollection("EPC_Collection", EPC.class);

		addAustrianEPC(epcCollection);
        addRomanianEPC(epcCollection);
        addGermanEPC(epcCollection);
        addDanischEPC(epcCollection);
        
        for (Document doc: database.getCollection("EPC_Collection").find()) {
        	System.out.println(doc.toJson());
        }
        
        System.out.println("Total: " + database.getCollection("EPC_Collection").countDocuments());
	}
	
    private static void addAustrianEPC(MongoCollection<EPC> epcCollection) {
    	//particularities: this is a EPC for a building
    	//fields missing in the model, like "Organisation: Neue Heimat Tirol" - or is it part of the assessors organisation?!,
    	//or like "Eigentümerin: Neue Heimat Tirol"
    	
		Calendar creationDate = Calendar.getInstance();
		creationDate.set(2008, 11, 3, 0, 0);
		
		Calendar validUntil = Calendar.getInstance();
		validUntil.set(2018, 11, 3, 0, 0);
		
		Assessor assessor = new Assessor("Ing.", "Rutzinger", "Kajetan", null, null, null);
		
		ClimateData climateData = new ClimateData();
		climateData.setIdealIndoorTemperature(new Temperature(20.0, TemperatureUnit.CELCIUS));
		climateData.setAverageOutdoorTemperature(new Temperature(-11.4, TemperatureUnit.CELCIUS));
		climateData.setHightAboveSeaLevel(new Measure(573, MeasuringUnit.METER));
		climateData.setHeatingDaysPerYear(220); 
		
		SpatialData spatialData = new SpatialData();
		spatialData.setTotalFloorArea(new Measure(800.86, MeasuringUnit.SQUARE_METER));
		spatialData.setTotalVolume(new Measure(2272.87, MeasuringUnit.CUBIC_METER));
		
		ThermalData thermalData = new ThermalData();
		thermalData.setUValue(new Measure(0.90, MeasuringUnit.WATTS_SQUARE_METER_KELVIN));
		thermalData.setHeatingDemand(new Measure(103.91, MeasuringUnit.KWH_SQUARE_METER_YEAR));
		
		Dwelling dwelling = new Dwelling(
				new Address("Pacherstr.", "14", null, null, "6020", "Innsbruck", "Austria"), 
				1960, 
				DwellingType.APARTMENT_BUILDING,
				"1361",
				(List<String>) Arrays.asList("1557/9", "1558/3"), 
				spatialData,
				thermalData);
		dwelling.setClimateData(climateData);
		
		Rating rating = new Rating("D+", null);
		
		Software software = new Software("Allplan", "AX3000");
		Calendar releaseDate = Calendar.getInstance();
		releaseDate.set(2008, 9, 20, 0, 0);
		software.setReleaseDate(releaseDate.getTime());
		RatingMethodology ratingMethodology = new RatingMethodology(null, null, null, software);
		
		epcCollection.insertOne(new EPC(null, creationDate.getTime(), validUntil.getTime(), dwelling, assessor, rating, ratingMethodology, "ÖNORM H 5055, Rechtlinie 2002/91/EG"));
		
		for (EPC epc : epcCollection.find()) {
			System.out.println(epc.toString());
		}
	}

	private static void addDanischEPC(MongoCollection<EPC> epcCollection) {
		// TODO Auto-generated method stub
	}

	private static void addGermanEPC(MongoCollection<EPC> epcCollection) {
		// TODO Auto-generated method stub
		
	}

	private static void addRomanianEPC(MongoCollection<EPC> epcCollection) {
		//missing fields for apa calda de consum
		//climatisare
		//ventilare mecanica
		//iluminat artificial
		//surse regenerabile
		
		Calendar creationDate = Calendar.getInstance();
		creationDate.set(2016, 7, 2, 0, 0);
		
		Calendar validUntil = Calendar.getInstance();
		validUntil.set(2026, 7, 2, 0, 0);
		
		Rating rating = new Rating("B", 97.3);
		
		ThermalData thermalData = new ThermalData();
		thermalData.setUValue(new Measure(0.90, MeasuringUnit.WATTS_SQUARE_METER_KELVIN));
		thermalData.setHeatingDemand(new Measure(150.83, MeasuringUnit.KWH_SQUARE_METER_YEAR));
		thermalData.setCarbonFootprint(new Measure(29.55, MeasuringUnit.KG_SQUARE_METER_YEAR));
		
		SpatialData spatialData = new SpatialData();
		spatialData.setTotalFloorArea(new Measure(45.95, MeasuringUnit.SQUARE_METER));
		spatialData.setTotalVolume(new Measure(121.77, MeasuringUnit.CUBIC_METER));
		spatialData.setOrientation("SE-SV");
		
		Assessor assessor = new Assessor("I-CI", "Rotaru", "Nicolae Mihai", "UA-01579", null, null);
		
		Dwelling dwelling = new Dwelling(
				new Address("Stejarului", "60", "1", "13", "407280", "Floresti", "Romania"), 
				2016, 
				DwellingType.FLAT,
				null,
				null, 
				spatialData,
				thermalData);
		
		RatingMethodology ratingMethodology = new RatingMethodology();
		ratingMethodology.setCalculationName("Methodology for energy performance of buildings");
		ratingMethodology.setCalculationMethod("monthly");
		ratingMethodology.setSoftwareUsed(new Software("termoexpert", "3.1"));
		
		EPC epc = new EPC("UA-01579 365", creationDate.getTime(), validUntil.getTime(), dwelling, assessor, rating, ratingMethodology, null);
		epc.setPurpose(PurposeType.RENTING_OR_SELLING);
		
		epcCollection.insertOne(epc);
    }
}
