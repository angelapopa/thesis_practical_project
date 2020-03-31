package at.uibk.epc.importer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import at.uibk.epc.model.DwellingType;
import at.uibk.epc.model.FuelType;

public class OpenDataCommunitiesImporterTest {

	@Test
	public void testFuelType() {
		assertEquals(FuelType.GAS, OpenDataCommunitiesImporter.getFuelType("mains gas"));
		assertEquals(FuelType.GAS, OpenDataCommunitiesImporter.getFuelType("mains gas (not community)"));
		assertEquals(FuelType.GAS, OpenDataCommunitiesImporter.getFuelType("biogas"));
		assertEquals(FuelType.BIOMASS, OpenDataCommunitiesImporter.getFuelType("biomass"));
		assertEquals(FuelType.ELECTRICITY, OpenDataCommunitiesImporter.getFuelType("Electricity: electricity, unspecified tariff"));
		assertEquals(FuelType.ELECTRICITY, OpenDataCommunitiesImporter.getFuelType("electricity (not community)"));
		assertEquals(FuelType.WOOD, OpenDataCommunitiesImporter.getFuelType("wood logs"));
	}
	
	@Test 
	public void testDwellingType() {
		assertEquals(DwellingType.HOUSE, OpenDataCommunitiesImporter.parseDwellingType("House"));
		assertEquals(DwellingType.FLAT, OpenDataCommunitiesImporter.parseDwellingType("Flat"));
		assertEquals(DwellingType.BUNGALOW, OpenDataCommunitiesImporter.parseDwellingType("Bungalow"));
		assertEquals(DwellingType.MAISONETTE, OpenDataCommunitiesImporter.parseDwellingType("Maisonette"));
		assertEquals(DwellingType.OTHER, OpenDataCommunitiesImporter.parseDwellingType("Park home"));
	}
}
