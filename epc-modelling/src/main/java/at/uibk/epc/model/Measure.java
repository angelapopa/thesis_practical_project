package at.uibk.epc.model;

public class Measure {

	private Double value;
	
	private MeasuringUnit unit;
	
	public Measure() {
		//needed by MongoDB
	}

	public Measure(Double value, MeasuringUnit unit) {
		this.value = value;
		this.unit = unit;
	}
	
	public MeasuringUnit getUnit() {
		return unit;
	}
	
	public void setUnit(MeasuringUnit unit) {
		this.unit = unit;
	}
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Measure [value=" + value + ", unit=" + unit + "]";
	}
}
