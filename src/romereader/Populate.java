package romereader;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

public class Populate {

    private final static Logger log = Logger.getLogger(Populate.class.getName());

    public List<SyndEntry> populate() throws MalformedURLException, IOException, IllegalArgumentException, FeedException {
        log.info("starting...");
        URL url = new URL("http://rss.cnn.com/rss/cnn_topstories.rss");
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(httpcon));
        List<SyndEntry> entries = feed.getEntries();
        return entries;
    }
}
