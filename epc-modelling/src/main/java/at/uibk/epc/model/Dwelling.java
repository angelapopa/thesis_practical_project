package at.uibk.epc.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.bson.types.ObjectId;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Dwelling {

	private ObjectId id;

	private BuildingAddress buildingAddress;

	private Integer constructionYear;
	
	private DwellingType type;
	
	private String identificationNumber;
	
	private List<String> parcelNumers;
	
	private SpatialData spatialData;
	
	private ThermalData thermalData;

	@XmlElement(name = "dwellingPhoto")
	private Photo photo;
	
	public Dwelling() {
		//needed by MongoDB
	}
	
	public Dwelling(BuildingAddress buildingAddress, Integer constructionYear, DwellingType type, String identificationNumber, List<String> parcelNumbers, SpatialData spatialData, ThermalData thermalData) {
		this.buildingAddress = buildingAddress;
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
	
	public BuildingAddress getBuildingAddress() {
		return buildingAddress;
	}
	
	public void setBuildingAddress(BuildingAddress buildingAddress) {
		this.buildingAddress = buildingAddress;
	}
	
	public Integer getConstructionYear() {
		return constructionYear;
	}
	
	public void setConstructionYear(Integer constructionYear) {
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
	
	public ThermalData getThermalData() {
		return thermalData;
	}
	
	public void setThermalData(ThermalData thermalData) {
		this.thermalData = thermalData;
	}

	@Override
	public String toString() {
		return "Dwelling [id=" + id + ", buildingAddress=" + buildingAddress + ", constructionYear=" + constructionYear
				+ ", type=" + type + ", identificationNumber=" + identificationNumber + ", parcelNumers=" + parcelNumers
				+ ", spatialData=" + spatialData + ", thermalData=" + thermalData + ", photo=" + photo + "]";
	}
	
}
