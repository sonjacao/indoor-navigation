package at.htl.indoornav.repository;

import at.htl.indoornav.entity.Node;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.StatementResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;

@Singleton
public class NodeRepository {

    @Inject
    Driver driver;

    public void createNode(Node node) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", node.getName());
        parameters.put("type", node.getType().name());
        parameters.put("isHidden", node.isHidden());
        parameters.put("x", node.getX());
        parameters.put("y", node.getY());
        parameters.put("z", node.getZ());

        driver.session().writeTransaction(transaction -> transaction.run(
                "CREATE (n:Point { name: $name, type: $type, isHidden: $isHidden , x: $x, y: $y, z: $z })",
                parameters
        ));
    }

    public List<Node> getAllNodes() {
        List<Node> nodes = new LinkedList<>();
        StatementResult result = driver.session()
                .run("MATCH (p:Point) RETURN p");

        while (result.hasNext()) {
            Record next = result.next();
            nodes.add(Node.from(next.get("p").asNode()));
        }

        return nodes;
    }

    public Node getNodeById(Long id) {
        StatementResult result = driver.session().run(
                "MATCH (p:Point) WHERE ID(p) = $id RETURN p", parameters("id", id)
        );

        if (result.hasNext()) {
            Record next = result.next();
            return Node.from(next.get("p").asNode());
        }
        return null;
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