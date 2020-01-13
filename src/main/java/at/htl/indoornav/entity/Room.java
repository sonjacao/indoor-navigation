package at.htl.indoornav.entity;

public class Room extends MapNode {

    public Room() {
    }

    public Room(Long nodeId, String type, String name, String floor, Double xCoordinate, Double yCoordinate) {
        super(nodeId, type, name, floor, xCoordinate, yCoordinate);
    }

    @Override
    public String toString() {
        return String.format("Room %d: %s", getNodeId(), getName());
    }
}
