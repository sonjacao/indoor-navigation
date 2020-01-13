package at.htl.indoornav.entity;

import org.neo4j.ogm.annotation.Relationship;

public class Lift extends MapNode {

    @Relationship(type = "LIFT_ABOVE")
    private Lift liftAbove;

    @Relationship(type = "LIFT_BELOW")
    private Lift liftBelow;

    public Lift() {
    }

    public Lift(Long nodeId, String type, String name, String floor, Double xCoordinate, Double yCoordinate, Lift liftAbove, Lift liftBelow) {
        super(nodeId, type, name, floor, xCoordinate, yCoordinate);
        this.liftAbove = liftAbove;
        this.liftBelow = liftBelow;
    }

    public Lift getLiftAbove() {
        return liftAbove;
    }

    public void setLiftAbove(Lift liftAbove) {
        this.liftAbove = liftAbove;
    }

    public Lift getLiftBelow() {
        return liftBelow;
    }

    public void setLiftBelow(Lift liftBelow) {
        this.liftBelow = liftBelow;
    }

    @Override
    public String toString() {
        return "Lift - " + getName();
    }
}
