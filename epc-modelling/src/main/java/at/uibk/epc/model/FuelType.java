package at.uibk.epc.model;

public enum  FuelType{
	OIL("Oil"),
	BIOMASS("Biomass"),
	ELECTRICITY("Electricity"),
	GAS("Gas"),
	WOOD("wood");
	
	String description;
	
	private FuelType(String description) {
		this.description = description;
	}
	
	public static FuelType approximateValue(String value){
		for (FuelType fuelType : FuelType.values()) {
			if ((value.toLowerCase().contains((fuelType.description.toLowerCase())))){
				return fuelType;
			}
		}
		return null;
	}
}
