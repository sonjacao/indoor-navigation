package at.htl.indoornav.entity;

public class Corner extends MapNode {

    public Corner() {
    }

    public Corner(Long nodeId, String type, String name, String floor, Double xCoordinate, Double yCoordinate) {
        super(nodeId, type, name, floor, xCoordinate, yCoordinate);
    }

    @Override
    public String toString() {
        return String.format("Corner %d: %s", getNodeId(), getName());
    }
}
