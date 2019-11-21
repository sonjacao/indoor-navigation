package at.htl.indoornav.control;

import at.htl.indoornav.entity.MapNode;
import at.htl.indoornav.repository.DatabaseRespository;
import io.quarkus.runtime.StartupEvent;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
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
        MapNode mapNode = new MapNode("Lift", "Lift OG", "1 OG", 149.0, 200.0);

        if (!nodeExists(mapNode)) {
            DatabaseRespository.getINSTANCE().getSession().save(mapNode);
            System.out.println(mapNode + " created");
        } else {
            System.out.println("Node already exists");
        }
    }

    /**
     * Check if node is already stored in the database
     *
     * @param mapNode Node that needs to be checked
     * @return true if the node exists, else return false if it does not exist
     */
    private boolean nodeExists(MapNode mapNode) {
        Filter filter = new Filter("name", ComparisonOperator.EQUALS, mapNode.getName());

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
}
