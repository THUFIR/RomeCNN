package romereader;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private final static Logger log = Logger.getLogger(Main.class.getName());
    private final Populate p = new Populate();
    private final LinksFacade lf = new LinksFacade();

    public static void main(String... args) {
        try {
            new Main().getLinks();
        } catch (IOException | IllegalArgumentException | FeedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void getLinks() throws IOException, MalformedURLException, IllegalArgumentException, FeedException {
        List<SyndEntry> entries = p.populate();
        Links l = null;
        for (SyndEntry entry : entries) {
            l = new Links();
            l.setLink(entry.getLink());
            lf.p(l);
        }
    }
}
