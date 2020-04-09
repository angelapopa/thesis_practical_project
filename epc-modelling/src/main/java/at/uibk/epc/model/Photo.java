package at.uibk.epc.model;

import java.sql.Blob;

import javax.xml.bind.annotation.XmlTransient;

public class Photo {

	private String name;

	//not sure here how an image would be saved
	private Blob image;
	
	public Photo() {
		// TODO Auto-generated constructor stub
	}
	
	@XmlTransient
	public Blob getImage() {
		return image;
	}
	public String getName() {
		return name;
	}
	
	public void setImage(Blob image) {
		this.image = image;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
