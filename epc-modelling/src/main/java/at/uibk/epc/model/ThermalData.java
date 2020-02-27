package at.uibk.epc.model;

/**
 * Building specific data
 *
 */
public class ThermalData {
		
	private Measure uValue;
	
	private Measure heatingDemand;
	
	private Measure carbonFootprint;
	
	public ThermalData() {
		// needed by MongoDB POJO Converter
	}
	
	public ThermalData(Measure uValue, Measure heatingDemand, Measure carbonFootprint) {
		this.uValue = uValue;
		this.heatingDemand = heatingDemand;
		this.carbonFootprint = carbonFootprint;
	}

	public Measure getUValue() {
		return uValue;
	}
	
	public void setUValue(Measure uValue) {
		this.uValue = uValue;
	}
	
	public Measure getHeatingDemand() {
		return heatingDemand;
	}
	
	public void setHeatingDemand(Measure heatingDemand) {
		this.heatingDemand = heatingDemand;
	}
	
	public Measure getCarbonFootprint() {
		return carbonFootprint;
	}
	
	public void setCarbonFootprint(Measure carbonFootprint) {
		this.carbonFootprint = carbonFootprint;
	}

	@Override
	public String toString() {
		return "ThermalData [uValue=" + uValue + ", heatingDemand=" + heatingDemand + ", carbonFootprint="
				+ carbonFootprint + "]";
	}

}
