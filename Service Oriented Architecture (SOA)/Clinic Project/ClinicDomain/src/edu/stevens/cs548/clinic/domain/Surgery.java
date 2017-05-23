package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@DiscriminatorValue("SU")
public class Surgery extends Treatment implements Serializable {

	private static final long serialVersionUID = 1L; 

	private String diagnosis;
	@Temporal(TemporalType.DATE)
	private Date date;

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public void visit(ITreatmentVisitor visitor){
		visitor.visitSurgery( this.getId(),
									this.getDiagnosis(),
									this.getDate());
	}
	public Surgery() {
		super();
		this.setTreatmentType("SU");
	}

	@Override
	public <T> T export(ITreatmentExporter<T> visitor) {
		return visitor.exportSurgery(this.getId(),this.getDiagnosis(), this.getDate());
	}
}