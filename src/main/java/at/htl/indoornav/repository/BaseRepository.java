package at.htl.indoornav.repository;

import at.htl.indoornav.entity.Node;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class BaseRepository {

    @Inject
    Driver driver;

    protected int executeUpdate(String query) {
        return executeUpdate(query, null);
    }

    protected int executeUpdate(String query, Map<String, Object> parameters) {
        Result result = driver.session().run(query, parameters);
        return result.next().get("c").asInt();
    }

    protected Node executeNodeQuery(String queryString) {
        return executeNodeQuery(queryString, null);
    }

    protected Node executeNodeQuery(String queryString, Map<String, Object> parameters) {
        Result result = driver.session().run(queryString, parameters);

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
        Result result = driver.session().run(queryString, parameters);

        while (result.hasNext()) {
            Record next = result.next();
            nodes.add(Node.from(next.get("p").asNode()));
        }
        return nodes;
    }

    protected Record executeQuery(String query) {
        return executeQuery(query, null);
    }

    protected Record executeQuery(String query, Map<String, Object> parameters) {
        Result result = driver.session().run(query, parameters);

        if (result.hasNext()) {
            return result.next();
        }
        return null;
    }

    protected List<Record> executeQueryList(String query) {
        return executeQueryList(query, null);
    }

    protected List<Record> executeQueryList(String query, Map<String, Object> parameters) {
        List<Record> records = new LinkedList<>();
        Result result = driver.session().run(query, parameters);

        while (result.hasNext()) {
            records.add(result.next());
        }

        return records;
    }
}
