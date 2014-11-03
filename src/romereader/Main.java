package romereader;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    private final static Logger log = Logger.getLogger(Main.class.getName());
    private final Populate p = new Populate();
    private LinkJpaController c = null;

    public static void main(String... args) {
        try {
            new Main().getLinks();
        } catch (IOException | IllegalArgumentException | FeedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void getLinks() throws IOException, MalformedURLException, IllegalArgumentException, FeedException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("RomeReaderPU");
        c = new LinkJpaController(emf);
        List<SyndEntry> entries = p.populate();
        Link l = new Link();
        for (SyndEntry entry : entries) {
            l = new Link();
            l.setLink(entry.getLink());
        }
    }
}
