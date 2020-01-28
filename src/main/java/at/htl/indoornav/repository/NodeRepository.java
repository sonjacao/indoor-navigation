package at.htl.indoornav.repository;

import at.htl.indoornav.entity.Node;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class NodeRepository extends BaseRepository {

    public Node createNode(Node node) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", node.getName());
        parameters.put("type", node.getType().name());
        parameters.put("isHidden", node.getIsHidden());
        parameters.put("x", node.getX());
        parameters.put("y", node.getY());
        parameters.put("z", node.getZ());

        String queryCreateNode = "CREATE (p:Point { name: $name, type: $type, isHidden: $isHidden , x: $x, y: $y, z: $z }) RETURN p";

        return executeNodeQuery(queryCreateNode, parameters);
    }

    public List<Node> getAllNodes() {
        String queryAllNodes = "MATCH (p:Point) RETURN p";

        return executeNodeListQuery(queryAllNodes);
    }

    public Node getNode(String name) {
        String queryGetNode = "MATCH (p:Point) WHERE p.name = $name RETURN p";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);

        return executeNodeQuery(queryGetNode, parameters);
    }

    public int deleteNode(String name) {
        String query = "MATCH (p:Point) WHERE p.name = $name DETACH DELETE p RETURN COUNT(p) as c";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);

        return executeUpdate(query, parameters);
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
}
