package romereader;

import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class LinkFacade {

    private final static Logger log = Logger.getLogger(LinkFacade.class.getName());
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("RomeReaderPU");
    private final LinkDAO dao;

    public LinkFacade() {
        dao = new LinkDAO(emf);
    }

    public void populateDatabase(Link newLink) {
        log.info("starting...");
        log.info("null link..");
        dao.create(newLink);
        log.info("...succeeded");
        log.info("...done");
    }
}
