package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

public interface ITreatmentExporter<T> 
{
	public T exportDrugTreatment(long id, String diagnosis, String drug, double dosage);
	public T exportSurgery (long id, String diagnosis, Date date);
	public T exportRadiology(long id, String diagnosis, List<Date> dates);
}