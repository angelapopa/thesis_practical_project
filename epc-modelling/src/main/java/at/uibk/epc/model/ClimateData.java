package at.uibk.epc.model;

public class ClimateData {

	private Temperature averageOutdoorTemperature;
	
	private Temperature idealIndoorTemperature;
	
	private Measure hightAboveSeaLevel;
	
	private String region;
	
	private int climateFactor;
	
	/**Die Heiztage, die auch auf dem Energieausweis Datenblatt zu finden waren, 
	 * beschreiben die Anzahl der Tage im Jahr, 
	 * an denen die mittlere Auﬂentemperatur die Heizgrenze (hier: 12∞C zw. 14∞C - je nach Norm) unterschreitet.
	 */
	private int heatingDaysPerYear;
	
	public ClimateData() {
		// needed by MongoDB POJO Converter
	}
	
	public ClimateData(Temperature averageOutdoorTemperature, Temperature idealIndoorTemperature,
			Measure hightAboveSeaLevel, String region, int climateFactor, int heatingDaysPerYear) {
		super();
		this.averageOutdoorTemperature = averageOutdoorTemperature;
		this.idealIndoorTemperature = idealIndoorTemperature;
		this.hightAboveSeaLevel = hightAboveSeaLevel;
		this.region = region;
		this.climateFactor = climateFactor;
		this.heatingDaysPerYear = heatingDaysPerYear;
	}

	public Temperature getAverageOutdoorTemperature() {
		return averageOutdoorTemperature;
	}
	
	public void setAverageOutdoorTemperature(Temperature averageOutdoorTemperature) {
		this.averageOutdoorTemperature = averageOutdoorTemperature;
	}
	
	public Temperature getIdealIndoorTemperature() {
		return idealIndoorTemperature;
	}
	
	public void setIdealIndoorTemperature(Temperature idealIndoorTemperature) {
		this.idealIndoorTemperature = idealIndoorTemperature;
	}
	
	public Measure getHightAboveSeaLevel() {
		return hightAboveSeaLevel;
	}
	
	public void setHightAboveSeaLevel(Measure hightAboveSeaLevel) {
		this.hightAboveSeaLevel = hightAboveSeaLevel;
	}
	
	public String getRegion() {
		return region;
	}
	
	public void setRegion(String region) {
		this.region = region;
	}
	
	public int getClimateFactor() {
		return climateFactor;
	}
	
	public void setClimateFactor(int climateFactor) {
		this.climateFactor = climateFactor;
	}

	public int getHeatingDaysPerYear() {
		return heatingDaysPerYear;
	}
	
	public void setHeatingDaysPerYear(int heatingDaysPerYear) {
		this.heatingDaysPerYear = heatingDaysPerYear;
		
	}

	@Override
	public String toString() {
		return "ClimateData [averageOutdoorTemperature=" + averageOutdoorTemperature + ", idealIndoorTemperature="
				+ idealIndoorTemperature + ", hightAboveSeaLevel=" + hightAboveSeaLevel + ", region=" + region
				+ ", climateFactor=" + climateFactor + ", heatingDaysPerYear=" + heatingDaysPerYear + "]";
	}
	
}
