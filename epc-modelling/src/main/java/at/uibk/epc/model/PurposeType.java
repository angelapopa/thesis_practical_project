package at.uibk.epc.model;

public enum PurposeType {
	SALE("sale"),
	RENTAL("rental"), //"letting" is used in Ireland
	NEW_DWELLING("new dwelling"),
	RENOVATION("renovation"),
	VOLUNTARY("voluntary"),
	OTHER("other");
	
	private final String description;
	
	PurposeType(String description) {
		this.description = description;
	}
	
	public static PurposeType approximateValue(String value){
		for (PurposeType purpose : PurposeType.values()) {
			if (value.contains(purpose.description)){
				return purpose;
			}
		}
		return PurposeType.OTHER;
	}
}
