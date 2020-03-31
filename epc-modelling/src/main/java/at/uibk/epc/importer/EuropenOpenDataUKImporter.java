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
 * Importer for CVS data of EuropeanData for UK (non-domestic) 615619 records
 * https://www.europeandataportal.eu/data/datasets/display-energy-certificate-data1?locale=en
 *
 */
public class EuropenOpenDataUKImporter {

	private static Boolean DRY_RUN = true;
	/**
	 * Includes only non-domestic data, but this time it will be imported.
	 * (Non-domestic data is available also in the OpenDataCommunties folder, but
	 * that data is not imported).
	 */
	private static String CVS_FILE_PATH = "e:\\docs\\Uni Innsbruck\\Masterthesis\\datasets\\European open data\\UK\\energycertificatedataforpublicbuildings.csv";
	private static String COUNTRY = "United Kingdom";

	/**
	 * CVS column names. The order counts for extracting the correct data field
	 * values.
	 */
	private enum CvsHeader {
		RRN("RRN"), RELATED_RRN("RELATED_RRN"), ISSUE_DATE("ISSUE_DATE"), ORGANISATION_NAME("ORGANISATION_NAME"),
		ADDR1("ADDR1"), ADDR2("ADDR2"), ADDR3("ADDR3"), ADDR4("ADDR4"), POST_TOWN("POST_TOWN"),
		COUNTY_OSGB("COUNTY_OSGB"), POSTCODE("POSTCODE"), ENERGY_RATING_BAND("ENERGY_RATING_BAND"),
		ENERGY_RATING("ENERGY_RATING"), TOTAL_CO2_EMISSIONS("TOTAL_CO2_EMISSIONS"),
		MAIN_HEATING_FUEL("MAIN_HEATING_FUEL"), BUILDING_ENVIRONMENT("BUILDING_ENVIRONMENT"), FLOOR_AREA("FLOOR_AREA"),
		/**
		 * annual == actual, so consumption, currently not imported.
		 */
		ANNUALENERGYUSEFUELTHERMAL("ANNUALENERGYUSEFUELTHERMAL"),
		ANNUALENERGYUSEELECTRICAL("ANNUALENERGYUSEELECTRICAL"),
		/**
		 * typical == reference, so estimated demand, currently not imported.
		 */
		TYPICALENERGYUSEFUELTHERMAL("TYPICALENERGYUSEFUELTHERMAL"),
		TYPICALENERGYUSEELECTRICAL("TYPICALENERGYUSEELECTRICAL"), RENEWABLESTHERMAL("RENEWABLESTHERMAL"),
		RENEWABLESELECTRICAL("RENEWABLESELECTRICAL"),
		/**
		 * annual == actual, so measured consumption
		 */
		ACTUAL_ANNUAL_HEAT_TOTAL("ACTUAL_ANNUAL_HEAT_TOTAL"), ACTUAL_ANNUAL_ELEC_TOTAL("ACTUAL_ANNUAL_ELEC_TOTAL"),
		/**
		 * typical == reference, so estimated demand.
		 */
		TYPICAL_ANNUAL_HEAT_TOTAL("TYPICAL_ANNUAL_HEAT_TOTAL"), TYPICAL_ANNUAL_ELEC_TOTAL("TYPICAL_ANNUAL_ELEC_TOTAL"),
		Longitude("Longitude"), Latitude("Latitude");

		private String description;

		CvsHeader(String description) {
			this.description = description;
		}
	}

