package at.uibk.epc.model;

/**
 * Building specific data
 *
 */
public class ThermalData {
		
	private Measure uValue;
	
	private Measure energyDemand;
	
	private Measure carbonFootprint;
	
	public ThermalData() {
		// needed by MongoDB POJO Converter
	}
	
	public ThermalData(Measure uValue, Measure energyDemand, Measure carbonFootprint) {
		this.uValue = uValue;
		this.energyDemand = energyDemand;
		this.carbonFootprint = carbonFootprint;
	}

	public Measure getUValue() {
		return uValue;
	}
	
	public void setUValue(Measure uValue) {
		this.uValue = uValue;
	}
	
	public Measure getEnergyDemand() {
		return energyDemand;
	}
	
	public void setEnergyDemand(Measure energyDemand) {
		this.energyDemand = energyDemand;
	}
	
	public Measure getCarbonFootprint() {
		return carbonFootprint;
	}
	
	public void setCarbonFootprint(Measure carbonFootprint) {
		this.carbonFootprint = carbonFootprint;
	}

	@Override
	public String toString() {
		return "ThermalData [uValue=" + uValue + ", energyDemand=" + energyDemand + ", carbonFootprint="
				+ carbonFootprint + "]";
	}

}
