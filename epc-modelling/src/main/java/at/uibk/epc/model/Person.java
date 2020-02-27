package at.uibk.epc.model;

public class Person {
	
	private String title;

	private String surname;
	
	private String firstname;
	
	public Person() {
		// needed by MongoDB POJO Converter
	}
	
	public Person(String title, String surname, String firstname) {
		this.title = title;
		this.surname = surname;
		this.firstname = firstname;
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

	@Override
	public String toString() {
		return "Person [title=" + title + ", surname=" + surname + ", firstname=" + firstname + "]";
	}
	
}
