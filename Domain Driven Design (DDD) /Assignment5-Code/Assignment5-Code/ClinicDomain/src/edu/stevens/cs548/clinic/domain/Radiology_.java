package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-09-29T18:27:52.880-0400")
@StaticMetamodel(Radiology.class)
public class Radiology_ extends Treatment_ {
	public static volatile SingularAttribute<Radiology, String> diagnosis;
	public static volatile ListAttribute<Radiology, Date> dates;
}
