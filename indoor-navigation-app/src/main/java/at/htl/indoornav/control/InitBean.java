package at.htl.indoornav.control;

import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class InitBean {

    /**
     * Fill database
     *
     * @param event
     */
    void init(@Observes StartupEvent event) {
    }
}
