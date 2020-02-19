package at.uibk.epc.model;

public enum MeasuringUnit {
	FEET("feet"),
	METER("meter"),
	SQUARE_FEET("square_feet"),
	CUBIC_METER("cubic_meter");
	
	//add some others
	
	private String unit;
	
	private MeasuringUnit(String unit) {
		this.unit = unit;
	}
	
}
