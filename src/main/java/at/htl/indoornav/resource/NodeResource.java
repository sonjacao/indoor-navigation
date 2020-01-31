package at.htl.indoornav.resource;

import at.htl.indoornav.entity.Node;
import at.htl.indoornav.repository.NodeRepository;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/node")
@Produces("application/json")
@Consumes("application/json")
public class NodeResource {

    @Inject
    NodeRepository nodeRepository;

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
    public Response createNode(@Valid Node node) {
        Node createdNode = nodeRepository.createNode(node);
        return Response.ok(createdNode).build();
    }

    @DELETE
    @Path("/{name}")
    public Response deleteNode(@PathParam("name") String name) {
        nodeRepository.deleteNode(name);
        return Response.ok().build();
    }
}
