package at.uibk.epc.model;

public class Address {
	
	private String street;
	
	private String streetNumber;
	
	private String stair;
	
	private String door;
	
	private String postalCode;
	
	private String city;
	
	private String country;
	
	public Address() {
		//needed by MongoDB
	}
	
	public Address(String street, String streetNumber, String stair, String door, String postalCode, String city,
			String country) {
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

	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getStreetNumber() {
		return streetNumber;
	}
	
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
	
	public String getStair() {
		return stair;
	}
	
	public void setStair(String stair) {
		this.stair = stair;
	}
	
	public String getDoor() {
		return door;
	}
	
	public void setDoor(String door) {
		this.door = door;
	}
	
	public String getPostalCode() {
		return postalCode;
	}
	
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Address [street=" + street + ", streetNumber=" + streetNumber + ", stair=" + stair + ", door=" + door
				+ ", postalCode=" + postalCode + ", city=" + city + ", country=" + country + "]";
	}
	
	
}
