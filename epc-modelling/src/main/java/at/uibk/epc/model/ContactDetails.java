package at.uibk.epc.model;

public class ContactDetails {

	private String email;
	
	private String phone;
	
	public ContactDetails() {
		// needed for MongoDB POJO Converter
	}
	
	public ContactDetails(String email, String phone) {
		this.email = email;
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getPhone() {
		return phone;
	}

	@Override
	public String toString() {
		return "ContactDetails [email=" + email + ", phone=" + phone + "]";
	}
}
