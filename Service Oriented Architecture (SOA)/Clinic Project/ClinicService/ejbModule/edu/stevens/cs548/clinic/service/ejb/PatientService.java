package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IPatientFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentExporter;
import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;

/**
 * Session Bean implementation class PatientService
 */
@Stateless(name="PatientServiceBean")
public class PatientService implements IPatientServiceLocal,IPatientServiceRemote , IPatientService
{
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PatientService.class.getCanonicalName());

	private IPatientFactory patientFactory;
	
	private PatientDtoFactory patientDtoFactory;

	private IPatientDAO patientDAO;
	
	public PatientService() {
		patientFactory=new PatientFactory();
	}
	
	@Inject @ClinicDomain
	private EntityManager em;
	
	@PostConstruct
	private void initialize()
	{
		patientDAO=new PatientDAO(em);
	}
	

	/**
	 * @see IPatientService#addPatient(String, Date, long)
	 */
	
	/**
	 * @see IPatientService#getPatient(long)
	 */

	@Override
	public PatientDto getPatientByDbId(long id) throws PatientServiceExn {
		
			try
			{
				Patient patient=patientDAO.getPatient(id);
				return patientDtoFactory.createPatientDto(patient);
			}
			catch(PatientExn e)
			{
				throw new PatientServiceExn(e.toString());
			}
	}

	@Override
	public PatientDto getPatientByPatientId(long pid) throws PatientServiceExn {
		try
		{
			Patient patient=patientDAO.getPatientByPatientId(pid);
			return patientDtoFactory.createPatientDto(patient);
		}
		catch(PatientExn e)
		{
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public PatientDto[] getPatientsByNameDob(String name, Date dob) {
		List<Patient> patients=patientDAO.getPatientByNameDob(name, dob);
		PatientDto[] dto=new PatientDto[patients.size()];
		for(int i=0;i<dto.length;i++)
			dto[i]= patientDtoFactory.createPatientDto(patients.get(i));
		return dto;
	}
	
	@Override
	public long createPatient(String name, Date dob, long patientId) throws PatientServiceExn, PatientExn {
		// TODO Auto-generated method stub
		Patient patient=patientFactory.createPatient(patientId, name, dob, age);
		try{
			patientDAO.addPatient(patient);
		}catch(PatientExn e)
		{
			throw new PatientServiceExn(e.toString());
		}
		return patient.getId();
	}

	
	@Override
	public void deletePatient(long pid) throws PatientServiceExn {
		try
		{
			Patient patient=patientDAO.getPatient(pid);
			patientDAO.deletePatient(patient);
		}
		catch(PatientExn e){
			throw new PatientServiceExn(e.toString());
		}
		
		
	}
	
	public class TreatmentExporter implements ITreatmentExporter<TreatmentDto> {
		
		private ObjectFactory factory = new ObjectFactory();
		
		@Override
		public TreatmentDto exportDrugTreatment(long tid, String diagnosis, String drug,
				float dosage) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			DrugTreatmentType drugInfo = factory.createDrugTreatmentType();
			drugInfo.setDosage(dosage);
			drugInfo.setName(drug);
			return dto;
		}

		@Override
		public TreatmentDto exportRadiology(long tid, String diagnosis, List<Date> dates) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			RadiologyType radiologyInfo = factory.createRadiologyType();
			radiologyInfo.setTreatmentDates(dates);
			
			return dto;
		}

		@Override
		public TreatmentDto exportSurgery(long tid, String diagnosis, Date date) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			SurgeryType surgeryInfo = factory.createSurgeryType();
			surgeryInfo.setDateOfSurgery(date);
			//dto.setSurgery(surgeryInfo);
			return dto;
		}
		
	}
	
	@Override
	public TreatmentDto[] getTreatments(long id, long[] tids)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		try {
			Patient patient=patientDAO.getPatient(id);
			TreatmentDto[] treatments=new TreatmentDto[tids.length];
			TreatmentPDOToDTO visitor= new TreatmentPDOToDTO();
			for(int i=0;i<tids.length;i++){
					
					patient.visitTreatment(tids[i], visitor);
					treatments[i]=visitor.getDTO();
			}
			return treatments;
		} catch (PatientExn e) {
			// TODO Auto-generated catch block
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			// TODO Auto-generated catch block
			throw new PatientServiceExn(e.toString());
		}
	}
	
	@Override
	public TreatmentDto getTreatment(long id, long tid)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		// Export treatment DTO from patient aggregate
		try {
			Patient patient = patientDAO.getPatient(id);
			TreatmentExporter visitor = new TreatmentExporter();
			return patient.exportTreatment(tid, visitor);
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}
static class TreatmentPDOToDTO implements ITreatmentVisitor{
		
