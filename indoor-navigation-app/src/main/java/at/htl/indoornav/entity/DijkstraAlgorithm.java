package at.htl.indoornav.entity;

import at.htl.indoornav.repository.DatabaseRespository;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DijkstraAlgorithm {

    private static Session session;

    public DijkstraAlgorithm() {
        this.session = DatabaseRespository.getINSTANCE().getSession();
    }

    public static List<Map<String, Object>> findShortestPath(MapNode startNode, MapNode endNode) {
        Map<String, Object> params = new HashMap<>();
        params.put("sName", startNode.getName());
        params.put("eName", endNode.getName());
        String checkQuery = "MATCH  (start:MapNode {name: {sName}}), (end:MapNode {name: {eName}}) " +
                "CALL algo.shortestPath.stream(start, end, 'length') " +
                "YIELD nodeId, cost " +
                "RETURN algo.asNode(nodeId).name AS name, cost as length";
        Result result = session.query(checkQuery, params);


       List<Map<String, Object>> mapResult = new LinkedList<>();

        result.iterator().forEachRemaining(stringObjectMap -> {
            Map<String, Object> mapNodeMap = new HashMap<>();
            MapNode mapNode = MapNode.findMapNodeByName(String.valueOf(stringObjectMap.get("name")));
            mapNodeMap.put("mapNode", mapNode);
            mapNodeMap.put("length", new Double(String.valueOf(stringObjectMap.get("length"))).longValue());
            mapResult.add(mapNodeMap);
        });

        return mapResult;
    }
}
