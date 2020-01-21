package at.htl.indoornav.resource;

import at.htl.indoornav.entity.Node;
import at.htl.indoornav.repository.NodeRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/path")
public class PathResource {

    @Inject
    NodeRepository nodeRepository;

    @GET
    @Path("/shortest")
    public Response getShortestPath(@QueryParam("start") String startName, @QueryParam("end") String endName) {
        System.out.println(startName);
        System.out.println(endName);
        if (startName == null || endName == null) {
            return Response.status(400).build();
        }

        Node startNode = nodeRepository.getNodeByName(startName);
        Node endNode = nodeRepository.getNodeByName(endName);
        System.out.println("startNode = " + startNode);
        System.out.println("endNode = " + endNode);
        if (startNode == null || endNode == null) {
            return Response.status(400).build();
        }
        return Response.ok(nodeRepository.getShortestPath(startNode, endNode)).build();
    }

    @GET
    @Path("/handicapped")
    public Response getShortestPathForHandicapped(@QueryParam("start") Long idStart, @QueryParam("end") Long idEnd) {
        Node startNode = nodeRepository.getNodeById(idStart);
        Node endNode = nodeRepository.getNodeById(idEnd);

        return Response.ok(nodeRepository.getShortestPathForHandicapped(startNode, endNode)).build();
    }
}