		private TreatmentDto dto;
		public TreatmentDto getDTO()
		{
			return dto;
		}

		@Override
		public void visitDrugTreatment(long tid, Provider provider, String diagnosis, String drug, float dosage) {
			// TODO Auto-generated method stub
			dto= new TreatmentDto();
			dto.setId(tid);
			dto.setDiagnosis(diagnosis);
			dto.setProvider(provider.getProviderId());
			DrugTreatmentType drugInfo=new DrugTreatmentType();
			drugInfo.setName(drug);
			drugInfo.setDosage(dosage);
			//dto.setDrugTreatment(drugInfo);
		}

		@Override
		public void visitSurgery(long tid, Provider provider, String diagnosis, Date date) {
			// TODO Auto-generated method stub
			dto= new TreatmentDto();
			dto.setId(tid);
			dto.setDiagnosis(diagnosis);
			dto.setProvider(provider.getProviderId());
			SurgeryType surgeryInfo=new SurgeryType();
			surgeryInfo.setDateOfSurgery(date);
			//dto.setSurgery(surgeryInfo);
		}

		@Override
		public void visitRadiology(long tid, Provider provider, String diagnosis, List<Date> date) {
			// TODO Auto-generated method stub
			dto.setId(tid);
			dto.setDiagnosis(diagnosis);
			dto.setProvider(provider.getProviderId());
			RadiologyType radiologyInfo=new RadiologyType();
			radiologyInfo.setTreatmentDates(date);
			//dto.setRadiology(radiologyInfo);
		}

		
	}

	// TODO inject resource value
	@Resource(name="SiteInfo")
	private String siteInformation;

	private int age;
	
	@Override
	public String siteInfo() {
		return siteInformation;
	}

	
	public void addDrugTreatment(long id, String diagnosis, String drug, float dosage) throws PatientNotFoundExn {
		// TODO Auto-generated method stub
		try
		{ 	
			Patient patient=patientDAO.getPatient(id);
			patient.addDrugTreatment(null, diagnosis, drug, dosage);
		}	catch(PatientExn e)
		{
			throw new PatientNotFoundExn(e.toString());
		}
	}
	@Override
	public void addRadiology(long id,String diagnosis, List<Date> dates) throws PatientNotFoundExn {
		
		try
		{ 	
			Patient patient=patientDAO.getPatient(id);
			patient.addRadiology(null, diagnosis, dates);
		}	catch(PatientExn e)
		{
			throw new PatientNotFoundExn(e.toString());
		}
	}
	@Override
	public void addSurgery(long id, String diagnosis, Date date) throws PatientNotFoundExn {
		// TODO Auto-generated method stub
		try
		{ 	
			Patient patient=patientDAO.getPatient(id);
			patient.addSurgery(null, diagnosis, date);
		}	catch(PatientExn e)
		{
			throw new PatientNotFoundExn(e.toString());
		}
	}
	
	@Override
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		// TODO Auto-generated method stub
		try{
			Patient patient=patientDAO.getPatient(id);
			patient.deleteTreatment(tid);
		}catch(PatientExn e)
		{
			throw new PatientNotFoundExn(e.toString());
		}
		catch(TreatmentExn e)
		{
			throw new PatientServiceExn(e.toString());
		}
	}

}
