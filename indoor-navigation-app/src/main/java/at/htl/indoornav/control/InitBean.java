package at.htl.indoornav.control;

import at.htl.indoornav.entity.MapNode;
import at.htl.indoornav.entity.NodeRelation;
import at.htl.indoornav.repository.DatabaseRespository;
import at.htl.indoornav.serialization.MapNodeDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.quarkus.runtime.StartupEvent;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;

@ApplicationScoped
public class InitBean {

    /**
     * Fill database
     *
     * @param event
     */
    void init(@Observes StartupEvent event) {

        Gson gson = new GsonBuilder().registerTypeAdapter(MapNode.class, new MapNodeDeserializer()).create();
        JsonArray jsonArray = readJsonFromFile("data.json");

        jsonArray.forEach(jsonValue -> {
            MapNode mapNode = gson.fromJson(jsonValue.toString(), MapNode.class);

            if (!nodeExists(mapNode)) {
                DatabaseRespository.getINSTANCE().getSession().save(mapNode);
                System.out.println(mapNode + " created");
            } else {
                System.out.println("Node already exists");
            }
        });

        readNodeRelationFromFile("relations.json");
    }

    /**
     * Check if node is already stored in the database
     *
     * @param mapNode Node that needs to be checked
     * @return true if the node exists, else return false if it does not exist
     */
    private boolean nodeExists(MapNode mapNode) {
        Filter filter = new Filter("nodeId", ComparisonOperator.EQUALS, mapNode.getNodeId());

        Collection<MapNode> result = DatabaseRespository.getINSTANCE().getSession().loadAll(MapNode.class, filter);

        Iterator<MapNode> iterator = result.iterator();
        while (iterator.hasNext()) {
            MapNode currentMapNode = DatabaseRespository.getINSTANCE().getSession().load(MapNode.class, iterator.next().getId());
            if (currentMapNode != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Read Json Array from file
     *
     * @param filename name of the file
     * @return Json Array with data
     */
    private JsonArray readJsonFromFile(String filename) {
        try (JsonReader jsonReader = Json.createReader(
                new InputStreamReader(getClass().getResourceAsStream("/" + filename))
        )) {
            return jsonReader.readArray();
        }
    }

    private void readNodeRelationFromFile(String relationFilename) {
        JsonArray jsonArray = readJsonFromFile("relations.json");

        jsonArray.forEach(jsonValue -> {
            MapNode startNode = new MapNode();
            MapNode endNode = new MapNode();
            Collection<MapNode> filterResult;

            // load start node
            Filter filter = new Filter("nodeId",
                    ComparisonOperator.EQUALS,
                    (jsonValue.asJsonObject().getJsonObject("startNode").getInt("nodeId"))
            );
            filterResult = DatabaseRespository.getINSTANCE().getSession().loadAll(MapNode.class, filter);

            if (filterResult.iterator().hasNext()) {
                startNode = filterResult.iterator().next();
            }

            // load end node
            filter = new Filter("nodeId",
                    ComparisonOperator.EQUALS,
                    (jsonValue.asJsonObject().getJsonObject("endNode").getInt("nodeId"))
            );
            filterResult = DatabaseRespository.getINSTANCE().getSession().loadAll(MapNode.class, filter);

            if (filterResult.iterator().hasNext()) {
                endNode = filterResult.iterator().next();
            }

            NodeRelation nodeRelation = new NodeRelation();
            nodeRelation.setStartNode(startNode);
            nodeRelation.setEndNode(endNode);
            nodeRelation.setLength(
                    nodeRelation.calculateLength(startNode,endNode)
            );
            DatabaseRespository.getINSTANCE().getSession().save(nodeRelation);
        });
    }
}
