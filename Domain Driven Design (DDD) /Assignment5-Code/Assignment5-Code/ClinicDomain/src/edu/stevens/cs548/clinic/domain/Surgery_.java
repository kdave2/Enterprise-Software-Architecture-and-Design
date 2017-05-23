package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-09-29T18:12:07.032-0400")
@StaticMetamodel(Surgery.class)
public class Surgery_ extends Treatment_ {
	public static volatile SingularAttribute<Surgery, String> diagnosis;
	public static volatile SingularAttribute<Surgery, Date> date;
}
