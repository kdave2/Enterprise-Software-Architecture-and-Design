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
	public ProviderDAO(EntityManager em){
		this.em=em;
		this.treatmentDAO=new TreatmentDAO(em);
	}
}
