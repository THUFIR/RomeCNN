package romereader;

import com.rometools.rome.io.FeedException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private final static Logger log = Logger.getLogger(Main.class.getName());
    private final Populate populate = new Populate();

    public static void main(String... args) {
        new Main().getLinks();
    }

    private void getLinks() {
        try {
            populate.getFeeds();
        } catch (IOException | IllegalArgumentException | FeedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.info("done");
    }

}
