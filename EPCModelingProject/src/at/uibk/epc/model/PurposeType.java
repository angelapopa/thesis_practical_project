package at.uibk.epc.model;

public enum PurposeType {
	RENTING_OR_SELLING("rent/sell");
	
	private final String type;
	
	PurposeType(String type) {
		this.type = type;
	}
}
