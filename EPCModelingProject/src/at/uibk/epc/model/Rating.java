package at.uibk.epc.model;

public class Rating {
	
	public String ratingLevel;
	
	public byte ratingPoints;

	public Rating(String ratingLevel, byte ratingPoints) {
		super();
		this.ratingLevel = ratingLevel;
		this.ratingPoints = ratingPoints;
	}

	public byte getRating() {
		return ratingPoints;
	}
	
	public String getRatingLevel() {
		return ratingLevel;
	}
}