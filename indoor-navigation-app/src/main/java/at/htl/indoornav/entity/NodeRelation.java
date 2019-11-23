package at.htl.indoornav.entity;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "CONNECTS_TO")
public class NodeRelation {

    @Id @GeneratedValue
    Long id;

    @Property(name = "length")
    private Double length;

    @StartNode
    private MapNode startNode;
    @EndNode
    private MapNode endNode;

    public NodeRelation() {
    }

    public Long getId() {
        return id;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public MapNode getStartNode() {
        return startNode;
    }

    public void setStartNode(MapNode startNode) {
        this.startNode = startNode;
    }

    public MapNode getEndNode() {
        return endNode;
    }

    public void setEndNode(MapNode endNode) {
        this.endNode = endNode;
    }

    public Double calculateLength(MapNode startNode, MapNode endNode) {
        double lengthX = 0;
        double lengthY = 0;

        if(startNode != null && endNode != null) {
            lengthX = Math.pow((endNode.getxCoordinate() - startNode.getxCoordinate()), 2);
            lengthY = Math.pow((endNode.getyCoordinate() - startNode.getyCoordinate()), 2);
        }
        return Math.sqrt(lengthX + lengthY);
    }
}
