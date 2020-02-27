package at.uibk.epc.model;

public class RatingMethodology {

	private String standardName;
	
	private String calculationName;
	
	private String calculationMethod;
	
	private Software softwareUsed;
	
	public RatingMethodology() {
		//needed for MongoDB POJO Converter
	}
	
	public RatingMethodology(String standardName, String calculationName, String calculationMethod,
			Software softwareUsed) {
		super();
		this.standardName = standardName;
		this.calculationName = calculationName;
		this.calculationMethod = calculationMethod;
		this.softwareUsed = softwareUsed;
	}



	public String getCalculationName() {
		return calculationName;
	}
	
	public void setCalculationName(String calculationName) {
		this.calculationName = calculationName;
	}
	
	public String getStandardName() {
		return standardName;
	}
	
	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}

	public String getCalculationMethod() {
		return calculationMethod;
	}
	
	public Software getSoftwareUsed() {
		return softwareUsed;
	}
	
	public void setCalculationMethod(String calculationMethod) {
		this.calculationMethod = calculationMethod;
	}
	
	public void setSoftwareUsed(Software softwareUsed) {
		this.softwareUsed = softwareUsed;
	}
}
