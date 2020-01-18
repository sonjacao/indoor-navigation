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
}