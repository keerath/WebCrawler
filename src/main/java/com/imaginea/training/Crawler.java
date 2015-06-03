package com.imaginea.training;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import uk.org.lidalia.slf4jext.LoggerFactory;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler {

	private static List<String> urlList = new LinkedList<String>();
	private final Logger logger = LoggerFactory.getLogger(Crawler.class);

	public static List<String> getUrlList() {
		return urlList;
	}

	public static void setUrlList(List<String> urlList) {
		Crawler.urlList = urlList;
	}

	public static String getYearForDownloadingMails() {
		return yearForDownloadingMails;
	}

	public static void setYearForDownloadingMails(String yearForDownloadingMails) {
		Crawler.yearForDownloadingMails = yearForDownloadingMails;
	}

	private static String yearForDownloadingMails = "2014";
	private final String baseUrl = "http://mail-archives.apache.org/mod_mbox/maven-users/";

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
				&& href.startsWith(baseUrl + yearForDownloadingMails)
				&& href.contains("mbox/thread") && !href.endsWith("?0");
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		logger.info("Visiting :"+url);
		if (!url.equals(baseUrl))
			urlList.add(url);
	}
}
