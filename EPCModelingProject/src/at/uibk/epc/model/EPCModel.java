package at.uibk.epc.model;

import java.math.BigInteger;
import java.util.Date;

public class EPCModel {

	private BigInteger identificationNumber;
	
	private Date creationDate;
	
	private Date validUntil;
	
	private Dwelling ratedDwelling;
	
	private PurposeType ratingPurpose;
	
	private Rating rating;
	
	private Rating potentialRating;

	private Assessor assessor;
	
	private RatingMethodology usedMethodology;
	
	private String legalReference;
	
	public EPCModel(BigInteger identificationNumber, Date creationDate, Date validUntil, Dwelling ratedDwelling,
			Rating rating, Assessor assessor, String legalReference) {
		super();
		this.identificationNumber = identificationNumber;
		this.creationDate = creationDate;
		this.validUntil = validUntil;
		this.ratedDwelling = ratedDwelling;
		this.rating = rating;
		this.assessor = assessor;
		this.legalReference = legalReference;
	}

	public BigInteger getIdentificationNumber() {
		return identificationNumber;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public Date getValidUntil() {
		return validUntil;
	}
	
	public Assessor getAssessor() {
		return assessor;
	}
	
	public Rating getRating() {
		return rating;
	}
	
	public Dwelling getRatedDwelling() {
		return ratedDwelling;
	}
	
	public String getLegalReference() {
		return legalReference;
	}
	
	public Rating getPotentialRating() {
		return potentialRating;
	}
	
	public void setPotentialRating(Rating potentialRating) {
		this.potentialRating = potentialRating;
	}
	
	public PurposeType getRatingPurpose() {
		return ratingPurpose;
	}
	
	public RatingMethodology getUsedMethodology() {
		return usedMethodology;
	}
	
}
