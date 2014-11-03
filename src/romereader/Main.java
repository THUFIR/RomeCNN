package romereader;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    private final static Logger log = Logger.getLogger(Main.class.getName());
    private final Populate populate = new Populate();
    private LinkJpaController controller = null;

    public static void main(String... args) {

        new Main().getLinks();

    }

    private void getLinks() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("RomeReaderPU");
        controller = new LinkJpaController(emf);
        List<SyndEntry> entries = null;
        try {
            entries = populate.populate();
        } catch (IOException | IllegalArgumentException | FeedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.fine(entries.toString());
        Link link = null;
        for (SyndEntry entry : entries) {
            log.fine("ping");
            link = new Link();
            link.setCreated(new Date());
            link.setLink(entry.getLink());
            controller.create(link);
        }
        log.severe("..completed");
    }
}
