package at.uibk.epc.model;

public enum TemperatureUnit {
	CELCIUS("celcius"), FAHRENHEIT("fahrenheit");
	
	private final String unit;
	
	private TemperatureUnit(String unit) {
		this.unit = unit;
	}
	
	public String getUnit() {
		return unit;
	}
}
