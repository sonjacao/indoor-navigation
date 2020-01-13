package at.htl.indoornav.repository;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

public class DatabaseRespository {

    private static DatabaseRespository INSTANCE;

    private Session session;

    public DatabaseRespository() {
        Configuration configuration = new Configuration.Builder()
                .uri("http://localhost:7474")
                .credentials("neo4j", "yourPassword")
                .build();

        SessionFactory sessionFactory = new SessionFactory(configuration, "at.htl.indoornav.entity");
        session = sessionFactory.openSession();
    }

    public static DatabaseRespository getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseRespository();
        }
        return INSTANCE;
    }

    public Session getSession() {
        return session;
    }


}
