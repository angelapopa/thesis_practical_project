package at.uibk.epc.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import at.uibk.epc.model.BuildingAddress;
import at.uibk.epc.model.Dwelling;
import at.uibk.epc.model.DwellingType;
import at.uibk.epc.model.EPC;
import at.uibk.epc.model.FuelType;
import at.uibk.epc.model.Measure;
import at.uibk.epc.model.MeasuringUnit;
import at.uibk.epc.model.PurposeType;
import at.uibk.epc.model.Rating;
import at.uibk.epc.model.SpatialData;
import at.uibk.epc.model.ThermalData;

/**
 * Import of cvs data for England and Wales.
 * 
 * Description of properties https://epc.opendatacommunities.org/docs/guidance
 */

public class OpenDataCommunitiesImporter {

	private static Boolean DRY_RUN = false;

	/**
	 * England & Wales, not all fit into 1 cluster
	 * 
	 * Version 1.0 with double values:
	 * 749500 were imported, until domestic-E06000018-Nottingham (incomplete)
	 * 
	 * Version 2.0 with integer values and importing every 10th valid entry:
	 * 759464 were imported, until domestic-E07000142-West-Lindsey (incomplete)
	 */	
	private static final String DB_NAME = "EPC";
	private static final String ENGLAND_COLLECTION_NAME = "EPC_England";
	
	// first import with Double Type values 
	// private static final String
	// ENGLAND_CONNECTION_STRING="mongodb+srv://epcuser4:pw14epc@cluster0-1w0r9.mongodb.net/test?retryWrites=true&w=majority";
	
	//second import with integer values
	private static final String ENGLAND_CONNECTION_STRING = "mongodb+srv://admin:admin@cluster0.ojmf2.mongodb.net/EPC?retryWrites=true&w=majority";

	private static String OPENDATACOMMUNITIES_CSV_FOLDER = "e:\\docs\\Uni Innsbruck\\Masterthesis\\datasets\\opendatacommunities\\all-domestic-certificates\\";
	private static String CVS_FILE = "certificates.csv";
	private static String COUNTRY = "England"; // England & Wales

