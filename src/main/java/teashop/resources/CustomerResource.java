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
import teashop.model.Customer;

@Path("/customers")
public class CustomerResource {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public List<Customer> getAllKunden(@Context UriInfo uriInfo){
		return DatabaseService.getAllCustomers();
	}
	
	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getKundeById(@PathParam ("id") long id) {
		Customer customer = DatabaseService.getCustomerById(id);
		if(customer == null) return Response.status(Status.NOT_FOUND).build();
		return Response.ok(customer).build();
	}
	
	
	@PUT
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response updateOrCreateCustomer(@PathParam("id") long customerId, @NotNull Customer nCustomer, @Context UriInfo uriInfo) {
		Optional<Customer> optCustomer = Optional.ofNullable(DatabaseService.getCustomerById(customerId));
		if(!optCustomer.isPresent()) return addCustomer(nCustomer, uriInfo);
		nCustomer.setId(customerId);
		boolean updated = DatabaseService.updateCustomer(nCustomer);
		if(!updated) Response.serverError().build();
		return Response.ok(nCustomer).build();
	}

	@DELETE
	@Path("{customer_id}/orderings/{ordering_id}")
	public Response removeBestellungById(@PathParam("customer_id") long customerId, 
										 @PathParam("ordering_id") long orderingId) {
		boolean isRemoved = DatabaseService.removeOrderingById(customerId, orderingId);
		if(!isRemoved) return Response.status(Status.NOT_FOUND).build();
		return Response.noContent().build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response addCustomer(@NotNull Customer nCustomer, @Context UriInfo uriInfo) {
		long id = DatabaseService.addCustomer(nCustomer);
		if(id == -1) return Response.serverError().build();
		URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(id)).build();
		return Response.created(uri).entity(nCustomer).build();
	}

	
}
