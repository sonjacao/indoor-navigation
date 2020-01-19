package at.htl.indoornav.resource;

import at.htl.indoornav.entity.Node;
import at.htl.indoornav.repository.NodeRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("node")
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
    @Path("{id}")
    public Response getNodeById(@PathParam("id") Long id) {
        Node node = nodeRepository.getNodeById(id);
        if (node == null) {
            return Response.status(404).build();
        }
        return Response.ok(node).build();
    }
}
