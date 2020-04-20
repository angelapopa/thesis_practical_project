package at.uibk.epc.importer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import at.uibk.epc.model.DwellingType;
import at.uibk.epc.model.FuelType;

public class ScottlandEPCImporterTests {

	@Test
	public void testStandardName() {
		assertEquals("RdSAP", ScottlandEPCImporter.parseStandardName("RdSAP, existing dwelling"));
	}
	
	@Test
	public void testDwellingTypParser() {
		assertEquals(DwellingType.HOUSE, ScottlandEPCImporter.parseDwellingType("End-terrace house"));
		assertEquals(DwellingType.HOUSE, ScottlandEPCImporter.parseDwellingType("Mid-terrace house"));
		assertEquals(DwellingType.HOUSE, ScottlandEPCImporter.parseDwellingType("Semi-detached house"));
		assertEquals(DwellingType.HOUSE, ScottlandEPCImporter.parseDwellingType("Detached house"));
		
		assertEquals(DwellingType.FLAT, ScottlandEPCImporter.parseDwellingType("Basement flat"));
		assertEquals(DwellingType.FLAT, ScottlandEPCImporter.parseDwellingType("Ground-floor flat"));
		assertEquals(DwellingType.FLAT, ScottlandEPCImporter.parseDwellingType("Mid-floor flat"));
		assertEquals(DwellingType.FLAT, ScottlandEPCImporter.parseDwellingType("Top-floor flat"));
		
		assertEquals(DwellingType.BUNGALOW, ScottlandEPCImporter.parseDwellingType("Detached bungalow"));
		assertEquals(DwellingType.BUNGALOW, ScottlandEPCImporter.parseDwellingType("Semi-detached bungalow"));
		
		assertEquals(DwellingType.MAISONETTE, ScottlandEPCImporter.parseDwellingType("Mid-floor maisonette"));
		assertEquals(DwellingType.MAISONETTE, ScottlandEPCImporter.parseDwellingType("Top-floor maisonette"));
	}
	
	/**
	@Test
	public void testCalculateCaronFootprint() {
		assertEquals(MeasuringUnit.KG_SQUARE_METER_YEAR, ScottlandEPCImporter.getCarbonFootprint("28", 28.0).getUnit());
		
		assertEquals(1000.0, ScottlandEPCImporter.getCarbonFootprint("28", 28.0).getValue());
		assertEquals(575.76, ScottlandEPCImporter.getCarbonFootprint("19", 33.0).getValue());
	}*/
	
	@Test
	public void testFuelType() {
		assertEquals(FuelType.BIOMASS, ScottlandEPCImporter.getFuelType("biomass (community)"));
		assertEquals(FuelType.ELECTRICITY, ScottlandEPCImporter.getFuelType("electricity (not community)"));
		assertEquals(FuelType.ELECTRICITY, ScottlandEPCImporter.getFuelType("electricity (community)"));
		assertEquals(FuelType.GAS, ScottlandEPCImporter.getFuelType("mains gas (not community)"));
		assertEquals(FuelType.GAS, ScottlandEPCImporter.getFuelType("mains gas (community)"));
		assertEquals(FuelType.OIL, ScottlandEPCImporter.getFuelType("oil (not community)"));
		assertEquals(FuelType.OIL, ScottlandEPCImporter.getFuelType("oil (community)"));
		assertEquals(FuelType.WOOD, ScottlandEPCImporter.getFuelType("wood logs"));
		assertEquals(FuelType.WOOD, ScottlandEPCImporter.getFuelType("wood chips"));
		assertEquals(FuelType.OTHER, ScottlandEPCImporter.getFuelType("LPG special condition"));
	}
}


