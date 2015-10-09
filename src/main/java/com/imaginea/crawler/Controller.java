package com.imaginea.crawler;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.File;

public class Controller {


    /**
     * Main function which control the entire application.
     *
     * @param args Command Line Arguments
     * @throws Exception Throws Exception
     */
    public static void main(String[] args) throws Exception {
        Crawler crawler = new Crawler();
        crawler.addURLS();
        ActorSystem actorSystem = ActorSystem.create("DownloadMails");
        ActorRef master = actorSystem.actorOf(Props.create(MasterDownloader.class),
                "master");
        new File(Constants.mailDownloadFolder).mkdir();
        master.tell(new MasterDownloader.Download(Crawler.getUrlList()),
                ActorRef.noSender());
    }
}
