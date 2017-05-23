package edu.stevens.cs548.clinic.domain;

public interface ITreatmentFactory {
	public Treatment createTreatment(long pid, String name, String specialization);
}
