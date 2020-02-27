package at.uibk.epc.model;

public class Temperature {
	
	private double value;
	
	private TemperatureUnit unit;
	
	public Temperature() {
		// needed by MongoDB POJO Converter
	}

	public Temperature(double value, TemperatureUnit unit) {
		this.value = value;
		this.unit = unit;
	}
	
	public TemperatureUnit getUnit() {
		return unit;
	}
	
	public void setUnit(TemperatureUnit unit) {
		this.unit = unit;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Temperature [value=" + value + ", unit=" + unit + "]";
	}
}
