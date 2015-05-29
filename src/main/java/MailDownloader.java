import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;


public class MailDownloader {
	
	
	public void findAnchors(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException
	{
		WebClient webClient = new WebClient();
		HtmlPage currentPage = webClient.getPage(url);
		List<HtmlAnchor> anchors = currentPage.getAnchors().stream().filter(anchor -> anchor.getHrefAttribute().charAt(0) == '%').collect(Collectors.toList());
		
		for(HtmlAnchor anchor : anchors)
		{
			currentPage = anchor.click();
			HtmlTable table = currentPage.getHtmlElementById("msgview");
			String content = currentPage.getBody().asText();
			System.out.println(content);
			break;
		}
		webClient.close();
		
	}
}
