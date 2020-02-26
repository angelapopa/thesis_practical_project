package at.uibk.epc.model;

public class Measure {

	private long value;
	
	private MeasuringUnit unit;
	
	public Measure() {
		//needed by MongoDB
	}

	public Measure(long value, MeasuringUnit unit) {
		this.value = value;
		this.unit = unit;
	}
	
	public MeasuringUnit getUnit() {
		return unit;
	}
	
	public void setUnit(MeasuringUnit unit) {
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
		return "Measure [value=" + value + ", unit=" + unit + "]";
	}
}
