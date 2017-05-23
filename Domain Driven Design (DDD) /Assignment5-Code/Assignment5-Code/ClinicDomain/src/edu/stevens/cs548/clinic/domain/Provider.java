/*
 * Provider Main Class
 */
package edu.stevens.cs548.clinic.domain;

import static javax.persistence.CascadeType.REMOVE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;

@NamedQueries({
	@NamedQuery(
		name="SearchProviderByProviderId",
		query="select provider from Provider provider where provider.ProviderId = :pid"),
	@NamedQuery(
		name="CountProviderByProviderId",
		query="select count(provider) from Provider provider where provider.ProviderId = :pid"),
	@NamedQuery(
		name = "RemoveAllProviders", 
		query = "delete from Provider provider")
})

@Entity 
@Table(name="PROVIDER")
public class Provider implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private long id;
	
	private long ProviderId;
	
	private String name;
	
	private String specialization;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSpecialz(){
		return specialization;
	}
	
	public void setSpecialz(String specialization){
		this.specialization=specialization;
	}
	public long getProviderId() {
		return ProviderId;
	}

	public void setProviderId(long ProviderId) {
		this.ProviderId = ProviderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(mappedBy = "provider", cascade = REMOVE)
	@OrderBy
	private List<Treatment> treatments;

	protected List<Treatment> getTreatments() {
		return treatments;
	}

	protected void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}
	@Transient
	private ITreatmentDAO treatmentDAO;
	public void setTreatmentDAO(ITreatmentDAO tdao){
		this.treatmentDAO=tdao;
	}
	 public void addTreatment (Treatment t)
	  {
		  this.treatmentDAO.addTreatment(t);
	  		this.getTreatments().add(t);
	  		if(t.getProvider()!=this)
	  		 t.setProvider(this);
	  }
	 public void addDrugTreatment (String diagnosis,String drug, float dosage){
		 
		 /*........*/
		 DrugTreatment treatment =new DrugTreatment();
		 treatment.setDiagnosis(diagnosis);
		 treatment.setDrug(drug);
		 treatment.setDosage(dosage);
		 this.addTreatment(treatment);
	 }

	 public List<Long> getTreatmentIds(){
		 List<Long> tids=new ArrayList<Long>();
		 for(Treatment t:this.getTreatments()){
			 tids.add(t.getId());
		 }
		 return tids;
	 }
	public void deleteTreatment(long tid) throws TreatmentExn{
		Treatment t=treatmentDAO.getTreatmentById(tid);
		 if(t.getProvider()==this){
			 throw new TreatmentExn("Inappropriate treatment access: provider = "+id+",treatment = "+tid);
		 }
		 treatmentDAO.deleteTreatments(t);
	}
	
	public void visitTreatment(long tid, ITreatmentVisitor visitor	) throws TreatmentExn{
		
		 Treatment t=treatmentDAO.getTreatmentById(tid);
		 if(t.getProvider()==this){
			 throw new TreatmentExn("Inappropriate treatment access: provider = "+id+",treatment = "+tid);
		 }
		 t.visit(visitor);
	}

public Provider() {
	super();
	treatments=new ArrayList<Treatment>();
}
	
}