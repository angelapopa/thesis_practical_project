package at.uibk.epc.model;

public class Dwelling {

	private Address address;
	
	private int constructionYear;
	
	private DwellingType type;
	
	private String identificationNumber;
	
	//not building specific
	private ClimateData climateData;
	
	private SpatialData spatialData;
	
	private ThermalData thermalData;

	private Photo photo;
	
	public Dwelling(Address address, int constructionYear, DwellingType type, String identificationNumber, SpatialData spatialData) {
		this.address = address;
		this.type = type;
		this.constructionYear = constructionYear;
		this.identificationNumber = identificationNumber;
		this.spatialData = spatialData;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public int getConstructionYear() {
		return constructionYear;
	}
	
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	
	public DwellingType getType() {
		return type;
	}
	
	public Photo getPhoto() {
		return photo;
	}
	
	public SpatialData getSpatialData() {
		return spatialData;
	}
	
	public ClimateData getClimateData() {
		return climateData;
	}
	
	public ThermalData getThermalData() {
		return thermalData;
	}
}
