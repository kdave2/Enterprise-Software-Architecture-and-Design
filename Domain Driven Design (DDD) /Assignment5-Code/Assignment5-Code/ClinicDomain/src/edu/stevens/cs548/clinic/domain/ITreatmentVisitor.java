/*
 * Treatment Visitor-who request treatment records- Interface
 */
package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

public interface ITreatmentVisitor {
		
	public void visitDrugTreatment	(long tid,
									 String diagnosis,
								     String Drug,
								     float dosage);
	public void visitRadiology( long tid,
								String diagnosis,
								List<Date> dates);
	public void visitSurgery( long tid,
							  String diagnosis,
							  Date date);
}
