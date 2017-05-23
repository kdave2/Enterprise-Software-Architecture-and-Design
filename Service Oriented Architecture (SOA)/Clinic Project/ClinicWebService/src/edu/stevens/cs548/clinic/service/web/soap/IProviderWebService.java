package edu.stevens.cs548.clinic.service.web.soap;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;

@WebService(
		name="IProviderWebPort",
		targetNamespace="http://cs548.stevens.edu/clinic/service/web/soap")

@SOAPBinding(
		style=SOAPBinding.Style.DOCUMENT,
		use=SOAPBinding.Use.LITERAL,
		parameterStyle=SOAPBinding.ParameterStyle.WRAPPED)

public interface IProviderWebService {

	@WebMethod
	public void createProvider(long id,String name,String specialization);
	@WebMethod
	public long getProviderById(long pid) throws ProviderServiceExn, ProviderExn;
	@WebMethod
	public ProviderDto getProviderByProviderId(long pid) throws ProviderServiceExn;
	@WebMethod
	public void addDrugTreatment(long pid, String diagnosis, String drug, float dosage) throws ProviderServiceExn, ProviderExn;
	@WebMethod
	public void addRadiology(long pid, String diagnosis) throws ProviderServiceExn;
	@WebMethod
	public void addSurgery(long pid, String diagnosis) throws ProviderServiceExn;
	@WebMethod
	List<Treatment> getTreatment(long tid) throws ProviderServiceExn, PatientServiceExn;
}
