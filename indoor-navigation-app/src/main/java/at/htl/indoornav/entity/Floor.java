package at.htl.indoornav.entity;

import at.htl.indoornav.repository.DatabaseRespository;
import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.session.Session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NodeEntity
public class Floor {

    private static Session session = DatabaseRespository.getINSTANCE().getSession();

    @Id
    @GeneratedValue
    private Long id;

    @Property
    private Long floorId;
    @Property
    private String name;
    @Property
    private String shortName;

    @Relationship(type = "HAS_NODE", direction = Relationship.OUTGOING)
    List<MapNode> mapNodes = new ArrayList<>();

    public Floor() {
    }

    public Floor(Long floorId, String name, String shortName) {
        this.floorId = floorId;
        this.name = name;
        this.shortName = shortName;
    }

    public static Floor findFloorByFloorId(Long floorId) {
        Floor floor = new Floor();
        Filter filter = new Filter("floorId", ComparisonOperator.EQUALS, floorId);
        Collection<Floor> filterResult = session.loadAll(Floor.class, filter);

        if (filterResult.iterator().hasNext()) {
            floor = filterResult.iterator().next();
            return floor;
        }

        return null;
    }

    public Long getId() {
        return id;
    }

    /*
    public void setId(Long id) {
        this.id = id;
    }
    */

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<MapNode> getMapNodes() {
        return mapNodes;
    }

    public void setMapNodes(List<MapNode> mapNodes) {
        this.mapNodes = mapNodes;
    }

    @Override
    public String toString() {
        return name + " - " + shortName;
    }
}
