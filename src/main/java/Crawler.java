import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler {
	
	private static String yearForDownloadingMails = "2014";
	public static Set<String> urls = new HashSet<String>();
	
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
				&& href.startsWith("http://mail-archives.apache.org/mod_mbox/maven-users/"
						+ yearForDownloadingMails)
				&& href.contains("mbox/thread");
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);
	}
	
}
