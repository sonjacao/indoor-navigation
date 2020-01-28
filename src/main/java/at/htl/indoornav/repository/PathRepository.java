package at.htl.indoornav.repository;

import at.htl.indoornav.entity.Node;
import at.htl.indoornav.entity.NodeType;
import org.neo4j.driver.Record;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PathRepository extends BaseRepository {

    @Inject
    NodeRepository nodeRepository;

    public JsonArray getShortestPath(Node start, Node end) {
        return getShortestPathByTwoNodes(start, end, false);
    }

    public JsonArray getShortestPathForHandicapped(Node start, Node end) {
        return getShortestPathByTwoNodes(start, end, true);
    }

    private JsonArray getShortestPathByTwoNodes(Node start, Node end, boolean forHandicapped) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.getName());
        parameters.put("end", end.getName());
        parameters.put("type", forHandicapped ? NodeType.STAIRS.name() : "null");
        // TODO: 20.01.20 handicapped handling

        String query = "MATCH (start:Point), (end:Point) WHERE start.name = $start AND end.name = $end " +
                "CALL algo.shortestPath.stream(start, end, 'distance') " +
                "YIELD nodeId, cost RETURN algo.asNode(nodeId).name AS name, cost";

        List<Record> records = executeQueryList(query, parameters);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Record record : records) {
            // Exception for asFloat
            float cost = (float) record.get("cost").asDouble();
            String nodeName = record.get("name").asString();
            Node node = nodeRepository.getNode(nodeName);
            JsonObjectBuilder jsonNode = Json.createObjectBuilder()
                    .add("id", node.getId())
                    .add("name", node.getName())
                    .add("type", node.getType().name())
                    .add("isHidden", node.getIsHidden())
                    .add("x", node.getX())
                    .add("y", node.getY())
                    .add("z", node.getZ());

            JsonObjectBuilder response = Json.createObjectBuilder()
                    .add("node", jsonNode)
                    .add("distance", cost);
            arrayBuilder.add(response);
        }
        return arrayBuilder.build();
    }
}
