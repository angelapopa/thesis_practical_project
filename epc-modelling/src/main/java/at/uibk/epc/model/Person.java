package at.uibk.epc.model;

public class Person {
	
	private String title;

	private String surname;
	
	private String firstname;
	
	private ContactDetails contactDetails;
	
	public Person() {
		// needed by MongoDB POJO Converter
	}
	
	public Person(String title, String surname, String firstname, ContactDetails contactDetails) {
		this.title = title;
		this.surname = surname;
		this.firstname = firstname;
		this.contactDetails = contactDetails;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public ContactDetails getContactDetails() {
		return contactDetails;
	}

	@Override
	public String toString() {
		return "Person [title=" + title + ", surname=" + surname + ", firstname=" + firstname + ", contactDetails="
				+ contactDetails + "]";
	}

}
