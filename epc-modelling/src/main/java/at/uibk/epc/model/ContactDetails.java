package at.uibk.epc.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
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
