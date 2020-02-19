package at.uibk.epc.model;

public class ThermalData {
	
	private Temperature idealIndoorTemperature;
	
	private byte heatingDaysPerYear;
	
	private String orientation;
	
	public Temperature getIdealIndoorTemperature() {
		return idealIndoorTemperature;
	}
	
	public byte getHeatingDaysPerYear() {
		return heatingDaysPerYear;
	}
	
	public String getOrientation() {
		return orientation;
	}
}
