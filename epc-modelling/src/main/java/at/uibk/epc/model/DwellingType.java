package at.uibk.epc.model;

public enum DwellingType {
	BUSINESS_CENTER("business-center"),
	APARTMENT_BUILDING("apartment-building"),
	DETACHED_HOUSE("detached-house"),
	FLAT("flat");
	
    private final String type;

    DwellingType(String type) {
        this.type = type;
    }
    
    public String getDwellingType() {
        return this.type;
    }
}
