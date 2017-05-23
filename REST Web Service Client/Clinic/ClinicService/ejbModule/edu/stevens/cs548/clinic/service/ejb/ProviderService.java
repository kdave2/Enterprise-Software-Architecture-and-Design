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
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.IProviderFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO;
import edu.stevens.cs548.clinic.domain.ITreatmentExporter;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.PatientService.TreatmentExporter;

/**
 * Session Bean implementation class PatientService
 */
@Stateless(name = "ProviderServiceBean")
public class ProviderService implements IProviderServiceLocal, IProviderServiceRemote {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(ProviderService.class.getCanonicalName());

	private IProviderFactory providerFactory;

	private ProviderDtoFactory providerDtoFactory;

	private IProviderDAO providerDAO;

	private IPatientDAO patientDAO;

	public ProviderService() {
		providerFactory = new ProviderFactory();
		providerDtoFactory = new ProviderDtoFactory();
	}

	@Inject
	@ClinicDomain
	private EntityManager em;

	@PostConstruct
	private void initialize() {
		providerDAO = new ProviderDAO(em);
		patientDAO = new PatientDAO(em);
	}

	@Override
	public long addProvider(ProviderDto dto) throws ProviderServiceExn {
		try {
			Provider provider = providerFactory.createProvider(dto.getProviderId(), dto.getName(),
					dto.getSpecialization());
			providerDAO.addProvider(provider);
			return provider.getId();
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public ProviderDto getProvider(long id) throws ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProvider(id);
			ProviderDto dto = providerDtoFactory.createProviderDto(provider);
			return dto;

		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}

	}

	@Override
	public ProviderDto getProviderByProId(long pid) throws ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProviderById(pid);
			ProviderDto dto = providerDtoFactory.createProviderDto(provider);
			return dto;

		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

	public class TreatmentExporter implements ITreatmentExporter<TreatmentDto> {

		private ObjectFactory factory = new ObjectFactory();

		@Override
		public TreatmentDto exportDrugTreatment(long tid, String diagnosis, String drug, float dosage) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			DrugTreatmentType drugInfo = factory.createDrugTreatmentType();
			drugInfo.setDosage(dosage);
			drugInfo.setName(drug);
			dto.setDrugTreatment(drugInfo);
			return dto;
		}

		@Override
		public TreatmentDto exportRadiology(long tid, String diagnosis, List<Date> dates) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			RadiologyType radiologyInfo = factory.createRadiologyType();
			for (Date radiologyDate : dates) {
				radiologyInfo.getDate().add(radiologyDate);
			}
			dto.setRadiology(radiologyInfo);
			return dto;
		}

		@Override
		public TreatmentDto exportSurgery(long tid, String diagnosis, Date date) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			SurgeryType surgeryInfo = factory.createSurgeryType();
			surgeryInfo.setDate(date);
			dto.setSurgery(surgeryInfo);
			return dto;

		}

	}

	@Override
	public long addTreatment(TreatmentDto tdto) throws ProviderServiceExn {
		long tid = 0;
		try {
			Provider provider = providerDAO.getProvider(tdto.getProvider());
			Patient patient = patientDAO.getPatient(tdto.getPatient());
			if (tdto.getDrugTreatment() != null) {
				tid = provider.addDrugTreatment(tdto.getDiagnosis(), tdto.getDrugTreatment().getName(),
						tdto.getDrugTreatment().getDosage(), patient);
			} else if (tdto.getRadiology() != null) {
				tid = provider.addRadiology(tdto.getRadiology().getDate(), patient, tdto.getDiagnosis());
			} else if (tdto.getSurgery() != null) {
				tid = provider.addSurgery(tdto.getDiagnosis(), tdto.getSurgery().getDate(), patient);
			}

		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		} catch (PatientExn e) {
			throw new ProviderServiceExn(e.toString());
		}
		return tid;
	}

	@Override
	public List<Treatment> getTreatments(long id) throws ProviderExn {
		// Export treatment DTO from provider aggregate

		Provider provider = providerDAO.getProvider(id);
		return provider.getTreatments();
	}

	@Override
	public TreatmentDto getTreatment(long id, long tid) throws TreatmentNotFoundExn, ProviderServiceExn {

		try {
			Provider prov = providerDAO.getProvider(id);
			TreatmentExporter visitor = new TreatmentExporter();
			return prov.exportTreatment(tid, visitor);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		} catch (TreatmentExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Resource(name = "SiteInfoPro")
	private String siteInformation;

	@Override
	public String siteInfoPro() {
		return siteInformation;
	}

	@Override
	public void deleteProvider() {
		providerDAO.deleteProviders();

	}

}
