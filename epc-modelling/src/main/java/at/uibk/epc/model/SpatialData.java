package at.uibk.epc.model;

public class SpatialData {
	
	private Measure totalFloorArea;
	
	private Measure totalVolume;
	
	private String orientation;
	
	public SpatialData() {
		//needed by MongoDB
	}
	
	public SpatialData(Measure totalFloorArea, Measure totalVolume, String orientation) {
		this.totalFloorArea = totalFloorArea;
		this.totalVolume = totalVolume;
		this.orientation = orientation;
	}

	public Measure getTotalFloorArea() {
		return totalFloorArea;
	}
	
	public void setTotalFloorArea(Measure totalFloorArea) {
		this.totalFloorArea = totalFloorArea;
	}
	
	public Measure getTotalVolume() {
		return totalVolume;
	}
	
	public void setTotalVolume(Measure totalVolume) {
		this.totalVolume = totalVolume;
	}
	
	public String getOrientation() {
		return orientation;
	}
	
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	@Override
	public String toString() {
		return "SpatialData [totalFloorArea=" + totalFloorArea + ", totalVolume=" + totalVolume + ", orientation="
				+ orientation + "]";
	}
}
