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

        String query = "MATCH (a:Point), (b:Point) WHERE a.name = $start AND b.name = $end " +
                "CREATE (a)-[c:CONNECTED_TO { distance: $distance }]->(b) RETURN COUNT(c) as c";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.getName());
        parameters.put("end", end.getName());
        parameters.put("distance", distance);

        return executeUpdate(query, parameters);
    }

    public int deleteRelationship(Node start, Node end) {
        String query = "MATCH (a:Point { name: $start })-[c:CONNECTED_TO]-(b: Point { name: $end }) " +
                "DELETE c RETURN COUNT(c) as c";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.getName());
        parameters.put("end", end.getName());

        return executeUpdate(query, parameters);
    }
}
