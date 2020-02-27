package at.uibk.epc.model;

import java.util.Date;

public class Software {
	
	private String softwareName;

	private String softwareVersion;
	
	private Date releaseDate;
	
	public Software() {
		// needed by MongoDB POJO Converter
	}
	
	public Software(String softwareName, String softwareVersion) {
		this.softwareName = softwareName;
		this.softwareVersion = softwareVersion;
	}

	public String getSoftwareName() {
		return softwareName;
	}
	
	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}
	
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
}
