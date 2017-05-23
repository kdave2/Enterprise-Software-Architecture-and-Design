/*
 * Patient DAO Class implements its interface
 */
package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class PatientDAO implements IPatientDAO {
	
	private EntityManager em;
	private TreatmentDAO treatmentDAO;

	public Patient getPatientByDbId(long id) throws PatientExn {
		Patient p =em.find(Patient.class, id);
		if(p==null)
		{
			throw new PatientExn("Patient not found: primary key=:"+id);
		}
		else {
			p.setTreatmentDAO(this.treatmentDAO);
			return p;
		}
		
	}
	public Patient getPatientByPatientId(long pid) throws PatientExn {
		TypedQuery<Patient> query=em.createNamedQuery("SearchPatientByPatientId", Patient.class)
				.setParameter("pid", pid);
			List<Patient> patients=query.getResultList();
			if(patients.size()>1)
				throw new PatientExn("Duplicate Patient record: "+pid);
			else if(patients.size()<=0)
				throw new PatientExn("No record:"+pid);
			else {
				Patient p = patients.get(0);
				p.setTreatmentDAO(this.treatmentDAO);
				return p;
			}
			
	}

	

	public List<Patient> getPatientByNameDob(String name, Date dob) {
		TypedQuery<Patient> query=em.createNamedQuery("SearchPatientByNameDob", Patient.class)
				.setParameter("name", name)
				.setParameter("dob", dob);
		List<Patient> patients=query.getResultList();
		for(Patient p:patients){
			p.setTreatmentDAO(this.treatmentDAO);
		}
		return patients;
	}
	public void addPatient(Patient patient) throws PatientExn {
		long pid=patient.getPatientId();
		TypedQuery<Patient> query=em.createNamedQuery("SearchPatientByPatientId", Patient.class)
				.setParameter("pid", pid);
			List<Patient> patients=query.getResultList();
			if(patients.size()<1){
				em.persist(patient);
				patient.setTreatmentDAO(this.treatmentDAO);
			}
			else {
				Patient patient2=patients.get(0);
				throw new PatientExn("Insertion: Patient with patient id: ("+pid+") already exists.\n** Name: "+patient2.getName());
			}
	
		
	}
	public void deletePatient(Patient patient) throws PatientExn {
		//em.createQuery("delete from treatment t where t.patient.id=:id")
		//	.setParameter("id", patient.getId())
		//	.executeUpdate();
		em.remove(patient);
	}
	public PatientDAO(EntityManager em){
		this.em=em;
		this.treatmentDAO=new TreatmentDAO(em);
	}
}