	/**
	 * CVS column names. The order counts for extracting the correct data field
	 * values.
	 */
	private enum CvsHeader {
		LMK_KEY("LMK_KEY"), ADDRESS1("ADDRESS1"), ADDRESS2("ADDRESS2"), ADDRESS3("ADDRESS3"), POSTCODE("POSTCODE"),
		BUILDING_REFERENCE_NUMBER("BUILDING_REFERENCE_NUMBER"), 
		CURRENT_ENERGY_RATING("CURRENT_ENERGY_RATING"),
		POTENTIAL_ENERGY_RATING("POTENTIAL_ENERGY_RATING"),
		/*
		 * Based on cost of energy, i.e. energy required for space heating, water
		 * heating and lighting [in kWh/year] multiplied by fuel costs. (£/m²/year where
		 * cost is derived from kWh).
		 */
		CURRENT_ENERGY_EFFICIENCY_POINTS("CURRENT_ENERGY_EFFICIENCY"),
		POTENTIAL_ENERGY_EFFICIENCY_POINTS("POTENTIAL_ENERGY_EFFICIENCY"), PROPERTY_TYPE("PROPERTY_TYPE"),
		BUILD_FORM("BUILD_FORM"), INSPECTION_DATE("INSPECTION_DATE"), LOCAL_AUTHORITY("LOCAL_AUTHORITY"),
		CONSTITUENCY("CONSTITUENCY"), COUNTY("COUNTY"), LODGEMENT_DATE("LODGEMENT_DATE"),
		TRANSACTION_TYPE("TRANSACTION_TYPE"),
		/*
		 * The Environmental Impact Rating. A measure of the property's current impact
		 * on the environment in terms of carbon dioxide (CO2) emissions. The higher the
		 * rating the lower the CO2 emissions. (CO2 emissions in tonnes / year)
		 */
		ENVIRONMENTAL_IMPACT_CURRENT("ENVIRONMENTAL_IMPACT_CURRENT"),
		ENVIRONMENTAL_IMPACT_POTENTIAL("ENVIRONMENTAL_IMPACT_POTENTIAL"),
		/*
		 * Estimated total energy consumption for the Property in a 12 month period.
		 * Value is Kilowatt Hours per Square Metre (kWh/m²)
		 */
		ENERGY_CONSUMPTION_CURRENT("ENERGY_CONSUMPTION_CURRENT"),
		ENERGY_CONSUMPTION_POTENTIAL("ENERGY_CONSUMPTION_POTENTIAL"),
		/*
		 * CO2 emissions per year in tonnes/year.
		 */
		CO2_EMISSIONS_CURRENT("CO2_EMISSIONS_CURRENT"),
		/*
		 * CO2 emissions per square meter floor area per year in kg/m².
		 */
		CO2_EMISS_CURR_PER_FLOOR_AREA("CO2_EMISS_CURR_PER_FLOOR_AREA"),
		CO2_EMISSIONS_POTENTIAL("CO2_EMISSIONS_POTENTIAL"), LIGHTING_COST_CURRENT("LIGHTING_COST_CURRENT"),
		LIGHTING_COST_POTENTIAL("LIGHTING_COST_POTENTIAL"), HEATING_COST_CURRENT("HEATING_COST_CURRENT"),
		HEATING_COST_POTENTIAL("HEATING_COST_POTENTIAL"), HOT_WATER_COST_CURRENT("HOT_WATER_COST_CURRENT"),
		HOT_WATER_COST_POTENTIAL("HOT_WATER_COST_POTENTIAL"), TOTAL_FLOOR_AREA("TOTAL_FLOOR_AREA"),
		ENERGY_TARIFF("ENERGY_TARIFF"), MAINS_GAS_FLAG("MAINS_GAS_FLAG"), FLOOR_LEVEL("FLOOR_LEVEL"),
		FLAT_TOP_STOREY("FLAT_TOP_STOREY"), FLAT_STOREY_COUNT("FLAT_STOREY_COUNT"),
		MAIN_HEATING_CONTROLS("MAIN_HEATING_CONTROLS"), MULTI_GLAZE_PROPORTION("MULTI_GLAZE_PROPORTION"),
		GLAZED_TYPE("GLAZED_TYPE"), GLAZED_AREA("GLAZED_AREA"), EXTENSION_COUNT("EXTENSION_COUNT"),
		NUMBER_HABITABLE_ROOMS("NUMBER_HABITABLE_ROOMS"), NUMBER_HEATED_ROOMS("NUMBER_HEATED_ROOMS"),
		LOW_ENERGY_LIGHTING("LOW_ENERGY_LIGHTING"), NUMBER_OPEN_FIREPLACES("NUMBER_OPEN_FIREPLACES"),
		HOTWATER_DESCRIPTION("HOTWATER_DESCRIPTION"),
		/**
		 * Energy efficiency rating. One of: very good; good; average; poor; very poor.
		 * On actual energy certificate shown as one to five star rating.
		 */
		HOT_WATER_ENERGY_EFF("HOT_WATER_ENERGY_EFF"),
		/**
		 * Environmental efficiency rating. One of: very good; good; average; poor; very
		 * poor. On actual energy certificate shown as one to five star rating.
		 */
		HOT_WATER_ENV_EFF("HOT_WATER_ENV_EFF"), FLOOR_DESCRIPTION("FLOOR_DESCRIPTION"),
		/**
		 * Energy efficiency rating. One of: very good; good; average; poor; very poor.
		 * On actual energy certificate shown as one to five star rating.
		 */
		FLOOR_ENERGY_EFF("FLOOR_ENERGY_EFF"), FLOOR_ENV_EFF("FLOOR_ENV_EFF"),
		WINDOWS_DESCRIPTION("WINDOWS_DESCRIPTION"), WINDOWS_ENERGY_EFF("WINDOWS_ENERGY_EFF"),
		WINDOWS_ENV_EFF("WINDOWS_ENV_EFF"), WALLS_DESCRIPTION("WALLS_DESCRIPTION"),
		WALLS_ENERGY_EFF("WALLS_ENERGY_EFF"), WALLS_ENV_EFF("WALLS_ENV_EFF"),
		SECONDHEAT_DESCRIPTION("SECONDHEAT_DESCRIPTION"), SHEATING_ENERGY_EFF("SHEATING_ENERGY_EFF"),
		SHEATING_ENV_EFF("SHEATING_ENV_EFF"), ROOF_DESCRIPTION("ROOF_DESCRIPTION"), ROOF_ENERGY_EFF("ROOF_ENERGY_EFF"),
		ROOF_ENV_EFF("ROOF_ENV_EFF"), MAINHEAT_DESCRIPTION("MAINHEAT_DESCRIPTION"),
		/**
		 * Energy efficiency rating. One of: very good; good; average; poor; very poor.
		 * On actual energy certificate shown as one to five star rating.
		 */
		MAINHEAT_ENERGY_EFF("MAINHEAT_ENERGY_EFF"), MAINHEAT_ENV_EFF("MAINHEAT_ENV_EFF"),
		MAINHEATCONT_DESCRIPTION("MAINHEATCONT_DESCRIPTION"), MAINHEATC_ENERGY_EFF("MAINHEATC_ENERGY_EFF"),
		MAINHEATC_ENV_EFF("MAINHEATC_ENV_EFF"), LIGHTING_DESCRIPTION("LIGHTING_DESCRIPTION"),
		LIGHTING_ENERGY_EFF("LIGHTING_ENERGY_EFF"), LIGHTING_ENV_EFF("LIGHTING_ENV_EFF"),
		/**
		 * The type of fuel used to power the central heating e.g. Gas, Electricity
		 */
		MAIN_FUEL("MAIN_FUEL"), WIND_TURBINE_COUNT("WIND_TURBINE_COUNT"), HEAT_LOSS_CORRIDOOR("HEAT_LOSS_CORRIDOOR"),
		UNHEATED_CORRIDOR_LENGTH("UNHEATED_CORRIDOR_LENGTH"), FLOOR_HEIGHT("FLOOR_HEIGHT"),
		/**
		 * Percentage of photovoltaic area as a percentage of total roof area. 0%
		 * indicates that a Photovoltaic Supply is not present in the property.
		 */
		PHOTO_SUPPLY("PHOTO_SUPPLY"), SOLAR_WATER_HEATING_FLAG("SOLAR_WATER_HEATING_FLAG"),
		MECHANICAL_VENTILATION("MECHANICAL_VENTILATION"), ADDRESS("ADDRESS"),
		LOCAL_AUTHORITY_LABEL("LOCAL_AUTHORITY_LABEL"), CONSTITUENCY_LABEL("CONSTITUENCY_LABEL");

