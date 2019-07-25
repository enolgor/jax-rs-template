package es.enolgor.app.api.resources;

import static es.enolgor.app.api.ResponseUtils.error;
import static es.enolgor.app.api.ResponseUtils.success;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.enolgor.app.api.auth.Secured;
import es.enolgor.app.datasource.DataSource;
import es.enolgor.app.datasource.dao.DAOException.AlreadyExistsException;
import es.enolgor.app.datasource.dao.DAOException.InternalException;
import es.enolgor.app.datasource.dao.DAOException.NotFoundException;
import es.enolgor.app.model.Pet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Pet Resource")
@Path("/resource/pet")
public class PetResource {
	
	private static final Logger logger = LoggerFactory.getLogger(PetResource.class);
	
	@Inject DataSource dataSource;
	
	@Secured.Bearer
	@GET @Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "List all pets", response = Pet.class, responseContainer = "List")
	@ApiResponses({
		@ApiResponse(code = 500, message = "Internal error")
	})
	public Response list(){
		try {
			Collection<Pet> pets = dataSource.getPetProvider().list();
			return success(pets);
		} catch (InternalException e) {
			logger.error(e.getMessage(), e);
			return error("Internal error", 500);
		}
 	}
	
	@GET @Path("/get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a pet by id", response = Pet.class)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Pet not found"),
		@ApiResponse(code = 500, message = "Internal error")
	})
	public Response get(@ApiParam(value = "ID of pet", required = true) @PathParam("id") String id){
		try {
			Pet pet = dataSource.getPetProvider().read(id);
			return success(pet);
		}catch(NotFoundException e) {
			return error("Pet not found", 400);
		}catch(InternalException e) {
			logger.error(e.getMessage(), e);
			return error("Internal error", 500);
		}
 	}
	
	@PUT @Path("/create")
	@Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a pet", response = Pet.class, notes = "If id is not provided, it will be randomly generated")
	@ApiResponses({
		@ApiResponse(code = 400, message = "Pet already exists"),
		@ApiResponse(code = 500, message = "Internal error")
	})
	public Response create(@ApiParam(value = "Pet description", required = true) Pet pet){
		try {
			String id = dataSource.getPetProvider().create(pet);
			pet.setId(id);
			return success(pet);
		}catch(AlreadyExistsException e) {
			return error("Pet already exists", 400);
		}catch(InternalException e) {
			logger.error(e.getMessage(), e);
			return error("Internal error", 500);
		}
 	}
	
	@POST @Path("/update")
	@Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update a pet", response = Pet.class)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Pet not found"),
		@ApiResponse(code = 500, message = "Internal error")
	})
	public Response update(@ApiParam(value = "Pet description", required = true) Pet pet){
		try {
			dataSource.getPetProvider().update(pet);
			return success(pet);
		}catch(NotFoundException e) {
			return error("Pet not found", 400);
		}catch(InternalException e) {
			logger.error(e.getMessage(), e);
			return error("Internal error", 500);
		}
 	}
	
	@DELETE @Path("/delete/{id}")
	@ApiOperation(value = "Delete a pet")
	@ApiResponses({
		@ApiResponse(code = 400, message = "Pet not found"),
		@ApiResponse(code = 500, message = "Internal error")
	})
	public Response delete(@ApiParam(value = "ID of pet", required = true) @PathParam("id") String id){
		try {
			dataSource.getPetProvider().delete(id);
			return success();
		}catch(NotFoundException e) {
			return error("Pet not found", 400);
		}catch(InternalException e) {
			logger.error(e.getMessage(), e);
			return error("Internal error", 500);
		}
 	}
}
