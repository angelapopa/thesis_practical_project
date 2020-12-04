package at.uibk.epc.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
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
import at.uibk.epc.model.PurposeType;
import at.uibk.epc.model.Rating;
import at.uibk.epc.model.RatingMethodology;
import at.uibk.epc.model.Software;
import at.uibk.epc.model.SpatialData;
import at.uibk.epc.model.ThermalData;

/**
 * A tab-delimited import file with data for Ireland. 902253 records.
 */

public class IrelandImporter {

	private static Boolean DRY_RUN = false;

	private static final String NEW_DATABASE = "EPC";
	private static final String NEW_COLLECTION_NAME = "EPC_Ireland";

	// first import with double values
	// Ireland, //902253 total records, but only 635574 were imported in 1 cluster,
	// from which afterwards the totalfloorarea=0 were deleted
	// Ireland, but not full import, due to limitation to 512MB
	// 565587 total records
	// private static final String
	// NEW_CONNECTION_STRING="mongodb+srv://epcuser2:pw12epc559@epcfull-2jvr7.mongodb.net/test?retryWrites=true&w=majority";

	// second import with integer values, every 3 entry
	// Ireland, //300751 were imported in 1 cluster
	private static final String NEW_CONNECTION_STRING = "mongodb+srv://ire_1:t9YjjOigsWGmPTJJ@cluster0.fxx98.mongodb.net/EPC?retryWrites=true&w=majority";

	private static String IRELAND_FOLDER = "e:\\docs\\Uni Innsbruck\\Masterthesis\\datasets\\IRELAND BER\\BERPublicSearch\\";
	private static String IMPORT_FILE = "BERPublicsearch.txt";
	private static String COUNTRY = "Ireland";

	/**
	 * CVS column names. The order counts for extracting the correct data field
	 * values.
	 */
	private enum CvsHeader {

		COUNTY_NAME("CountyName"), DWELLING_TYPE("DwellingTypeDescr"), CONSTRUCTION_YEAR("Year_of_Construction"),
		TYPE_OF_RATING("TypeofRating"), // possible value: existing, final

		/**
		 * The BER scale is divided into categories from G (largest primary energy
		 * usage) to an A1 rating (lowest primary energy usage). The full range of
		 * categories is described in Table 2 of the Appendix (A1, A2, A3, B1, B2, B3,
		 * C1, C2, C3, D1, D2, E, F, G).
		 * 
		 * The importer maps e.g. A1, A2, A3 to A.
		 */
		ENERGY_RATING("EnergyRating"),

		/**
		 * The Building Energy Rating of the dwelling, i.e. the total Primary Energy Use
		 * for the dwelling expressed in units of kWh/m2/year. A low BER indicates an
		 * energy efficient dwelling. It is critical to note that multiple factors
		 * including dwelling dimensions, orientation, ventilation, dwelling fabric,
		 * water heating, lighting, space heating, heating controls and fuel type
		 * influence the building energy rating and must all be considered when
		 * determining the actual rating and where potential improvements could be made.
		 * e.g. 441.440
		 */
		BER_RATING("BerRating"),

		/**
		 * Any floor separating the dwelling from another heated dwelling, e.g. the
		 * floor in a mid-floor apartment that is situated directly over another
		 * apartment, is assumed to have no heat loss so it is not included here.
		 */
		GROUND_FLOOR_AREA("GroundFloorArea(sq m)"), U_VALUE_WALL("UValueWall"), U_VALUE_ROOF("UValueRoof"),
		U_VALUE_FLOOR("UValueFloor"), U_VALUE_WINDOW("UValueWindow"), U_VALUE_DOOR("UvalueDoor"), WALL_AREA("WallArea"),
		ROOF_AREA("RoofArea"),