		CvsHeader(String fieldname) {
		}
	}

	public static void main(String[] args) {

		MongoDatabaseClient.dropAndCreateDB(ENGLAND_CONNECTION_STRING, DB_NAME, ENGLAND_COLLECTION_NAME);
		MongoDatabase database = MongoDatabaseClient.getDatabase(DB_NAME, ENGLAND_CONNECTION_STRING);

		System.out.println("Opendatacommunities Import - Before import: "
				+ database.getCollection(ENGLAND_COLLECTION_NAME).countDocuments());

		MongoCollection<EPC> epcCollection = database.getCollection(ENGLAND_COLLECTION_NAME, EPC.class);

		for (String folderName : new File(OPENDATACOMMUNITIES_CSV_FOLDER).list()) {
			if (!folderName.equals("LICENCE.txt")) {
				for (String cvsFileName : new File(OPENDATACOMMUNITIES_CSV_FOLDER + folderName).list()) {
					if (cvsFileName.equals(CVS_FILE)) {
						if (DRY_RUN) {
							importCSVFileOpenDataCommunities(epcCollection, folderName);
							break;
						} else
							importCSVFileOpenDataCommunities(epcCollection, folderName);
					}
				}
			}
		}
		System.out.println("Opendatacommunities Englsnd & Wales Import finished successfully.");
	}

