package at.uibk.epc.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import at.uibk.epc.model.Address;
import at.uibk.epc.model.BuildingAddress;
import at.uibk.epc.model.Dwelling;
import at.uibk.epc.model.DwellingType;
import at.uibk.epc.model.EPC;
import at.uibk.epc.model.FuelType;
import at.uibk.epc.model.Measure;
import at.uibk.epc.model.MeasuringUnit;
import at.uibk.epc.model.Rating;
import at.uibk.epc.model.RatingMethodology;
import at.uibk.epc.model.SpatialData;
import at.uibk.epc.model.ThermalData;

/**
 * Importer for Scottland's epc cvs data.
 * 
 * Ratings: the cvs raw data includes also potential rating and potential
 * savings, which is currently not imported.
 * 
 * Dwelling types: the cvs data makes distinction between the different house
 * types (detached, semi-detached, mid-terrace). All are imported under the
 * generic house dwelling type. Same goes for flats (ground-floor, mid-floor,
 * top-floor) are imported under the generic flat dwelling type.
 * 
 * Energy demand: the cvs raw data makes a differentiation between energy demand
 * for space heating and for water heating. The importer sums this values.
 */

public class ScottlandEPCImporter {

	private static Boolean DRY_RUN = false;

	private static String SCOTTLAND_COLLECTION_NAME = "EPC_Scottland";
	private static final String DB_NAME = "EPC";

	// Scottland, not all fit into 1 cluster,
	
	// first import with Double type values
	// imported 701461 records, until D EPC data 2016 Q2 extract0517.csv
	// private static final String SCOTTLAND_CONNECTION_STRING = "mongodb+srv://epcuser3:pw13epc@cluster0-aivtv.mongodb.net/test?retryWrites=true&w=majority";
	
	// second import with integer values, 
	// imported 300871 records, from all the cvs files, where every 3th cvs entry was imported
	private static final String SCOTTLAND_CONNECTION_STRING = "mongodb+srv://scott_1:JP3JXGuQQC9fkPiZ@cluster0.licv8.mongodb.net/EPC?retryWrites=true&w=majority";

	private static String SCOTTLAND_CSV_FOLDER = "e:\\docs\\Uni Innsbruck\\Masterthesis\\datasets\\Scottland\\D EPC data extract0517";
	private static String COUNTRY = "Scottland";

	/**
	 * CVS column names. The order counts for extracting the correct data field
	 * values.
	 */
	private enum CvsHeader {
		POSTCODE("Postcode Sector"), DWELLING_TYPE("Dwelling Type"), DATE_OF_ASSESSMENT("Date Of Assessment"),
		TYPE_OF_ASSESSMENT("Type of Assessment"), DATE_OF_CERTIFICATE("Date of Certificate"),
		PRIMARY_ENERGY_INDICATOR("Primary Energy Indicator (kWh/m²/year)"), TOTAL_FLOOR_AREA("Total floor area (m²)"),
		TOTAL_CURRENT_ENERGY_COSTS_O3Y("Total current energy costs over 3 years (£)"),
		POTENTIAL_FUTURE_SAVINGS_O3Y("Potential future savings over 3 years (£)"),
		CURRENT_ENERGY_EFFICIENCY_RATING("Current energy efficiency rating"),
		CURRENT_ENERGY_EFFICIENCY_RATING_BAND("Current energy efficiency rating band"),
		POTENTIAL_ENERGY_EFFICIENCY_RATING("Potential Energy Efficiency Rating"),
		POTENTIAL_ENERGY_EFFICIENCY_RATING_BAND("Potential energy efficiency rating band"),
		CURRENT_ENVIRONMENTAL_IMPACT_RATING("Current Environmental Impact Rating"),
		CURRENT_ENVIRONMENTAL_IMPACT_RATING_BAND("Current Environmental Impact Rating Band"),
		POTENTIAL_ENVIRONMENTAL_IMPACT_RATING("Potential Environmental Impact Rating"),
		POTENTIAL_ENVIRONMENTAL_IMPACT_RATING_BAND("Potential Environmental Impact Rating Band"),
		IMPROVEMENTS("Improvements"), WALL_SUMMARY("Wall Summary"), ROOF_SUMMARY("Roof Summary"),
		FLOOR_SUMMARY("Floor Summary"), WINDOWS_SUMMARY("Window Summary"), MAIN_HEATING_SUMMARY("Main Heating Summary"),
		MAIN_HEATING_CONTROLS_SUMMARY("Main Heating Controls Summary"),
		SECOND_HEATING_SUMMARY("Secondary Heating Summary"), HOT_WATER_SUMMARY("Hot Water Summary"),
		LIGHTING_SUMMARY("Lighting Summary"), AIR_TIGHTNESS_SUMMARY("Air Tightness Summary"),
		CURRENT_EMISSIONS("Current Emissions (T.CO2/yr)"),
		POTENTIAL_REDUCTION_EMISSIONS("Potential Reduction in Emissions (T.CO2/yr)"),
		CURRENT_HEATING_COSTS_O3Y("Current heating costs over 3 years (£)"),
		POTENTIAL_HEATING_COSTS_O3Y("Potential heating costs over 3 years (£)"),
		CURRENT_HOT_WATER_COSTS_O3Y("Current hot water costs over 3 years (£)"),
		POTENTIAL_HOT_WATER_COSTS_O3Y("Potential hot water costs over 3 years (£)"),
		CURRENT_LIGHTING_COSTS_O3Y("Current lighting costs over 3 years (£)"),
		POTENTIAL_LIGHTING_COSTS_O3Y("Potential lighting costs over 3 years (£)"),
		TOTAL_CURRENT_ENERGY_COSTS_O3Y_1("Total current energy costs over 3 years (£)_1"),
		TOTAL_POTENTIAL_ENERGY_COST_A3Y("Total potential energy cost after 3 years (£)"),
		POTENTIAL_FUTURE_SAVINGS_O3Y_2("Potential future savings over 3 years (£)_2"),
		ALTERNATIVE_MEASURES_2("Alternative Measures 2"), LZC_Energy_Source("LZC Energy Source"),
		SPACE_HEATING_EXISTING_DWELLING("Space Heating Existing Dwelling"), WATER_HEATING("Water Heating"),
		IMPACT_LOFT_INSULATION("Impact Of Loft Insulation"),
		IMPACT_CAVITY_WALL_INSULATION("Impact Of Cavity Wall Insulation"),
		IMPACT_SOLID("Impact Of Solid Wall Insulation"), ADDENDUM_TEXT("Addendum Text"), MAIN_HEATING("Main Heating"),
		MAIN_HEATING_FUEL_Type("Main Heating Fuel Type");

