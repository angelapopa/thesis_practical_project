package at.uibk.epc.importer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import at.uibk.epc.model.DwellingType;

public class IrelandImporterTest {

	@Test
	public void testParseDwellingType() {
		assertEquals(DwellingType.FLAT, IrelandImporter.approximateDwellingType("Mid-floor apartment"));
		assertEquals(DwellingType.FLAT, IrelandImporter.approximateDwellingType("Top-floor apartment"));
		
		assertEquals(DwellingType.HOUSE, IrelandImporter.approximateDwellingType("Semi-detached house"));
		assertEquals(DwellingType.HOUSE, IrelandImporter.approximateDwellingType("End of Terrace house"));
		assertEquals(DwellingType.HOUSE, IrelandImporter.approximateDwellingType("Mid-terrace house"));
	}
	
	/**
	 * CEST - Central European Summer Time
	 * CET - Central European (Winter) Time
	 * If the original format is set to be GMT, than the converted Date will be 14:00 hours, instead of the original 12:00
	 */
	@Test
	public void testParseDate() throws ParseException {
		assertEquals("Wed Jun 02 12:00:00 CEST 2010", IrelandImporter.getCreationDate("Jun  2 2010 12:00AM").toString());
		assertEquals("Tue Jan 26 12:00:00 CET 2010", IrelandImporter.getCreationDate("Jan 26 2010 12:00AM").toString());
		assertEquals("Wed May 26 12:00:00 CEST 2010",IrelandImporter.getCreationDate("May 26 2010 12:00AM").toString());
	}
	
	@Test
	public void parseRatingType() {
		assertEquals("Existing Dwelling BER", IrelandImporter.parseRatingType("Existing"));
		assertEquals("New Dwelling – Final BER", IrelandImporter.parseRatingType("Final"));
		assertEquals("New Dwelling – Provisional BER", IrelandImporter.parseRatingType("Provisional"));
	}
	
	@Test
	public void checkValidityFloorArea() {
		assertFalse(isValidFloorArea("0"));
		assertFalse(isValidFloorArea(""));
	}
	
	private boolean isValidFloorArea(String floorArea) {
		if ("0".equals(floorArea) || "NO DATA!".equalsIgnoreCase(floorArea) || "N/A".equals(floorArea) || "".equals(floorArea.trim())) {
			return false;
		}
		return true;
	}
}
