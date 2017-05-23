/*
 * Provider DAO Class
 */
package edu.stevens.cs548.clinic.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;


public class ProviderDAO implements IProviderDAO {
	private EntityManager em;
	private TreatmentDAO treatmentDAO;
	
	
	public Provider getProviderByProviderId(long pid) throws ProviderExn {
		TypedQuery<Provider> query=em.createNamedQuery("SearchProviderByProviderId", Provider.class)
				.setParameter("pid", pid);
			List<Provider> providers=query.getResultList();
			if(providers.size()>1)
				throw new ProviderExn("Duplicate Provider record: "+pid);
			else if(providers.size()<=0)
				throw new ProviderExn("No record:"+pid);
			else {
				Provider provider = providers.get(0);
				provider.setTreatmentDAO(this.treatmentDAO);
				return provider;
			}
			
	}
	public Provider getProviderById(long id) throws ProviderExn {
		TypedQuery<Provider> query =em.createNamedQuery("SearchProviderById",Provider.class)
				.setParameter("id",  id);
		List<Provider> providers =query.getResultList();
		if(providers.size()>1)
			throw new ProviderExn("Duplicate Provider Record: "+id);
		else if(providers.size()<=0)
			throw new ProviderExn("No record "+id);
		else {
			Provider provider=providers.get(0);
			provider.setTreatmentDAO(this.treatmentDAO);
			return provider;
		}
	}
	
	public void addProvider(Provider provider) throws ProviderExn {
		long pid=provider.getProviderId();
		TypedQuery<Provider> query=em.createNamedQuery("SearchProviderByProviderId", Provider.class)
				.setParameter("pid", pid);
			List<Provider> providers=query.getResultList();
			if(providers.size()<1){
				em.persist(provider);
				provider.setTreatmentDAO(this.treatmentDAO);
			}
			else {
				Provider provider2=providers.get(0);
				throw new ProviderExn("Insertion: Provider with provider id: ("+pid+") already exists.\n** Name: "+provider2.getName());
			}
	
		
	}
	public void deleteProvider(Provider provider) throws ProviderExn {
		//em.createQuery("delete from treatment t where t.patient.id=:id")
		//	.setParameter("id", patient.getId())
		//	.executeUpdate();
		em.remove(provider);
	}
	/*	*/ 
	int exists=0;
	public void addTreatment(Treatment t, Patient pat)  {
		for(Treatment t1:pat.getTreatments())
		{
			if(t1==t)
			{
				exists=1;
			}
		}
		if(exists==0)
		{
			this.treatmentDAO.addTreatment(t);
		}
	}
	
	public List<Treatment> getTreatmentsByProviderId(long pid) throws ProviderExn {
		TypedQuery<Treatment> query =em.createNamedQuery("SearchProviderByProviderIdAndDisplayTreatments",Treatment.class)
				.setParameter("pid", pid);
		List<Treatment> treatments=query.getResultList();
		if(treatments.size()<=0)
			throw new ProviderExn("No record found "+pid);
		return treatments;
	}

	public void deleteAllProviders()
	{
		TypedQuery<Provider> query =em.createNamedQuery("DeleteAllProviders", Provider.class);
		query.executeUpdate();
	}
	public ProviderDAO(EntityManager em){
		this.em=em;
		this.treatmentDAO=new TreatmentDAO(em);
	}
	
	
}
