package at.htl.indoornav.boundary;

import at.htl.indoornav.entity.MapNode;
import at.htl.indoornav.repository.DatabaseRespository;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.session.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("map")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MapEndpoint {

    private Session session = DatabaseRespository.getINSTANCE().getSession();

    @GET
    public Response getAllMapNodes() {
        Collection<MapNode> mapNodeCollection = session.loadAll(MapNode.class);
        return Response
                .ok(mapNodeCollection)
                .build();
    }

    @GET
    @Path("{floor}")
    public Response getMapNodesByFloor(@PathParam("floor") String floor) {
        Filter filter = new Filter("floor", ComparisonOperator.EQUALS, floor);
        Collection<MapNode> mapNodeCollection = session.loadAll(MapNode.class, filter);
        if (mapNodeCollection != null) {
            return  Response
                    .ok(mapNodeCollection)
                    .build();
        }
        return Response.noContent().build();
    }
}