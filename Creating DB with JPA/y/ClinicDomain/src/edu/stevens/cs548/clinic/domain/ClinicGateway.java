/* 
 * Clinic GateWay Main Class
 */

package edu.stevens.cs548.clinic.domain;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ClinicGateway implements IClinicGateway {

	public IPatientFactory getPatientFactory() {
		return new PatientFactory();
	}
	public IProviderFactory getProviderFactory() {
		return new ProviderFactory();
	}
	private EntityManagerFactory emf;
	public IPatientDAO getPatientDAO() {
		EntityManager em=emf.createEntityManager();
		return new PatientDAO(em);
	}
	public IProviderDAO getProviderDAO() {
		EntityManager em=emf.createEntityManager();
		return new ProviderDAO(em);
	}
	public ClinicGateway(){
		emf=javax.persistence.Persistence.createEntityManagerFactory("ClinicDomain");
	}
	
}
