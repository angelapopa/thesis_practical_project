package at.uibk.epc.model;

public class SpatialData {
	
	private Measure totalFloorArea;
	
	private Measure totalVolume;
	
	private String orientation;
	
	public SpatialData(Measure totalFloorArea, Measure totalVolume, String orientation) {
		this.totalFloorArea = totalFloorArea;
		this.totalVolume = totalVolume;
		this.orientation = orientation;
	}

	public Measure getTotalFloorArea() {
		return totalFloorArea;
	}
	
	public Measure getTotalVolume() {
		return totalVolume;
	}
	
	public String getOrientation() {
		return orientation;
	}

}
