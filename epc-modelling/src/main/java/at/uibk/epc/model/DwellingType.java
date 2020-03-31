package at.uibk.epc.model;

public enum DwellingType {
	APARTMENT_BUILDING("apartment-building"),
	HOUSE("house"),
	FLAT("flat", "apartment"), //An alternative would be to use EnumMap and keep more than one possible value for a key.
	BUNGALOW("bungalow"),
	MAISONETTE("maisonette"),
	NONDOMESTIC("non-domestic"), //schools, city halls, etc.
	OTHER("other");
	
    private final String type;
    private final String alternative;

    DwellingType(String type) {
        this.type = type;
        this.alternative = null;
    }
    
    DwellingType(String type, String alternative) {
        this.type = type;
        this.alternative = alternative;
    }
    
	public static DwellingType approximateValue(String value){
		for (DwellingType dwellingType : DwellingType.values()) {
			if ((value.toLowerCase().contains((dwellingType.type.toLowerCase())))){
				return dwellingType;
			}
		}
		for (DwellingType dwellingType : DwellingType.values()) {
			if ((dwellingType.alternative != null) && (value.toLowerCase().contains((dwellingType.alternative.toLowerCase())))){
				return dwellingType;
			}
		}
		return OTHER;
	}
}
