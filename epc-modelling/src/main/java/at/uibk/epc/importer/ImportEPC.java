package at.uibk.epc.importer;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import at.uibk.epc.model.Address;
import at.uibk.epc.model.Assessor;
import at.uibk.epc.model.ClimateData;
import at.uibk.epc.model.ContactDetails;
import at.uibk.epc.model.Dwelling;
import at.uibk.epc.model.DwellingType;
import at.uibk.epc.model.EPC;
import at.uibk.epc.model.Measure;
import at.uibk.epc.model.MeasuringUnit;
import at.uibk.epc.model.Organisation;
import at.uibk.epc.model.PurposeType;
import at.uibk.epc.model.Rating;
import at.uibk.epc.model.RatingMethodology;
import at.uibk.epc.model.Software;
import at.uibk.epc.model.SpatialData;
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
        addDanishEPC(epcCollection);
        
        for (Document doc: database.getCollection("EPC_Collection").find()) {
        	System.out.println(doc.toJson());
        }
        
        System.out.println("Total: " + database.getCollection("EPC_Collection").countDocuments());
	}
	
    private static void addAustrianEPC(MongoCollection<EPC> epcCollection) {
    	
		Calendar creationDate = Calendar.getInstance();
		creationDate.set(2008, 11, 3, 0, 0);
		
		Calendar validUntil = Calendar.getInstance();
		validUntil.set(2018, 11, 3, 0, 0);
		
		Assessor assessor = new Assessor("Ing.", "Rutzinger", "Kajetan", null, null, null, new Organisation("Neue Heimat Tirol", null, null));
		
		ClimateData climateData = new ClimateData();
		climateData.setIdealIndoorTemperature(new Measure(20.0, MeasuringUnit.CELCIUS));
		climateData.setAverageOutdoorTemperature(new Measure(-11.4, MeasuringUnit.CELCIUS));
		climateData.setHightAboveSeaLevel(new Measure(573, MeasuringUnit.METER));
		climateData.setHeatingDaysPerYear(220); 
		
		SpatialData spatialData = new SpatialData();
		spatialData.setTotalFloorArea(new Measure(800.86, MeasuringUnit.SQUARE_METER));
		spatialData.setTotalVolume(new Measure(2272.87, MeasuringUnit.CUBIC_METER));
		
		ThermalData thermalData = new ThermalData();
		thermalData.setUValue(new Measure(0.90, MeasuringUnit.WATTS_SQUARE_METER_KELVIN));
		thermalData.setEnergyDemand(new Measure(103.91, MeasuringUnit.KWH_SQUARE_METER_YEAR));
		
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
    
    //UK
    //e:\docs\Uni Innsbruck\Masterthesis\building certifications\example_of_building_certificate.pdf
    
    //https://docplayer.dk/18645796-Energimaerke-adresse-dr-lassens-gade-7-postnr-by.html    EPC
	private static void addDanishEPC(MongoCollection<EPC> epcCollection) {
		//renovation year: 1994
		//Art der Heizung: fernwärme
		//list of flats and their size and consumption costs per year
		//epc Datum der Genehmigung: 10-09-2010
		//berechnete Wärmeanforderung: 76.091 kr./year (Preis)
		//recommendations
		
		Calendar creationDate = Calendar.getInstance();
		creationDate.set(2010, 8, 29, 0, 0);
		
		Calendar validUntil = Calendar.getInstance();
		validUntil.set(2015, 8, 29, 0, 0);
		
		Organisation organisation = new Organisation("Ingeniorfirmaet Henrij Mogelgaard ApS", 
				new Address("Hillerødgade","30A", null, null, "2200", "København N", "Denmark"), 
				new ContactDetails(null, "35360727"));
		Assessor assessor = new Assessor(null, "Hansen", "Jan Ole", "250611", null, new ContactDetails("joh@hmenergi.dk", null), organisation);
		
		Address address = new Address();
		address.setStreet("Dr. Lassens Gade");
		address.setStreetNumber("7");
		address.setPostalCode("8900");
		address.setCity("Randers C");
		
		RatingMethodology ratingMethodology = new RatingMethodology();
		ratingMethodology.setSoftwareUsed(new Software("Energy08", "Be06 version 4"));
		
		//The energy-labelling scale runs from A to G, where A is divided into A2020, A2015 and A2010. A2020 covers low energy buildings, which only consume a minimum of energy, 
		//while G-labelled buildings consume the most energy.
		Rating awordedRating = new Rating("D", null);
		
		ThermalData thermalData = new ThermalData();
		thermalData.setEnergyDemand(new Measure(109.509, MeasuringUnit.KWH_YEAR));
		
		SpatialData spatialData = new SpatialData();
		spatialData.setTotalFloorArea(new Measure(880, MeasuringUnit.SQUARE_METER));
		//beheiztes areal TODO: check with others if volume is refffered to beheiztes volumen
		spatialData.setTotalVolume(new Measure(880, MeasuringUnit.SQUARE_METER));
		
		Dwelling ratedDwelling = new Dwelling(address, 1919, DwellingType.APARTMENT_BUILDING, "730-009955-001", null, spatialData, thermalData);
		
		EPC epc = new EPC("200038127", creationDate.getTime(), validUntil.getTime(), ratedDwelling, assessor, awordedRating, null, null);
		epcCollection.insertOne(epc);
		
		for (EPC epcF : epcCollection.find()) {
			System.out.println(epcF.toString());
		}
	}

	private static void addGermanEPC(MongoCollection<EPC> epcCollection) {
		//missing fields in this epc model
		//Gebäudeteil:Vorderhaus
		//Baujahr Wärmeerzeuger:1982
		//nr. of flats: 9
		//type of main heating source: Erdgas
		//renewable energy sources: yes, no
		//Art der Lüftung: Fensterlüftung
		//here it is a distinction between Energiebedarfausweis(estimated) and Energieverbrauch (measured)
		//distinction between Primärenergiebdarf (248 kWh/(m^2a) and Endenergiebedarf (222 kWh/(m^2a)))
		//Recommendations on renovations
		
		Calendar validUntil = Calendar.getInstance();
		validUntil.set(2024, 5, 23, 0, 0);
		
		Calendar creationDate = Calendar.getInstance();
		creationDate.set(2014, 5, 24, 0, 0);
		
		Assessor assessor = new Assessor(null, "Mustermann", "Paul", null, null, null,
				new Organisation("", new Address("Austeller Musterstraße", "45", null, null, "12345", "Musterstadt", "Deutschland"), null));
		
		SpatialData spatialData = new SpatialData();
		spatialData.setTotalFloorArea(new Measure(546, MeasuringUnit.SQUARE_METER));
		
		ThermalData thermalData = new ThermalData();
		thermalData.setCarbonFootprint(new Measure(56, MeasuringUnit.KG_SQUARE_METER_YEAR));
		thermalData.setEnergyDemand(new Measure(222, MeasuringUnit.KWH_SQUARE_METER_YEAR));
		thermalData.setUValue(new Measure(1.11, MeasuringUnit.WATTS_SQUARE_METER_KELVIN));
		
		Dwelling ratedDwelling = new Dwelling(
				new Address("Musterstr", "123", null, null, "10115", "Musterstadt", "Deutschland"), 
				1927, 
				DwellingType.APARTMENT_BUILDING,
				null,
				null, 
				spatialData,
				thermalData);
		
		//estimated rating
		Rating awordedRating = new Rating("G", null);   //here should be a distinction between estimated and measured rating
		//maybe add also a scala, to see the possible rating levels.
	
		RatingMethodology ratingMethodology = new RatingMethodology();
		ratingMethodology.setStandardName("DIN V 4108-6 and DIN 4701-10 and Vereinfachungen nach §9 Absatz 2 EnEV"); //consider creating a list
		
		EPC epc = new EPC("123456789", creationDate.getTime(), validUntil.getTime(), ratedDwelling, assessor, awordedRating, ratingMethodology, "§§ 16 ff. Energiesparverordnung (EnEV) 18.11.2013");
		epc.setPurpose(PurposeType.RENTING_OR_SELLING);
		
		epcCollection.insertOne(epc);
		
		for (EPC epcF : epcCollection.find()) {
			System.out.println(epcF.toString());
		}
	}

	private static void addRomanianEPC(MongoCollection<EPC> epcCollection) {
		//missing fields for 
		//apa calda de consum
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
		thermalData.setEnergyDemand(new Measure(150.83, MeasuringUnit.KWH_SQUARE_METER_YEAR));
		thermalData.setCarbonFootprint(new Measure(29.55, MeasuringUnit.KG_SQUARE_METER_YEAR));
		
		SpatialData spatialData = new SpatialData();
		spatialData.setTotalFloorArea(new Measure(45.95, MeasuringUnit.SQUARE_METER));
		spatialData.setTotalVolume(new Measure(121.77, MeasuringUnit.CUBIC_METER));
		spatialData.setOrientation("SE-SV");
		
		Assessor assessor = new Assessor("I-CI", "Rotaru", "Nicolae Mihai", "UA-01579", null, null, null);
		
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
		
		for (EPC epcF : epcCollection.find()) {
			System.out.println(epcF.toString());
		}
    }
}
