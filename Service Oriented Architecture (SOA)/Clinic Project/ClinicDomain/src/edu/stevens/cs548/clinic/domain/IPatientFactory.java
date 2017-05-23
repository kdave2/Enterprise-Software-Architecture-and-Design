/*
 *Patient Factory Interface 
 */
package edu.stevens.cs548.clinic.domain;

import java.util.Date;

import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;

public interface IPatientFactory {

	public Patient createPatient(long pid, String name, Date dob,int age) throws PatientExn;
}
