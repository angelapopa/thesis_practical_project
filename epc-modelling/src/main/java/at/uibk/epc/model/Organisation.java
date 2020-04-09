package at.uibk.epc.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Organisation {

	private String name;
	
	private Address address;
	
	private ContactDetails contactDetails;
	
	public Organisation() {
		// needed by MongoDB POJO Converter
	}
	
	public Organisation(String name, Address address, ContactDetails contactDetails) {
		this.name = name;
		this.address = address;
		this.contactDetails = contactDetails;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public ContactDetails getContactDetails() {
		return contactDetails;
	}
	
	public void setContactDetails(ContactDetails contactDetails) {
		this.contactDetails = contactDetails;
	}

	@Override
	public String toString() {
		return "Organisation [name=" + name + ", address=" + address + ", contactDetails=" + contactDetails + "]";
	}
}