		/**
		 * The total internal floor area of the dwelling, excluding any unheated areas
		 * that are thermally separated from the dwelling. Units: m2. The total area of
		 * exposed and semi-exposed floors in the dwelling.
		 */
		FLOOR_AREA("FloorArea"), WINDOW_AREA("WindowArea"), DOOR_AREA("DoorArea"),
		/**
		 * Number of storeys
		 */
		NR_STOREYS("NoStoreys"),
		/**
		 * The amount of carbon dioxide emitted by the dwelling per annum expressed in
		 * units of kg CO2/m2/year.
		 */
		CO2_Rating("CO2Rating"),
		/**
		 * The fuel used by the Main Space Heating system.
		 */
		MainSpaceHeatingFuel("MainSpaceHeatingFuel"), MainWaterHeatingFuel("MainWaterHeatingFuel"),
		HSMainSystemEfficiency("HSMainSystemEfficiency"), MultiDwellingMPRN("MultiDwellingMPRN"),
		TGDLEdition("TGDLEdition"), MPCDERValue("MPCDERValue"), HSEffAdjFactor("HSEffAdjFactor"),
		HSSupplHeatFraction("HSSupplHeatFraction"), HSSupplSystemEff("HSSupplSystemEff"),
		WHMainSystemEff("WHMainSystemEff"), WHEffAdjFactor("WHEffAdjFactor"), SupplSHFuel("SupplSHFuel"),
		SupplWHFuel("SupplWHFuel"), SHRenewableResources("SHRenewableResources"),
		WHRenewableResources("WHRenewableResources"), NoOfChimneys("NoOfChimneys"), NoOfOpenFlues("NoOfOpenFlues"),
		NoOfFansAndVents("NoOfFansAndVents"), NoOfFluelessGasFires("NoOfFluelessGasFires"), DraftLobby("DraftLobby"),
		VentilationMethod("VentilationMethod"), FanPowerManuDeclaredValue("FanPowerManuDeclaredValue"),
		HeatExchangerEff("HeatExchangerEff"), StructureType("StructureType"),
		SuspendedWoodenFloor("SuspendedWoodenFloor"), PercentageDraughtStripped("PercentageDraughtStripped"),
		NoOfSidesSheltered("NoOfSidesSheltered"), PermeabilityTest("PermeabilityTest"),
		PermeabilityTestResult("PermeabilityTestResult"), TempAdjustment("TempAdjustment"),
		HeatSystemControlCat("HeatSystemControlCat"), HeatSystemResponseCat("HeatSystemResponseCat"),
		NoCentralHeatingPumps("NoCentralHeatingPumps"), CHBoilerThermostatControlled("CHBoilerThermostatControlled"),
		NoOilBoilerHeatingPumps("NoOilBoilerHeatingPumps"),
		OBBoilerThermostatControlled("OBBoilerThermostatControlled"), OBPumpInsideDwelling("OBPumpInsideDwelling"),
		NoGasBoilerHeatingPumps("NoGasBoilerHeatingPumps"), WarmAirHeatingSystem("WarmAirHeatingSystem"),
		UndergroundHeating("UndergroundHeating"), GroundFloorUValue("GroundFloorUValue"),
		DistributionLosses("DistributionLosses"), StorageLosses("StorageLosses"),
		ManuLossFactorAvail("ManuLossFactorAvail"), SolarHotWaterHeating("SolarHotWaterHeating"),
		ElecImmersionInSummer("ElecImmersionInSummer"), CombiBoiler("CombiBoiler"), KeepHotFacility("KeepHotFacility"),
		WaterStorageVolume("WaterStorageVolume"), DeclaredLossFactor("DeclaredLossFactor"),
		TempFactorUnadj("TempFactorUnadj"), TempFactorMultiplier("TempFactorMultiplier"),
		InsulationType("InsulationType"), InsulationThickness("InsulationThickness"),
		PrimaryCircuitLoss("PrimaryCircuitLoss"), CombiBoilerAddLoss("CombiBoilerAddLoss"),
		ElecConsumpKeepHot("ElecConsumpKeepHot"), ApertureArea("ApertureArea"),
		ZeroLossCollectorEff("ZeroLossCollectorEff"), CollectorHeatLossCoEff("CollectorHeatLossCoEff"),
		/**
		 * The Annual Solar Radiation incident on 1 m2 of solar collectors. This depends
		 * on the orientation and angle of the collectors to the horizontal. The optimal
		 * set-up is to have the collectors facing south at an angle of 30° to the
		 * horizontal though any orientation between south-east and south-west or any
		 * angle between 15° to 45° will be close to the optimum.
		 */
		AnnualSolarRadiation("AnnualSolarRadiation"), OvershadingFactor("OvershadingFactor"),
		CylinderStat("CylinderStat"),
		/**
		 * The Dedicated Solar Storage Volume. This is the volume of water in the
		 * storage cylinder(s) that is only heated by the solar collectors.
		 */
		SolarStorageVolume("SolarStorageVolume"), VolumeOfPreHeatStore("VolumeOfPreHeatStore"),
		CombinedCylinder("CombinedCylinder"),
		/**
		 * The electricity consumption of the solar circuit circulation pump. If the
		 * pump is solar powered (see next entry) this has a value of zero. Otherwise a
		 * default value of 75 kWh/year is assumed.
		 */
		ElectricityConsumption("ElectricityConsumption"), SWHPumpSolarPowered("SWHPumpSolarPowered"),
		ChargingBasisHeatConsumed("ChargingBasisHeatConsumed"), gsdHSSupplHeatFraction("gsdHSSupplHeatFraction"),
		gsdHSSupplSystemEff("gsdHSSupplSystemEff"), DistLossFactor("DistLossFactor"),
		CHPUnitHeatFraction("CHPUnitHeatFraction"), CHPSystemType("CHPSystemType"), CHPElecEff("CHPElecEff"),
		CHPHeatEff("CHPHeatEff"), CHPFuelType("CHPFuelType"), SupplHSFuelTypeID("SupplHSFuelTypeID"),
		gsdSHRenewableResources("gsdSHRenewableResources"), gsdWHRenewableResources("gsdWHRenewableResources"),
		/**
		 * This is the percentage of the heating demand met by the solar collectors. For
		 * a solar hot water installation this should ideally be greater than 45% but
		 * less than 60% to avoid overheating and stagnation in the solar water heating
		 * system.
		 */
		SolarHeatFraction("SolarHeatFraction"),
		/**
		 * Estimated lighting energy demand (final consumption)
		 */
		DeliveredLightingEnergy("DeliveredLightingEnergy"), DeliveredEnergyPumpsFans("DeliveredEnergyPumpsFans"),
		/**
		 * Estimated main water heating energy demand (final consumption)
		 */
		DeliveredEnergyMainWater("DeliveredEnergyMainWater"),
		/**
		 * Estimated main space energy demand (final consumption)
		 */
		DeliveredEnergyMainSpace("DeliveredEnergyMainSpace"),
		/**
		 * Estimated lighting energy requirement (primary energy)
		 */
		PrimaryEnergyLighting("PrimaryEnergyLighting"), PrimaryEnergyPumpsFans("PrimaryEnergyPumpsFans"),
		PrimaryEnergyMainWater("PrimaryEnergyMainWater"),
		/**
		 * Estimated main space heating energy requirement (primary energy)
		 */
		PrimaryEnergyMainSpace("PrimaryEnergyMainSpace"), CO2Lighting("CO2Lighting"), CO2PumpsFans("CO2PumpsFans"),
		CO2MainWater("CO2MainWater"), CO2MainSpace("CO2MainSpace"),
		/**
		 * GroundFloorArea(sq m) Any floor separating the dwelling from another heated
		 * dwelling, e.g. the floor in a mid-floor apartment that is situated directly
		 * over another apartment, is assumed to have no heat loss so it is not included
		 * here.
		 */
		GroundFloorArea("GroundFloorArea"), GroundFloorHeight("GroundFloorHeight"), FirstFloorArea("FirstFloorArea"),
		FirstFloorHeight("FirstFloorHeight"), SecondFloorArea("SecondFloorArea"),
		SecondFloorHeight("SecondFloorHeight"), ThirdFloorArea("ThirdFloorArea"), ThirdFloorHeight("ThirdFloorHeight"),
		ThermalBridgingFactor("ThermalBridgingFactor"), ThermalMassCategory("ThermalMassCategory"),
		PredominantRoofTypeArea("PredominantRoofTypeArea"), PredominantRoofType("PredominantRoofType"),
		LowEnergyLightingPercent("LowEnergyLightingPercent"),
		/**
		 * Estimated total energy demand (final consumption)
		 */
		TotalDeliveredEnergy("TotalDeliveredEnergy"), DeliveredEnergySecondarySpace("DeliveredEnergySecondarySpace"),
		DeliveredEnergySupplementaryWater("DeliveredEnergySupplementaryWater"), LivingAreaPercent("LivingAreaPercent"),
		CO2SecondarySpace("CO2SecondarySpace"), CO2SupplementaryWater("CO2SupplementaryWater"),
		PrimaryEnergySecondarySpace("PrimaryEnergySecondarySpace"),
		PrimaryEnergySupplementaryWater("PrimaryEnergySupplementaryWater"), HESSchemeUpgrade("HESSchemeUpgrade"),
		RoomInRoofArea("RoomInRoofArea"), PurposeOfRating("PurposeOfRating"), DATE_OF_ASSESSMENT("DateOfAssessment"),
		FirstEnergyTypeId("FirstEnergyTypeId"), FirstEnergyType_Description("FirstEnergyType_Description"),
		FirstEnerProdComment("FirstEnerProdComment"),
		// the rest is not imported
		// FirstEnerProdDelivered FirstPartLTotalContribution FirstEnerProdConvFactor
		// FirstEnerProdCO2EmissionFactor
		// FirstEnerConsumedComment FirstEnerConsumedDelivered
		// FirstEnerConsumedConvFactor FirstEnerConsumedCO2EmissionFactor
		// SecondEnergyTypeId SecondEnergyType_Description SecondEnerProdComment
		// SecondEnerProdDelivered SecondPartLTotalContribution
		// SecondEnerProdConvFactor SecondEnerProdCO2EmissionFactor
		// SecondEnerConsumedComment SecondEnerConsumedDelivered
		// SecondEnerConsumedConvFactor
		// SecondEnerConsumedCO2EmissionFactor ThirdEnergyTypeId
		// ThirdEnergyType_Description ThirdEnerProdComment ThirdEnerProdDelivered
		// ThirdPartLTotalContribution
		// ThirdEnerProdConvFactor ThirdEnerProdCO2EmissionFactor
		// ThirdEnerConsumedComment ThirdEnerConsumedDelivered
		// ThirdEnerConsumedConvFactor
		// ThirdEnerConsumedCO2EmissionFactor FirstBoilerFuelType FirstHeatGenPlantEff
		// FirstPercentageHeat SecondBoilerFuelType SecondHeatGenPlantEff
		// SecondPercentageHeat ThirdBoilerFuelType ThirdHeatGenPlantEff
		// ThirdPercentageHeat SolarSpaceHeatingSystem TotalPrimaryEnergyFact
		// TotalCO2Emissions
		// FirstWallType_Description FirstWallDescription FirstWallArea FirstWallUValue
		// FirstWallIsSemiExposed FirstWallAgeBandId FirstWallTypeId
		// SecondWallType_Description SecondWallDescription SecondWallArea
		// SecondWallUValue SecondWallIsSemiExposed SecondWallAgeBandId SecondWallTypeId
		// ThirdWallType_Description ThirdWallDescription ThirdWallArea ThirdWallUValue
		// ThirdWallIsSemiExposed ThirdWallAgeBandId
		ThirdWallTypeId("ThirdWallTypeId");

