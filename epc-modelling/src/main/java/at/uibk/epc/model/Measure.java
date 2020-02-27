package at.uibk.epc.model;

public class Measure {

	private double value;
	
	private MeasuringUnit unit;
	
	public Measure() {
		//needed by MongoDB
	}

	public Measure(double value, MeasuringUnit unit) {
		this.value = value;
		this.unit = unit;
	}
	
	public MeasuringUnit getUnit() {
		return unit;
	}
	
	public void setUnit(MeasuringUnit unit) {
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
		return "Measure [value=" + value + ", unit=" + unit + "]";
	}
}
