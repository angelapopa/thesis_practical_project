package at.uibk.epc.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ClimateData {

	private Measure averageOutdoorTemperature;
	
	private Measure idealIndoorTemperature;
	
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
	
	public ClimateData(Measure averageOutdoorTemperature, Measure idealIndoorTemperature,
			Measure hightAboveSeaLevel, String region, int climateFactor, int heatingDaysPerYear) {
		super();
		this.averageOutdoorTemperature = averageOutdoorTemperature;
		this.idealIndoorTemperature = idealIndoorTemperature;
		this.hightAboveSeaLevel = hightAboveSeaLevel;
		this.region = region;
		this.climateFactor = climateFactor;
		this.heatingDaysPerYear = heatingDaysPerYear;
	}

	public Measure getAverageOutdoorTemperature() {
		return averageOutdoorTemperature;
	}
	
	public void setAverageOutdoorTemperature(Measure averageOutdoorTemperature) {
		this.averageOutdoorTemperature = averageOutdoorTemperature;
	}
	
	public Measure getIdealIndoorTemperature() {
		return idealIndoorTemperature;
	}
	
	public void setIdealIndoorTemperature(Measure idealIndoorTemperature) {
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