		CvsHeader(String fieldname) {
		}

	}

	public static void main(String[] args) {

		MongoDatabaseClient.dropAndCreateDB(SCOTTLAND_CONNECTION_STRING, DB_NAME, SCOTTLAND_COLLECTION_NAME);
		MongoDatabase database = MongoDatabaseClient.getDatabase(DB_NAME, SCOTTLAND_CONNECTION_STRING);

		System.out.println("Scottland Import - Before import: "
				+ database.getCollection(SCOTTLAND_COLLECTION_NAME).countDocuments());

		MongoCollection<EPC> epcCollection = database.getCollection(SCOTTLAND_COLLECTION_NAME, EPC.class);

		for (String csvFileName : new File(SCOTTLAND_CSV_FOLDER).list()) {
			if (DRY_RUN) {
				importCSVFileScottland(epcCollection, csvFileName);
				break;
			} else
				importCSVFileScottland(epcCollection, csvFileName);
		}
		System.out.println("Scottland Import finished successfully.");
	}

	private static void importCSVFileScottland(MongoCollection<EPC> epcCollection, String csvFileName) {
		System.out.println(csvFileName);
		String csv_file_path = SCOTTLAND_CSV_FOLDER + File.separator + csvFileName;

		try {
			InputStreamReader input = new InputStreamReader((new FileInputStream(new File(csv_file_path))));
			CSVParser csvParser = CSVFormat.DEFAULT.withHeader(CvsHeader.class).withSkipHeaderRecord().parse(input);

			long counter = 0;
			int temp_counter = 0;
			/**
			 * import every 3th valid csv entry
			 */
			for (CSVRecord record : csvParser) {
				if (validRecord(record)) {
					temp_counter = temp_counter + 1;
					if (temp_counter == 3) {
						if (DRY_RUN) {
							System.out.println(parseCVSInputRow(record).toString());
						} else {
							epcCollection.insertOne(parseCVSInputRow(record));
						}
						counter++;
						temp_counter = 0;
					}
				}
			}
			System.out.println("File " + csvFileName + ": imported " + counter);
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("File " + csv_file_path + " not found!");
		} catch (IOException e) {
			System.out.println("File " + csv_file_path + " not readable!");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private static boolean validRecord(CSVRecord record) {
		String floorArea = record.get(CvsHeader.TOTAL_FLOOR_AREA);
		if ("0".equals(floorArea) || "NO DATA!".equalsIgnoreCase(floorArea) || "N/A".equals(floorArea)) {
			return false;
		}

		String energyConsumption = record.get(CvsHeader.PRIMARY_ENERGY_INDICATOR);
		if ("0".equals(energyConsumption) || "NO DATA!".equalsIgnoreCase(energyConsumption)
				|| "N/A".equals(energyConsumption)) {
			return false;
		}
		String energyRating = record.get(CvsHeader.CURRENT_ENERGY_EFFICIENCY_RATING_BAND);
		if ("INVALID!".equals(energyRating) || "N/A".equals(energyRating)) {
			return false;
		}
		return true;
	}

	private static EPC parseCVSInputRow(CSVRecord record) throws ParseException {

		Measure totalFloorArea = new Measure(Math.round(Double.valueOf(record.get(CvsHeader.TOTAL_FLOOR_AREA))),
				MeasuringUnit.SQUARE_METER);

		SpatialData spatialData = new SpatialData(totalFloorArea, null, null);

		ThermalData thermalData = new ThermalData();

		thermalData.setMainHeatingFuelType(getFuelType(record.get(CvsHeader.MAIN_HEATING_FUEL_Type)));

		if (!record.get(CvsHeader.SPACE_HEATING_EXISTING_DWELLING).isEmpty()) {
			Measure spaceHeatingEnergyDemand = new Measure(
					Math.round(Double.valueOf(record.get(CvsHeader.SPACE_HEATING_EXISTING_DWELLING))),
					MeasuringUnit.KWH_YEAR);
			thermalData.setSpaceHeatingEnergyDemand(spaceHeatingEnergyDemand);
		}
		if (!record.get(CvsHeader.WATER_HEATING).isEmpty()) {
			Measure waterHeatingEnergyDemand = new Measure(
					Math.round(Double.valueOf(record.get(CvsHeader.WATER_HEATING))), MeasuringUnit.KWH_YEAR);
			thermalData.setWaterHeatingEnergyDemand(waterHeatingEnergyDemand);
		}
		if (!record.get(CvsHeader.PRIMARY_ENERGY_INDICATOR).isEmpty()) {
			Measure primaryEnergyDemand = new Measure(
					Math.round(Double.valueOf(record.get(CvsHeader.PRIMARY_ENERGY_INDICATOR))),
					MeasuringUnit.KWH_SQUARE_METER_YEAR);

			thermalData.setPrimaryEnergyDemand(primaryEnergyDemand);
		}

		// TODO: smth is wrong with the cvs read of current_emissions, workaround * 10
		// TODO: recheck, open file with text editor and check the real data, since it
		// might be a wrong representation of the excel programm
		thermalData.setCarbonFootprint(
				new Measure(Math.round(Double.valueOf(record.get(CvsHeader.CURRENT_EMISSIONS))) * 10,
						MeasuringUnit.KG_SQUARE_METER_YEAR));

		Rating awardedRating = new Rating(record.get(CvsHeader.CURRENT_ENERGY_EFFICIENCY_RATING_BAND),
				Integer.valueOf(record.get(CvsHeader.CURRENT_ENERGY_EFFICIENCY_RATING)));

		Rating potentialRating = new Rating(record.get(CvsHeader.POTENTIAL_ENERGY_EFFICIENCY_RATING_BAND),
				Integer.valueOf(record.get(CvsHeader.POTENTIAL_ENERGY_EFFICIENCY_RATING)));

		RatingMethodology ratingMethodology = new RatingMethodology(
				parseStandardName(record.get(CvsHeader.TYPE_OF_ASSESSMENT)), record.get(CvsHeader.TYPE_OF_ASSESSMENT),
				null, null);

		// TODO: climateData: if relevant, calculate climateData based on country value
		// from other databases

		// TODO: map postcode to city
		BuildingAddress buildingAddress = new BuildingAddress(
				new Address(null, null, null, null, record.get(CvsHeader.POSTCODE), null, COUNTRY), null);

		Dwelling ratedDwelling = new Dwelling(buildingAddress, null,
				parseDwellingType(record.get(CvsHeader.DWELLING_TYPE)), null, null, spatialData, thermalData);

		return new EPC(null, new SimpleDateFormat("dd/MM/yyyy").parse(record.get(CvsHeader.DATE_OF_CERTIFICATE)), null,
				ratedDwelling, null, awardedRating, potentialRating, ratingMethodology, null);
	}

	static FuelType getFuelType(String cvsFuelType) {
		return FuelType.approximateValue(cvsFuelType);
	}

	/**
	 * According to the dataset documentation, the carbon emissions is measured in
	 * tonnes/year, but the conversion to kg/m^2/year gives a quite big number,
	 * which does not seems to be realistic, so the value for tonnes/year is kept
	 * and used as kg/m^2/year
	 */
	/**
	 * static Measure calculateCarbonFootprint(String carbonEmissionsTonsPerYear,
	 * Double totalFloorArea) { return new Measure( new
	 * BigDecimal(1000).multiply(new BigDecimal(carbonEmissionsTonsPerYear))
	 * .divide(new BigDecimal(totalFloorArea), 2,
	 * RoundingMode.HALF_UP).doubleValue(), MeasuringUnit.KG_SQUARE_METER_YEAR); }
	 */

	static String parseStandardName(String rawStandardName) {
		return rawStandardName.substring(0, rawStandardName.indexOf(","));
	}

	static DwellingType parseDwellingType(String rawDwellingType) {
		try {
			return DwellingType.valueOf(rawDwellingType
					.substring(rawDwellingType.lastIndexOf(" ") + 1, rawDwellingType.length()).toUpperCase());
		} catch (IllegalArgumentException ex) {
			return DwellingType.OTHER;
		}
	}

}
