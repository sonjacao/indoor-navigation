package at.htl.indoornav.resource;

import at.htl.indoornav.repository.NodeRepository;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("node")
@Produces("application/json")
@Consumes("application/json")
public class NodeResource {

    @Inject
    NodeRepository nodeRepository;

    @GET
    @Path("all")
    public Response getAllNodes() {
        return Response.ok(nodeRepository.getAllNodes()).build();
    }
}
