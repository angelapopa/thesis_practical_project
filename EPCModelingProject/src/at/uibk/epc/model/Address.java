package at.uibk.epc.model;

public class Address {
	
	private String street;
	
	private String streetNumber;
	
	private String stair;
	
	private String door;
	
	private String postalCode;
	
	private String city;
	
	private String country;
	
	public Address(String street, String streetNumber, String stair, String door, String postalCode, String city,
			String country) {
		super();
		this.street = street;
		this.streetNumber = streetNumber;
		this.stair = stair;
		this.door = door;
		this.postalCode = postalCode;
		this.city = city;
		this.country = country;
	}
	
	public String getStreet() {
		return street;
	}
	
	public String getStreetNumber() {
		return streetNumber;
	}
	
	public String getStair() {
		return stair;
	}
	
	public String getDoor() {
		return door;
	}
	
	public String getPostalCode() {
		return postalCode;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getCountry() {
		return country;
	}
}
