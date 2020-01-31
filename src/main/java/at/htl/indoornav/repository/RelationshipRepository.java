package at.htl.indoornav.repository;

import at.htl.indoornav.entity.Node;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class RelationshipRepository extends BaseRepository {

    public int createRelationship(Node start, Node end) {
        double distance = Math.sqrt(
                Math.pow(end.getX() - start.getX(), 2) +
                Math.pow(end.getY() - start.getY(), 2) +
                Math.pow(end.getZ() - start.getZ(), 2)
        );

        String query = "MATCH (a:Point), (b:Point) WHERE ID(a) = $start AND ID(b) = $end " +
                "CREATE (a)-[c:CONNECTED_TO { distance: $distance }]->(b) RETURN COUNT(c) as c";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.getId());
        parameters.put("end", end.getId());
        parameters.put("distance", distance);

        return executeUpdate(query, parameters);
    }
}
