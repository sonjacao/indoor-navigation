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
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@ApplicationScoped
public class InitBean {

    private Session session = DatabaseRespository.getINSTANCE().getSession();

    /**
     * Fill database
     *
     * @param event
     */
    void init(@Observes StartupEvent event) {
//        session.deleteAll(MapNode.class);
        readMapNodeFromFile("data.json");
        readNodeRelationFromFile("relations.json");
    }

    /**
     * Check if node is already stored in the database
     *
     * @param mapNode Node that needs to be checked
     * @return true if the node exists, else return false if it does not exist
     */
    private boolean nodeExists(MapNode mapNode) {
        if(MapNode.findMapNodeByNodeId(mapNode.getNodeId()) != null) {
            return true;
        }
        return false;
    }

    private boolean relationshipExists(MapNode startNode, MapNode endNode) {
        boolean exists = true;

        Map<String, Object> params = new HashMap<>();
        params.put("sId", startNode.getNodeId());
        params.put("eId", endNode.getNodeId());
        String checkQuery = "MATCH  (s:MapNode {nodeId: {sId}}), (e:MapNode {nodeId: {eId}}) " +
                "RETURN EXISTS( (s)-[:CONNECTS_TO]-(e) )";
        Result result = session.query(checkQuery, params);

        Object[] resultValue = result.iterator().next().values().toArray();
        exists = Boolean.parseBoolean(resultValue[0].toString());

        return exists;
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

    private void readMapNodeFromFile(String mapNodeFilename) {
        Gson gson = new GsonBuilder().registerTypeAdapter(MapNode.class, new MapNodeDeserializer()).create();
        JsonArray jsonArray = readJsonFromFile(mapNodeFilename);

        jsonArray.forEach(jsonValue -> {
            MapNode mapNode = gson.fromJson(jsonValue.toString(), MapNode.class);

            if (!nodeExists(mapNode)) {
                session.save(mapNode);
                System.out.println(mapNode + " created");
            } else {
                System.out.println("Node already exists");
            }
        });
    }

    private void readNodeRelationFromFile(String relationshipFilename) {
        JsonArray jsonArray = readJsonFromFile(relationshipFilename);

        jsonArray.forEach(jsonValue -> {
            MapNode startNode = new MapNode();
            MapNode endNode = new MapNode();
            Collection<MapNode> filterResult;

            startNode = MapNode.findMapNodeByNodeId((long) jsonValue.asJsonObject().getJsonObject("startNode").getInt("nodeId"));
            endNode = MapNode.findMapNodeByNodeId((long) jsonValue.asJsonObject().getJsonObject("endNode").getInt("nodeId"));


            if (!relationshipExists(startNode, endNode)) {
                NodeRelation nodeRelation = new NodeRelation();
                nodeRelation.setStartNode(startNode);
                nodeRelation.setEndNode(endNode);
                nodeRelation.setLength(
                        nodeRelation.calculateLength(startNode, endNode)
                );

                DatabaseRespository.getINSTANCE().getSession().save(nodeRelation);
                System.out.println("Relationship created");
            } else {
                System.out.println("Relationship already exists");
            }
        });
    }
}
