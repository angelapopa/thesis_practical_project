package at.uibk.epc.model;

public class ThermalData {
	
	private Temperature idealIndoorTemperature;
	
	private int heatingDaysPerYear;
	
	public ThermalData() {
		// needed by MongoDB POJO Converter
	}
	
	public ThermalData(Temperature idealIndoorTemperature, int heatingDaysPerYear) {
		this.idealIndoorTemperature = idealIndoorTemperature;
		this.heatingDaysPerYear = heatingDaysPerYear;
	}

	public Temperature getIdealIndoorTemperature() {
		return idealIndoorTemperature;
	}
	
	public void setIdealIndoorTemperature(Temperature idealIndoorTemperature) {
		this.idealIndoorTemperature = idealIndoorTemperature;
	}
	
	public int getHeatingDaysPerYear() {
		return heatingDaysPerYear;
	}
	
	public void setHeatingDaysPerYear(int heatingDaysPerYear) {
		this.heatingDaysPerYear = heatingDaysPerYear;
	}

	@Override
	public String toString() {
		return "ThermalData [idealIndoorTemperature=" + idealIndoorTemperature + ", heatingDaysPerYear="
				+ heatingDaysPerYear + "]";
	}
}
