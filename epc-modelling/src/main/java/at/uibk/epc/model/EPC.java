package at.uibk.epc.model;

import java.util.Date;

import org.bson.types.ObjectId;

public class EPC {
	
	private ObjectId id; 

	private String identificationNumber;
	
	private Date creationDate;
	
	private Date validUntil;
	
	private Assessor assessor;
	
	private Dwelling ratedDwelling;
	
	private PurposeType purpose;
	
	private Rating awordedRating;
	
	private Rating potentialRating;
	
	private RatingMethodology usedMethodology;
	
	private String legalReference;
	
	public EPC() {
		// needed By MongoSB POJO Converter
	}
	
	public EPC(String identificationNumber, Date creationDate, Date validUntil, Dwelling ratedDwelling,
			Assessor assessor, Rating awordedRating, Rating potentialRating, RatingMethodology usedMethodology, String legalReference) {
		this.identificationNumber = identificationNumber;
		this.creationDate = creationDate;
		this.validUntil = validUntil;
		this.ratedDwelling = ratedDwelling;
		this.awordedRating = awordedRating;
		this.potentialRating = potentialRating;
		this.assessor = assessor;
		this.usedMethodology = usedMethodology;
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
	
	public Rating getAwordedRating() {
		return awordedRating;
	}
	
	public void setAwordedRating(Rating rating) {
		this.awordedRating = rating;
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
	
	public PurposeType getPurpose() {
		return purpose;
	}
	
	public void setPurpose(PurposeType purpose) {
		this.purpose = purpose;
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
				+ ", validUntil=" + validUntil + ", assessor=" + assessor + ", ratedDwelling=" + ratedDwelling
				+ ", purpose=" + purpose + ", awordedRating=" + awordedRating + ", potentialRating=" + potentialRating
				+ ", usedMethodology=" + usedMethodology + ", legalReference=" + legalReference + "]";
	}
	
}
