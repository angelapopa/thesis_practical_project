package at.uibk.epc.importer;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import at.uibk.epc.model.Address;
import at.uibk.epc.model.Assessor;
import at.uibk.epc.model.BuildingAddress;
import at.uibk.epc.model.ClimateData;
import at.uibk.epc.model.ContactDetails;
import at.uibk.epc.model.Dwelling;
import at.uibk.epc.model.DwellingType;
import at.uibk.epc.model.EPC;
import at.uibk.epc.model.Measure;
import at.uibk.epc.model.MeasuringUnit;
import at.uibk.epc.model.Organisation;
import at.uibk.epc.model.Person;
import at.uibk.epc.model.PurposeType;
import at.uibk.epc.model.Rating;
import at.uibk.epc.model.RatingMethodology;
import at.uibk.epc.model.Software;
import at.uibk.epc.model.SpatialData;
import at.uibk.epc.model.ThermalData;

public class AnnexedEPCImporter {

	public static void main(String args[]) {

		MongoDatabase database = MongoDatabaseClient.getDatabase();
		
		System.out.println("Annexed Import - Before import: " + database.getCollection("EPC_Collection").countDocuments());
		
		MongoCollection<EPC> epcCollection = database.getCollection("EPC_Collection", EPC.class);

		addAustrianEPC(epcCollection); 	//estimated
        addRomanianEPC(epcCollection); 	//estimated
        addGermanEPC(epcCollection); 	//estimated and measured
        addDanishEPC(epcCollection); 	//measured
        
        for (Document doc: database.getCollection("EPC_Collection").find()) {
        	System.out.println(doc.toJson());
        }
        
        System.out.println("Total: " + database.getCollection("EPC_Collection").countDocuments());
	}
	
