package at.uibk.epc.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Rating {
	
	public String ratingLevel;
	
	public Double ratingPoints;
	
	public Rating() {
		// needed by MongoDB POJO Converter
	}

	public Rating(String ratingLevel, Double ratingPoints) {
		super();
		this.ratingLevel = ratingLevel;
		this.ratingPoints = ratingPoints;
	}
	
	public String getRatingLevel() {
		return ratingLevel;
	}
	
	public Double getRatingPoints() {
		return ratingPoints;
	}

	@Override
	public String toString() {
		return "Rating [ratingLevel=" + ratingLevel + ", ratingPoints=" + ratingPoints + "]";
	}
	
}