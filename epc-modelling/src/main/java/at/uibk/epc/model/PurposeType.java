package at.uibk.epc.model;

public enum PurposeType {
	RENTING_OR_SELLING("rent/sell"),
	NEW_BUILDNG_COMISSIONING("comissioning of a new building"),
	RENOVATION("renovation"),
	VOLUNTARY("voluntary"),
	OTHER("other");
	
	private final String type;
	
	PurposeType(String type) {
		this.type = type;
	}
}
