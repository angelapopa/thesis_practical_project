package at.uibk.epc.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Building specific data
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ThermalData {
		
	private Measure uValue;
	
	/**
	 * The estimated amount of energy required at source, before conversion (Units: kWh/m²/year).
	 */
	private Measure primaryEnergyDemand;
	
	/**
	 * The estimated amount of energy after conversion. (Units: kWh/m²/year).
	 */
	private Measure finalEnergyDemand;
	
	/**
	 * The estimated heat demand for provision of hot water (Units: kWh per year).
	 */
	private Measure waterHeatingEnergyDemand;
	
	/**
	 * The estimated heat demand for space heating (Units: kWh per year).
	 */
	private Measure spaceHeatingEnergyDemand;

	/**
	 * The measured amount of energy required at source, before conversion (Units: kWh/m²/year).
	 */
	private Measure primaryEnergyConsumption;
	
	/**
	 * The measured amount of energy after conversion. (Units: kWh/m²/year).
	 */
	private Measure finalEnergyConsumption;
	
	/**
	 * The measured heat demand for provision of hot water (Units: kWh per year).
	 */
	private Measure waterHeatingEnergyConsumption;
	
	/**
	 * The measured heat demand for space heating (Units: kWh per year).
	 */
	private Measure spaceHeatingEnergyConsumption;
	
	/**
	 * Assuming this is water + space heating
	 */
	private Measure thermalEnergyConsumption;
	
	/**
	 * Lighting and appliances, but might also be used for heating.
	 */
	private Measure electricalEnergyConsumption;
	
	/**
	 * Assuming this is water + space heating
	 */
	private Measure thermalEnergyDemand;
	
	/**
	 * Lighting and appliances, but might also be used for heating.
	 */
	private Measure electricalEnergyDemand;
	
	/**
	 * Main heating fuel type
	 */
	private FuelType mainHeatingFuelType;
	
	/**
	 * The total annual emissions (heating, cooling, lighting and ventilating) (Units: kg per year).
	 */
	private Measure carbonFootprint;
	
	public ThermalData() {
		// needed by MongoDB POJO Converter
	}

	public Measure getUValue() {
		return uValue;
	}
	
	public void setUValue(Measure uValue) {
		this.uValue = uValue;
	}
	
	public Measure getCarbonFootprint() {
		return carbonFootprint;
	}
	
	public void setCarbonFootprint(Measure carbonFootprint) {
		this.carbonFootprint = carbonFootprint;
	}
	
	public Measure getPrimaryEnergyDemand() {
		return primaryEnergyDemand;
	}
	
	public void setPrimaryEnergyDemand(Measure primaryEnergyDemand) {
		this.primaryEnergyDemand = primaryEnergyDemand;
	}
	
	public Measure getFinalEnergyDemand() {
		return finalEnergyDemand;
	}
	
	public void setFinalEnergyDemand(Measure finalEnergyDemand) {
		this.finalEnergyDemand = finalEnergyDemand;
	}
	
	public Measure getSpaceHeatingEnergyDemand() {
		return spaceHeatingEnergyDemand;
	}
	
	public void setSpaceHeatingEnergyDemand(Measure spaceHeatingEnergyDemand) {
		this.spaceHeatingEnergyDemand = spaceHeatingEnergyDemand;
	}
	
	public Measure getWaterHeatingEnergyDemand() {
		return waterHeatingEnergyDemand;
	}

	public void setWaterHeatingEnergyDemand(Measure waterHeatingEnergyDemand) {
		this.waterHeatingEnergyDemand = waterHeatingEnergyDemand;
	}
	
	public Measure getFinalEnergyConsumption() {
		return finalEnergyConsumption;
	}
	
	public void setFinalEnergyConsumption(Measure finalEnergyConsumption) {
		this.finalEnergyConsumption = finalEnergyConsumption;
	}
	
	public Measure getPrimaryEnergyConsumption() {
		return primaryEnergyConsumption;
	}
	
	public void setPrimaryEnergyConsumption(Measure primaryEnergyConsumption) {
		this.primaryEnergyConsumption = primaryEnergyConsumption;
	}
	
	public Measure getSpaceHeatingEnergyConsumption() {
		return spaceHeatingEnergyConsumption;
	}
	
	public void setSpaceHeatingEnergyConsumption(Measure spaceHeatingEnergyConsumption) {
		this.spaceHeatingEnergyConsumption = spaceHeatingEnergyConsumption;
	}
	
	public Measure getWaterHeatingEnergyConsumption() {
		return waterHeatingEnergyConsumption;
	}
	
	public void setWaterHeatingEnergyConsumption(Measure waterHeatingEnergyConsumption) {
		this.waterHeatingEnergyConsumption = waterHeatingEnergyConsumption;
	}
	
	public Measure getElectricalEnergyDemand() {
		return electricalEnergyDemand;
	}
	
	public void setElectricalEnergyDemand(Measure electricalEnergyDemand) {
		this.electricalEnergyDemand = electricalEnergyDemand;
	}
	
	public Measure getThermalEnergyDemand() {
		return thermalEnergyDemand;
	}
	
	public void setThermalEnergyDemand(Measure thermalEnergyDemand) {
		this.thermalEnergyDemand = thermalEnergyDemand;
	}
	
	public Measure getElectricalEnergyConsumption() {
		return electricalEnergyConsumption;
	}
	
	public void setElectricalEnergyConsumption(Measure electricalEnergyConsumption) {
		this.electricalEnergyConsumption = electricalEnergyConsumption;
	}
	
	public Measure getThermalEnergyConsumption() {
		return thermalEnergyConsumption;
	}
	
	public void setThermalEnergyConsumption(Measure thermalEnergyConsumption) {
		this.thermalEnergyConsumption = thermalEnergyConsumption;
	}
	
	public FuelType getMainHeatingFuelType() {
		return mainHeatingFuelType;
	}
	
	public void setMainHeatingFuelType(FuelType mainHeatingFuelType) {
		this.mainHeatingFuelType = mainHeatingFuelType;
	}

	@Override
	public String toString() {
		return "ThermalData [uValue=" + uValue + ", primaryEnergyDemand=" + primaryEnergyDemand + ", finalEnergyDemand="
				+ finalEnergyDemand + ", waterHeatingEnergyDemand=" + waterHeatingEnergyDemand
				+ ", spaceHeatingEnergyDemand=" + spaceHeatingEnergyDemand + ", primaryEnergyConsumption="
				+ primaryEnergyConsumption + ", finalEnergyConsumption=" + finalEnergyConsumption
				+ ", waterHeatingEnergyConsumption=" + waterHeatingEnergyConsumption
				+ ", spaceHeatingEnergyConsumption=" + spaceHeatingEnergyConsumption + ", thermalEnergyConsumption="
				+ thermalEnergyConsumption + ", electricalEnergyConsumption=" + electricalEnergyConsumption
				+ ", thermalEnergyDemand=" + thermalEnergyDemand + ", electricalEnergyDemand=" + electricalEnergyDemand
				+ ", mainHeatingFuelType=" + mainHeatingFuelType + ", carbonFootprint=" + carbonFootprint + "]";
	}
}
