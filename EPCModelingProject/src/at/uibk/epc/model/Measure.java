package at.uibk.epc.model;

public class Measure {
	
	private long value;
	
	private MeasuringUnit unit;

	public Measure(long value, MeasuringUnit unit) {
		super();
		this.value = value;
		this.unit = unit;
	}
	
	public MeasuringUnit getUnit() {
		return unit;
	}
	
	public long getValue() {
		return value;
	}
}
