package at.htl.indoornav.resource;

import at.htl.indoornav.entity.Node;
import at.htl.indoornav.repository.NodeRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("path")
public class PathResource {

    @Inject
    NodeRepository nodeRepository;

    @GET
    @Path("shortest")
    public Response getShortestPath(@QueryParam("start") Long idStart, @QueryParam("end") Long idEnd) {
        Node startNode = nodeRepository.getNodeById(idStart);
        Node endNode = nodeRepository.getNodeById(idEnd);

        return Response.ok(nodeRepository.getShortestPath(startNode, endNode)).build();
    }

}
