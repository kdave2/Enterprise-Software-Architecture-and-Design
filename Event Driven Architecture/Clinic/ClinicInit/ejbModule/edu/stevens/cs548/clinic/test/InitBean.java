package edu.stevens.cs548.clinic.test;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.persistence.EntityManager;

import edu.stevens.cs548.clinic.billing.domain.BillingRecordDAO;
import edu.stevens.cs548.clinic.billing.domain.IBillingRecordDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentDAO;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;
import edu.stevens.cs548.clinic.research.domain.IResearchDAO;
import edu.stevens.cs548.clinic.research.domain.ResearchDAO;
import edu.stevens.cs548.clinic.service.dto.patient.PatientDto;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.treatment.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDtoFactory;
import edu.stevens.cs548.clinic.service.ejb.ClinicDomain;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceLocal;

/**
 * Session Bean implementation class TestBean
 */
@Singleton
@LocalBean
@Startup
public class InitBean {

	private static Logger logger = Logger.getLogger(InitBean.class.getCanonicalName());

	/**
	 * Default constructor.
	 */
	public InitBean() {
	}

	@Inject
	@ClinicDomain
	EntityManager em;

	@Inject
	IPatientServiceLocal patientService;
	@Inject
	IProviderServiceLocal providerService;

	@PostConstruct
	private void init() {

		logger.info("Yugaank Sharma");

		try {

			Calendar calendar = Calendar.getInstance();
			calendar.set(1989, 4, 2);
			IBillingRecordDAO ibrDAO=new BillingRecordDAO(em);
			ibrDAO.deleteBillingRecords();
			IResearchDAO irDAO=new ResearchDAO(em);
			irDAO.deleteDrugTreatmentRecords();
			IPatientDAO patientDAO = new PatientDAO(em);
			IProviderDAO providerDAO = new ProviderDAO(em);
			@SuppressWarnings("unused")
			ITreatmentDAO treatmentDAO = new TreatmentDAO(em);

			PatientFactory patientFactory = new PatientFactory();
			ProviderFactory providerFactory = new ProviderFactory();
			TreatmentFactory treatmentFactory = new TreatmentFactory();

			patientDAO.deletePatients();

			Patient carl = patientFactory.createPatient(12345678, "carl letterman", calendar.getTime(), 27);
			patientDAO.addPatient(carl);

			logger.info("Added patient " + carl.getName() + " with id " + carl.getId());

			Provider penn = providerFactory.createProvider(00000001, "kenn penn", "cold");
			providerDAO.addProvider(penn);
			logger.info("Added provider " + penn.getName() + " with id " + penn.getId());

			Treatment drug = treatmentFactory.createDrugTreatment("Fever", "Crocin", 12);
			drug.setProvider(penn);

			Treatment surgery = treatmentFactory.createSurgery("help", new Date());
			surgery.setProvider(penn);

			carl.addTreatment(drug);
			logger.info("Added " + drug.getTreatmentType() + " treatment with id " + drug.getId() + " to "
					+ drug.getPatient().getName());

			carl.addTreatment(surgery);
			logger.info("Added " + surgery.getTreatmentType() + " treatment with id " + surgery.getId() + " to "
					+ surgery.getPatient().getName());

			PatientDtoFactory patFac = new PatientDtoFactory();
			PatientDto pdto = patFac.createPatientDto();
			pdto.setName("Jean Grey");
			pdto.setPatientId(54521);
			pdto.setDob(calendar.getTime());
			pdto.setAge(27);

			patientService.addDrugTreatment(carl.getId(), "abc", "xyz", 5, penn);

			long patId = patientService.addPatient(pdto);
			String pdtoName = patientService.getPatient(patId).getName();
			long pdtoId = patientService.getPatient(patId).getId();
			logger.info("Added patient " + pdtoName + " with id " + pdtoId);

			ProviderDtoFactory proFac = new ProviderDtoFactory();
			ProviderDto max = proFac.createProviderDto();
			max.setName("max chloe");
			max.setNpi(1215);
			max.setSpecialization("fever");
			long proId = providerService.addProvider(max);

			String maxName = providerService.getProvider(proId).getName();
			long maxId = providerService.getProvider(proId).getId();
			logger.info("Added provider " + maxName + " with id " + maxId);

			TreatmentDtoFactory treatFac = new TreatmentDtoFactory();

			TreatmentDto treatDto = treatFac.createDrugTreatmentDto();
			treatDto.setDiagnosis("some xyz problem");
			treatDto.setPatient(pdtoId);
			treatDto.setProvider(maxId);// getNpi()
			DrugTreatmentType drugTreatmentType = new DrugTreatmentType();
			drugTreatmentType.setName("some drug");
			drugTreatmentType.setDosage(25);
			treatDto.setDrugTreatment(drugTreatmentType);
//			treatDto.setId(658);
			logger.info("jms function incoming!!");
			long treatId = treatDto.getId();
			logger.info("treatid before function call: " + treatId);
			try {
				treatId = providerService.addTreatmentForPat(treatDto, patId, maxId);
			} catch (JMSException e) {
				logger.severe("Jms error");
				e.printStackTrace();
			}

			logger.info("patient's Id:" + patId + " and provider's NPI:" + providerService.getProvider(proId).getNpi()
					+ " add drug treatment with id:" + treatId);

			String diag = patientService.getTreatment(patId, treatId).getDiagnosis();
			logger.info("new treatment diagnosis is " + diag);

		} catch (ProviderExn | ProviderServiceExn e) {
			throw new IllegalStateException("Failed to add provider");
		} catch (PatientServiceExn | PatientExn e) {
			throw new IllegalStateException("Failed to add patient");
		}

	}

}
