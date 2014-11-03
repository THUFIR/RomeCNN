package romereader;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import dur.bounceme.net.rome.jpa.Feed;
import dur.bounceme.net.rome.jpa.FeedJpaController;
import dur.bounceme.net.rome.jpa.Link;
import dur.bounceme.net.rome.jpa.LinkJpaController;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Populate {

    private final static Logger log = Logger.getLogger(Populate.class.getName());
    private LinkJpaController linksController = null;
    private FeedJpaController feedsController = null;

    public void getFeeds() throws IOException, MalformedURLException, IllegalArgumentException, FeedException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("RomeReaderPU");
        feedsController = new FeedJpaController(emf);
        List<Feed> feeds = feedsController.findFeedEntities();
        for (Feed f : feeds) {
            log.info(f.toString());
            getLinkFromSyndEntry(f);
        }
    }

    private void getLinkFromSyndEntry(Feed f) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("RomeReaderPU");
        linksController = new LinkJpaController(emf);
        List<SyndEntry> entries = null;
        try {
            entries = getSyndEntryList(f);
        } catch (IOException | IllegalArgumentException | FeedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.fine(entries.toString());
        Link link = null;
        for (SyndEntry entry : entries) {
            log.info(entry.getTitle());
            link = new Link();
            link.setCreated(new Date());
            link.setLink(entry.getLink());
            linksController.create(link);
        }
        log.info("..completed");
    }

    private List<SyndEntry> getSyndEntryList(Feed f) throws MalformedURLException, IOException, IllegalArgumentException, FeedException {
        log.info(f.toString());
        URL url = new URL(f.getUrl());
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(httpcon));
        List<SyndEntry> entries = feed.getEntries();
        return entries;
    }
}
