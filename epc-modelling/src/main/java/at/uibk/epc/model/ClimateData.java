package at.uibk.epc.model;

public class ClimateData {

	private Temperature averageOutdoorTemperature;
	
	private Temperature idealIndoorTemperature;
	
	private Measure hightAboveSeaLevel;
	
	private String region;
	
	private int climateFactor;
	
	public Temperature getAverageOutdoorTemperature() {
		return averageOutdoorTemperature;
	}
	
	public Temperature getIdealIndoorTemperature() {
		return idealIndoorTemperature;
	}
	
	public Measure getHightAboveSeaLevel() {
		return hightAboveSeaLevel;
	}
	
	public String getRegion() {
		return region;
	}
	
	public int getClimateFactor() {
		return climateFactor;
	}
}