		private String description;

		CvsHeader(String description) {
			this.description = description;
		}
	}

	private static enum RatingType {
		EXISTING, PROVISIONAL, FINAL;
	}

	private static EnumMap<RatingType, String> ratingTypeEnumMap = new EnumMap<RatingType, String>(RatingType.class);

	/**
	 * //BER (Building energy rating) is the name of the energy certificates in
	 * Ireland
	 */
	private static void constructRatingType() {
		ratingTypeEnumMap.put(RatingType.EXISTING, "Existing Dwelling BER");
		ratingTypeEnumMap.put(RatingType.PROVISIONAL, "New Dwelling – Provisional BER");
		ratingTypeEnumMap.put(RatingType.FINAL, "New Dwelling – Final BER");
	}

	private static void dropAndCreateDB(String databaseName, String collectionName) {
		MongoDatabase database = MongoDatabaseClient.getDatabase(databaseName, NEW_CONNECTION_STRING);
		MongoCollection<EPC> epcCollection = database.getCollection(collectionName, EPC.class);
		epcCollection.drop();
		database.createCollection(collectionName);
	}

	public static void main(String[] args) {

		dropAndCreateDB(NEW_DATABASE, NEW_COLLECTION_NAME);

		MongoDatabase database = MongoDatabaseClient.getDatabase(NEW_DATABASE, NEW_CONNECTION_STRING);

		System.out.println("Ireland Import - Before import: " + database.getCollection(NEW_COLLECTION_NAME).countDocuments());

		MongoCollection<EPC> epcCollection = database.getCollection(NEW_COLLECTION_NAME, EPC.class);

		try {
			InputStreamReader input = new InputStreamReader(
					(new FileInputStream(new File(IRELAND_FOLDER + IMPORT_FILE))));
			// a few records have quotation marks which makes the parser fail, so the
			// withQuote(null) fixes the problem.
			CSVParser csvParser = CSVFormat.TDF.withHeader(CvsHeader.class).withSkipHeaderRecord().withQuote(null)
					.parse(input);

			long counter = 0;
			int temp_counter = 0;
			/**
			 * import every 10th valid csv entry
			 */
			for (CSVRecord record : csvParser) {
				if (validRecord(record)) {
					temp_counter = temp_counter + 1;
					if (temp_counter == 3) {
						EPC epc = parseCVSInputRow(record);
						if (epc != null) {
							if (DRY_RUN) {
								System.out.println(epc.toString());
							} else {
								epcCollection.insertOne(epc);
							}
							counter++;
							temp_counter = 0;
						}
					}
				}
			}
			System.out.println("File " + IMPORT_FILE + ": imported " + counter);
			input.close();
			System.out.println("Ireland Import finished successfully.");
		} catch (FileNotFoundException e) {
			System.out.println("File " + IRELAND_FOLDER + IMPORT_FILE + " not found!");
		} catch (IOException e) {
			System.out.println("File " + IRELAND_FOLDER + IMPORT_FILE + " not readable!");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private static EPC parseCVSInputRow(CSVRecord record) throws ParseException {

		String floorAreaCVS = record.get(CvsHeader.FLOOR_AREA);
		if (floorAreaCVS == null || floorAreaCVS.isEmpty()) {
			return null;
		}

		Measure totalFloorArea = new Measure(Math.round(Double.valueOf(record.get(CvsHeader.FLOOR_AREA))),
				MeasuringUnit.SQUARE_METER);

		SpatialData spatialData = new SpatialData(totalFloorArea, null, null);

		BuildingAddress buildingAddress = new BuildingAddress();
		buildingAddress.setCity(record.get(CvsHeader.COUNTY_NAME));
		buildingAddress.setCountry(COUNTRY);

		ThermalData thermalData = new ThermalData();
		thermalData.setCarbonFootprint(
				new Measure(Math.round(Double.valueOf(record.get(CvsHeader.CO2_Rating))), MeasuringUnit.KG_SQUARE_METER_YEAR));
		thermalData.setMainHeatingFuelType(FuelType.approximateValue(record.get(CvsHeader.MainSpaceHeatingFuel)));
		thermalData.setWaterHeatingEnergyDemand(
				new Measure(Math.round(Double.valueOf(record.get(CvsHeader.DeliveredEnergyMainWater))), MeasuringUnit.KWH_YEAR));
		thermalData.setSpaceHeatingEnergyDemand(
				new Measure(Math.round(Double.valueOf(record.get(CvsHeader.DeliveredEnergyMainSpace))), MeasuringUnit.KWH_YEAR));
		thermalData.setFinalEnergyDemand(new Measure(Math.round(Double.valueOf(record.get(CvsHeader.TotalDeliveredEnergy))),
				MeasuringUnit.KWH_SQUARE_METER_YEAR));

		Dwelling ratedDwelling = new Dwelling(buildingAddress,
				Integer.parseInt(record.get(CvsHeader.CONSTRUCTION_YEAR)),
				approximateDwellingType(record.get(CvsHeader.DWELLING_TYPE)), null, null, spatialData, thermalData);

		Rating awardedRating = new Rating(record.get(CvsHeader.ENERGY_RATING).substring(0, 1),
				Integer.valueOf(Math.round(Math.round(Double.valueOf(record.get(CvsHeader.BER_RATING))))));
		Rating potentialRating = new Rating();

		RatingMethodology ratingMethodology = new RatingMethodology();
		ratingMethodology.setCalculationName("Dwelling Energy Assessment Procedure (DEAP)");
		ratingMethodology.setCalculationMethod(parseRatingType(record.get(CvsHeader.TYPE_OF_RATING)));
		ratingMethodology.setSoftwareUsed(new Software("DEAP", null));

		Date creationDate = getCreationDate(record.get(CvsHeader.DATE_OF_ASSESSMENT));

		EPC epc = new EPC(null, creationDate, null, ratedDwelling, null, awardedRating, potentialRating,
				ratingMethodology, null);
		epc.setPurpose(PurposeType.approximateValue(record.get(CvsHeader.PurposeOfRating)));
		return epc;
	}

	private static boolean validRecord(CSVRecord record) {
		String floorArea = record.get(CvsHeader.FLOOR_AREA);
		if ("0".equals(floorArea) || "NO DATA!".equalsIgnoreCase(floorArea) || "N/A".equals(floorArea)) {
			return false;
		}

		String energyConsumption = record.get(CvsHeader.TotalDeliveredEnergy);
		if ("0".equals(energyConsumption) || "NO DATA!".equalsIgnoreCase(energyConsumption)
				|| "N/A".equals(energyConsumption)) {
			return false;
		}
		String energyRating = record.get(CvsHeader.ENERGY_RATING);
		if ("INVALID!".equals(energyRating) || "N/A".equals(energyRating)) {
			return false;
		}
		return true;
	}

	static Date getCreationDate(String csvDateString) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy HH:mmaaa", Locale.ENGLISH);
		return dateFormat.parse(csvDateString);
	}

	public static String parseRatingType(String csvValue) {
		constructRatingType();
		return ratingTypeEnumMap.get(RatingType.valueOf(csvValue.toUpperCase()));
	}

	public static DwellingType approximateDwellingType(String string) {
		return DwellingType.approximateValue(string);
	}

}
