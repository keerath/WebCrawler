package com.imaginea.crawler;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

  
  /**
   * Main function which control the entire application.
   * @param args Command Line Arguments
   * @throws Exception Throws Exception
   */
  public static void main(String[] args) throws Exception {
    String crawlStorageFolder = Constants.crawlStorageFolder;
    final int  numberOfCrawlers = Constants.numOfCrawlers;
    CrawlConfig config = new CrawlConfig();

    config.setCrawlStorageFolder(crawlStorageFolder);
    config.setMaxDepthOfCrawling(-1);
    config.setPolitenessDelay(50);

    config.setMaxPagesToFetch(-1);
    PageFetcher pageFetcher = new PageFetcher(config);
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
    robotstxtConfig.setEnabled(false);
    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
        pageFetcher);
    CrawlController controller = new CrawlController(config, pageFetcher,
        robotstxtServer);
    controller.addSeed("http://mail-archives.apache.org/mod_mbox/maven-users/");
    controller.start(Crawler.class, numberOfCrawlers);
    ActorSystem actorSystem = ActorSystem.create("DownloadMails");
    ActorRef master = actorSystem.actorOf(Props.create(MasterDownloader.class),
        "master");
    master.tell(new MasterDownloader.Download(Crawler.getUrlList()),
        ActorRef.noSender());
  }
}
