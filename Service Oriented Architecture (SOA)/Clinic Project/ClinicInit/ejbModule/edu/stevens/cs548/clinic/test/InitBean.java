package edu.stevens.cs548.clinic.test;

import java.util.Calendar;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;
import edu.stevens.cs548.clinic.service.ejb.ClinicDomain;
//import edu.stevens.cs548.clinic.service.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceLocal;



/**
 * Session Bean implementation class TestBean
 */
@Singleton
@LocalBean
@Startup
public class InitBean {

	private static Logger logger = Logger.getLogger(InitBean.class.getCanonicalName());
	
	@EJB
	IPatientServiceLocal patientService;
	@EJB
	IProviderServiceLocal providerService;
	
	/**
	 * Default constructor.
	 */
	public InitBean() {
	}
	
	@Inject @ClinicDomain
	private EntityManager em;
	
	

	
	@PostConstruct
	private void init() {
		/*
		 * Put your testing logic here. Use the logger to display testing output in the server logs.
		 */
		logger.info("Yugaank");
		IPatientDAO patientDAO = new PatientDAO(em);
		IProviderDAO providerDAO=new ProviderDAO(em);
		PatientFactory patientFactory = new PatientFactory();
		TreatmentFactory treatmentFactory=new TreatmentFactory();
		
		ProviderFactory providerFactory = new ProviderFactory();
		
		try {

			Calendar calendar = Calendar.getInstance();
			calendar.set(1985, 9, 4);
						
			/*
			 * Clear the database and populate with fresh data.
			 * 
			 * If we ensure that deletion of patients cascades deletes of treatments,
			 * then we only need to delete patients.
			 */
			
			patientDAO.deletePatients();
			logger.info("Patients have been removed");
			providerDAO.deleteProviders();
			logger.info("Providers have been removed");
			
			logger.info(calendar.getTime().toString());
		
			Patient john = patientFactory.createPatient(12345679L, "John Doe", calendar.getTime(), 31);
			
			
			logger.info("Patient Added "+john.getName()+" with id "+john.getId());
			
		
			Provider trent=providerFactory.createProvider("228350L", "Trent Cyrus", "Physician");
			logger.info("Provider name: "+trent.getName()+" added!");
			
		//	john.addDrugTreatment(trent, "Fever", "Crocin", 35);
			
			logger.info("Treatment Added");
			} 
		catch (Exception e) 
		{
			logger.info(e.toString());
		}
		
	}
}