    private static void addAustrianEPC(MongoCollection<EPC> epcCollection) {
    	
		Calendar creationDate = Calendar.getInstance();
		creationDate.clear();
		creationDate.set(2008, 11, 3);
		
		Calendar validUntil = Calendar.getInstance();
		validUntil.clear();
		validUntil.set(2018, 11, 3);
		
		Assessor assessor = new Assessor(new Person("Ing.", "Rutzinger", "Kajetan", null), null, null, new Organisation("Neue Heimat Tirol", null, null));
		
		ClimateData climateData = new ClimateData();
		climateData.setIdealIndoorTemperature(new Measure(20.0, MeasuringUnit.CELCIUS));
		climateData.setAverageOutdoorTemperature(new Measure(-11.4, MeasuringUnit.CELCIUS));
		climateData.setHightAboveSeaLevel(new Measure(573.0, MeasuringUnit.METER));
		climateData.setHeatingDaysPerYear(220); 
		
		SpatialData spatialData = new SpatialData();
		spatialData.setTotalFloorArea(new Measure(800.86, MeasuringUnit.SQUARE_METER));
		spatialData.setTotalVolume(new Measure(2272.87, MeasuringUnit.CUBIC_METER));
		
		ThermalData thermalData = new ThermalData();
		thermalData.setUValue(new Measure(0.90, MeasuringUnit.WATTS_SQUARE_METER_KELVIN));
		thermalData.setPrimaryEnergyDemand(new Measure(103.91, MeasuringUnit.KWH_SQUARE_METER_YEAR));
		
		Dwelling dwelling = new Dwelling(
				new BuildingAddress(new Address("Pacherstr.", "14", null, null, "6020", "Innsbruck", "Austria"), climateData), 
				1960, 
				DwellingType.APARTMENT_BUILDING,
				"1361",
				(List<String>) Arrays.asList("1557/9", "1558/3"), 
				spatialData,
				thermalData);
		
		Rating rating = new Rating("D+", null);
		
		Software software = new Software("Allplan", "AX3000");
		Calendar releaseDate = Calendar.getInstance();
		releaseDate.set(2008, 9, 20, 0, 0, 0);
		software.setReleaseDate(releaseDate.getTime());
		RatingMethodology ratingMethodology = new RatingMethodology(null, null, null, software);
		
		EPC epc = new EPC(null, creationDate.getTime(), validUntil.getTime(), dwelling, assessor, rating, null, ratingMethodology, "ÖNORM H 5055, Rechtlinie 2002/91/EG");
		epc.setPurpose(PurposeType.RENTAL);
		
		epcCollection.insertOne(epc);
		for (EPC epcs : epcCollection.find()) {
			System.out.println(epcs.toString());
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
		creationDate.clear();
		creationDate.set(2010, 8, 29, 0, 0, 0);
		
		Calendar validUntil = Calendar.getInstance();
		validUntil.clear();
		validUntil.set(2015, 8, 29, 0, 0, 0);
		
		Organisation organisation = new Organisation("Ingeniorfirmaet Henrij Mogelgaard ApS", 
				new Address("Hillerødgade","30A", null, null, "2200", "København N", "Denmark"),
				new ContactDetails(null, "35360727"));
		Assessor assessor = new Assessor(new Person(null, "Hansen", "Jan Ole", new ContactDetails("joh@hmenergi.dk", null)), "250611", null, organisation);
		
		Address address = new Address();
		address.setStreet("Dr. Lassens Gade");
		address.setStreetNumber("7");
		address.setPostalCode("8900");
		address.setCity("Randers C");
		address.setCountry("Denmark");
		
		RatingMethodology ratingMethodology = new RatingMethodology();
		ratingMethodology.setSoftwareUsed(new Software("Energy08", "Be06 version 4"));
		
		//The energy-labelling scale runs from A to G, where A is divided into A2020, A2015 and A2010. A2020 covers low energy buildings, which only consume a minimum of energy, 
		//while G-labelled buildings consume the most energy.
		Rating awardedRating = new Rating("D", null);
		
		ThermalData thermalData = new ThermalData();
		//109509 kwh/y divided by 880 floor size = 124.44 kwh/m^2/year
		thermalData.setFinalEnergyConsumption(new Measure(124.44, MeasuringUnit.KWH_SQUARE_METER_YEAR));
		
		SpatialData spatialData = new SpatialData();
		spatialData.setTotalFloorArea(new Measure(new Double(880), MeasuringUnit.SQUARE_METER));
		//beheiztes areal TODO: check with others if volume is referred to beheiztes volumen
		spatialData.setTotalVolume(new Measure(new Double(880), MeasuringUnit.SQUARE_METER));
		
		Dwelling ratedDwelling = new Dwelling(new BuildingAddress(address, null), 1919, DwellingType.APARTMENT_BUILDING, "730-009955-001", null, spatialData, thermalData);
		
		EPC epc = new EPC("200038127", creationDate.getTime(), validUntil.getTime(), ratedDwelling, assessor, awardedRating, null, null, null);
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
		//Recommendations on renovations
		
		Calendar validUntil = Calendar.getInstance();
		validUntil.clear();
		validUntil.set(2024, 5, 23, 0, 0, 0);
		
		Calendar creationDate = Calendar.getInstance();
		creationDate.clear();
		creationDate.set(2014, 5, 24, 0, 0, 0);
		
		Assessor assessor = new Assessor(new Person(null, "Mustermann", "Paul", null), null, null,
				new Organisation("", new Address("Aussteller Musterstraße", "45", null, null, "12345", "Musterstadt", "Deutschland"), null));
		
		SpatialData spatialData = new SpatialData();
		spatialData.setTotalFloorArea(new Measure(new Double(546), MeasuringUnit.SQUARE_METER));
		
		ThermalData thermalData = new ThermalData();
		thermalData.setCarbonFootprint(new Measure(new Double(56), MeasuringUnit.KG_SQUARE_METER_YEAR));
		thermalData.setPrimaryEnergyDemand(new Measure(new Double(248), MeasuringUnit.KWH_SQUARE_METER_YEAR));
		thermalData.setFinalEnergyDemand(new Measure(new Double(222), MeasuringUnit.KWH_SQUARE_METER_YEAR));
		thermalData.setUValue(new Measure(1.11, MeasuringUnit.WATTS_SQUARE_METER_KELVIN));
		
		Dwelling ratedDwelling = new Dwelling(
				new BuildingAddress(new Address("Musterstr", "123", null, null, "10115", "Musterstadt", "Deutschland"), null), 
				1927, 
				DwellingType.APARTMENT_BUILDING,
				null,
				null, 
				spatialData,
				thermalData);
		
		//estimated rating
		Rating awardedRating = new Rating("G", null);   //here should be a distinction between estimated and measured rating
		//maybe add also a scala, to see the possible rating levels.
	
		RatingMethodology ratingMethodology = new RatingMethodology();
		ratingMethodology.setStandardName("DIN V 4108-6 and DIN 4701-10 and Vereinfachungen nach §9 Absatz 2 EnEV"); //consider creating a list
		
		EPC epc = new EPC("123456789", creationDate.getTime(), validUntil.getTime(), ratedDwelling, assessor, awardedRating, null, ratingMethodology, "§§ 16 ff. Energiesparverordnung (EnEV) 18.11.2013");
		epc.setPurpose(PurposeType.SALE);
		
		epcCollection.insertOne(epc);
		
		for (EPC epcF : epcCollection.find()) {
			System.out.println(epcF.toString());
		}
	}

	private static void addRomanianEPC(MongoCollection<EPC> epcCollection) {
		//missing fields for 
		//climatisare:0
		//ventilare mecanica: 0
		//iluminat artificial: 11.88
		//surse regenerabile: 0
		//rating per energy source type (heating: B, water: C, lighting: A)
		
		Calendar creationDate = Calendar.getInstance();
		creationDate.clear();
		creationDate.set(2016, 7, 2, 0, 0, 0);
		
		Calendar validUntil = Calendar.getInstance();
		validUntil.clear();
		validUntil.set(2026, 7, 2, 0, 0, 0);
		
		Rating rating = new Rating("B", 97.3);
		
		ThermalData thermalData = new ThermalData();
		thermalData.setUValue(new Measure(0.90, MeasuringUnit.WATTS_SQUARE_METER_KELVIN));
		
		thermalData.setFinalEnergyDemand(new Measure(150.83, MeasuringUnit.KWH_SQUARE_METER_YEAR));
		thermalData.setSpaceHeatingEnergyDemand(new Measure(97.92, MeasuringUnit.KWH_SQUARE_METER_YEAR));
		thermalData.setWaterHeatingEnergyDemand(new Measure(41.04, MeasuringUnit.KWH_SQUARE_METER_YEAR));
		thermalData.setCarbonFootprint(new Measure(29.55, MeasuringUnit.KG_SQUARE_METER_YEAR));
		
		SpatialData spatialData = new SpatialData();
		spatialData.setTotalFloorArea(new Measure(45.95, MeasuringUnit.SQUARE_METER));
		spatialData.setTotalVolume(new Measure(121.77, MeasuringUnit.CUBIC_METER));
		spatialData.setOrientation("SE-SV");
		
		Assessor assessor = new Assessor(new Person("I-CI", "Rotaru", "Nicolae Mihai", null), "UA-01579", null, null);
		
		Dwelling dwelling = new Dwelling(
				new BuildingAddress(new Address("Stejarului", "60", "1", "13", "407280", "Floresti", "Romania"), null), 
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
		
		EPC epc = new EPC("UA-01579 365", creationDate.getTime(), validUntil.getTime(), dwelling, assessor, rating, null, ratingMethodology, null);
		epc.setPurpose(PurposeType.SALE);
		
		epcCollection.insertOne(epc);
		
		for (EPC epcF : epcCollection.find()) {
			System.out.println(epcF.toString());
		}
    }
}