	public static void main(String[] args) {

		MongoDatabase database = MongoDatabaseClient.getDatabase();

		System.out.println("UK Import - Before import: " + database.getCollection("EPC_Collection").countDocuments());

		MongoCollection<EPC> epcCollection = database.getCollection("EPC_Collection", EPC.class);

		System.out.println("Importing file " + CVS_FILE_PATH);
		try {
			InputStreamReader input = new InputStreamReader((new FileInputStream(new File(CVS_FILE_PATH))));
			CSVParser csvParser = CSVFormat.DEFAULT.withHeader(CvsHeader.class).withSkipHeaderRecord().parse(input);

			long counter = 0;
			for (CSVRecord record : csvParser) {
				if (DRY_RUN) {
					System.out.println(parseCVSInputRow(record).toString());
				} else {
					epcCollection.insertOne(parseCVSInputRow(record));
				}
				counter++;
			}
			System.out.println("File " + CVS_FILE_PATH + ": imported " + counter);
			input.close();
			System.out.println("UK EuropeanOpenData Import finished successfully.");
		} catch (FileNotFoundException e) {
			System.out.println("File " + CVS_FILE_PATH + " not found!");
		} catch (IOException e) {
			System.out.println("File " + CVS_FILE_PATH + " not readable!");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private static EPC parseCVSInputRow(CSVRecord record) throws ParseException {

		SpatialData spatialData = new SpatialData();
		spatialData.setTotalFloorArea(
				new Measure(Double.valueOf(record.get(CvsHeader.FLOOR_AREA).replace(",", "")), MeasuringUnit.SQUARE_METER));

		ThermalData thermalData = new ThermalData();
		thermalData.setCarbonFootprint(new Measure(Double.valueOf(record.get(CvsHeader.TOTAL_CO2_EMISSIONS).replace(",", "")),
				MeasuringUnit.KG_SQUARE_METER_YEAR));
		thermalData.setMainHeatingFuelType(getFuelType(record.get(CvsHeader.MAIN_HEATING_FUEL)));

		thermalData.setElectricalEnergyConsumption(
				new Measure(Double.valueOf(record.get(CvsHeader.ACTUAL_ANNUAL_ELEC_TOTAL).replace(",", "")), MeasuringUnit.KWH_YEAR));
		thermalData.setThermalEnergyConsumption(
				new Measure(Double.valueOf(record.get(CvsHeader.ACTUAL_ANNUAL_HEAT_TOTAL).replace(",", "")), MeasuringUnit.KWH_YEAR));

		thermalData.setElectricalEnergyDemand(
				new Measure(Double.valueOf(record.get(CvsHeader.TYPICAL_ANNUAL_ELEC_TOTAL).replace(",", "")), MeasuringUnit.KWH_YEAR));
		thermalData.setThermalEnergyDemand(
				new Measure(Double.valueOf(record.get(CvsHeader.TYPICAL_ANNUAL_HEAT_TOTAL).replace(",", "")), MeasuringUnit.KWH_YEAR));

		thermalData.setFinalEnergyDemand(new Measure(
				thermalData.getElectricalEnergyDemand().getValue() + thermalData.getThermalEnergyDemand().getValue(),
				MeasuringUnit.KWH_YEAR));
		thermalData.setFinalEnergyConsumption(new Measure(thermalData.getElectricalEnergyConsumption().getValue()
				+ thermalData.getThermalEnergyConsumption().getValue(), MeasuringUnit.KWH_YEAR));

		BuildingAddress buildingAddress = new BuildingAddress();
		buildingAddress.setCity(record.get(CvsHeader.POST_TOWN));
		buildingAddress.setPostalCode(record.get(CvsHeader.POSTCODE));
		buildingAddress.setCountry(COUNTRY);

		Dwelling ratedDwelling = new Dwelling(buildingAddress, null, DwellingType.NONDOMESTIC, null, null, spatialData,
				thermalData);

		Date creationDate = getCreationDate(record.get(CvsHeader.ISSUE_DATE));

		Rating awardedRating = new Rating(record.get(CvsHeader.ENERGY_RATING_BAND),
				Double.valueOf(record.get(CvsHeader.ENERGY_RATING)));

		EPC epc = new EPC(record.get(CvsHeader.RRN), creationDate, null, ratedDwelling, null, awardedRating, null, null, null);
		return epc;
	}

	static Date getCreationDate(String csvDateString) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		return dateFormat.parse(csvDateString);
	}

	static FuelType getFuelType(String fuelType) {
		return FuelType.approximateValue(fuelType);
	}
}