	private static void importCSVFileOpenDataCommunities(MongoCollection<EPC> epcCollection, String folderName) {
		String csv_file_path = OPENDATACOMMUNITIES_CSV_FOLDER + folderName + File.separator + CVS_FILE;
		System.out.println("Starting import: " + csv_file_path);
		String city = folderName.substring(folderName.lastIndexOf("-") + 1);

		try {
			InputStreamReader input = new InputStreamReader((new FileInputStream(new File(csv_file_path))));
			CSVParser csvParser = CSVFormat.DEFAULT.withHeader(CvsHeader.class).withSkipHeaderRecord().parse(input);

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
							System.out.println(parseCVSInputRow(record, city).toString());
						} else {
							epcCollection.insertOne(parseCVSInputRow(record, city));
						}
						counter++;
						temp_counter = 0;
					}
				}
			}
			System.out.println("File " + csv_file_path + ": imported " + counter);
			input.close();
			System.out.println("End of import: " + csv_file_path);
		} catch (FileNotFoundException e) {
			System.out.println("File " + csv_file_path + " not found!");
		} catch (IOException e) {
			System.out.println("File " + csv_file_path + " not readable!");
		}
	}

	private static boolean validRecord(CSVRecord record) {
		String floorArea = record.get(CvsHeader.TOTAL_FLOOR_AREA);
		if ("0".equals(floorArea) || "NO DATA!".equalsIgnoreCase(floorArea) || "N/A".equals(floorArea)){
			return false;
		}
		
		String energyConsumption = record.get(CvsHeader.ENERGY_CONSUMPTION_CURRENT);
		if ("0".equals(energyConsumption) || "NO DATA!".equalsIgnoreCase(energyConsumption) || "N/A".equals(energyConsumption)) {
			return false;
		}
		String energyRating = record.get(CvsHeader.CURRENT_ENERGY_RATING);
		if("INVALID!".equals(energyRating) || "N/A".equals(energyRating)) {
			return false;
		}
		return true;
	}

	private static EPC parseCVSInputRow(CSVRecord record, String city) {
		Measure totalFloorArea = new Measure(Math.round(Double.valueOf(record.get(CvsHeader.TOTAL_FLOOR_AREA))),
				MeasuringUnit.SQUARE_METER);

		SpatialData spatialData = new SpatialData(totalFloorArea, null, null);
		Measure energyDemand = new Measure(Math.round(Double.valueOf(record.get(CvsHeader.ENERGY_CONSUMPTION_CURRENT))),
				MeasuringUnit.KWH_SQUARE_METER_YEAR);

		ThermalData thermalData = new ThermalData();
		thermalData.setMainHeatingFuelType(getFuelType(record.get(CvsHeader.MAIN_FUEL)));
		thermalData.setFinalEnergyDemand(energyDemand);
		thermalData.setCarbonFootprint(new Measure(Math.round(Double.valueOf(record.get(CvsHeader.CO2_EMISS_CURR_PER_FLOOR_AREA))),
				MeasuringUnit.KG_SQUARE_METER_YEAR));

		Rating awardedRating = new Rating(record.get(CvsHeader.CURRENT_ENERGY_RATING),
				Integer.valueOf(record.get(CvsHeader.CURRENT_ENERGY_EFFICIENCY_POINTS)));

		Rating potentialRating = new Rating(record.get(CvsHeader.POTENTIAL_ENERGY_RATING),
				Integer.valueOf(record.get(CvsHeader.POTENTIAL_ENERGY_EFFICIENCY_POINTS)));

		String county = !record.get(CvsHeader.COUNTY).isEmpty() ? record.get(CvsHeader.COUNTY) : city;

		BuildingAddress buildingAddress = parseSimpleStreetDetails(new BuildingAddress(), record);
		buildingAddress.setPostalCode(record.get(CvsHeader.POSTCODE));
		buildingAddress.setCity(county);
		buildingAddress.setCountry(COUNTRY);

		Dwelling ratedDwelling = new Dwelling(buildingAddress, null,
				parseDwellingType(record.get(CvsHeader.PROPERTY_TYPE)), record.get(CvsHeader.BUILDING_REFERENCE_NUMBER),
				null, spatialData, thermalData);

		EPC epc = new EPC(record.get(CvsHeader.LMK_KEY), parseCreationDate(record.get(CvsHeader.INSPECTION_DATE)), null,
				ratedDwelling, null, awardedRating, potentialRating, null, null);

		epc.setPurpose(PurposeType.approximateValue(record.get(CvsHeader.TRANSACTION_TYPE)));
		return epc;
	}

	private static Date parseCreationDate(String parsableCreationDate) {
		try {
			if (parsableCreationDate.contains("-")) {
				return new SimpleDateFormat("yyyy-MM-dd").parse(parsableCreationDate);
			} else {
				return new SimpleDateFormat("dd.MM.yyyy").parse(parsableCreationDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// simple, no parsing, all address data into one field: street
	static BuildingAddress parseSimpleStreetDetails(BuildingAddress buildingAddress, CSVRecord record) {
		buildingAddress.setStreet(record.get(CvsHeader.ADDRESS1) + " " + record.get(CvsHeader.ADDRESS2));
		return buildingAddress;
	}

	// to complicated
	static BuildingAddress parseStreetDetails(BuildingAddress buildingAddress, CSVRecord record) {
		String streetAndStreetnumber = record.get(CvsHeader.ADDRESS1);
		if (streetAndStreetnumber.contains(",")) {
			parseAddress(buildingAddress, streetAndStreetnumber);
		} else {
			parseAddress(buildingAddress, record.get(CvsHeader.ADDRESS2));
		}
		if (buildingAddress.getStreet() == null && buildingAddress.getStreetNumber() == null) {
			buildingAddress.setStreetNumber(record.get(CvsHeader.ADDRESS1));
			buildingAddress.setStreet(record.get(CvsHeader.ADDRESS2));
		}
		return buildingAddress;
	}

	private static void parseAddress(BuildingAddress buildingAddress, String streetAndStreetnumber) {
		if (streetAndStreetnumber.contains(",")) {
			buildingAddress.setStreetNumber(streetAndStreetnumber.substring(0, streetAndStreetnumber.indexOf(",")));
			buildingAddress.setStreet(streetAndStreetnumber.substring(streetAndStreetnumber.indexOf(",") + 2));
		}
	}

	static DwellingType parseDwellingType(String dwellingType) {
		return DwellingType.approximateValue(dwellingType.toUpperCase());
	}

	static FuelType getFuelType(String fuelType) {
		return FuelType.approximateValue(fuelType);
	}
}