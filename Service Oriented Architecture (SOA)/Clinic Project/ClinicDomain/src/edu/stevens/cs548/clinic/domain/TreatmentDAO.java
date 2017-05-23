package edu.stevens.cs548.clinic.domain;

import javax.persistence.EntityManager;

public class TreatmentDAO implements ITreatmentDAO {
		private EntityManager em;
		public TreatmentDAO(EntityManager em){
			this.em=em;
		}
	public Treatment getTreatmentById(long Id) throws TreatmentExn {
		Treatment t=em.find(Treatment.class, Id);
		if(t==null)
			throw new TreatmentExn("Missing Treatment: id = "+Id);
		else 
		return t;
	}
    public void addTreatment(Treatment t) {
		em.persist(t);

	}
    public void deleteTreatments(Treatment t){
    	em.remove(t);
    }

}
