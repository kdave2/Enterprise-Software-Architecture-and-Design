package edu.stevens.cs548.clinic.domain;

public class ProviderFactory implements IProviderFactory {

	public Provider createProvider(long pid, String name, String specialization) {
		// TODO Auto-generated method stub
		Provider pro=new Provider();
			pro.setProviderId(pid);
			pro.setName(name);
			pro.setSpecialz(specialization);
		return null;
	}

}
