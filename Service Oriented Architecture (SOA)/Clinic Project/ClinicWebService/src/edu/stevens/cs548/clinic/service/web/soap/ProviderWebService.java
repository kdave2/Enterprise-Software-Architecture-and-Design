package edu.stevens.cs548.clinic.service.web.soap;

import java.util.List;

import javax.ejb.EJB;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceRemote;

@WebService(
		endpointInterface = "edu.stevens.cs548.clinic.service.web.soap.IProviderWebService", 
		targetNamespace = "http://cs548.stevens.edu/clinic/service/web/soap", 
		serviceName = "ProviderWeb", portName = "ProviderWebPort")

public class ProviderWebService implements IProviderWebService {

	@EJB(beanName="ProviderServiceBean")
	IProviderServiceRemote providerService;
	
	public void createProvider(long id, String name, String specialization) {
		providerService.createProvider(id, name, specialization);

	}

	public long getProviderById(long pid) throws ProviderServiceExn, ProviderExn {
		return pid;
		//return ((IProviderService) providerService).getProviderById(pid);
	}

	public ProviderDto getProviderByProviderId(long pid) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		return null;
	}

	public void addDrugTreatment(long pid, String diagnosis, String drug, float dosage)
			throws ProviderServiceExn, ProviderExn {
	
		providerService.addDrugTreatment(pid, diagnosis, drug, dosage);
	}

	public void addRadiology(long pid, String diagnosis) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		providerService.addRadiology(pid, diagnosis);
	}

	public void addSurgery(long pid, String diagnosis) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		providerService.addSurgery(pid, diagnosis);
	}

	public List<Treatment> getTreatment(long tid) throws ProviderServiceExn, PatientServiceExn {
		return  providerService.getTreatment(tid);
	}

	

}
