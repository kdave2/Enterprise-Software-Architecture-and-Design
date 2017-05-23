/*
 * Treatment DAO Interface
 */
package edu.stevens.cs548.clinic.domain;

public interface ITreatmentDAO {
		public static class TreatmentExn extends Exception {
			private static final long serialVersionUID = 1L;
			public TreatmentExn(String msg)
			{
				super(msg);
			}
			
		}
		public Treatment getTreatmentById(long Id) throws TreatmentExn;
		public  void addTreatment(Treatment t);
		public void deleteTreatments(Treatment t);
}
