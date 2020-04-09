package at.uibk.epc.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BuildingAddress extends Address{

	private ClimateData climateData;
	
	public BuildingAddress() {
		// needed by MongoDB POJO Converter
	}

	public BuildingAddress(Address address, ClimateData climateData) {
		super(address);
		this.climateData = climateData;
	}
	
	public ClimateData getClimateData() {
		return climateData;
	}
	
	public void setClimateData(ClimateData climateData) {
		this.climateData = climateData;
	}
}
