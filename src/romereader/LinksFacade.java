package romereader;

import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class LinksFacade {

    private final static Logger log = Logger.getLogger(LinksFacade.class.getName());
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("RomeReaderPU");
    private final LinksDAO dao;
    private final List<Links> links;

    public LinksFacade() {
        dao = new LinksDAO(emf);
        links = dao.findLinksEntities();
    }

    public void p(Links newLink) {
        log.info("starting...");
        Links oldLink = dao.searchForMatch(newLink);
        if (oldLink == null) {
            log.info("null link..");
            dao.create(newLink);
            log.info("...succeeded");
        } else {
            log.info("non-null link");
        }
        log.info("...done");
    }
}
