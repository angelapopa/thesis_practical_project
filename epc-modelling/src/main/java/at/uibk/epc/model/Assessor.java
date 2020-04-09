package at.uibk.epc.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Assessor extends Person {
//TODO: should authorisation Numer and identification Number be one field?
	private String authorisationNumber;
	
	private String identificationNumber;
	
	private Organisation organisation;
	
	public Assessor() {
		// needed by MongoDB POJO Converter
		super();
	}
	
	public Assessor(Person person,  String authorisationNumber, String identificationNumber, Organisation organisation) {
		super(person.getTitle(), person.getSurname(), person.getFirstname(), person.getContactDetails());
		this.authorisationNumber = authorisationNumber;
		this.identificationNumber = identificationNumber;
		this.organisation = organisation;
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
	
	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
	
	@Override
	public String toString() {
		return "Assessor [authorisationNumber=" + authorisationNumber + ", identificationNumber=" + identificationNumber
				+ ", organisation=" + organisation + "]";
	}
	
}
