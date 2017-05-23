package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;

import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.TreatmentNotFoundExn;

public interface IPatientService {
		public class PatientServiceExn extends Exception {
			private static final long serialVersionUID = 1L;
			public PatientServiceExn(String msg)
			{
				super(msg);
			}
		}
		public class PatientNotFoundExn extends PatientServiceExn {
			private static final long serialVersionUID = 1L;
			public PatientNotFoundExn(String m)
			{
				super(m);
			}
		}
		public class TreatmentNotFoundExn extends PatientServiceExn {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public TreatmentNotFoundExn(String m)
			{
				super(m);
			}
		}
		public long createPatient(String name, Date dob, long patientId) throws PatientServiceExn, PatientExn;
		public PatientDto getPatientByDbId(long id) throws PatientServiceExn;
		public PatientDto getPatientByPatientId(long pid) throws PatientServiceExn;
		public PatientDto[] getPatientsByNameDob (String name,Date dob);
		public void deletePatient(long pid) throws PatientServiceExn;
		public void addDrugTreatment(long id, String diagnosis, String drug, float dosage) throws PatientNotFoundExn;
		public TreatmentDto[] getTreatments(long id, long[] tids) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn; 
		public void deleteTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;
		public String siteInfo();
		TreatmentDto getTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;
		void addSurgery(long id, String diagnosis, Date date) throws PatientNotFoundExn;
		void addRadiology(long id, String diagnosis, List<Date> dates) throws PatientNotFoundExn;
	
}
