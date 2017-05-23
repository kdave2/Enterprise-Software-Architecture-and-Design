package edu.stevens.cs548.clinic.service.ejb;

import java.util.List;

import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;

public interface IProviderService {
	
	public class ProviderServiceExn extends Exception 
	{
		private static final long serialVersionUID = 1L;
		public ProviderServiceExn(String msg)
		{
			super(msg);
		}
	}
	
	public void createProvider(long id,String name,String specialization);
	public ProviderDto getProviderById(long pid) throws ProviderServiceExn, ProviderExn;
	public ProviderDto getProviderByProviderId(long pid) throws ProviderServiceExn;
	public void addDrugTreatment(long pid, String diagnosis, String drug, float dosage) throws ProviderServiceExn, ProviderExn;
	public void addRadiology(long pid, String diagnosis) throws ProviderServiceExn;
	public void addSurgery(long pid, String diagnosis) throws ProviderServiceExn;
	List<Treatment> getTreatment(long tid) throws ProviderServiceExn, PatientServiceExn;
	void createProvider(String id, String name, String specialization) throws ProviderExn;
	ProviderDto getProviderByProviderId(String pid) throws ProviderServiceExn;
	void addRadiology(long pid, String diagnosis, String drug, float dosage) throws ProviderServiceExn;
	
}
