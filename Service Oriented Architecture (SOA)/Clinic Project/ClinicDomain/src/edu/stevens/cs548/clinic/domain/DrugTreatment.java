/* 
 * Drug Treatment Class
 */
package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;



@Entity
@DiscriminatorValue("DT")
public class DrugTreatment extends Treatment implements Serializable {

	private static final long serialVersionUID = 1L;

	private String drug;
	private double dosage;

	public String getDrug() {
		return drug;
	}

	public void setDrug(String drug) {
		this.drug = drug;
	}

	public double getDosage() {
		return dosage;
	}

	public void setDosage(double dosage2) {
		this.dosage = dosage2;
	}
	public void visit(ITreatmentVisitor visitor){
		visitor.visitDrugTreatment( this.getId(),
									this.getDiagnosis(),
									this.drug, 
									this.dosage);
	}

	public DrugTreatment() {
		super();
		this.setTreatmentType("DT");
	}

	@Override
	public <T> T export(ITreatmentExporter<T> visitor) {
		return visitor.exportDrugTreatment(this.getId(), this.getDiagnosis(), this.getDrug(), this.getDosage());
	}

}
