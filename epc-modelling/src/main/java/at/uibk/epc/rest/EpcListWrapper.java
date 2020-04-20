package at.uibk.epc.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import at.uibk.epc.model.EPC;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EpcListWrapper {

	@XmlElement(name = "epcs")
	private List<EPC> wrappedEpcs;

	public List<EPC> getWrappedEpcs() {
		return wrappedEpcs;
	}

	public void setWrappedEpcs(List<EPC> wrappedEpcs) {
		this.wrappedEpcs = wrappedEpcs;
	}

}
