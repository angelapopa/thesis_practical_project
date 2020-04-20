package at.uibk.epc.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import at.uibk.epc.model.BuildingAddress;
import at.uibk.epc.model.ClimateData;
import at.uibk.epc.model.Dwelling;
import at.uibk.epc.model.DwellingType;
import at.uibk.epc.model.EPC;
import at.uibk.epc.model.FuelType;
import at.uibk.epc.model.Measure;
import at.uibk.epc.model.MeasuringUnit;
import at.uibk.epc.model.Rating;
import at.uibk.epc.model.SpatialData;
import at.uibk.epc.model.ThermalData;

/**
 * Importer for CVS data of EuropeanData for France 1048576+ records
 * https://data.ademe.fr/datasets/dpe-des-logements
 * 
 * The original file was split in other files to be able to be imported.
 * The France epc datafields are not that divers.
 *
 */
public class EuropeanOpenDataFranceImporter {

	//may be overwritten by main args
	private static Boolean DRY_RUN = true;

	private static String CVS_FILE_PATH = "e:\\docs\\Uni Innsbruck\\Masterthesis\\datasets\\European open data\\France\\";
	private static String ORIGINAL_CVS_FILE = "DPE_logements.csv";
	private static String COUNTRY = "France";

	/**
	 * CVS column names. The order counts for extracting the correct data field
	 * values.
	 */
	private enum CvsHeader {
		//						
		IDENTIFICATION_NR("numero_dpe"),
		MODEL_ID("tr001_modele_dpe_id"),
		ENERGY_CONSUMPTION("consommation_energie"),
		ENERGY_EFFICIENCY_RATING_BAND("classe_consommation_energie"),
		ESTIMATION_GAS("estimation_ges"),
		GAS_EFFICIENCY_RATING_BAND("classe_estimation_ges"),
		BUILDING_TYPE_ID("tr002_type_batiment_id"), //values: 1, 2.
		CONSTRUCTION_YEAR("annee_construction"),
		FLOOR_AREA("surface_habitable"),
		DEPARTMENT_ID("tv016_departement_id"),
		CITY("commune"),
		POSTCODE("code_insee_commune_actualise"),
		shon("shon"), //????
		CREATION_DATE("date_reception_dpe"),
		HEATING_NEED("besoin_chauffage"),
		ENVELOPE_PERFECTION("deperdition_enveloppe"),
		AIR_LOSS("deperdition_renouvellement_air"),
		LACK_OF_ACTIVITY_CLASS("tv026_classe_inertie_id"), //???
		ALTITUDE("altitude"),
		LEVEL_NR("nombre_niveau"), //??
		AVERAGE("hsp_moyenne"); //??
		
		private String description;

		CvsHeader(String description) {
			this.description = description;
		}
	}

	public static void main(String[] args) {
		
		DRY_RUN = (args != null && args[0] != null) ? Boolean.parseBoolean(args[0]) : DRY_RUN ;

		MongoDatabase database = MongoDatabaseClient.getDatabase();

		System.out.println("UK Import - Before import: " + database.getCollection("EPC_Collection").countDocuments());

		MongoCollection<EPC> epcCollection = database.getCollection("EPC_Collection", EPC.class);

		for (String filename : new File(CVS_FILE_PATH).list()) {
			if (!filename.equals(ORIGINAL_CVS_FILE)){
				System.out.println("Importing file " + CVS_FILE_PATH + filename);
				importFile(filename, epcCollection);
			}
		}	
	}
	
	private static void importFile(String filename, MongoCollection<EPC> epcCollection) {
		try {
			InputStreamReader input = new InputStreamReader((new FileInputStream(new File(CVS_FILE_PATH + filename))));
			CSVParser csvParser = CSVFormat.EXCEL.withDelimiter(';').withHeader(CvsHeader.class).withSkipHeaderRecord().parse(input);

			long counter = 0;
			for (CSVRecord record : csvParser) {
				if (DRY_RUN) {
					System.out.println(parseCVSInputRow(record).toString());
				} else {
					epcCollection.insertOne(parseCVSInputRow(record));
				}
				counter++;
			}
			System.out.println("File " + CVS_FILE_PATH + filename + ": imported " + counter);
			input.close();
			System.out.println("UK EuropeanOpenData Import finished successfully.");
		} catch (FileNotFoundException e) {
			System.out.println("File " + CVS_FILE_PATH + filename + " not found!");
		} catch (IOException e) {
			System.out.println("File " + CVS_FILE_PATH + filename + " not readable!");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private static EPC parseCVSInputRow(CSVRecord record) throws ParseException {

		SpatialData spatialData = new SpatialData();
		spatialData.setTotalFloorArea(
				new Measure(Double.valueOf(record.get(CvsHeader.FLOOR_AREA).replace(",", "")), MeasuringUnit.SQUARE_METER));

		ThermalData thermalData = new ThermalData();
		thermalData.setThermalEnergyDemand(
				new Measure(Double.valueOf(record.get(CvsHeader.HEATING_NEED)), MeasuringUnit.KWH_YEAR));
		thermalData.setFinalEnergyConsumption(new Measure(Double.valueOf(record.get(CvsHeader.ENERGY_CONSUMPTION)), MeasuringUnit.KWH_YEAR));

		ClimateData climateData = new ClimateData(null, null, new Measure(Double.valueOf(record.get(CvsHeader.ALTITUDE)), MeasuringUnit.METER), null, 0, 0);
		
		BuildingAddress buildingAddress = new BuildingAddress();
		buildingAddress.setCity(record.get(CvsHeader.CITY));
		buildingAddress.setPostalCode(record.get(CvsHeader.POSTCODE));
		buildingAddress.setCountry(COUNTRY);
		buildingAddress.setClimateData(climateData);

		Dwelling ratedDwelling = new Dwelling(buildingAddress, Integer.parseInt(record.get(CvsHeader.CONSTRUCTION_YEAR)), estimateDwellingType(record.get(CvsHeader.BUILDING_TYPE_ID)), null, null, spatialData,
				thermalData);

		Date creationDate = getCreationDate(record.get(CvsHeader.CREATION_DATE));

		Rating awardedRating = new Rating(record.get(CvsHeader.ENERGY_EFFICIENCY_RATING_BAND), null);

		EPC epc = new EPC(record.get(CvsHeader.IDENTIFICATION_NR), creationDate, null, ratedDwelling, null, awardedRating, null, null, null);
		return epc;
	}

	/**
	 * It is guessed, no real clue what 1 or 2 actually means
	 * 1 -> house
	 * 2 -> flat
	 */
	private static DwellingType estimateDwellingType(String buildingType) {
		return (buildingType.equals("1")) ? DwellingType.HOUSE : DwellingType.FLAT;
	}

	static Date getCreationDate(String csvDateString) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.FRENCH);
		return dateFormat.parse(csvDateString);
	}

	static FuelType getFuelType(String fuelType) {
		return FuelType.approximateValue(fuelType);
	}
}
