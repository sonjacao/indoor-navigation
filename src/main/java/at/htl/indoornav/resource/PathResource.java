package at.htl.indoornav.resource;

import at.htl.indoornav.entity.Node;
import at.htl.indoornav.repository.NodeRepository;
import at.htl.indoornav.repository.PathRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/path")
public class PathResource {

    @Inject
    NodeRepository nodeRepository;

    @Inject
    PathRepository pathRepository;

    @GET
    @Path("/shortest")
    public Response getShortestPath(@QueryParam("start") String startName, @QueryParam("end") String endName) {
        return shortestPathForStartAndEnd(startName, endName, false);
    }

    @GET
    @Path("/handicapped")
    public Response getShortestPathForHandicapped(@QueryParam("start") String nameStart, @QueryParam("end") String nameEnd) {
        return shortestPathForStartAndEnd(nameStart, nameEnd, true);
    }

    private Response shortestPathForStartAndEnd(String startName, String endName, boolean isHandicapped) {
        if (startName == null || endName == null) {
            return Response.status(400).build();
        }

        Node startNode = nodeRepository.getNode(startName);
        Node endNode = nodeRepository.getNode(endName);
        if (startNode == null || endNode == null) {
            return Response.status(404).build();
        }

        if (isHandicapped) {
            return Response.ok(pathRepository.getShortestPathForHandicapped(startNode, endNode)).build();
        }
        return Response.ok(pathRepository.getShortestPath(startNode, endNode)).build();
    }
}
