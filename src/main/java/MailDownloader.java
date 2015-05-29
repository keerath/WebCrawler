import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;


public class MailDownloader {
	
	
	public List<String> getMailContent(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException
	{
		WebClient webClient = new WebClient();
		HtmlPage currentPage = webClient.getPage(url);
		List<HtmlAnchor> anchors = currentPage.getAnchors().stream().filter(anchor -> anchor.getHrefAttribute().charAt(0) == '%').collect(Collectors.toList());
		List<String> content = new LinkedList<String>();
		for(HtmlAnchor anchor : anchors)
		{
			currentPage = anchor.click();
			content.add(currentPage.getElementsByTagName("pre").get(0).asText());
		}
		webClient.close();
		return content;
	}
}
