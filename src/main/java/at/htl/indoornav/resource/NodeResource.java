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
    public Response getNodeByName(@PathParam("name") String name) {
        return Response.ok(nodeRepository.getNode(name)).build();
    }

    @POST
    public Response createNode(@Valid Node node) {
        Node createdNode = nodeRepository.createNode(node);
        return Response.ok(createdNode).build();
    }

    @POST
    @Path("/relationship")
    public Response createRelationship(@QueryParam("start") String startName, @QueryParam("end") String endName) {
        if (startName == null || endName == null) {
            return Response.status(400).build();
        }
        Node nodeStart = nodeRepository.getNode(startName);
        Node nodeEnd = nodeRepository.getNode(endName);

        if (nodeStart == null || nodeEnd == null) {
            return Response.status(400).build();
        }
        nodeRepository.createRelationship(nodeStart, nodeEnd);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{name}")
    public Response deleteNodeByName(@PathParam("name") String name) {
        nodeRepository.deleteNodeByName(name);
        return Response.ok().build();
    }
}
