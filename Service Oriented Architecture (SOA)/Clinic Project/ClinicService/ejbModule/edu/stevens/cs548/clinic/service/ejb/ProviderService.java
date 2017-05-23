package edu.stevens.cs548.clinic.service.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.domain.IProviderFactory;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.PatientService.TreatmentExporter;

/**
 * Session Bean implementation class ProviderService
 */
@Stateless
public class ProviderService implements IProviderServiceRemote,IProviderServiceLocal,IProviderService{

    IProviderFactory providerFactory;
    ProviderDtoFactory providerDtoFactory;
    IProviderDAO providerDAO;
    IPatientDAO patientDAO;
    public ProviderService() {
        providerFactory=new ProviderFactory();
    }

    @Inject @ClinicDomain
	private EntityManager em;
    
    public void initialize()
    {
    	providerDAO=new ProviderDAO(em);
    }
    
	@Override
	public void createProvider(String id,String name,String specialization) throws ProviderExn
	{
		try {
	
		Provider provider=providerFactory.createProvider(id, name, specialization);
			providerDAO.addProvider(provider);
	}
	 catch (Exception e)
		{
		// TODO Auto-generated catch block
		throw new ProviderExn(e.toString());
	}
	}


	@Override
	public ProviderDto getProviderById(long pid) throws ProviderServiceExn, ProviderExn {
		try
		{
			Provider provider=providerDAO.getProvider(pid);
			return providerDtoFactory.createProviderDto(provider);
		}
		catch(ProviderExn e)
		{
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public ProviderDto getProviderByProviderId(String pid) throws ProviderServiceExn {
		try
		{
			Provider provider=providerDAO.getProviderByNPI(pid);
			return providerDtoFactory.createProviderDto(provider);
		}
		catch(ProviderExn e)
		{
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public void addDrugTreatment(long pid, String diagnosis, String drug, float dosage) throws ProviderServiceExn, ProviderExn {
		try
		{ 	
			Provider provider=providerDAO.getProvider(pid);
			provider.addTreatment(null);
		}	catch(ProviderExn e)
		{
			throw new ProviderServiceExn(e.toString());
		}
		
	}

	@Override
	public void addRadiology(long pid, String diagnosis, String drug, float dosage) throws ProviderServiceExn {
		try
		{ 	
			Provider provider=providerDAO.getProvider(pid);
			provider.addTreatment(null);
		}	catch(ProviderExn e)
		{
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public void addSurgery(long pid, String diagnosis) throws ProviderServiceExn {
		try
		{ 	
			Provider provider=providerDAO.getProvider(pid);
			provider.addTreatment(null);
		}	catch(ProviderExn e)
		{
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public List<Treatment> getTreatment(long pid) throws ProviderServiceExn, PatientServiceExn {
		try {
			Provider provider = providerDAO.getProvider(pid);
			return	provider.getTreatments();
		} 
		catch (ProviderExn e) 
		{
			throw new PatientServiceExn(e.toString());
		}
	
	}

	@Override
	public void addRadiology(long pid, String diagnosis) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createProvider(long id, String name, String specialization) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProviderDto getProviderByProviderId(long pid) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		return null;
	}

	
}
