/*
 * Treatment Visitor-who request treatment records- Interface
 */
package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

public interface ITreatmentVisitor {

	public void visitDrugTreatment(long tid, long pid, String diagnosis, String drug, float dosage);

	public void visitRadiology(long tid, long pid, String diagnosis, List<RadiologyDateValues> dates);

	public void visitSurgery(long tid, long pid, String diagnosis, Date date);
}
