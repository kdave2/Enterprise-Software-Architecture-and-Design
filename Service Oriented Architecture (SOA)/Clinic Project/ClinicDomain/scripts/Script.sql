--<ScriptOptions statementTerminator=";"/>

CREATE TABLE radiologydates (
		radiology_id INT8,
		dates DATE
	);

CREATE TABLE radiology (
		id INT8 NOT NULL
	);

CREATE TABLE treatment (
		id INT8 NOT NULL,
		ttype VARCHAR(31),
		diagnosis VARCHAR(255),
		patient_fk INT8,
		provider_fk INT8
	);

CREATE TABLE patient (
		id INT8 NOT NULL,
		birthdate DATE,
		name VARCHAR(255),
		patientid INT8
	);

CREATE TABLE dates (
		radiology_id INT8,
		dates DATE
	);

CREATE TABLE drugtreatment (
		id INT8 NOT NULL,
		dosage FLOAT8,
		drug VARCHAR(255)
	);

CREATE TABLE surgery (
		id INT8 NOT NULL,
		date DATE
	);

CREATE TABLE provider (
		id INT8 NOT NULL,
		providerid INT8,
		name VARCHAR(255),
		specialization VARCHAR(255)
	);

CREATE UNIQUE INDEX provider_pkey ON provider (id ASC);

CREATE UNIQUE INDEX drugtreatment_pkey ON drugtreatment (id ASC);

CREATE UNIQUE INDEX patient_pkey ON patient (id ASC);

CREATE UNIQUE INDEX radiology_pkey ON radiology (id ASC);

CREATE UNIQUE INDEX treatment_pkey ON treatment (id ASC);

CREATE UNIQUE INDEX surgery_pkey ON surgery (id ASC);

ALTER TABLE drugtreatment ADD CONSTRAINT drugtreatment_pkey PRIMARY KEY (id);

ALTER TABLE provider ADD CONSTRAINT provider_pkey PRIMARY KEY (id);

ALTER TABLE radiology ADD CONSTRAINT radiology_pkey PRIMARY KEY (id);

ALTER TABLE treatment ADD CONSTRAINT treatment_pkey PRIMARY KEY (id);

ALTER TABLE surgery ADD CONSTRAINT surgery_pkey PRIMARY KEY (id);

ALTER TABLE patient ADD CONSTRAINT patient_pkey PRIMARY KEY (id);

ALTER TABLE surgery ADD CONSTRAINT fk_surgery_id FOREIGN KEY (id)
	REFERENCES treatment (id);

ALTER TABLE radiologydates ADD CONSTRAINT fk_radiologydates_radiology_id FOREIGN KEY (radiology_id)
	REFERENCES treatment (id);

ALTER TABLE drugtreatment ADD CONSTRAINT fk_drugtreatment_id FOREIGN KEY (id)
	REFERENCES treatment (id);

ALTER TABLE treatment ADD CONSTRAINT fk_treatment_provider_fk FOREIGN KEY (provider_fk)
	REFERENCES provider (id);

ALTER TABLE radiology ADD CONSTRAINT fk_radiology_id FOREIGN KEY (id)
	REFERENCES treatment (id);

ALTER TABLE treatment ADD CONSTRAINT fk_treatment_patient_fk FOREIGN KEY (patient_fk)
	REFERENCES patient (id);

