package teashop.resources;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import teashop.DatabaseService;
import teashop.model.Tea;

@Path("/tea")
public class TeaResource {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public List<Tea> getAllTeas(){
		return DatabaseService.getAllTeas();
	}
	
	@DELETE
	@Path("{id}")
	public Response removeTeaById(@PathParam("id") long teaId) {
		boolean isRemoved = DatabaseService.removeTeaById(teaId);
		if(!isRemoved) return Response.status(Status.NOT_FOUND).build();
		return Response.noContent().build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response addTea(@NotNull Tea nTea, @Context UriInfo uriInfo) {
		long id = DatabaseService.addTea(nTea);
		if(id == -1) return Response.serverError().build();
		URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(id)).build();
		return Response.created(uri).entity(nTea).build();
	}

	@PUT
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response updateOrCreateTea(@PathParam("id") long teaId, @NotNull Tea nTea, @Context UriInfo uriInfo) {
		Optional<Tea> optTea = Optional.ofNullable(DatabaseService.getTeaById(teaId));
		if(!optTea.isPresent()) return addTea(nTea, uriInfo);
		nTea.setId(teaId);
		boolean updated = DatabaseService.updateTea(nTea);
		if(!updated) Response.serverError().build();
		return Response.ok(nTea).build();
	}
}
