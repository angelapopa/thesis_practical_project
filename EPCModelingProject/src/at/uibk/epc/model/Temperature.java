package at.uibk.epc.model;

public class Temperature {
	
	private long value;
	
	private TemperatureUnit unit;

	public Temperature(long value, TemperatureUnit unit) {
		this.value = value;
		this.unit = unit;
	}
	
	public TemperatureUnit getUnit() {
		return unit;
	}
	
	public long getValue() {
		return value;
	}
}
