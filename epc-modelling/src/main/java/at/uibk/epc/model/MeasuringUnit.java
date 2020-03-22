package at.uibk.epc.model;

//TODO: maybe add a validation for correct measuring unit in the setters
public enum MeasuringUnit {
	FEET("ft","feet"),
	METER("m","meter"),
	SQUARE_METER("m^2", "square meters"),
	SQUARE_FEET("ft^2", "square feet"),
	CUBIC_METER("m^3", "cubic meters"),
	WATTS_SQUARE_METER_KELVIN("W/m^2K", "watts per square metre times kelvin"),
	KWH_SQUARE_METER_YEAR("kWh/m^2/yr","kilowattshour per square meter per year"), 
	KG_SQUARE_METER_YEAR("kg/m^2/yr", "kilogramms per square meter per year"), 
	KWH_YEAR("kWh/yr","kilowatt hour per year"),
	CELCIUS("C", "Celcius"), 
	FAHRENHEIT("K","Fahrenheit");
	
	private String unit;
	
	private String description;
	
	private MeasuringUnit(String unit, String description) {
		this.unit = unit;
		this.description = description;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public String getDescription() {
		return description;
	}

	private MeasuringUnit(String unit) {
		this.unit = unit;
	}
	
}
