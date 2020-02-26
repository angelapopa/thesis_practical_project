package at.uibk.epc.model;

public class Temperature {
	
	private long value;
	
	private TemperatureUnit unit;
	
	public Temperature() {
		// needed by MongoDB POJO Converter
	}

	public Temperature(long value, TemperatureUnit unit) {
		this.value = value;
		this.unit = unit;
	}
	
	public TemperatureUnit getUnit() {
		return unit;
	}
	
	public void setUnit(TemperatureUnit unit) {
		this.unit = unit;
	}
	
	public long getValue() {
		return value;
	}
	
	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Temperature [value=" + value + ", unit=" + unit + "]";
	}
}
