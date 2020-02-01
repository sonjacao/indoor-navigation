package at.htl.indoornav.resource;

import at.htl.indoornav.entity.Node;
import at.htl.indoornav.repository.NodeRepository;
import at.htl.indoornav.service.Result;
import at.htl.indoornav.service.ValidationService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/node")
@Produces("application/json")
@Consumes("application/json")
public class NodeResource {

    @Inject
    NodeRepository nodeRepository;

    @Inject
    ValidationService validationService;

    @GET
    public Response getAllNodes() {
        return Response.ok(nodeRepository.getAllNodes()).build();
    }

    @GET
    @Path("/{name}")
    public Response getNode(@PathParam("name") String name) {
        Node node = nodeRepository.getNode(name);
        if (node == null) {
            return Response.status(404).build();
        }
        return Response.ok(node).build();
    }

    @POST
    public Response createNode(Node node) {
        Result validation = validationService.getValidationResult(node);
        if (!validation.getIsSuccessful()) {
            return Response.status(400).entity(validation).build();
        }
        Node createdNode = nodeRepository.createNode(node);
        return Response.ok(createdNode).build();
    }

    @PUT
    @Path("/{name}")
    public Response updateNode(@PathParam("name") String name, Node node) {
        Node nodeToUpdate = nodeRepository.getNode(name);
        if (nodeToUpdate == null) {
            return Response.status(404).build();
        }

        Result validation = validationService.getValidationResult(node);
        validation.removeNameForUpdate(name);

        if (!validation.getIsSuccessful()) {
            return Response.status(400).entity(validation).build();
        }

        return Response.ok(nodeRepository.updateNode(name, node)).build();
    }

    @DELETE
    @Path("/{name}")
    public Response deleteNode(@PathParam("name") String name) {
        int deleteCount = nodeRepository.deleteNode(name);
        if (deleteCount == 0) {
            return Response.status(404).build();
        }
        return Response.ok().build();
    }
}
