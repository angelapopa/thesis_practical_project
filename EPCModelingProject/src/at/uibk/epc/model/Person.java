package at.uibk.epc.model;

public class Person {

	private String surname;
	
	private String firstname;
	
	public Person(String surname, String firstname) {
		this.surname = surname;
		this.firstname = firstname;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public String getSurname() {
		return surname;
	}
}
