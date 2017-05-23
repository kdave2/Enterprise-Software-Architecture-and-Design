/*
 * Provider DAO main class
 */
package edu.stevens.cs548.clinic.domain;

public interface IProviderDAO {
	
			public static class ProviderExn extends Exception {

				private static final long serialVersionUID = 1L;
				public ProviderExn(String msg){
					super(msg);
				}
			}
			public Provider getProviderByProviderId(long pid) throws ProviderExn;
			public void addProvider(Provider pro) throws ProviderExn;
			public void deleteProvider(Provider pro) throws ProviderExn;
			
	}


