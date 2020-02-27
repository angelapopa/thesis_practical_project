package at.uibk.epc.model;

import java.util.List;

import org.bson.types.ObjectId;

public class Dwelling {

	private ObjectId id;

	private Address address;

	private int constructionYear;
	
	private DwellingType type;
	
	private String identificationNumber;
	
	private List<String> parcelNumers;
	
	private SpatialData spatialData;
	
	private ThermalData thermalData;
	
	//not building specific
	private ClimateData climateData;

	private Photo photo;
	
	public Dwelling() {
		//needed by MongoDB
	}
	
	public Dwelling(Address address, int constructionYear, DwellingType type, String identificationNumber, List<String> parcelNumbers, SpatialData spatialData, ThermalData thermalData) {
		this.address = address;
		this.type = type;
		this.constructionYear = constructionYear;
		this.parcelNumers = parcelNumbers;
		this.identificationNumber = identificationNumber;
		this.spatialData = spatialData;
		this.thermalData = thermalData;
	}
	
	//getters and setters, are also needed by the MongoDB POJO converter
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public int getConstructionYear() {
		return constructionYear;
	}
	
	public void setConstructionYear(int constructionYear) {
		this.constructionYear = constructionYear;
	}
	
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	
	public List<String> getParcelNumers() {
		return parcelNumers;
	}
	
	public void setParcelNumers(List<String> parcelNumers) {
		this.parcelNumers = parcelNumers;
	}
	
	public DwellingType getType() {
		return type;
	}
	
	public void setType(DwellingType type) {
		this.type = type;
	}
	
	public Photo getPhoto() {
		return photo;
	}
	
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	
	public SpatialData getSpatialData() {
		return spatialData;
	}
	
	public void setSpatialData(SpatialData spatialData) {
		this.spatialData = spatialData;
	}
	
	public ClimateData getClimateData() {
		return climateData;
	}
	
	public void setClimateData(ClimateData climateData) {
		this.climateData = climateData;
	}
	
	public ThermalData getThermalData() {
		return thermalData;
	}
	
	public void setThermalData(ThermalData thermalData) {
		this.thermalData = thermalData;
	}

	@Override
	public String toString() {
		return "Dwelling [id=" + id + ", address=" + address + ", constructionYear=" + constructionYear + ", type="
				+ type + ", identificationNumber=" + identificationNumber + ", parcelNumers=" + parcelNumers
				+ ", spatialData=" + spatialData + ", thermalData=" + thermalData + ", climateData=" + climateData
				+ ", photo=" + photo + "]";
	}
	
}
