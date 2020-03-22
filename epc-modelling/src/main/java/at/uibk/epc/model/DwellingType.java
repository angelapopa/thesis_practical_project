package at.uibk.epc.model;

public enum DwellingType {
	BUSINESS_CENTER("business-center"),
	APARTMENT_BUILDING("apartment-building"),
	HOUSE("house"),
	FLAT("flat"),
	BUNGALOW("bungalow"),
	MAISONETTE("maisonette");
	
    private final String type;

    DwellingType(String type) {
        this.type = type;
    }
    
    public String getDwellingType() {
        return this.type;
    }
}
