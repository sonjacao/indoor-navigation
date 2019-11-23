package at.htl.indoornav.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
public class MapNode {

    @Id
    @GeneratedValue
    private Long id;

    @Property
    private Long nodeId;
    @Property
    private String type;
    @Property
    private String name;
    @Property
    private String floor;
    @Property
    private Double xCoordinate;
    @Property
    private Double yCoordinate;

    public MapNode() {
    }

    public MapNode(Long nodeId, String type, String name, String floor, Double xCoordinate, Double yCoordinate) {
        this.nodeId = nodeId;
        this.type = type;
        this.name = name;
        this.floor = floor;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public Long getId() {
        return id;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }


    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    @Override
    public String toString() {
        return String.format("Node %d - %s '%s': floor: %s, x-coordinate: %f, y-coordinate: %f", nodeId, type, name, type, xCoordinate, yCoordinate);
    }
}
