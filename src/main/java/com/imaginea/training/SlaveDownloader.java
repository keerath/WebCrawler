package com.imaginea.training;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import akka.actor.UntypedActor;

public class SlaveDownloader extends UntypedActor {

	private final Logger logger = LoggerFactory
			.getLogger(SlaveDownloader.class);

	private final WebClient webClient = new WebClient();
	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof DownloadWork) {

			DownloadWork downloadWork = (DownloadWork) message;
			String url = downloadWork.getUrl();
			String month = downloadWork.getMonth();
			MailInfo mailInfo;
				mailInfo = extractMailInfo(url);
				int x = writeMailInfo(mailInfo, month);
				if (x == 0)
					logger.info("[Mail Downloaded From: " + mailInfo.getFrom()
							+ " ]");
				else {
					logger.info("[Download Error! " + "Mail From: "
							+ mailInfo.getFrom() + " could not be downloaded]");
					System.out.println(x);
				}
			}
		else {
			logger.info("Invalid Message!");
			unhandled(message);
		}
	}

	public MailInfo extractMailInfo(String url) throws IOException {
		
		HtmlPage currentPage = webClient.getPage(url);
		
		HtmlTable msgView = currentPage.getHtmlElementById("msgview");
		List<HtmlElement> elementList = msgView.getElementsByAttribute("td",
				"class", "right");
		String from = elementList.get(0).asText();
		String subject = elementList.get(1).asText();
		String date = elementList.get(2).asText();
		String content = currentPage.getElementsByTagName("pre").get(0)
				.asText();
		return new MailInfo(from, subject, date, content);
	}

	public int writeMailInfo(MailInfo mailInfo, String month) {
		try {
			File mailFile = new File("~/Desktop/mails/" + month + "/"
					+ mailInfo.getDate());
			mailFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(mailFile));
			bw.write(mailInfo.toString());
			bw.close();
			return 0;
		} catch (IOException e) {
			System.out.println("IOException!");
			return -1;
		}
	}
}
