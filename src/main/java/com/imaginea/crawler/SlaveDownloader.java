package com.imaginea.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

public class SlaveDownloader extends UntypedActor {

  private final Logger logger = LoggerFactory.getLogger(SlaveDownloader.class);

  private final WebClient webClient = new WebClient();

  static class Terminate {

  }

  @Override
  public void onReceive(Object message) throws Exception {

    if (message instanceof DownloadWork) {

      DownloadWork downloadWork = (DownloadWork) message;
      String url = downloadWork.getUrl();
      String month = downloadWork.getMonth();
      List<HtmlAnchor> linksPointingToMails = findLinksPointingToMails(url);
      System.out.println("For Month " + month + " "
          + linksPointingToMails.size());
      for (HtmlAnchor linkPointingToMail : linksPointingToMails) {

        String mailUrl = url.substring(0, url.lastIndexOf('t'))
            + linkPointingToMail.getHrefAttribute();
        MailInfo mailInfo;
        mailInfo = extractMailInfo(mailUrl);
        int returnValue = writeMailInfo(mailInfo, month);
        if (returnValue == 0) {
          logger.info("[Mail Downloaded From: " + mailInfo.getFrom()
              + " Dated: " + mailInfo.getDate() + " ]");
        } else {
          logger.info("[Download Error! " + "Mail From: " + mailInfo.getFrom()
              + " could not be downloaded]");
        }
      }
      getSender().tell(new Terminate(), getSelf());
    } else {
      logger.info("Invalid Message!");
      unhandled(message);
    }
  }

  /**
   * Extracting Mail Information from a URL.
   * 
   * @param url
   *          URL of Mail
   * @return MailInfo object which contains all the information about a mail
   * @throws IOException
   *           Exception Thrown if webClient cannot connect
   */
  public MailInfo extractMailInfo(String url) throws IOException {

    HtmlPage currentPage = webClient.getPage(url);

    HtmlTable msgView = currentPage.getHtmlElementById("msgview");
    List<HtmlElement> elementList = msgView.getElementsByAttribute("td",
        "class", "right");
    String from = elementList.get(0).asText();
    String subject = elementList.get(1).asText();
    String date = elementList.get(2).asText();
    String content = currentPage.getElementsByTagName("pre").get(0).asText();
    return new MailInfo(from, subject, date, content);
  }

  /**
   * Writes Mail Information onto a file in the mails download folder.
   * 
   * @param mailInfo
   *          MailInfo containing information about a mail
   * @param month
   *          Month for writing the mails to specific folders according to their
   *          month.
   * @return Returns 0 on success and 1 on failure
   */
  public int writeMailInfo(MailInfo mailInfo, String month) {
    try {
      File mailFile = new File(Constants.mailDownloadFolder + month + "/"
          + mailInfo.getDate());
      mailFile.createNewFile();
      BufferedWriter bw = new BufferedWriter(new FileWriter(mailFile));
      bw.write(mailInfo.toString());
      bw.close();
      return 0;
    } catch (IOException e) {
      logger.info("IOException Occured!");
      return -1;
    }
  }

  /**
   * Returns a list of links on a page which refer to a mail.
   * 
   * @param url
   *          URL of the page which contains links
   * @return List of all links which refer to a mail
   * @throws FailingHttpStatusCodeException Caused if resource not found
   * @throws MalformedURLException Caused if URL is malformed
   * @throws IOException Caused during I/O transfer
   */
  public List<HtmlAnchor> findLinksPointingToMails(String url)
      throws FailingHttpStatusCodeException, MalformedURLException, IOException {
    HtmlPage currentPage = webClient.getPage(url);
    List<HtmlAnchor> linksPointingToMails = currentPage
        .getAnchors()
        .stream()
        .filter(
            linkPointingToMail -> linkPointingToMail.getHrefAttribute()
                .contains("@")).collect(Collectors.toList());
    webClient.close();
    return linksPointingToMails;
  }

}
