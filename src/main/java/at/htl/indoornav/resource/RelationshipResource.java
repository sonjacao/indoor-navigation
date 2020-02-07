package at.htl.indoornav.resource;

import at.htl.indoornav.entity.Node;
import at.htl.indoornav.repository.NodeRepository;
import at.htl.indoornav.repository.RelationshipRepository;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/relationship")
public class RelationshipResource {

    @Inject
    NodeRepository nodeRepository;

    @Inject
    RelationshipRepository relationshipRepository;

    @POST
    public Response createRelationship(@NotNull @QueryParam("start") String start, @NotNull @QueryParam("end") String end) {
        Node nodeStart = nodeRepository.getNode(start);
        Node nodeEnd = nodeRepository.getNode(end);

        if (nodeStart == null || nodeEnd == null) {
            return Response.status(404).build();
        }

        relationshipRepository.createRelationship(nodeStart, nodeEnd);
        return Response.ok().build();
    }

    @DELETE
    public Response deleteRelationship(@NotNull @QueryParam("start") String start, @NotNull @QueryParam("end") String end) {
        Node nodeStart = nodeRepository.getNode(start);
        Node nodeEnd = nodeRepository.getNode(end);

        if (nodeStart == null || nodeEnd == null) {
            return Response.status(404).build();
        }

        relationshipRepository.deleteRelationship(nodeStart, nodeEnd);
        return Response.noContent().build();
    }
}
