package at.htl.indoornav.repository;

import at.htl.indoornav.entity.Node;
import at.htl.indoornav.entity.NodeType;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.StatementResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;

@Singleton
public class NodeRepository {

    @Inject
    Driver driver;

    public Node createNode(Node node) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", node.getName());
        parameters.put("type", node.getType().name());
        parameters.put("isHidden", node.isHidden());
        parameters.put("x", node.getX());
        parameters.put("y", node.getY());
        parameters.put("z", node.getZ());

        String queryCreateNode = "CREATE (p:Point { name: $name, type: $type, isHidden: $isHidden , x: $x, y: $y, z: $z }) RETURN p";

        return executeNodeQuery(queryCreateNode, parameters);
    }

    public List<Node> getAllNodes() {
        String queryAllNodes = "MATCH (p:Point) RETURN p";

        return executeNodeListQuery(queryAllNodes, null);
    }

    public Node getNodeByName(String name) {
        String queryGetNode = "MATCH (p:Point) WHERE p.name = $name RETURN p";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);

        return executeNodeQuery(queryGetNode, parameters);
    }

    public void deleteNodeByName(String name) {
        driver.session().run(
                "MATCH (p:Point) WHERE p.name = $name DETACH DELETE p", parameters("name", name)
        );
    }

    public void createRelationship(Node start, Node end) {
        double distance = Math.sqrt(Math.pow(end.getX() - start.getX(), 2) + Math.pow(end.getY() - start.getY(), 2)
                + Math.pow(end.getZ() - start.getZ(), 2));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.getId());
        parameters.put("end", end.getId());
        parameters.put("distance", distance);
        driver.session().run(
                "MATCH (a:Point), (b:Point) WHERE ID(a) = $start AND ID(b) = $end " +
                        "CREATE (a)-[c:CONNECTED_TO { distance: $distance }]->(b)",
                parameters
        );
    }

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

        StatementResult result = driver.session().run(
                "MATCH (start:Point), (end:Point) WHERE start.name = $start AND end.name = $end " +
                        "CALL algo.shortestPath.stream(start, end, 'distance') " +
                        "YIELD nodeId, cost RETURN algo.asNode(nodeId).name AS name, cost",
                parameters
        );

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        while (result.hasNext()) {
            Record next = result.next();
            // Exception for asFloat
            float cost = (float) next.get("cost").asDouble();
            String nodeName = next.get("name").asString();
            Node node = getNodeByName(nodeName);
            JsonObjectBuilder jsonNode = Json.createObjectBuilder()
                    .add("id", node.getId())
                    .add("name", node.getName())
                    .add("type", node.getType().name())
                    .add("isHidden", node.isHidden())
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

    private Node executeNodeQuery(String queryString, Map<String, Object> parameters) {
        StatementResult result = driver.session().writeTransaction(transaction ->
                transaction.run(queryString, parameters)
        );

        if (result.hasNext()) {
            Record next = result.next();
            return Node.from(next.get("p").asNode());
        }
        return null;
    }

    private List<Node> executeNodeListQuery(String queryString, Map<String, Object> parameters) {
        List nodes = new LinkedList();

        StatementResult result = driver.session().writeTransaction(transaction ->
                transaction.run(queryString, parameters)
        );

        while (result.hasNext()) {
            Record next = result.next();
            nodes.add(Node.from(next.get("p").asNode()));
        }

        return nodes;
    }
}