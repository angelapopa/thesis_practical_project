package at.uibk.epc.model;

public class Assessor extends Person {

	private String authorisationNumber;
	
	private String identificationNumber;
	
	private Address corporateAddress;
	
	public Assessor() {
		// needed by MongoDB POJO Converter
		super();
	}
		
	public Assessor(String title, String surname, String firstname, String authorisationNumber, String identificationNumber,
			Address corporateAddress) {
		super(title, surname, firstname);
		this.authorisationNumber = authorisationNumber;
		this.identificationNumber = identificationNumber;
		this.corporateAddress = corporateAddress;
	}

	//getter and setters needed also for MongoDB POJO Converter
	public String getAuthorisationNumber() {
		return authorisationNumber;
	}
	
	public void setAuthorisationNumber(String authorisationNumber) {
		this.authorisationNumber = authorisationNumber;
	}
	
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	
	public Address getCorporateAddress() {
		return corporateAddress;
	}

	public void setCorporateAddress(Address corporateAddress) {
		this.corporateAddress = corporateAddress;
	}
	
	@Override
	public String toString() {
		return "Assessor [authorisationNumber=" + authorisationNumber + ", identificationNumber=" + identificationNumber
				+ ", corporateAddress=" + corporateAddress + "]";
	}
	
}
