package at.htl.indoornav.repository;

import at.htl.indoornav.entity.Node;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.StatementResult;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class BaseRepository {

    @Inject
    Driver driver;

    protected int executeUpdate(String query) {
        return executeUpdate(query, null);
    }

    protected int executeUpdate(String query, Map<String, Object> parameters) {
        StatementResult result = driver.session().writeTransaction(transaction -> transaction.run(query, parameters));
        return result.next().get("c").asInt();
    }

    protected Node executeNodeQuery(String queryString) {
        return executeNodeQuery(queryString, null);
    }

    protected Node executeNodeQuery(String queryString, Map<String, Object> parameters) {
        StatementResult result = driver.session().writeTransaction(transaction ->
                transaction.run(queryString, parameters)
        );

        if (result.hasNext()) {
            Record next = result.next();
            return Node.from(next.get("p").asNode());
        }
        return null;
    }

    protected List<Node> executeNodeListQuery(String queryString) {
        return executeNodeListQuery(queryString, null);
    }

    protected List<Node> executeNodeListQuery(String queryString, Map<String, Object> parameters) {
        List<Node> nodes = new LinkedList<>();

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
