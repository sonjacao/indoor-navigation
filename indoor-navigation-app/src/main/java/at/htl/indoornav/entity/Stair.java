package at.htl.indoornav.entity;

import org.neo4j.ogm.annotation.Relationship;

public class Stair extends MapNode {

    @Relationship(type = "STAIR_ABOVE")
    private Stair stairAbove;

    @Relationship(type = "STAIR_BELOW")
    private String stairBelow;

    public Stair() {
    }

    public Stair(Long nodeId, String type, String name, String floor, Double xCoordinate, Double yCoordinate, Stair stairAbove, String stairBelow) {
        super(nodeId, type, name, floor, xCoordinate, yCoordinate);
        this.stairAbove = stairAbove;
        this.stairBelow = stairBelow;
    }

    public Stair getStairAbove() {
        return stairAbove;
    }

    public void setStairAbove(Stair stairAbove) {
        this.stairAbove = stairAbove;
    }

    public String getStairBelow() {
        return stairBelow;
    }

    public void setStairBelow(String stairBelow) {
        this.stairBelow = stairBelow;
    }

    @Override
    public String toString() {
        return "Stair - " + getName();
    }
}
