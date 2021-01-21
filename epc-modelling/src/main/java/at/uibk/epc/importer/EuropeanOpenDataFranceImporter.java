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
 * The original file was split in other files to be able to be imported. The
 * France epc datafields are not that divers.
 *
 */
public class EuropeanOpenDataFranceImporter {

	private static Boolean DRY_RUN = false;

	private static final String DB_NAME = "EPC";
	private static final String FRANCE_COLLECTION_NAME = "EPC_France";

	// France, not all fit into 1 cluster

	// first import, with double values
	// until Kopie-007.csv were imported, and partly Kopie-008.csv, total of 881180
	// entries
	// private static final String
	// FRANCE_CONNECTION_STRING="mongodb+srv://epcuser5:pw15epc@cluster0-929id.mongodb.net/test?retryWrites=true&w=majority";

	// second import, with integer values
	// 368422 entries, all csv files were imported, every 10th entry.
	private static final String FRANCE_CONNECTION_STRING = "mongodb+srv://fra_1:Zg8RMRof0PiGOILE@cluster0.o03xt.mongodb.net/EPC?retryWrites=true&w=majority";

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
		BUILDING_TYPE_ID("tr002_type_batiment_id"), // values: 1, 2.
		CONSTRUCTION_YEAR("annee_construction"), 
		FLOOR_AREA("surface_habitable"), 
		DEPARTMENT_ID("tv016_departement_id"),
		CITY("commune"), 
		POSTCODE("code_insee_commune_actualise"), 
		shon("shon"), // ????
		CREATION_DATE("date_reception_dpe"), 
		HEATING_NEED("besoin_chauffage"),
		ENVELOPE_PERFECTION("deperdition_enveloppe"), 
		AIR_LOSS("deperdition_renouvellement_air"),
		LACK_OF_ACTIVITY_CLASS("tv026_classe_inertie_id"), // ???
		ALTITUDE("altitude"), 
		LEVEL_NR("nombre_niveau"), // ??
		AVERAGE("hsp_moyenne"); // ??

		private String description;

		CvsHeader(String description) {
			this.description = description;
		}
	}

	public static void main(String[] args) {

		// MongoDatabaseClient.dropAndCreateDB(FRANCE_CONNECTION_STRING, DB_NAME,
		// FRANCE_COLLECTION_NAME);
		MongoDatabase database = MongoDatabaseClient.getDatabase(DB_NAME, FRANCE_CONNECTION_STRING);

		System.out.println(
				"France Import - Before import: " + database.getCollection(FRANCE_COLLECTION_NAME).countDocuments());

		MongoCollection<EPC> epcCollection = database.getCollection(FRANCE_COLLECTION_NAME, EPC.class);

		for (String filename : new File(CVS_FILE_PATH).list()) {
			if (!filename.equals(ORIGINAL_CVS_FILE)) {
				System.out.println("Importing file " + CVS_FILE_PATH + filename);
				importFile(filename, epcCollection);
			}
		}
	}

	private static void importFile(String filename, MongoCollection<EPC> epcCollection) {
		try {
			InputStreamReader input = new InputStreamReader((new FileInputStream(new File(CVS_FILE_PATH + filename))));
			CSVParser csvParser = CSVFormat.EXCEL.withDelimiter(';').withHeader(CvsHeader.class).withSkipHeaderRecord()
					.parse(input);

			long counter = 0;
			int temp_counter = 0;
			/**
			 * import every 10th valid csv entry
			 */
			for (CSVRecord record : csvParser) {
				if (validRecord(record)) {
					temp_counter = temp_counter + 1;
					if (temp_counter == 10) {
						if (DRY_RUN) {
							System.out.println(parseCVSInputRow(record).toString());
						} else {
							try {
								epcCollection.insertOne(parseCVSInputRow(record));
							} catch (NumberFormatException ex) {
								// do nothing, skip insert of entries with missing or fault data
								System.out.println("Skipping this entry row");
								System.out.println(ex.getLocalizedMessage());
								System.out.println("Continuing import ... ");
							}
						}
						counter++;
						temp_counter = 0;
					}
				}
			}
			System.out.println("File " + CVS_FILE_PATH + filename + ": imported " + counter);
			input.close();
			System.out.println("France Import finished successfully.");
		} catch (FileNotFoundException e) {
			System.out.println("File " + CVS_FILE_PATH + filename + " not found!");
		} catch (IOException e) {
			System.out.println("File " + CVS_FILE_PATH + filename + " not readable!");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean validRecord(CSVRecord record) {
		String floorArea = record.get(CvsHeader.FLOOR_AREA);
		if ("0".equals(floorArea) || "NO DATA!".equalsIgnoreCase(floorArea) || "N/A".equals(floorArea) || "".equals(floorArea.trim())){
			return false;
		}
		
		String energyConsumption = record.get(CvsHeader.ENERGY_CONSUMPTION);
		if ("0".equals(energyConsumption) || "NO DATA!".equalsIgnoreCase(energyConsumption) || "N/A".equals(energyConsumption) || "".equals(energyConsumption.trim())) {
			return false;
		}
		String energyRating = record.get(CvsHeader.ENERGY_EFFICIENCY_RATING_BAND);
		if("INVALID!".equals(energyRating) || "N/A".equals(energyRating) || "N".equals(energyRating)) {
			return false;
		}
		return true;
	}

	private static EPC parseCVSInputRow(CSVRecord record) throws ParseException {

		SpatialData spatialData = new SpatialData();
		spatialData.setTotalFloorArea(new Measure(Math.round(Double.valueOf(record.get(CvsHeader.FLOOR_AREA).replace(",", ""))),
				MeasuringUnit.SQUARE_METER));

		ThermalData thermalData = new ThermalData();
		thermalData.setThermalEnergyDemand(
				new Measure(Math.round(Double.valueOf(record.get(CvsHeader.HEATING_NEED))), MeasuringUnit.KWH_YEAR));
		thermalData.setFinalEnergyConsumption(
				new Measure(Math.round(Double.valueOf(record.get(CvsHeader.ENERGY_CONSUMPTION))), MeasuringUnit.KWH_YEAR));

		ClimateData climateData = new ClimateData(null, null,
				new Measure(Math.round(Double.valueOf(record.get(CvsHeader.ALTITUDE))), MeasuringUnit.METER), null, 0, 0);

		BuildingAddress buildingAddress = new BuildingAddress();
		buildingAddress.setCity(record.get(CvsHeader.CITY));
		buildingAddress.setPostalCode(record.get(CvsHeader.POSTCODE));
		buildingAddress.setCountry(COUNTRY);
		buildingAddress.setClimateData(climateData);

		Dwelling ratedDwelling = new Dwelling(buildingAddress,
				Integer.parseInt(record.get(CvsHeader.CONSTRUCTION_YEAR)),
				estimateDwellingType(record.get(CvsHeader.BUILDING_TYPE_ID)), null, null, spatialData, thermalData);

		Date creationDate = getCreationDate(record.get(CvsHeader.CREATION_DATE));

		Rating awardedRating = new Rating(record.get(CvsHeader.ENERGY_EFFICIENCY_RATING_BAND), null);

		EPC epc = new EPC(record.get(CvsHeader.IDENTIFICATION_NR), creationDate, null, ratedDwelling, null,
				awardedRating, null, null, null);
		return epc;
	}

	/**
	 * It is guessed, no real clue what 1 or 2 actually means 1 -> house 2 -> flat
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
