package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@DiscriminatorValue("RA")
public class Radiology extends Treatment implements Serializable {

	private static final long serialVersionUID = 1L;

	private String diagnosis; 
	@Temporal(TemporalType.DATE)
	@ElementCollection
	@CollectionTable(name="RadiologyDates")
	private List<Date> dates;

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public List<Date> getDates() {
		return dates;
	}

	public void setDates(List<Date> dates) {
		this.dates = dates;
	}
	@Override
	public void visit(ITreatmentVisitor visitor){
		visitor.visitRadiology( this.getId(),
									this.getDiagnosis(),
									this.getDates());
	}
	public Radiology() {
		super();
		this.setTreatmentType("RA");
	}

	@Override
	public <T> T export(ITreatmentExporter<T> visitor) {
		return visitor.exportRadiology(this.getId(), this.getDiagnosis(), this.getDates());
	}
}
