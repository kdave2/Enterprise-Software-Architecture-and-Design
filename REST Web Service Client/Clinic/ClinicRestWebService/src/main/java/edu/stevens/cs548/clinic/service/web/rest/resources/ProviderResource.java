package main.java.edu.stevens.cs548.clinic.service.web.rest.resources;

import java.net.URI;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDtoFactory;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceLocal;
import edu.stevens.cs548.clinic.service.representations.ProviderRepresentation;
import edu.stevens.cs548.clinic.service.representations.Representation;
import edu.stevens.cs548.clinic.service.representations.TreatmentRepresentation;

@Path("/provider")
@RequestScoped
public class ProviderResource {

	final static Logger logger = Logger.getLogger(ProviderResource.class.getCanonicalName());

	@Context
	private UriInfo context;
	private UriInfo uriInfo;

	@Inject
	private IProviderServiceLocal providerService;

	private ProviderDtoFactory providerDtoFactory;
	private TreatmentDtoFactory treatmentDtoFactory;

	public ProviderResource() {
		providerDtoFactory = new ProviderDtoFactory();
	}

	@POST
	@Consumes("application/xml")
	public Response addProvider(ProviderRepresentation providerRep) throws ProviderServiceExn {
		ProviderDto dto = providerDtoFactory.createProviderDto();
		dto.setProviderId(providerRep.getProviderId());
		dto.setName(providerRep.getName());
		dto.setSpecialization(providerRep.getSpecialization());
		providerService.addProvider(dto);
		long id = dto.getProviderId();
		UriBuilder ub = context.getAbsolutePathBuilder().path("{id}");
		URI url = ub.build(Long.toString(id));
		return Response.created(url).build();
	}

	@GET
	@Path("{id}")
	@Produces("application/xml")
	public ProviderRepresentation getProviderByDbId(@PathParam("id") String pid) throws ProviderExn {
		try {
			long providerId = Long.parseLong(pid);
			ProviderDto providerDTO;
			providerDTO = providerService.getProvider(providerId);
			ProviderRepresentation providerRep = new ProviderRepresentation(providerDTO, context);
			return providerRep;
		} catch (ProviderServiceExn e) {
			throw new WebApplicationException("Not Found", 404);
		}
	}

	@GET
	@Path("byNPI")
	@Produces("application/xml")
	public ProviderRepresentation getProviderByNpi(@QueryParam("id") String npi) {
		try {
			ProviderDto providerDTO = providerService.getProviderByProId(Long.parseLong(npi));
			ProviderRepresentation providerRep = new ProviderRepresentation(providerDTO, context);
			return providerRep;
		} catch (ProviderServiceExn e) {
			throw new WebApplicationException("Not Found", 404);
		}
	}

	@GET
	@Path("{id}/treatments/{tid}")
	@Produces("application/xml")
	public TreatmentRepresentation getProviderTreatment(@PathParam("id") String id, @PathParam("tid") String tid) {
		try {
			TreatmentDto dto = providerService.getTreatment(Long.parseLong(id), Long.parseLong(tid));
			TreatmentRepresentation treatmentRep = new TreatmentRepresentation(dto, context);
			return treatmentRep;
		} catch (NumberFormatException e) {
			throw new WebApplicationException("RIP");
		} catch (TreatmentNotFoundExn e) {
			throw new WebApplicationException("Treatment not found");
		} catch (ProviderServiceExn e) {
			throw new WebApplicationException("Provider not found");
		}

	}

	@POST
	@Path("{id}/treatments")
	@Consumes("application/xml")
	public Response addTreatments(TreatmentRepresentation treatmentRep, @PathParam("id") String providerId,
			@HeaderParam("X­Patient") String patientURI) throws ProviderExn {
		try {
			long id = Long.parseLong(providerId);
			TreatmentDto tdf = null;
			if (treatmentRep.getDrugTreatment() != null) {
				tdf = treatmentDtoFactory.createDrugTreatmentDto();
				tdf.setDiagnosis(treatmentRep.getDiagnosis());
				tdf.setId(Representation.getId(treatmentRep.getId()));
				tdf.setPatient(Representation.getId(treatmentRep.getPatient()));
				treatmentRep.setProvider(ProviderRepresentation.getProviderLink(id, uriInfo));
				tdf.setProvider(id);
				tdf.setPatient(Representation.getId(treatmentRep.getLinkPatient()));
				DrugTreatmentType ddto = new DrugTreatmentType();
				ddto.setDosage(treatmentRep.getDrugTreatment().getDosage());
				ddto.setName(treatmentRep.getDrugTreatment().getName());
				tdf.setDrugTreatment(ddto);
			}
			if (treatmentRep.getSurgery() != null) {
				tdf = treatmentDtoFactory.createsurgeryTreatmentDto();
				tdf.setDiagnosis(treatmentRep.getDiagnosis());
				tdf.setId(Representation.getId(treatmentRep.getId()));
				tdf.setPatient(Representation.getId(treatmentRep.getPatient()));
				treatmentRep.setProvider(ProviderRepresentation.getProviderLink(id, uriInfo));
				tdf.setProvider(id);
				tdf.setPatient(Representation.getId(treatmentRep.getLinkPatient()));
				SurgeryType st = new SurgeryType();
				st.setDate(treatmentRep.getSurgery().getDate());
				tdf.setSurgery(st);
			}
			if (treatmentRep.getRadiology() != null) {
				tdf = treatmentDtoFactory.createRadiologyTreatmentDto();
				tdf.setDiagnosis(treatmentRep.getDiagnosis());
				tdf.setId(Representation.getId(treatmentRep.getId()));
				tdf.setPatient(Representation.getId(treatmentRep.getPatient()));
				treatmentRep.setProvider(ProviderRepresentation.getProviderLink(id, uriInfo));
				tdf.setProvider(id);
				tdf.setPatient(Representation.getId(treatmentRep.getLinkPatient()));
				RadiologyType rdto = new RadiologyType();
				rdto.getDate().addAll(treatmentRep.getRadiology().getDate());
				tdf.setRadiology(rdto);
			}
			long tid = providerService.addTreatment(tdf);
			UriBuilder ub = uriInfo.getAbsolutePathBuilder().path("tid");
			URI url = ub.build(Long.toString(tid));
			return Response.created(url).build();
		} catch (ProviderServiceExn e) {
			throw new ProviderExn("404");
		}
	}
}