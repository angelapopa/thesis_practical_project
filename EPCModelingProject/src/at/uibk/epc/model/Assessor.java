package at.uibk.epc.model;

public class Assessor extends Person {

	private String authorisationNumber;
	
	private String identificationNumber;
	
	private Address corporateAddress;
		
	public Assessor(String surname, String firstname, String authorisationNumber, String identificationNumber,
			Address corporateAddress) {
		super(surname, firstname);
		this.authorisationNumber = authorisationNumber;
		this.identificationNumber = identificationNumber;
		this.corporateAddress = corporateAddress;
	}

	public String getAuthorisationNumber() {
		return authorisationNumber;
	}
	
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	
	public Address getCorporateAddress() {
		return corporateAddress;
	}
}
