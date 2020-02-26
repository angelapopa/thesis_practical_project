package at.uibk.epc.model;

import java.math.BigInteger;
import java.util.Date;

import org.bson.types.ObjectId;

public class EPC {
	
	private ObjectId id; 

	private String identificationNumber;
	
	private Date creationDate;
	
	private Date validUntil;
	
	private Dwelling ratedDwelling;
	
	private PurposeType ratingPurpose;
	
	private Rating rating;
	
	private Rating potentialRating;

	private Assessor assessor;
	
	private RatingMethodology usedMethodology;
	
	private String legalReference;
	
	public EPC() {
		// needed By MongoSB POJO Converter
	}
	
	public EPC(String identificationNumber, Date creationDate, Date validUntil, Dwelling ratedDwelling,
			Rating rating, Assessor assessor, String legalReference) {
		this.identificationNumber = identificationNumber;
		this.creationDate = creationDate;
		this.validUntil = validUntil;
		this.ratedDwelling = ratedDwelling;
		this.rating = rating;
		this.assessor = assessor;
		this.legalReference = legalReference;
	}
	
	public ObjectId getId() {
		return id;
	}
	
	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}
	
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public Date getValidUntil() {
		return validUntil;
	}
	
	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}
	
	public Assessor getAssessor() {
		return assessor;
	}
	
	public void setAssessor(Assessor assessor) {
		this.assessor = assessor;
	}
	
	public Rating getRating() {
		return rating;
	}
	
	public void setRating(Rating rating) {
		this.rating = rating;
	}
	
	public Dwelling getRatedDwelling() {
		return ratedDwelling;
	}
	
	public void setRatedDwelling(Dwelling ratedDwelling) {
		this.ratedDwelling = ratedDwelling;
	}
	
	public String getLegalReference() {
		return legalReference;
	}
	
	public void setLegalReference(String legalReference) {
		this.legalReference = legalReference;
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
	
	public void setRatingPurpose(PurposeType ratingPurpose) {
		this.ratingPurpose = ratingPurpose;
	}
	
	public RatingMethodology getUsedMethodology() {
		return usedMethodology;
	}
	
	public void setUsedMethodology(RatingMethodology usedMethodology) {
		this.usedMethodology = usedMethodology;
	}

	@Override
	public String toString() {
		return "EPC [id=" + id + ", identificationNumber=" + identificationNumber + ", creationDate=" + creationDate
				+ ", validUntil=" + validUntil + ", ratedDwelling=" + ratedDwelling + ", ratingPurpose=" + ratingPurpose
				+ ", rating=" + rating + ", potentialRating=" + potentialRating + ", assessor=" + assessor
				+ ", usedMethodology=" + usedMethodology + ", legalReference=" + legalReference + "]";
	}
	
	
}
